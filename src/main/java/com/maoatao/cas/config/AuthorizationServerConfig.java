package com.maoatao.cas.config;

import com.maoatao.cas.security.CustomUserDetailsAuthenticationProvider;
import com.maoatao.cas.security.oauth2.auth.CustomAccessTokenGenerator;
import com.maoatao.cas.security.oauth2.auth.RedisAuthorizationService;
import com.maoatao.cas.security.filter.BearerTokenFilterConfigurer;
import com.maoatao.cas.security.service.CustomUserDetailsService;
import com.maoatao.cas.util.AuthorizationServerUtils;
import com.maoatao.cas.security.oauth2.auth.CustomRefreshTokenGenerator;
import com.maoatao.cas.security.generator.UUIDStringKeyGenerator;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * @author MaoAtao
 * @date 2022-10-05 22:15:01
 */
@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {

    private static final String OAUTH2_REDIS_KEY_PREFIX = "OAuth2:";

    /**
     * 配置OAuth2
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        AuthorizationServerUtils.applyConfigurer(http);
        http.exceptionHandling(exceptions ->
                exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
        );
        return http.build();
    }

    /**
     * 配置Spring Security
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          OAuth2AuthorizationService oAuth2AuthorizationService) throws Exception {
        permitSwagger(http);
        applyFilterConfigurer(http, oAuth2AuthorizationService);
        http.formLogin(Customizer.withDefaults())
                .httpBasic()
                .and()
                .csrf()
                .disable();
        return http.build();
    }

    /**
     * 用户详细信息身份验证提供程序
     */
    @Bean
    public AbstractUserDetailsAuthenticationProvider abstractUserDetailsAuthenticationProvider(CustomUserDetailsService userDetailsService) {
        CustomUserDetailsAuthenticationProvider customUserDetailsAuthenticationProvider = new CustomUserDetailsAuthenticationProvider();
        customUserDetailsAuthenticationProvider.setUserDetailsService(userDetailsService);
        return customUserDetailsAuthenticationProvider;
    }

    /**
     * 认证管理(password模式需要配置AuthenticationManager)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 客户端服务
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    /**
     * 生成令牌服务
     */
    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService() {
        return new RedisAuthorizationService(OAUTH2_REDIS_KEY_PREFIX);
    }

    /**
     * 记录授权服务
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 默认配置
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * 令牌生成器
     */
    @Bean
    public OAuth2TokenGenerator<OAuth2Token> tokenGenerator(JwtEncoder jwtEncoder,
                                                            OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer) {
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        jwtGenerator.setJwtCustomizer(jwtCustomizer);
        CustomAccessTokenGenerator customAccessTokenGenerator = new CustomAccessTokenGenerator(new UUIDStringKeyGenerator());
        CustomRefreshTokenGenerator customRefreshTokenGenerator = new CustomRefreshTokenGenerator(new UUIDStringKeyGenerator());
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, customAccessTokenGenerator, customRefreshTokenGenerator);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = AuthorizationServerUtils.generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            JwsHeader.Builder jwsHeader = context.getJwsHeader();
            JwtClaimsSet.Builder claims = context.getClaims();
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                // Customize headers/claims for access_token
                jwsHeader.header("customerHeader", "这是一个自定义access_token header");
                claims.claim("customerClaim", "这是一个自定义access_token claim");
            } else if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
                // Customize headers/claims for id_token
                jwsHeader.header("customerHeader", "这是一个自定义id_token header");
                claims.claim("customerClaim", "这是一个自定义id_token claim");
            }
        };
    }


    /**
     * 配置令牌过滤器并放行部分请求
     */
    private void applyFilterConfigurer(HttpSecurity http, OAuth2AuthorizationService oAuth2AuthorizationService) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/authorization").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/authorization").permitAll())
                .apply(new BearerTokenFilterConfigurer(oAuth2AuthorizationService))
                .and()
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
    }

    /**
     * 放行swagger
     */
    private void permitSwagger(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/webjars/**", "/v3/**", "/api/**", "/doc.html")
                .permitAll();
    }
}

