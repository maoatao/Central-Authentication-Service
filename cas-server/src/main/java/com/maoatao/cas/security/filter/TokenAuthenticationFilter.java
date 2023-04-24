package com.maoatao.cas.security.filter;

import cn.hutool.core.util.StrUtil;
import com.maoatao.cas.security.authorization.CasServerSettings;
import com.maoatao.cas.security.bean.BasicAuthentication;
import com.maoatao.cas.security.service.CasAuthorizationService;
import com.maoatao.cas.util.FilterUtils;
import com.maoatao.daedalus.core.context.DefalutOperatorContext;
import com.maoatao.daedalus.core.context.OperatorContextHolder;
import com.maoatao.synapse.lang.exception.SynaException;
import com.maoatao.synapse.lang.util.SynaAssert;
import com.maoatao.synapse.lang.util.SynaSafes;
import com.maoatao.synapse.lang.util.SynaStrings;
import com.maoatao.daedalus.web.response.HttpResponseStatus;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * 授权过滤器
 *
 * @author MaoAtao
 * @date 2022-10-24 11:17:31
 */
@Slf4j
public class TokenAuthenticationFilter extends GenericFilterBean {

    private static final String TOKEM_TYPE_BASIC = "Basic";

    private static final String TOKEM_TYPE_BEARER = "Bearer";

    /**
     * 获取授权码和令牌的两个接口使用 {@value TOKEM_TYPE_BASIC} 鉴权
     */
    private static final List<RequestMatcher> BASIC_REQUEST_MATCHER = FilterUtils.requestMatchersBuilder()
            .antMatchers(HttpMethod.POST, "/code", "/token")
            .build();

    private final CasServerSettings casServerSettings;

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    private final CasAuthorizationService casAuthorizationService;

    private final HandlerExceptionResolver handlerExceptionResolver;

    public TokenAuthenticationFilter(CasServerSettings casServerSettings,
                                     OAuth2AuthorizationService oAuth2AuthorizationService,
                                     CasAuthorizationService casAuthorizationService,
                                     HandlerExceptionResolver handlerExceptionResolver) {
        SynaAssert.notNull(casServerSettings, "casServerSettings cannot be null");
        SynaAssert.notNull(oAuth2AuthorizationService, "oAuth2AuthorizationService cannot be null");
        SynaAssert.notNull(casAuthorizationService, "casAuthorizationService cannot be null");
        SynaAssert.notNull(handlerExceptionResolver, "handlerExceptionResolver cannot be null");
        this.casServerSettings = casServerSettings;
        this.oAuth2AuthorizationService = oAuth2AuthorizationService;
        this.casAuthorizationService = casAuthorizationService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        doFilterInternal((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    private void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            try {
                doTokenFilter(request);
            } catch (SynaException e) {
                // 拦截异常,屏蔽异常信息,这里统一返回未授权响应
                log.error("鉴权拦截异常", e);
                this.handlerExceptionResolver.resolveException(request, response, null, new SynaException(HttpResponseStatus.UNAUTHORIZED));
                return;
            }
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
        if (token.startsWith(TOKEM_TYPE_BASIC) && FilterUtils.anyMatch(BASIC_REQUEST_MATCHER, request)) {
            doBasicTokenFilter(token);
            return;
        }
        if (token.startsWith(TOKEM_TYPE_BEARER)) {
            doBearerTokenFilter(token);
        }
    }

    private void doBasicTokenFilter(String token) {
        // 去掉令牌前缀
        token = token.replace(TOKEM_TYPE_BASIC, SynaStrings.EMPTY).trim();
        BasicAuthentication basicAuthentication = FilterUtils.buildBasicAuthentication(token);
        if (basicAuthentication == null) {
            return;
        }
        // 根据 token 生成已授权的主体
        Optional.ofNullable(this.casAuthorizationService.generateClientPrincipal(basicAuthentication.username(), basicAuthentication.password()))
                .ifPresent(principal -> SecurityContextHolder.getContext().setAuthentication(principal));
    }

    private void doBearerTokenFilter(String token) {
        // 去掉令牌前缀
        token = token.replace(TOKEM_TYPE_BEARER, SynaStrings.EMPTY).trim();
        OAuth2Authorization authorization = this.oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (authorization == null || authorization.getAccessToken() == null || !authorization.getAccessToken().isActive()) {
            return;
        }
        setSecurityContext(authorization);
        setOperatorContext(token);
    }

    private void setSecurityContext(OAuth2Authorization authorization) {
        Authentication principal = authorization.getAttribute(Principal.class.getName());
        if (principal != null && principal.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(principal);
        }
    }

    private void setOperatorContext(String token) {
        Optional.ofNullable(this.casAuthorizationService.getAuthorizationInfo(token))
                .ifPresent(authorization -> OperatorContextHolder.setContext(DefalutOperatorContext.builder()
                        .operatorId(authorization.getOpenId())
                        .operatorName(authorization.getUser())
                        .permissions(SynaSafes.of(SynaSafes.of(authorization.getPermissions()).get(this.casServerSettings.getAppKey())))
                        .expiresAt(authorization.getExpiresAt())
                        .clientCredentials(authorization.isClientCredentials())
                        .build()));
    }
}
