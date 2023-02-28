package com.maoatao.cas.security;

import com.maoatao.cas.core.entity.PermissionEntity;
import com.maoatao.cas.core.entity.RoleEntity;
import com.maoatao.cas.core.entity.UserEntity;
import com.maoatao.cas.core.entity.UserRoleEntity;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.cas.core.service.RoleService;
import com.maoatao.cas.core.service.UserRoleService;
import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.security.bean.CustomAuthority;
import com.maoatao.cas.security.bean.CustomUserDetails;
import com.maoatao.synapse.core.lang.SynaException;
import com.maoatao.synapse.core.util.SynaAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * OAuth2 认证授权要用到的查询用户 逻辑
 *
 * @author MaoAtao
 * @date 2021-05-13 15:16:19
 */
@Component
public class CustomUserDetailsManager implements UserDetailsManager {

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

    @Override
    public UserDetails loadUserByUsername(String username) {
        String[] params = username.split(",");
        SynaAssert.isTrue(params.length == 2, "参数异常:username需要包含客户端ID(userName,clientId)");
        UserEntity userEntity = userService.getByNameAndClient(params[0], params[1]);
        SynaAssert.notNull(userEntity, "用户名或密码错误");
        SynaAssert.isTrue(userEntity.isEnabled(), "该用户已被禁用");
        return new User(userEntity.getName(), userEntity.getPassword(), true, true, true, true, getAuthorities(userEntity.getId()));
    }

    /**
     * 获取权限
     *
     * @param id 用户id
     * @return 权限列表
     */
    private List<CustomAuthority> getAuthorities(Long id) {
        List<CustomAuthority> authorities = new ArrayList<>();
        List<PermissionEntity> permissions = permissionService.getPermissionByUser(id);
        if (permissions != null) {
            permissions.forEach(entity -> authorities.add(
                    CustomAuthority.builder().authority(entity.getName()).client(entity.getClientId()).build()
            ));
        }
        return authorities;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(UserDetails user) {
        if (user instanceof CustomUserDetails userDetails) {
            SynaAssert.notNull(registeredClientRepository.findByClientId(userDetails.getClientId()), "注册客户端不存在!");
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
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}
