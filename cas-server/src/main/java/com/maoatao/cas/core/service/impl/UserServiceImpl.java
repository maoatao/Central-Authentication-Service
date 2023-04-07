package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.param.user.UserQueryParam;
import com.maoatao.cas.core.bean.vo.UserVO;
import com.maoatao.cas.core.bean.entity.PermissionEntity;
import com.maoatao.cas.core.bean.entity.RoleEntity;
import com.maoatao.cas.core.bean.entity.UserEntity;
import com.maoatao.cas.core.mapper.UserMapper;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.cas.core.service.RoleService;
import com.maoatao.cas.core.service.UserRoleService;
import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.security.bean.CustomUserDetails;
import com.maoatao.cas.util.IdUtils;
import com.maoatao.cas.core.param.UserParam;
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
public class UserServiceImpl extends DaedalusServiceImpl<UserMapper, UserEntity> implements UserService {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<UserVO> page(UserQueryParam param) {
        UserEntity entity = BeanUtil.copyProperties(param, UserEntity.class);
        Page<UserEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, UserVO.class);
    }

    @Override
    public UserVO details(Long id){
        return BeanUtil.toBean(super.getById(id), UserVO.class);
    }

    @Override
    public UserDetails getUserDetails(String username, String clientId) throws UsernameNotFoundException {
        UserEntity existed = getByNameAndClient(username, clientId);
        if (existed == null) {
            throw new UsernameNotFoundException(SynaStrings.format("用户 {} 不存在!", username));
        }
        return buildUserDetails(existed);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long save(UserParam param) {
        checkClient(param.getClientId());
        checkUserName(param.getName(), param.getClientId());
        param.setId(null);
        param.setOpenId(IdUtils.nextUserOpenId());
        UserEntity user = BeanUtil.copyProperties(param, UserEntity.class);
        user.setPassword(passwordEncoder.encode(param.getPassword()));
        SynaAssert.isTrue(save(user), "新增用户失败!");
        SynaAssert.notNull(user.getId(), "新增用户失败:用户 ID 为空!");
        SynaAssert.isTrue(
                userRoleService.updateUserRole(getAndCheckRoleIds(param.getRoles(), param.getClientId()), user.getId()),
                "新用户绑定角色失败!"
        );
        return user.getId();
    }

    @Override
    public boolean update(UserParam param) {
        checkClient(param.getClientId());
        UserEntity existed = getAndCheckUser(param.getName(), param.getClientId());
        UserEntity user = BeanUtil.copyProperties(param, UserEntity.class);
        user.setId(existed.getId());
        if (!existed.getClientId().equals(param.getClientId())) {
            checkUserName(param.getName(), param.getClientId());
        }
        SynaAssert.isTrue(updateById(user), "更新用户失败!");
        SynaAssert.isTrue(
                userRoleService.updateUserRole(getAndCheckRoleIds(param.getRoles(), param.getClientId()), existed.getId()),
                "更新用户绑定角色失败!"
        );
        return true;
    }

    @Override
    public boolean remove(String username, String clientId) {
        checkClient(clientId);
        UserEntity existed = getAndCheckUser(username, clientId);
        SynaAssert.isTrue(removeById(existed), "删除用户失败!");
        SynaAssert.isTrue(userRoleService.updateUserRole(null, existed.getId()), "删除用户解绑角色失败!");
        return true;
    }

    @Override
    public boolean changePassword(String username, String clientId, String oldPassword, String newPassword) {
        checkClient(clientId);
        UserEntity existed = getAndCheckUser(username, clientId);
        SynaAssert.isTrue(passwordEncoder.matches(oldPassword, existed.getPassword()), "修改密码失败:原密码错误!");
        existed.setPassword(passwordEncoder.encode(newPassword));
        return updateById(existed);
    }

    @Override
    public boolean exists(String username, String clientId) {
        return getByNameAndClient(username, clientId) != null;
    }

    @Override
    public UserEntity getByNameAndClient(String name, String clientId) {
        SynaAssert.isTrue(StrUtil.isNotBlank(name), "用户名称不能为空!");
        SynaAssert.isTrue(StrUtil.isNotBlank(clientId), "客户端 Id 不能为空!");
        return getOne(Wrappers.query(UserEntity.builder().name(name).clientId(clientId).build()));
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
    private UserEntity getAndCheckUser(String username, String clientId) {
        UserEntity existed = getByNameAndClient(username, clientId);
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

    private UserDetails buildUserDetails(UserEntity userEntity) {
        CustomUserDetails.CustomUserDetailsBuilder customUserDetailsBuilder = CustomUserDetails.builder()
                .openId(userEntity.getOpenId())
                .clientId(userEntity.getClientId())
                .username(userEntity.getName())
                .password(userEntity.getPassword())
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true);
        List<PermissionEntity> permissionEntities = permissionService.listByUser(userEntity.getId());
        if (IterUtil.isNotEmpty(permissionEntities)) {
            customUserDetailsBuilder.permissions(permissionEntities.stream()
                    .map(PermissionEntity::getName)
                    .collect(Collectors.toSet()));
        }
        List<RoleEntity> roleEntities = roleService.listByUser(userEntity.getId());
        if (IterUtil.isNotEmpty(roleEntities)) {
            customUserDetailsBuilder.roles(roleEntities.stream()
                    .map(RoleEntity::getName)
                    .collect(Collectors.toSet()));
        }
        return customUserDetailsBuilder.build();
    }
}
