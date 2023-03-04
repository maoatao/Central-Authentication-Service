package com.maoatao.cas.security.filter;

import com.maoatao.cas.util.FilterUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.StringUtils;
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
public class AuthorizationFilter extends GenericFilterBean {

    private static final String TOKEM_TYPE_BEARER = "Bearer";

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    public AuthorizationFilter(OAuth2AuthorizationService oAuth2AuthorizationService) {
        this.oAuth2AuthorizationService = oAuth2AuthorizationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (doFilterInternal(httpServletRequest)) {
            chain.doFilter(request, response);
        } else {
            FilterUtils.unauthorized(response);
        }
    }

    private boolean doFilterInternal(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        if (!StringUtils.hasText(token) || !token.startsWith(TOKEM_TYPE_BEARER)) {
            return false;
        }
        // 去掉令牌前缀
        token = token.replace(TOKEM_TYPE_BEARER, "").trim();
        OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        SecurityContextHolder.getContext().setAuthentication(null);
        if (authorization == null || authorization.getAccessToken() == null || !authorization.getAccessToken().isActive()) {
            return false;
        }
        Authentication principal = authorization.getAttribute(Principal.class.getName());
        if (principal == null || !principal.isAuthenticated()) {
            return false;
        }
        SecurityContextHolder.getContext().setAuthentication(principal);
        return true;
    }
}
