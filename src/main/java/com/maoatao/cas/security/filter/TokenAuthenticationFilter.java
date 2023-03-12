package com.maoatao.cas.security.filter;

import cn.hutool.core.util.StrUtil;
import com.maoatao.cas.core.service.AuthorizationService;
import com.maoatao.cas.security.bean.ClientUser;
import com.maoatao.cas.util.FilterUtils;
import com.maoatao.synapse.lang.util.SynaAssert;
import com.maoatao.synapse.lang.util.SynaStrings;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.security.Principal;

/**
 * 授权过滤器1
 *
 * @author MaoAtao
 * @date 2022-10-24 11:17:31
 */
public class TokenAuthenticationFilter extends GenericFilterBean {

    private static final String TOKEM_TYPE_BASIC = "Basic";

    private static final String TOKEM_TYPE_BEARER = "Bearer";

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    private final AuthorizationService authorizationService;

    public TokenAuthenticationFilter(OAuth2AuthorizationService oAuth2AuthorizationService,
                                     AuthorizationService authorizationService) {
        SynaAssert.notNull(oAuth2AuthorizationService, "oAuth2AuthorizationService cannot be null");
        SynaAssert.notNull(authorizationService, "authorizationService cannot be null");
        this.oAuth2AuthorizationService = oAuth2AuthorizationService;
        this.authorizationService = authorizationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        doFilterInternal((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    private void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            doTokenFilter(request);
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private void doTokenFilter(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            return;
        }
        if (token.startsWith(TOKEM_TYPE_BASIC)) {
            doBasicTokenFilter(token);
        }
        if (token.startsWith(TOKEM_TYPE_BEARER)) {
            doBearerTokenFilter(token);
        }
    }

    private void doBasicTokenFilter(String token) {
        // 去掉令牌前缀
        token = token.replace(TOKEM_TYPE_BASIC, SynaStrings.EMPTY).trim();
        ClientUser clientUser = FilterUtils.buildClientUserByToken(token);
        if (clientUser == null) {
            return;
        }
        Authentication principal = authorizationService.generatePrincipal(clientUser);
        if (principal == null) {
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(principal);
    }

    private void doBearerTokenFilter(String token) {
        // 去掉令牌前缀
        token = token.replace(TOKEM_TYPE_BEARER, SynaStrings.EMPTY).trim();
        OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (authorization == null || authorization.getAccessToken() == null || !authorization.getAccessToken().isActive()) {
            return;
        }
        Authentication principal = authorization.getAttribute(Principal.class.getName());
        boolean isClient = AuthorizationGrantType.CLIENT_CREDENTIALS.equals(authorization.getAuthorizationGrantType());
        if (isClient) {
            // TODO: 2023-03-06 18:10:26 客户端授权主体咋搞?
            SecurityContextHolder.getContext().setAuthentication(principal);
            return;
        }
        boolean isAuthenticated = principal != null && principal.isAuthenticated();
        if (isAuthenticated) {
            SecurityContextHolder.getContext().setAuthentication(principal);
        }
    }
}
