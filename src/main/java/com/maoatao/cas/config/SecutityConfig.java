package com.maoatao.cas.config;

import com.maoatao.cas.security.configurer.AuthorizationFilterConfigurer;
import com.maoatao.cas.security.configurer.AuthorizationServerContextFilterConfigurer;
import com.maoatao.cas.security.configurer.CustomLoginPageGeneratingFilterConfigurer;
import com.maoatao.cas.security.configurer.CustomUserAuthenticationFilterConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
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
        http.csrf().disable()
                .authorizeHttpRequests()
                .anyRequest().authenticated();
        http.formLogin(Customizer.withDefaults());
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
    @SuppressWarnings("unchecked")
    private void applyConfigurers(HttpSecurity http) throws Exception {
        // 移除 AuthorizationFilter
        http.removeConfigurer(AuthorizeHttpRequestsConfigurer.class);

        // 权限,白名单拦截
        http.apply(new AuthorizationFilterConfigurer());
        // 授权服务器上下文配置
        http.apply(new AuthorizationServerContextFilterConfigurer());
        // 用户登录授权
        http.apply(new CustomUserAuthenticationFilterConfigurer());
        // 登录页面
        http.apply(new CustomLoginPageGeneratingFilterConfigurer());
    }
}
