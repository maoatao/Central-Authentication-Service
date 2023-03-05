package com.maoatao.cas.config;

import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.security.CustomUserAuthenticationProvider;
import com.maoatao.cas.security.HttpConstants;
import com.maoatao.cas.security.filter.CustomFilterConfigurer;
import com.maoatao.cas.security.oauth2.auth.CustomAccessTokenGenerator;
import com.maoatao.cas.security.oauth2.auth.CustomAuthorizationCodeAccessTokenProvider;
import com.maoatao.cas.security.oauth2.auth.CustomAuthorizationCodeGenerator;
import com.maoatao.cas.security.oauth2.auth.CustomRefreshTokenProvider;
import com.maoatao.cas.security.oauth2.auth.RedisAuthorizationService;
import com.maoatao.cas.security.oauth2.odic.CustomClientRegistrationProvider;
import com.maoatao.cas.util.AuthorizationUtils;
import com.maoatao.cas.security.oauth2.auth.CustomRefreshTokenGenerator;
import com.maoatao.cas.security.UUIDStringKeyGenerator;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;

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
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      OAuth2AuthorizationServerConfigurer configurer) throws Exception {
        http.securityMatcher(configurer.getEndpointsMatcher()).apply(configurer);
        return http.build();
    }

    /**
     * 配置Spring Security
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          CustomFilterConfigurer configurer) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(HttpConstants.WHITE_LIST).permitAll();
                    authorize.anyRequest().authenticated();
                })
                .apply(configurer);
        return http.build();
    }

    /**
     * 用户详细信息身份验证提供程序
     * <p>
     * 自定义查询用户,通过用户名称和客户端 id 查询一个用户.提供多客户端同名用户身份验证
     */
    @Bean
    public CustomUserAuthenticationProvider customUserAuthenticationProvider(UserService userService) {
        return new CustomUserAuthenticationProvider(userService);
    }

    /**
     * 自定义授权码生成访问令牌提供者
     */
    @Bean
    public CustomAuthorizationCodeAccessTokenProvider customAuthorizationCodeAccessTokenProvider(OAuth2AuthorizationService oAuth2AuthorizationService,
                                                                                                 OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator) {
        return new CustomAuthorizationCodeAccessTokenProvider(oAuth2AuthorizationService, oAuth2TokenGenerator);
    }


    /**
     * 授权码提供者(自定义授权码生成器)
     */
    @Bean
    public OAuth2AuthorizationCodeRequestAuthenticationProvider oAuth2AuthorizationCodeRequestAuthenticationProvider(RegisteredClientRepository registeredClientRepository,
                                                                                                                     OAuth2AuthorizationService oAuth2AuthorizationService,
                                                                                                                     OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService) {
        OAuth2AuthorizationCodeRequestAuthenticationProvider oAuth2AuthorizationCodeRequestAuthenticationProvider =
                new OAuth2AuthorizationCodeRequestAuthenticationProvider(registeredClientRepository, oAuth2AuthorizationService, oAuth2AuthorizationConsentService);
        // 配置自定义授权码生成器
        oAuth2AuthorizationCodeRequestAuthenticationProvider.setAuthorizationCodeGenerator(new CustomAuthorizationCodeGenerator(new UUIDStringKeyGenerator()));
        return oAuth2AuthorizationCodeRequestAuthenticationProvider;
    }

    /**
     * 自定义客户端注册提供者
     */
    @Bean
    public CustomClientRegistrationProvider customClientRegistrationProvider(RegisteredClientRepository registeredClientRepository,
                                                                             OAuth2AuthorizationService oAuth2AuthorizationService,
                                                                             OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator) {
        return new CustomClientRegistrationProvider(registeredClientRepository, oAuth2AuthorizationService, oAuth2TokenGenerator);
    }

    /**
     * 自定义刷新令牌提供者
     */
    @Bean
    public CustomRefreshTokenProvider customRefreshTokenProvider(OAuth2AuthorizationService oAuth2AuthorizationService,
                                                                 OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator) {
        return new CustomRefreshTokenProvider(oAuth2AuthorizationService, oAuth2TokenGenerator);
    }

    /**
     * OAuth2 授权服务器配置程序(自定义令牌提供者)
     */
    @Bean
    public OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer(CustomAuthorizationCodeAccessTokenProvider accessTokenProvider,
                                                                                   OAuth2AuthorizationCodeRequestAuthenticationProvider authorizationCodeProvider,
                                                                                   CustomClientRegistrationProvider clientRegistrationProvider,
                                                                                   CustomRefreshTokenProvider refreshTokenProvider) {
        OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        oAuth2AuthorizationServerConfigurer.authorizationEndpoint(endpoint -> endpoint
                .authenticationProvider(authorizationCodeProvider)
                .authenticationProvider(clientRegistrationProvider)
                .authenticationProvider(refreshTokenProvider)
                .authenticationProvider(accessTokenProvider));
        return oAuth2AuthorizationServerConfigurer;
    }

    /**
     * 认证管理
     */
    @Bean
    public AuthenticationManager authenticationManager(CustomUserAuthenticationProvider userAuthenticationProvider,
                                                       CustomAuthorizationCodeAccessTokenProvider accessTokenProvider) {
        return new ProviderManager(userAuthenticationProvider, accessTokenProvider);
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
        KeyPair keyPair = AuthorizationUtils.generateRsaKey();
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
                jwsHeader.header("customerHeader", "这是一个自定义access_token header");
                claims.claim("customerClaim", "这是一个自定义access_token claim");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

