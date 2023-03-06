package com.maoatao.cas.security.filter;

import com.maoatao.cas.core.service.AuthorizationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 令牌过滤配置器
 *
 * @author MaoAtao
 * @date 2022-10-24 11:16:14
 */
@Configuration
public class CustomFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    private final AuthorizationService authorizationService;

    private final AuthorizationServerSettings authorizationServerSettings;

    public CustomFilterConfigurer(OAuth2AuthorizationService oAuth2AuthorizationService, AuthorizationService authorizationService, AuthorizationServerSettings authorizationServerSettings) {
        this.oAuth2AuthorizationService = oAuth2AuthorizationService;
        this.authorizationService = authorizationService;
        this.authorizationServerSettings = authorizationServerSettings;
    }

    @Override
    public void configure(HttpSecurity http) {
        TokenFilter tokenFilter = new TokenFilter(oAuth2AuthorizationService, authorizationService);
        // 令牌过滤放在原 login 登录过滤前(登录过滤在添加了 clientId 参数后不能用了,不想重新写,且原接口禁止访问了)
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 资源过滤,放在令牌过滤前(禁止原有接口的请求)
        ResourcesFilter resourcesFilter = new ResourcesFilter();
        http.addFilterBefore(resourcesFilter, TokenFilter.class);
        // 获取令牌和授权码时要用,放在令牌过滤后
        AuthorizationServerContextFilter authorizationServerContextFilter = new AuthorizationServerContextFilter(authorizationServerSettings);
        http.addFilterAfter(authorizationServerContextFilter, TokenFilter.class);
    }
}
