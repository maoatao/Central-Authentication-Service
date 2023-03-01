package com.maoatao.cas.security.service.impl;

import cn.hutool.core.collection.IterUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.maoatao.cas.core.entity.RoleEntity;
import com.maoatao.cas.core.entity.UserEntity;
import com.maoatao.cas.core.entity.UserRoleEntity;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.cas.core.service.RoleService;
import com.maoatao.cas.core.service.UserRoleService;
import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.security.bean.CustomUserDetails;
import com.maoatao.cas.security.bean.CustomAuthority;
import com.maoatao.cas.security.bean.ClientUser;
import com.maoatao.cas.security.service.ClientUserService;
import com.maoatao.synapse.core.util.SynaAssert;
import com.maoatao.synapse.core.util.SynaSafes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * OAuth2 认证授权要用到的查询用户 逻辑
 *
 * @author MaoAtao
 * @date 2021-05-13 15:16:19
 */
@Service
public class ClientUserServiceImpl implements ClientUserService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    /**
     * 获取权限
     *
     * @param id 用户id
     * @return 权限列表
     */
    private List<CustomAuthority> getAuthorities(Long id) {
        return SynaSafes.of(permissionService.getPermissionByUser(id)).stream()
                .map(o -> CustomAuthority.builder().authority(o.getName()).client(o.getClientId()).build())
                .toList();
    }

    @Override
    public CustomUserDetails getUser(String username, Object details) throws UsernameNotFoundException {
        UserEntity userEntity = getUser(username, details.toString());
        SynaAssert.notNull(userEntity, "用户名或密码错误");
        SynaAssert.isTrue(userEntity.getEnabled(), "该用户已被禁用");
        return ClientUser.builder()
                .clientId(userEntity.getClientId())
                .username(userEntity.getName())
                .password(userEntity.getPassword())
                .disabled(false)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .authorities(getAuthorities(userEntity.getId()))
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long createUser(CustomUserDetails userDetails) {
        SynaAssert.notNull(registeredClientRepository.findByClientId(userDetails.getClientId()), "注册客户端不存在!");
        long userId = saveUser(userDetails);
        updateUserRole(getAndCheckRoleIds(userDetails.getAuthorities(), userDetails.getClientId()), userId);
        return userId;
    }

    @Override
    public boolean updateUser(CustomUserDetails userDetails) {
        return false;
    }

    @Override
    public boolean deleteUser(String username) {
        return false;
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        return false;
    }

    @Override
    public boolean userExists(String username, String clientId) {
        return false;
    }

    private UserEntity getUser(String username, String clientId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(username);
        userEntity.setClientId(clientId);
        return userService.getOne(Wrappers.query(userEntity));
    }

    private long saveUser(CustomUserDetails userDetails) {
        SynaAssert.isNull(getUser(userDetails.getUsername(), userDetails.getClientId()), "用户名 {} 已存在", userDetails.getUsername());
        UserEntity userEntity = UserEntity.builder()
                .clientId(userDetails.getClientId())
                .name(userDetails.getUsername())
                .password(userDetails.getPassword())
                .enabled(userDetails.isEnabled())
                .build();
        SynaAssert.isTrue(userService.save(userEntity), "新增用户失败!");
        return userEntity.getId();
    }

    /**
     * 获取并检查角色ID
     * <p>
     * 给用户新增角色时,这些角色必须已存在
     *
     * @param authorities 角色集合
     * @param clientId    客户端 id
     * @return 如果角色都存在, 返回这些角色的 id
     */
    private List<Long> getAndCheckRoleIds(Collection<? extends GrantedAuthority> authorities, String clientId) {
        if (IterUtil.isEmpty(authorities)) {
            return Collections.emptyList();
        }
        List<String> roleNames = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .distinct()
                .toList();
        // 通过角色名和客户端 id 查询
        List<RoleEntity> existedRoles = roleService.list(
                Wrappers.<RoleEntity>lambdaQuery()
                        .in(RoleEntity::getName, roleNames)
                        .eq(RoleEntity::getClientId, clientId)
        );
        return existedRoles.stream()
                .map(o -> {
                    SynaAssert.isTrue(roleNames.contains(o.getName()), "角色 {} 不存在", o.getName());
                    return o.getId();
                })
                .toList();
    }

    private void updateUserRole(List<Long> roleIds, long userId) {
        if (IterUtil.isEmpty(roleIds)) {
            return;
        }
        // 查询已有,排除已有,列出新增和删除
        List<Long> existed = userRoleService.list(Wrappers.<UserRoleEntity>lambdaQuery().eq(UserRoleEntity::getUserId, userId))
                .stream()
                .map(UserRoleEntity::getRoleId).toList();
        // 已有不在入参为删除
        List<Long> preDelete = existed.stream().filter(o -> !roleIds.contains(o)).toList();
        // 入参不在已有为新增
        List<Long> preAdd = roleIds.stream().filter(o -> !existed.contains(o)).toList();
        SynaAssert.isTrue(userRoleService.removeBatchByIds(preDelete), "删除用户角色失败!");
        List<UserRoleEntity> newUserRoles = preAdd.stream()
                .map(o -> UserRoleEntity.builder().userId(userId).roleId(o).build())
                .toList();
        SynaAssert.isTrue(userRoleService.saveBatch(newUserRoles), "新增用户角色失败!");
    }
}
