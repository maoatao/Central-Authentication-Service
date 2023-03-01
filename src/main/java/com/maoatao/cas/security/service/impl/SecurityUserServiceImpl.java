package com.maoatao.cas.security.service.impl;

import cn.hutool.core.collection.IterUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.maoatao.cas.core.entity.UserEntity;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.cas.core.service.RoleService;
import com.maoatao.cas.core.service.UserRoleService;
import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.security.bean.CustomUserDetails;
import com.maoatao.cas.security.bean.CustomAuthority;
import com.maoatao.cas.security.bean.ClientUser;
import com.maoatao.cas.security.service.SecurityUserService;
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
public class SecurityUserServiceImpl implements SecurityUserService {

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
        UserEntity userEntity = userService.getByNameAndClient(username, details.toString());
        SynaAssert.notNull(userEntity, "用户 {} 不存在!", username);
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
        SynaAssert.isTrue(
                userRoleService.updateUserRole(getAndCheckRoleIds(userDetails.getAuthorities(), userDetails.getClientId()), userId),
                "新用户绑定角色失败!"
        );
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

    /**
     * 新增用户
     *
     * @param userDetails 用户详情
     * @return 用户id(表主键)
     */
    private long saveUser(CustomUserDetails userDetails) {
        SynaAssert.isNull(getUser(userDetails.getUsername(), userDetails.getClientId()), "用户名 {} 已存在", userDetails.getUsername());
        UserEntity userEntity = UserEntity.builder()
                .clientId(userDetails.getClientId())
                .name(userDetails.getUsername())
                .password(userDetails.getPassword())
                .enabled(userDetails.isEnabled())
                .build();
        SynaAssert.isTrue(userService.save(userEntity), "新增用户失败!");
        SynaAssert.notNull(userEntity.getId(), "新增用户失败:用户 ID 为空!");
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
        return roleService.listByRolesAndClient(roleNames, clientId).stream()
                .map(o -> {
                    SynaAssert.isTrue(roleNames.contains(o.getName()), "角色 {} 不存在", o.getName());
                    return o.getId();
                })
                .toList();
    }
}
