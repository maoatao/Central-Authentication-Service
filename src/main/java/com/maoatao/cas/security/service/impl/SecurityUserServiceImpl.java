package com.maoatao.cas.security.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.IterUtil;
import com.maoatao.cas.core.entity.UserEntity;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.cas.core.service.RoleService;
import com.maoatao.cas.core.service.UserRoleService;
import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.security.bean.CustomUserDetails;
import com.maoatao.cas.security.bean.CustomAuthority;
import com.maoatao.cas.security.bean.SecurityUser;
import com.maoatao.cas.security.service.SecurityUserService;
import com.maoatao.synapse.core.util.SynaAssert;
import com.maoatao.synapse.core.util.SynaSafes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 获取权限
     *
     * @param userId 用户id
     * @return 权限列表
     */
    private List<CustomAuthority> getAuthorities(Long userId) {
        return SynaSafes.of(permissionService.getByUser(userId)).stream()
                .map(o -> CustomAuthority.builder().authority(o.getName()).client(o.getClientId()).build())
                .toList();
    }

    @Override
    public CustomUserDetails getUser(String username, Object details) throws UsernameNotFoundException {
        UserEntity userEntity = userService.getByNameAndClient(username, details.toString());
        SynaAssert.notNull(userEntity, "用户 {} 不存在!", username);
        SynaAssert.isTrue(userEntity.getEnabled(), "用户 {} 已被禁用", username);
        return SecurityUser.builder()
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
        checkClient(userDetails.getClientId());
        SynaAssert.isNull(userService.getByNameAndClient(userDetails.getUsername(), userDetails.getClientId()), "用户名 {} 已存在", userDetails.getUsername());
        UserEntity userEntity = UserEntity.builder()
                .clientId(userDetails.getClientId())
                .name(userDetails.getUsername())
                .password(userDetails.getPassword())
                .enabled(userDetails.isEnabled())
                .build();
        SynaAssert.isTrue(userService.save(userEntity), "新增用户失败!");
        SynaAssert.notNull(userEntity.getId(), "新增用户失败:用户 ID 为空!");
        SynaAssert.isTrue(
                userRoleService.updateUserRole(getAndCheckRoleIds(userDetails.getAuthorities(), userDetails.getClientId()), userEntity.getId()),
                "新用户绑定角色失败!"
        );
        return userEntity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(CustomUserDetails userDetails) {
        checkClient(userDetails.getClientId());
        UserEntity existed = getAndCheckUser(userDetails.getUsername(), userDetails.getClientId());
        BeanUtil.copyProperties(UserEntity.builder()
                .clientId(userDetails.getClientId())
                .name(userDetails.getUsername())
                .enabled(userDetails.isEnabled())
                .build(), existed);
        SynaAssert.isTrue(userService.updateById(existed), "更新用户失败!");
        SynaAssert.isTrue(
                userRoleService.updateUserRole(getAndCheckRoleIds(userDetails.getAuthorities(), userDetails.getClientId()), existed.getId()),
                "更新用户绑定角色失败!"
        );
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(String username, String clientId) {
        UserEntity existed = getAndCheckUser(username, clientId);
        SynaAssert.isTrue(userService.removeById(existed), "删除用户失败!");
        SynaAssert.isTrue(userRoleService.updateUserRole(null, existed.getId()), "删除用户解绑角色失败!");
        return true;
    }

    @Override
    public boolean changePassword(String username, String clientId, String oldPassword, String newPassword) {
        UserEntity existed = getAndCheckUser(username, clientId);
        SynaAssert.isTrue(passwordEncoder.matches(oldPassword, existed.getPassword()), "修改密码失败:原密码错误!");
        existed.setPassword(passwordEncoder.encode(newPassword));
        return userService.updateById(existed);
    }

    @Override
    public boolean userExists(String username, String clientId) {
        return userService.getByNameAndClient(username, clientId) != null;
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
     * 按用户名称和客户端 id 获取并校验用户是否存在
     *
     * @param username 用户名称
     * @param clientId 客户端 id
     * @return 用户
     */
    private UserEntity getAndCheckUser(String username, String clientId) {
        UserEntity existed = userService.getByNameAndClient(username, clientId);
        SynaAssert.notNull(existed, "用户 {} 不存在!", username);
        return existed;
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
