package com.maoatao.cas.security.bean;

import lombok.Data;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 授权信息
 *
 * @author MaoAtao
 * @date 2022-10-28 17:54:45
 */
@Getter
public class AuthorizationInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = -1540019424256148805L;

    /**
     * 被授予令牌的主体
     */
    private final Context context;
    /**
     * 令牌是否有效
     */
    private final boolean active;

    private AuthorizationInfo(Context context, boolean active) {
        this.context = context;
        this.active = active;
    }

    public static AuthorizationInfo of(Authentication principal, boolean isActive) {
        if (principal != null) {
            Context context = new Context();
            context.setPrincipal(principal.getPrincipal());
            context.setCredentials(principal.getCredentials());
            List<CustomAuthority> authorities = new ArrayList<>();
            for (GrantedAuthority authority : principal.getAuthorities()) {
                authorities.add((CustomAuthority)authority);
            }
            context.setAuthorities(authorities);
            context.setDetails(principal.getDetails());
            context.setAuthenticated(principal.isAuthenticated());
            return new AuthorizationInfo(context, isActive);
        }
        return null;
    }

    public static UsernamePasswordAuthenticationToken toAuthenticationToken(AuthorizationInfo authorizationInfo) {
        Context context = authorizationInfo.getContext();
        return !context.isAuthenticated()
                ? UsernamePasswordAuthenticationToken.unauthenticated(context.getPrincipal(), context.getCredentials())
                : UsernamePasswordAuthenticationToken.authenticated(context.getPrincipal(), context.getCredentials(), context.getAuthorities());
    }

    /**
     * {@link UsernamePasswordAuthenticationToken}
     */
    @Data
    private static class Context implements Serializable {
        @Serial
        private static final long serialVersionUID = -1494320068675864592L;

        private Object principal;
        private Object credentials;
        // 这个种类不能写 Collection<? extends GrantedAuthority> 会引起feign调用报simple type的错
        private Collection<CustomAuthority> authorities;
        private Object details;
        private boolean authenticated;
    }
}
