package com.maoatao.cas.security.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.maoatao.cas.core.entity.RoleEntity;
import com.maoatao.cas.core.entity.UserEntity;
import com.maoatao.cas.core.entity.UserRoleEntity;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.cas.core.service.RoleService;
import com.maoatao.cas.core.service.UserRoleService;
import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.security.CustomUserDetails;
import com.maoatao.cas.security.bean.CustomAuthority;
import com.maoatao.cas.security.bean.CustomUser;
import com.maoatao.cas.security.service.CustomUserDetailsService;
import com.maoatao.synapse.core.util.SynaAssert;
import com.maoatao.synapse.core.util.SynaSafes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * OAuth2 认证授权要用到的查询用户 逻辑
 *
 * @author MaoAtao
 * @date 2021-05-13 15:16:19
 */
@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

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
        SynaAssert.isTrue(userEntity.isEnabled(), "该用户已被禁用");
        return CustomUser.builder()
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
    public boolean createUser(CustomUserDetails user) {
        if (user instanceof CustomUser userDetails) {
            SynaAssert.notNull(registeredClientRepository.findByClientId(userDetails.getClientId()), "注册客户端不存在!");
            // TODO: 2023/2/28 校验用户已存在
            UserEntity userEntity = new UserEntity();
            userEntity.setClientId(userDetails.getClientId());
            userEntity.setName(userDetails.getUsername());
            userEntity.setPassword(userDetails.getPassword());
            userEntity.setEnabled(userDetails.isEnabled());
            SynaAssert.isTrue(userService.save(userEntity), "新增用户失败!");
            Set<RoleEntity> roleEntities = userDetails.getAuthorities().stream()
                    .map(o -> {
                        RoleEntity roleEntity = new RoleEntity();
                        roleEntity.setClientId(userDetails.getClientId());
                        roleEntity.setName(o.getAuthority());
                        return roleEntity;
                    })
                    .collect(Collectors.toSet());
            SynaAssert.isTrue(roleService.saveBatch(roleEntities), "新增角色失败!");
            Set<UserRoleEntity> userRoleEntities = roleEntities.stream()
                    .map(o -> {
                        UserRoleEntity userRoleEntity = new UserRoleEntity();
                        userRoleEntity.setUserId(userEntity.getId());
                        userRoleEntity.setRoleId(o.getId());
                        return userRoleEntity;
                    })
                    .collect(Collectors.toSet());
            SynaAssert.isTrue(userRoleService.saveBatch(userRoleEntities), "新增用户角色失败!");
        } else {
            throw new UnsupportedOperationException("不支持的新增用户参数");
        }
        return false;
    }

    @Override
    public boolean updateUser(CustomUserDetails user) {
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
}
