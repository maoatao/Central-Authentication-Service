package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.param.clientuser.ClientUserQueryParam;
import com.maoatao.cas.core.bean.param.clientuser.ClientUserSaveParam;
import com.maoatao.cas.core.bean.param.clientuser.ClientUserUpdateParam;
import com.maoatao.cas.core.bean.vo.ClientUserVO;
import com.maoatao.cas.core.bean.entity.PermissionEntity;
import com.maoatao.cas.core.bean.entity.RoleEntity;
import com.maoatao.cas.core.bean.entity.ClientUserEntity;
import com.maoatao.cas.core.mapper.ClientUserMapper;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.cas.core.service.RoleService;
import com.maoatao.cas.core.service.ClientUserRoleService;
import com.maoatao.cas.core.service.ClientUserService;
import com.maoatao.cas.security.bean.CustomUserDetails;
import com.maoatao.cas.util.IdUtils;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import com.maoatao.synapse.lang.util.SynaAssert;
import com.maoatao.synapse.lang.util.SynaStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户
 *
 * @author: MaoAtao
 * @create: 2022-03-11 16:13:35
 */
@Service
public class ClientUserServiceImpl extends DaedalusServiceImpl<ClientUserMapper, ClientUserEntity> implements ClientUserService {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ClientUserRoleService clientUserRoleService;

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<ClientUserVO> page(ClientUserQueryParam param) {
        ClientUserEntity entity = BeanUtil.copyProperties(param, ClientUserEntity.class);
        Page<ClientUserEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, ClientUserVO.class);
    }

    @Override
    public ClientUserVO details(Long id) {
        return BeanUtil.toBean(super.getById(id), ClientUserVO.class);
    }

    @Override
    public UserDetails getUserDetails(String username, String clientId) throws UsernameNotFoundException {
        ClientUserEntity existed = getByNameAndClient(username, clientId);
        if (existed == null) {
            throw new UsernameNotFoundException(SynaStrings.format("用户 {} 不存在!", username));
        }
        return buildUserDetails(existed);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long save(ClientUserSaveParam param) {
        checkClient(param.getClientId());
        checkUserName(param.getName(), param.getClientId());
        param.setUserOpenId(IdUtils.nextUserOpenId());
        ClientUserEntity user = BeanUtil.copyProperties(param, ClientUserEntity.class);
        user.setPassword(passwordEncoder.encode(param.getPassword()));
        SynaAssert.isTrue(save(user), "新增用户失败!");
        SynaAssert.notNull(user.getId(), "新增用户失败:用户 ID 为空!");
        SynaAssert.isTrue(
                clientUserRoleService.updateUserRole(getAndCheckRoleIds(param.getRoles().stream().toList(), param.getClientId()), user.getId()),
                "新用户绑定角色失败!"
        );
        return user.getId();
    }

    @Override
    public boolean update(ClientUserUpdateParam param) {
        checkClient(param.getClientId());
        ClientUserEntity existed = getAndCheckUser(param.getName(), param.getClientId());
        ClientUserEntity user = BeanUtil.copyProperties(param, ClientUserEntity.class);
        user.setId(existed.getId());
        checkUserName(param.getName(), param.getClientId());
        SynaAssert.isTrue(updateById(user), "更新用户失败!");
        SynaAssert.isTrue(
                clientUserRoleService.updateUserRole(getAndCheckRoleIds(param.getRoles().stream().toList(), param.getClientId()), existed.getId()),
                "更新用户绑定角色失败!"
        );
        return true;
    }

    @Override
    public boolean remove(String username, String clientId) {
        checkClient(clientId);
        ClientUserEntity existed = getAndCheckUser(username, clientId);
        SynaAssert.isTrue(removeById(existed), "删除用户失败!");
        SynaAssert.isTrue(clientUserRoleService.updateUserRole(null, existed.getId()), "删除用户解绑角色失败!");
        return true;
    }

    @Override
    public boolean changePassword(String username, String clientId, String oldPassword, String newPassword) {
        checkClient(clientId);
        ClientUserEntity existed = getAndCheckUser(username, clientId);
        SynaAssert.isTrue(passwordEncoder.matches(oldPassword, existed.getPassword()), "修改密码失败:原密码错误!");
        existed.setPassword(passwordEncoder.encode(newPassword));
        return updateById(existed);
    }

    @Override
    public boolean exists(String username, String clientId) {
        return getByNameAndClient(username, clientId) != null;
    }

    @Override
    public ClientUserEntity getByNameAndClient(String name, String clientId) {
        SynaAssert.isTrue(StrUtil.isNotBlank(name), "用户名称不能为空!");
        SynaAssert.isTrue(StrUtil.isNotBlank(clientId), "客户端 Id 不能为空!");
        return getOne(Wrappers.query(ClientUserEntity.builder().loginName(name).clientId(clientId).build()));
    }

    /**
     * 校验客户端是否存在
     *
     * @param clientId 客户端 id
     */
    private void checkClient(String clientId) {
        SynaAssert.notNull(registeredClientRepository.findByClientId(clientId), "客户端 {} 不存在!", clientId);
    }

    /**
     * 校验用户名在自定客户端是否存在
     *
     * @param username 用户名称
     * @param clientId 客户端 id
     */
    private void checkUserName(String username, String clientId) {
        SynaAssert.isNull(getByNameAndClient(username, clientId), "用户名 {} 已存在", username);

    }

    /**
     * 按用户名称和客户端 id 获取并校验用户是否存在
     *
     * @param username 用户名称
     * @param clientId 客户端 id
     * @return 用户
     */
    private ClientUserEntity getAndCheckUser(String username, String clientId) {
        ClientUserEntity existed = getByNameAndClient(username, clientId);
        SynaAssert.notNull(existed, "用户 {} 不存在!", username);
        return existed;
    }

    /**
     * 获取并检查角色ID
     * <p>
     * 给用户新增角色时,这些角色必须已存在
     *
     * @param roleNames 角色名称集合
     * @param clientId  客户端 id
     * @return 如果角色都存在, 返回这些角色的 id
     */
    private List<Long> getAndCheckRoleIds(List<String> roleNames, String clientId) {
        if (IterUtil.isEmpty(roleNames)) {
            return Collections.emptyList();
        }
        Map<String, Long> roleEntityMap = roleService.listByRolesAndClient(roleNames, clientId).stream()
                .collect(Collectors.toMap(RoleEntity::getName, RoleEntity::getId));
        return roleNames.stream().map(o -> {
            Long roleId = roleEntityMap.get(o);
            SynaAssert.notNull(roleId, "角色 {} 不存在", o);
            return roleId;
        }).toList();
    }

    private UserDetails buildUserDetails(ClientUserEntity clientUserEntity) {
        CustomUserDetails.CustomUserDetailsBuilder customUserDetailsBuilder = CustomUserDetails.builder()
                .openId(clientUserEntity.getUserOpenId())
                .clientId(clientUserEntity.getClientId())
                .username(clientUserEntity.getLoginName())
                .password(clientUserEntity.getPassword())
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true);
        List<PermissionEntity> permissionEntities = permissionService.listByUser(clientUserEntity.getId());
        if (IterUtil.isNotEmpty(permissionEntities)) {
            customUserDetailsBuilder.permissions(permissionEntities.stream()
                    .map(PermissionEntity::getName)
                    .collect(Collectors.toSet()));
        }
        List<RoleEntity> roleEntities = roleService.listByUser(clientUserEntity.getId());
        if (IterUtil.isNotEmpty(roleEntities)) {
            customUserDetailsBuilder.roles(roleEntities.stream()
                    .map(RoleEntity::getName)
                    .collect(Collectors.toSet()));
        }
        return customUserDetailsBuilder.build();
    }
}
