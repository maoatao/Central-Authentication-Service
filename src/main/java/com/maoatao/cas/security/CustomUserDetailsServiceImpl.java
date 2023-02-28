package com.maoatao.cas.security;

import com.maoatao.cas.core.entity.PermissionEntity;
import com.maoatao.cas.core.entity.UserEntity;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.cas.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * OAuth2 认证授权要用到的查询用户 逻辑
 *
 * @author MaoAtao
 * @date 2021-05-13 15:16:19
 */
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.getUserByName(username);
        if (userEntity != null) {
            return new User(userEntity.getName(), userEntity.getPassword(), true, true, true, true, getAuthorities(userEntity.getId()));
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    /**
     * 获取权限
     *
     * @param id 用户id
     * @return 权限列表
     */
    private List<CustomAuthority> getAuthorities(String id) {
        List<CustomAuthority> authorities = new ArrayList<>();
        List<PermissionEntity> permissions = permissionService.getPermissionByUser(id);
        if (permissions != null) {
            permissions.forEach(entity -> authorities.add(
                    CustomAuthority.builder().authority(entity.getName()).client(entity.getClientId()).build()
            ));
        }
        return authorities;
    }
}
