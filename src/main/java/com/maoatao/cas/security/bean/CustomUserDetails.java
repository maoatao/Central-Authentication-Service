package com.maoatao.cas.security.bean;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;

/**
 * 自定义用户详情
 *
 * @author MaoAtao
 * @date 2023-02-28 15:26:47
 */
@Getter
@ToString
public class CustomUserDetails extends User {

    @Serial
    private static final long serialVersionUID = -1660229161744567202L;

    private final String clientId;

    public CustomUserDetails(String clientId, UserDetails user) {
        this(clientId, user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities());
    }

    public CustomUserDetails(String clientId, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.clientId = clientId;
    }
}
