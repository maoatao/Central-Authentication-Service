package com.maoatao.cas.config.secutity;

import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.security.CustomUserAuthenticationProvider;
import com.maoatao.cas.security.UUIDStringKeyGenerator;
import com.maoatao.cas.security.oauth2.auth.CustomAuthorizationCodeAccessTokenProvider;
import com.maoatao.cas.security.oauth2.auth.CustomAuthorizationCodeGenerator;
import com.maoatao.cas.security.oauth2.auth.CustomRefreshTokenProvider;
import com.maoatao.cas.security.oauth2.odic.CustomClientRegistrationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.ClientSecretAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * 提供者配置
 * <p>
 * 这是大部分是自定义的提供者,需要重新配置替换原有的
 *
 * @author MaoAtao
 * @date 2023-03-06 13:54:38
 */
@Configuration
public class AuthenticationProviderConfig {

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
     *
     * @param userAuthenticationProvider   用户身份授权
     * @param clientAuthenticationProvider 客户端授权
     * @param accessTokenProvider          授权码模式 生成访问令牌
     * @param refreshTokenProvider         刷新令牌模式 生成访问令牌
     * @param clientTokenProvider          客户端模式 生成访问令牌
     */
    @Bean
    public AuthenticationManager authenticationManager(CustomUserAuthenticationProvider userAuthenticationProvider,
                                                       ClientSecretAuthenticationProvider clientAuthenticationProvider,
                                                       CustomAuthorizationCodeAccessTokenProvider accessTokenProvider,
                                                       CustomRefreshTokenProvider refreshTokenProvider,
                                                       OAuth2ClientCredentialsAuthenticationProvider clientTokenProvider) {
        return new ProviderManager(userAuthenticationProvider, clientAuthenticationProvider,
                accessTokenProvider, refreshTokenProvider, clientTokenProvider);
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
     * 客户端身份验证提供程序
     */
    @Bean
    public ClientSecretAuthenticationProvider clientSecretAuthenticationProvider(RegisteredClientRepository registeredClientRepository,
                                                                                 OAuth2AuthorizationService oAuth2AuthorizationService) {
        return new ClientSecretAuthenticationProvider(registeredClientRepository, oAuth2AuthorizationService);
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
     * 客户端模式生成访问令牌提供者
     */
    @Bean
    public OAuth2ClientCredentialsAuthenticationProvider oAuth2ClientCredentialsAuthenticationProvider(OAuth2AuthorizationService oAuth2AuthorizationService,
                                                                                                       OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator) {
        return new OAuth2ClientCredentialsAuthenticationProvider(oAuth2AuthorizationService, oAuth2TokenGenerator);
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


}
