package com.maoatao.cas.config;

import com.maoatao.cas.core.service.ClientUserService;
import com.maoatao.cas.security.authorization.CustomClientSecretAuthenticationProvider;
import com.maoatao.cas.security.authorization.CustomUserAuthenticationProvider;
import com.maoatao.cas.security.oauth2.auth.provider.CustomAuthorizationCodeAccessTokenProvider;
import com.maoatao.cas.security.oauth2.auth.provider.CustomAuthorizationCodeProvider;
import com.maoatao.cas.security.oauth2.auth.provider.CustomAuthorizationConsentProvider;
import com.maoatao.cas.security.oauth2.auth.provider.CustomClientCredentialsTokenProvider;
import com.maoatao.cas.security.oauth2.auth.provider.CustomRefreshTokenProvider;
import com.maoatao.cas.security.oauth2.odic.CustomClientRegistrationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
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
public class ProviderConfig {

    /**
     * OAuth2 授权服务器配置程序(给原生接口使用的配置)
     *
     * @param authorizationCodeProvider    授权码提供者(无需授权同意)
     * @param authorizationConsentProvider 授权码提供者(需授权同意)
     * @param clientRegistrationProvider   客户端注册提供者
     * @param accessTokenProvider          授权码模式 生成访问令牌
     * @param refreshTokenProvider         刷新令牌模式 生成访问令牌
     * @param clientTokenProvider          客户端模式 生成访问令牌
     */
    @Bean
    public OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer(
            CustomAuthorizationCodeProvider authorizationCodeProvider,
            CustomAuthorizationConsentProvider authorizationConsentProvider,
            CustomClientRegistrationProvider clientRegistrationProvider,
            CustomAuthorizationCodeAccessTokenProvider accessTokenProvider,
            CustomRefreshTokenProvider refreshTokenProvider,
            CustomClientCredentialsTokenProvider clientTokenProvider) {
        OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        oAuth2AuthorizationServerConfigurer.authorizationEndpoint(endpoint -> endpoint
                .authenticationProvider(authorizationCodeProvider)
                .authenticationProvider(authorizationConsentProvider)
                .authenticationProvider(clientRegistrationProvider)
                .authenticationProvider(refreshTokenProvider)
                .authenticationProvider(accessTokenProvider)
                .authenticationProvider(clientTokenProvider)
        );
        return oAuth2AuthorizationServerConfigurer;
    }

    /**
     * 认证管理(和{@link #oAuth2AuthorizationServerConfigurer} 类似,是给自己的接口使用的)
     *
     * @param userAuthenticationProvider   用户身份授权
     * @param clientAuthenticationProvider 客户端授权
     * @param accessTokenProvider          授权码模式 生成访问令牌
     * @param refreshTokenProvider         刷新令牌模式 生成访问令牌
     * @param clientTokenProvider          客户端模式 生成访问令牌
     */
    @Bean
    public AuthenticationManager authenticationManager(
            CustomUserAuthenticationProvider userAuthenticationProvider,
            CustomClientSecretAuthenticationProvider clientAuthenticationProvider,
            CustomAuthorizationCodeAccessTokenProvider accessTokenProvider,
            CustomRefreshTokenProvider refreshTokenProvider,
            CustomClientCredentialsTokenProvider clientTokenProvider) {
        return new ProviderManager(userAuthenticationProvider, clientAuthenticationProvider,
                accessTokenProvider, refreshTokenProvider, clientTokenProvider);
    }

    /**
     * 用户信息身份验证提供程序
     * <p>
     * 自定义查询用户,通过用户名称和客户端 id 查询一个用户.提供多客户端同名用户身份验证
     */
    @Bean
    public CustomUserAuthenticationProvider customUserAuthenticationProvider(ClientUserService clientUserService) {
        return new CustomUserAuthenticationProvider(clientUserService);
    }

    /**
     * 客户端身份验证提供程序
     */
    @Bean
    public CustomClientSecretAuthenticationProvider customClientSecretAuthenticationProvider(RegisteredClientRepository registeredClientRepository,
                                                                                             OAuth2AuthorizationService oAuth2AuthorizationService) {
        return new CustomClientSecretAuthenticationProvider(registeredClientRepository, oAuth2AuthorizationService);
    }

    /**
     * 访问令牌提供者(授权码模式)
     */
    @Bean
    public CustomAuthorizationCodeAccessTokenProvider customAuthorizationCodeAccessTokenProvider(OAuth2AuthorizationService oAuth2AuthorizationService,
                                                                                                 OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator) {
        // TODO: MaoAtao 2023-04-14 10:18:52 用户 客户端 多对多(解决单点登录)
        return new CustomAuthorizationCodeAccessTokenProvider(oAuth2AuthorizationService, oAuth2TokenGenerator);
    }

    /**
     * 访问令牌提供者(客户端模式)
     */
    @Bean
    public CustomClientCredentialsTokenProvider customClientCredentialsTokenProvider(OAuth2AuthorizationService oAuth2AuthorizationService,
                                                                                     OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator) {
        return new CustomClientCredentialsTokenProvider(oAuth2AuthorizationService, oAuth2TokenGenerator);
    }

    /**
     * 无需授权同意,授权码提供者(自定义授权码生成器)
     */
    @Bean
    public CustomAuthorizationCodeProvider customAuthorizationCodeProvider(RegisteredClientRepository registeredClientRepository,
                                                                           OAuth2AuthorizationService oAuth2AuthorizationService,
                                                                           OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService,
                                                                           OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator,
                                                                           ClientUserService clientUserService) {
        CustomAuthorizationCodeProvider provider =
                new CustomAuthorizationCodeProvider(registeredClientRepository, oAuth2AuthorizationService, oAuth2AuthorizationConsentService, clientUserService);
        // 配置自定义授权码生成器
        provider.setAuthorizationCodeGenerator(oAuth2TokenGenerator);
        return provider;
    }

    /**
     * 同意授权后,授权码提供者(自定义授权码生成器)
     */
    @Bean
    public CustomAuthorizationConsentProvider customAuthorizationConsentProvider(RegisteredClientRepository registeredClientRepository,
                                                                                 OAuth2AuthorizationService oAuth2AuthorizationService,
                                                                                 OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService,
                                                                                 OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator,
                                                                                 ClientUserService clientUserService) {
        CustomAuthorizationConsentProvider provider =
                new CustomAuthorizationConsentProvider(registeredClientRepository, oAuth2AuthorizationService, oAuth2AuthorizationConsentService, clientUserService);
        // 配置自定义授权码生成器
        provider.setAuthorizationCodeGenerator(oAuth2TokenGenerator);
        return provider;
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
