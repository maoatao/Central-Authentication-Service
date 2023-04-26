package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientEntity;
import com.maoatao.cas.core.bean.entity.ClientScopeEntity;
import com.maoatao.cas.core.bean.param.clientuser.ClientUserQueryParam;
import com.maoatao.cas.core.bean.param.clientuser.ClientUserSaveParam;
import com.maoatao.cas.core.bean.param.clientuser.ClientUserUpdateParam;
import com.maoatao.cas.core.bean.vo.ClientUserVO;
import com.maoatao.cas.core.bean.entity.PermissionEntity;
import com.maoatao.cas.core.bean.entity.RoleEntity;
import com.maoatao.cas.core.bean.entity.ClientUserEntity;
import com.maoatao.cas.core.mapper.ClientUserMapper;
import com.maoatao.cas.core.service.ClientScopeService;
import com.maoatao.cas.core.service.ClientService;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.cas.core.service.RoleService;
import com.maoatao.cas.core.service.ClientUserRoleService;
import com.maoatao.cas.core.service.ClientUserService;
import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.security.bean.ClientDetails;
import com.maoatao.cas.security.bean.CustomUserDetails;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import com.maoatao.synapse.lang.util.SynaAssert;
import com.maoatao.synapse.lang.util.SynaStrings;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
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
 * 客户端用户
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

    @Autowired
    private UserService userService;

    @Autowired
    private ClientScopeService clientScopeService;

    @Autowired
    private ClientService clientService;

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
    public UserDetails getUserDetails(String username, ClientDetails clientDetails) throws UsernameNotFoundException {
        ClientUserEntity existed = getByNameAndClient(username, clientDetails.getClientId());
        if (existed == null) {
            throw new UsernameNotFoundException(SynaStrings.format("客户端用户 {} 不存在!", username));
        }
        return buildUserDetails(existed, clientDetails.getScopes());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long save(ClientUserSaveParam param) {
        checkClient(param.getClientId());
        checkUserName(param.getLoginName(), param.getClientId());
        ClientUserEntity clientUserEntity = BeanUtil.copyProperties(param, ClientUserEntity.class);
        clientUserEntity.setPassword(passwordEncoder.encode(param.getPassword()));
        SynaAssert.isTrue(save(clientUserEntity), "新增客户端用户失败!");
        SynaAssert.isTrue(
                clientUserRoleService.updateUserRole(getAndCheckRoleIds(param.getRoles().stream().toList(), param.getClientId()), clientUserEntity.getId()),
                "新客户端用户绑定角色失败!"
        );
        return clientUserEntity.getId();
    }

    @Override
    public boolean update(ClientUserUpdateParam param) {
        checkClient(param.getClientId());
        ClientUserEntity existed = getAndCheckUser(param.getLoginName(), param.getClientId());
        ClientUserEntity user = BeanUtil.copyProperties(param, ClientUserEntity.class);
        user.setId(existed.getId());
        checkUserName(param.getLoginName(), param.getClientId());
        SynaAssert.isTrue(updateById(user), "更新客户端用户失败!");
        SynaAssert.isTrue(
                clientUserRoleService.updateUserRole(getAndCheckRoleIds(param.getRoles().stream().toList(), param.getClientId()), existed.getId()),
                "更新客户端用户绑定角色失败!"
        );
        return true;
    }

    @Override
    public boolean remove(String username, String clientId) {
        checkClient(clientId);
        ClientUserEntity existed = getAndCheckUser(username, clientId);
        SynaAssert.isTrue(removeById(existed), "删除客户端用户失败!");
        SynaAssert.isTrue(clientUserRoleService.updateUserRole(null, existed.getId()), "删除客户端用户解绑角色失败!");
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
     * @return 客户端用户
     */
    private ClientUserEntity getAndCheckUser(String username, String clientId) {
        ClientUserEntity existed = getByNameAndClient(username, clientId);
        SynaAssert.notNull(existed, "客户端用户 {} 不存在!", username);
        return existed;
    }

    /**
     * 获取并检查角色ID
     * <p>
     * 给客户端用户新增角色时,这些角色必须已存在
     *
     * @param roleNames 角色名称集合
     * @param clientId  客户端 id
     * @return 如果角色都存在, 返回这些角色的 id
     */
    private List<Long> getAndCheckRoleIds(List<String> roleNames, String clientId) {
        if (CollectionUtil.isEmpty(roleNames)) {
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

    private UserDetails buildUserDetails(ClientUserEntity clientUserEntity, Map<String, Set<String>> scopes) {
        CustomUserDetails.CustomUserDetailsBuilder customUserDetailsBuilder = CustomUserDetails.builder()
                .username(clientUserEntity.getLoginName())
                .password(clientUserEntity.getPassword())
                .permissions(Map.of())
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true);
        Optional.ofNullable(userService.getById(clientUserEntity.getUserId()))
                .ifPresent(userEntity -> customUserDetailsBuilder.openId(userEntity.getOpenId()));
        if (MapUtil.isEmpty(scopes)) {
            return customUserDetailsBuilder.build();
        }
        List<String> scopeNames = scopes.values().stream().flatMap(Collection::stream).toList();
        List<String> clientAliases = scopes.keySet().stream().toList();
        if (CollectionUtil.isEmpty(scopeNames) || CollectionUtil.isEmpty(clientAliases)) {
            return customUserDetailsBuilder.build();
        }
        List<String> clientIds = clientService.listByClientAliases(clientAliases).stream().map(ClientEntity::getClientId).toList();
        if (CollectionUtil.isEmpty(clientIds)) {
            return customUserDetailsBuilder.build();
        }
        // 按作用域名称查找所有客户端的作用域
        List<ClientScopeEntity> clientScopeEntities = clientScopeService.listByScopeNames(scopeNames);
        if (CollectionUtil.isEmpty(clientScopeEntities)) {
            return customUserDetailsBuilder.build();
        }
        // 筛选指定的客户端作用域
        List<Long> scopeIds = clientScopeEntities.stream()
                .filter(o -> clientIds.contains(o.getClientId()))
                .map(ClientScopeEntity::getId)
                .distinct().toList();
        if (CollectionUtil.isEmpty(scopeIds)) {
            return customUserDetailsBuilder.build();
        }
        // 查询作用域权限
        List<PermissionEntity> permissionEntities = permissionService.listByClientScopes(scopeIds);
        if (CollectionUtil.isNotEmpty(permissionEntities)) {
            customUserDetailsBuilder.permissions(permissionEntities.stream()
                    .collect(Collectors.groupingBy(PermissionEntity::getClientId, Collectors.mapping(PermissionEntity::getName, Collectors.toSet())))
            );
        }
        return customUserDetailsBuilder.build();
    }
}
