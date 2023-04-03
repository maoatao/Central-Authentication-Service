package com.maoatao.cas.config;

import com.maoatao.cas.security.authorization.CasServerSettings;
import com.maoatao.cas.common.keygen.AlphabetDigitalGenerator;
import com.maoatao.cas.security.oauth2.auth.service.RedisAuthorizationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * 授权服务基本配置
 *
 * @author MaoAtao
 * @date 2022-10-05 22:15:01
 */
@Configuration
@EnableWebSecurity
public class CasServerConfig {

    private static final String OAUTH2_REDIS_KEY_PREFIX = "OAuth2:";

    /**
     * 配置授权服务
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      OAuth2AuthorizationServerConfigurer configurer) throws Exception {
        http.securityMatcher(configurer.getEndpointsMatcher())
                .exceptionHandling((exceptions) ->
                        exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
                .csrf(csrf -> csrf.ignoringRequestMatchers(configurer.getEndpointsMatcher()))
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .authorizeHttpRequests().anyRequest().authenticated()
                .and()
                .apply(configurer);
        return http.build();
    }

    /**
     * 客户端服务
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        // TODO: 2023-03-06 14:03:49 这个要改,改成自己的客户端表和服务
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    /**
     * 生成令牌服务
     * <p>
     * 官方提供了内存和数据库储存授权信息,根据需要自己实现了redis储存令牌信息
     */
    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService() {
        return new RedisAuthorizationService(OAUTH2_REDIS_KEY_PREFIX);
    }

    /**
     * 记录授权服务
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
                                                                         RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 授权服务默认配置
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * 默认CAS服务端配置
     */
    @Bean
    public CasServerSettings casServerSettings() {
        return CasServerSettings.builder().build();
    }
}

