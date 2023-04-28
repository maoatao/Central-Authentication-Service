package com.maoatao.cas.security.bean;

import com.maoatao.cas.security.authorization.CustomUserAuthenticationProvider;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Set;

/**
 * 自定义用户信息
 * <p>
 * Customized by {@link org.springframework.security.core.userdetails.User}
 * <p>
 * {@link CustomUserAuthenticationProvider}
 *
 * @author MaoAtao
 * @date 2023-02-28 15:26:47
 */
@Getter
@Builder
@ToString
public class CustomUserDetails implements UserDetails, CredentialsContainer {

    @Serial
    private static final long serialVersionUID = -1660229161744567202L;

    private final String openId;

    private String password;

    private final String username;

    @Setter
    private Map<String, Set<String>> permissions;

    private final boolean accountNonExpired;

    private final boolean accountNonLocked;

    private final boolean credentialsNonExpired;

    private final boolean enabled;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.permissions.entrySet().stream()
                .map(o -> o.getValue().stream().map(x -> ClientAuthority.builder().clientId(o.getKey()).permission(x).build()).toList())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
