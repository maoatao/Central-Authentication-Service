package com.maoatao.cas.config;

import com.maoatao.cas.security.configurer.CustomFormLoginConfigurer;
import com.maoatao.cas.security.configurer.TokenAuthenticationFilterConfigurer;
import com.maoatao.cas.security.configurer.AuthorizationServerContextFilterConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 基础配置
 *
 * @author MaoAtao
 * @date 2023-03-06 13:58:25
 */
@Configuration
public class SecutityConfig {

    /**
     * 请求拦截,过滤器配置
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        applyRequestMatchers(http);
        applyConfigurers(http);
        return http.build();
    }

    /**
     * 密码加密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 自定义配置
     * <p>
     * {@link org.springframework.security.web.FilterChainProxy}
     */
    private void applyConfigurers(HttpSecurity http) throws Exception {
        // 令牌
        http.apply(new TokenAuthenticationFilterConfigurer());
        // 授权服务器上下文配置
        http.apply(new AuthorizationServerContextFilterConfigurer());
        // 用户登录授权(自定义了登录页面Fliter 不要使用默认的 http.formLogin 否则这里的配置无效)
        CustomFormLoginConfigurer<HttpSecurity> customFormLoginConfigurer = new CustomFormLoginConfigurer<>();
        // 这里配置页面和处理登录请求前必须配置 securityBuilder
        customFormLoginConfigurer.setBuilder(http);
        customFormLoginConfigurer.loginPage("/login").loginProcessingUrl("/login");
        http.apply(customFormLoginConfigurer);
    }

    /**
     * 配置授权访问的白名单(匿名访问)
     */
    private void applyRequestMatchers(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeHttpRequests -> {
            authorizeHttpRequests.requestMatchers(
                    "/login", "/consent",
                    "/static/**", "/error", "/favicon.ico",
                    "/swagger-ui/**", "/swagger-resources/**", "/webjars/**", "/v3/**", "/api/**", "/doc.html"
            ).permitAll();
            authorizeHttpRequests.requestMatchers(HttpMethod.GET, "/token").permitAll();
            authorizeHttpRequests.requestMatchers(HttpMethod.DELETE, "/token").permitAll();
            authorizeHttpRequests.anyRequest().authenticated();
        });
    }
}
