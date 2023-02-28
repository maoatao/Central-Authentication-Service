package com.maoatao.cas.security.filter;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 令牌过滤配置器
 *
 * @author MaoAtao
 * @date 2022-10-24 11:16:14
 */
public class BearerTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    public BearerTokenFilterConfigurer(OAuth2AuthorizationService oAuth2AuthorizationService) {
        this.oAuth2AuthorizationService = oAuth2AuthorizationService;
    }

    @Override
    public void configure(HttpSecurity http) {
        BearerTokenFilter customFilter = new BearerTokenFilter(oAuth2AuthorizationService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}