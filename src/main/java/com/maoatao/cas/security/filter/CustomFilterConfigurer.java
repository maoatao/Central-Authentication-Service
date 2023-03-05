package com.maoatao.cas.security.filter;

import com.maoatao.cas.core.service.AuthorizationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.DefaultSecurityFilterChain;
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
        AuthorizationFilter authorizationFilter = new AuthorizationFilter(oAuth2AuthorizationService, authorizationService);
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
        ResourcesFilter resourcesFilter = new ResourcesFilter();
        http.addFilterBefore(resourcesFilter, AuthorizationFilter.class);
        AuthorizationServerContextFilter authorizationServerContextFilter = new AuthorizationServerContextFilter(authorizationServerSettings);
        http.addFilterBefore(authorizationServerContextFilter, ResourcesFilter.class);
    }
}
