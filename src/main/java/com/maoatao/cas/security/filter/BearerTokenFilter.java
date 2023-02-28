package com.maoatao.cas.security.filter;

import com.maoatao.cas.security.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
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
 * 令牌过滤
 *
 * @author MaoAtao
 * @date 2022-10-24 11:17:31
 */
@Slf4j
public class BearerTokenFilter extends GenericFilterBean {

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    public BearerTokenFilter(OAuth2AuthorizationService oAuth2AuthorizationService) {
        this.oAuth2AuthorizationService = oAuth2AuthorizationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        doFilterInternal(httpServletRequest);
        chain.doFilter(request, response);
    }

    private void doFilterInternal(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        if (!StringUtils.hasText(token) || !token.startsWith(SecurityConstants.TOKEM_TYPE_BEARER)) {
            return;
        }
        // 去掉令牌前缀
        token = token.replace(SecurityConstants.TOKEM_TYPE_BEARER, "").trim();
        OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        SecurityContextHolder.getContext().setAuthentication(null);
        if (authorization == null || authorization.getAccessToken() == null || !authorization.getAccessToken().isActive()) {
            return;
        }
        Authentication principal = authorization.getAttribute(Principal.class.getName());
        if (principal == null || !principal.isAuthenticated()) {
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(principal);
    }
}
