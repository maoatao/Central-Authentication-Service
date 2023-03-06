package com.maoatao.cas.config.secutity;

import com.maoatao.cas.security.HttpConstants;
import com.maoatao.cas.security.filter.CustomFilterConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          CustomFilterConfigurer configurer) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpConstants.WHITE_LIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(configurer);
        return http.build();
    }

    /**
     * 密码加密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
