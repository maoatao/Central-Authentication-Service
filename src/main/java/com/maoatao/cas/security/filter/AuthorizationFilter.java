package com.maoatao.cas.security.filter;

import cn.hutool.core.util.StrUtil;
import com.maoatao.cas.core.service.AuthorizationService;
import com.maoatao.cas.security.bean.ClientUser;
import com.maoatao.cas.util.FilterUtils;
import com.maoatao.synapse.lang.exception.SynaException;
import com.maoatao.synapse.lang.util.SynaAssert;
import com.maoatao.synapse.lang.util.SynaSafes;
import com.maoatao.synapse.lang.util.SynaStrings;
import com.maoatao.synapse.web.response.HttpResponseStatus;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.security.Principal;
import java.util.Set;

/**
 * 授权过滤器
 * <p>
 * 检查{@value TOKEM_TYPE_BEARER}令牌
 *
 * @author MaoAtao
 * @date 2022-10-24 11:17:31
 */
@Component
public class AuthorizationFilter extends GenericFilterBean {

    private static final String TOKEM_TYPE_BASIC = "Basic";

    private static final String TOKEM_TYPE_BEARER = "Bearer";

    private static final Set<String> WHITE_LIST = Set.of(
            "/error",
            "/swagger-ui",
            "/swagger-resources",
            "/webjars",
            "/v3",
            "/api",
            "/doc.html",
            "/favicon.ico"
    );

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    private final AuthorizationService authorizationService;

    private final HandlerExceptionResolver resolver;

    public AuthorizationFilter(OAuth2AuthorizationService oAuth2AuthorizationService,
                               AuthorizationService authorizationService,
                               @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        SynaAssert.notNull(oAuth2AuthorizationService, "oAuth2AuthorizationService cannot be null");
        SynaAssert.notNull(authorizationService, "authorizationService cannot be null");
        SynaAssert.notNull(resolver, "handlerExceptionResolver cannot be null");
        this.oAuth2AuthorizationService = oAuth2AuthorizationService;
        this.authorizationService = authorizationService;
        this.resolver = resolver;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        doFilterInternal((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    private void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!isPermit(request) && !isAuthenticated(request)) {
            // 既不是白名单也没有授权就拦截请求(抛出异常,全局拦截)
            resolver.resolveException(request, response, null, new SynaException(HttpResponseStatus.UNAUTHORIZED));
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isPermit(HttpServletRequest request) {
        String url = SynaSafes.of(request.getRequestURI());
        for (String s : WHITE_LIST) {
            if (url.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            return false;
        }
        if (token.startsWith(TOKEM_TYPE_BASIC)) {
            return isBasicAuthenticated(token);
        }
        if (token.startsWith(TOKEM_TYPE_BEARER)) {
            return isBearerAuthenticated(token);
        }
        return false;
    }

    private boolean isBasicAuthenticated(String token) {
        // 去掉令牌前缀
        token = token.replace(TOKEM_TYPE_BASIC, SynaStrings.EMPTY).trim();
        ClientUser clientUser = FilterUtils.buildClientUserByToken(token);
        if (clientUser == null) {
            return false;
        }
        Authentication principal = authorizationService.generatePrincipal(clientUser);
        if (principal == null) {
            return false;
        }
        SecurityContextHolder.getContext().setAuthentication(principal);
        return true;
    }

    private boolean isBearerAuthenticated(String token) {
        // 去掉令牌前缀
        token = token.replace(TOKEM_TYPE_BEARER, SynaStrings.EMPTY).trim();
        OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (authorization == null || authorization.getAccessToken() == null || !authorization.getAccessToken().isActive()) {
            return false;
        }
        Authentication principal = authorization.getAttribute(Principal.class.getName());
        boolean isClient = AuthorizationGrantType.CLIENT_CREDENTIALS.equals(authorization.getAuthorizationGrantType());
        if (isClient) {
            // TODO: 2023-03-06 18:10:26 客户端授权主体咋搞?
            SecurityContextHolder.getContext().setAuthentication(principal);
            return true;
        }
        boolean isAuthenticated = principal != null && principal.isAuthenticated();
        if (isAuthenticated) {
            SecurityContextHolder.getContext().setAuthentication(principal);
            return true;
        }
        return false;
    }
}
