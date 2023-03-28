package com.maoatao.cas.security.bean;

import com.maoatao.cas.security.CustomUserAuthenticationProvider;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serial;
import java.util.Collection;
import java.util.Set;

/**
 * 自定义用户信息
 * <p>
 * Customized by {@link org.springframework.security.core.userdetails.User}
 *
 * @author MaoAtao
 * @date 2023-02-28 15:26:47
 */
@Slf4j
@Getter
@Builder
@ToString
public class CustomUserDetails implements UserDetails, CredentialsContainer {

    @Serial
    private static final long serialVersionUID = -1660229161744567202L;

    private final String openId;

    private String password;

    private final String clientId;

    private final String username;

    private final Set<String> roles;

    private final Set<String> permissions;

    private final boolean accountNonExpired;

    private final boolean accountNonLocked;

    private final boolean credentialsNonExpired;

    private final boolean enabled;

    /**
     * Calls the more complex constructor with all boolean arguments set to {@code true}.
     */
    public CustomUserDetails(String openId, String clientId, String username, String password, Set<String> roles, Set<String> permissions) {
        this(openId, clientId, username, password, roles, permissions, true, true, true, true);
    }

    /**
     * Construct the <code>CustomUser</code> with the details required by
     * {@link CustomUserAuthenticationProvider}.
     *
     * @param openId                开放 Id
     * @param clientId              客户端 Id
     * @param username              the username presented to the
     *                              <code>CustomAuthenticationProvider</code>
     * @param password              the password that should be presented to the
     *                              <code>CustomAuthenticationProvider</code>
     * @param roles                 角色
     * @param permissions           权限
     * @param enabled               set to <code>true</code> if the user is enabled
     * @param accountNonExpired     set to <code>true</code> if the account has not expired
     * @param credentialsNonExpired set to <code>true</code> if the credentials have not
     *                              expired
     * @param accountNonLocked      set to <code>true</code> if the account is not locked
     * @throws IllegalArgumentException if a <code>null</code> value was passed either as
     *                                  a parameter or as an element in the <code>GrantedAuthority</code> collection
     */
    public CustomUserDetails(String openId, String clientId, String username, String password, Set<String> roles, Set<String> permissions, boolean enabled, boolean accountNonExpired,
                             boolean credentialsNonExpired, boolean accountNonLocked) {
        Assert.isTrue(username != null && !"".equals(username) && password != null,
                "Cannot pass null or empty values to constructor");
        this.openId = openId;
        this.clientId = clientId;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.permissions = permissions;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
