package com.maoatao.cas.util;

import com.maoatao.cas.security.oauth2.auth.CustomAuthorizationCodeAccessTokenProvider;
import com.maoatao.cas.security.oauth2.auth.CustomAuthorizationCodeGenerator;
import com.maoatao.cas.security.oauth2.auth.CustomRefreshTokenProvider;
import com.maoatao.cas.security.UUIDStringKeyGenerator;
import com.maoatao.cas.security.oauth2.odic.CustomClientRegistrationProvider;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2配置工具类
 *
 * @author MaoAtao
 * @date 2022-10-13 12:00:37
 */
public abstract class AuthorizationServerUtils {

    private static OAuth2AuthorizationCodeRequestAuthenticationProvider oAuth2AuthorizationCodeRequestAuthenticationProvider;
    private static CustomClientRegistrationProvider customClientRegistrationProvider;
    private static CustomRefreshTokenProvider customRefreshTokenProvider;
    private static CustomAuthorizationCodeAccessTokenProvider customAuthorizationCodeAccessTokenProvider;

    public static void applyConfigurer(HttpSecurity http) throws Exception {
        oAuth2AuthorizationCodeRequestAuthenticationProvider = SpringContextUtils.getBean(OAuth2AuthorizationCodeRequestAuthenticationProvider.class);
        customClientRegistrationProvider = SpringContextUtils.getBean(CustomClientRegistrationProvider.class);
        customRefreshTokenProvider = SpringContextUtils.getBean(CustomRefreshTokenProvider.class);
        customAuthorizationCodeAccessTokenProvider = SpringContextUtils.getBean(CustomAuthorizationCodeAccessTokenProvider.class);
        OAuth2AuthorizationServerConfigurer configurer = new OAuth2AuthorizationServerConfigurer();
        applyAuthenticationProviders(configurer);
        applyOidcConfigurer(configurer);
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                .apply(configurer);
    }

    public static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    public static <T extends OAuth2Token> OAuth2Authorization invalidate(
            OAuth2Authorization authorization, T token) {

        // @formatter:off
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.from(authorization)
                .token(token,
                        (metadata) ->
                                metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, true));

        if (OAuth2RefreshToken.class.isAssignableFrom(token.getClass())) {
            authorizationBuilder.token(
                    authorization.getAccessToken().getToken(),
                    (metadata) ->
                            metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, true));

            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                    authorization.getToken(OAuth2AuthorizationCode.class);
            if (authorizationCode != null && !authorizationCode.isInvalidated()) {
                authorizationBuilder.token(
                        authorizationCode.getToken(),
                        (metadata) ->
                                metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, true));
            }
        }
        // @formatter:on

        return authorizationBuilder.build();
    }

    public static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

    /**
     * 配置身份验证提供程序
     */
    private static void applyAuthenticationProviders(OAuth2AuthorizationServerConfigurer configurer) {
        configurer.authorizationEndpoint(endpoint -> endpoint
                .authenticationProvider(oAuth2AuthorizationCodeRequestAuthenticationProvider)
                .authenticationProvider(customClientRegistrationProvider)
                .authenticationProvider(customRefreshTokenProvider)
                .authenticationProvider(customAuthorizationCodeAccessTokenProvider));
    }

    /**
     * 应用Oidc配置
     */
    private static void applyOidcConfigurer(OAuth2AuthorizationServerConfigurer configurer) {
        configurer.oidc(oidc -> {
            // 用户信息
            oidc.userInfoEndpoint(
                    endpoit -> endpoit.userInfoMapper(
                            mapper -> {
                                OAuth2AccessToken accessToken = mapper.getAccessToken();
                                Map<String, Object> claims = new HashMap<>();
                                claims.put("url", "https://github.com/ITLab1024");
                                claims.put("accessToken", accessToken);
                                claims.put("sub", mapper.getAuthorization().getPrincipalName());
                                return new OidcUserInfo(claims);
                            }
                    )
            );
            // 客户端注册
            oidc.clientRegistrationEndpoint(Customizer.withDefaults());
        });
    }
}
