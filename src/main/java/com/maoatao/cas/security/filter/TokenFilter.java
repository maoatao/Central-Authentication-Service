package com.maoatao.cas.security.filter;

import cn.hutool.core.util.StrUtil;
import com.maoatao.cas.core.service.AuthorizationService;
import com.maoatao.cas.security.bean.ClientUser;
import com.maoatao.cas.util.FilterUtils;
import com.maoatao.synapse.core.lang.SynaException;
import com.maoatao.synapse.core.web.HttpResponseStatus;
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
 * 授权过滤器
 * <p>
 * 检查{@value TOKEM_TYPE_BEARER}令牌
 *
 * @author MaoAtao
 * @date 2022-10-24 11:17:31
 */
public class TokenFilter extends GenericFilterBean {

    private static final String TOKEM_TYPE_BASIC = "Basic";

    private static final String TOKEM_TYPE_BEARER = "Bearer";

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    private final AuthorizationService authorizationService;

    public TokenFilter(OAuth2AuthorizationService oAuth2AuthorizationService, AuthorizationService authorizationService) {
        this.oAuth2AuthorizationService = oAuth2AuthorizationService;
        this.authorizationService = authorizationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilterInternal(request);
        chain.doFilter(request, response);
        // Full Authentication Is Required To Access This Resource
        // throw new SynaException(HttpResponseStatus.UNAUTHORIZED);
    }

    private void doFilterInternal(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("Authorization");
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
        token = token.replace(TOKEM_TYPE_BASIC, "").trim();
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
        token = token.replace(TOKEM_TYPE_BEARER, "").trim();
        OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (authorization == null || authorization.getAccessToken() == null || !authorization.getAccessToken().isActive()) {
            return;
        }
        Authentication principal = authorization.getAttribute(Principal.class.getName());
        boolean isClient = AuthorizationGrantType.CLIENT_CREDENTIALS.equals(authorization.getAuthorizationGrantType());
        if (isClient){
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
