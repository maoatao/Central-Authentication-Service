package com.maoatao.cas.config;

import com.maoatao.cas.security.authorization.CasServerSettings;
import com.maoatao.cas.security.bean.CustomUserDetails;
import com.maoatao.cas.security.oauth2.auth.generator.CustomAccessTokenGenerator;
import com.maoatao.cas.security.oauth2.auth.generator.CustomAuthorizationCodeGenerator;
import com.maoatao.cas.security.oauth2.auth.generator.CustomRefreshTokenGenerator;
import com.maoatao.cas.util.AuthorizationUtils;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.security.KeyPair;
import java.security.Principal;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Optional;
import java.util.UUID;

/**
 * 生成者配置
 *
 * @author MaoAtao
 * @date 2023-03-19 17:38:50
 */
@Configuration
public class GeneratorConfig {

    /**
     * 令牌生成器(多生成方式,构造函数参数最左最先使用,如果成功生成将不再继续尝试剩下的生成发生)
     * <p>
     * {@link DelegatingOAuth2TokenGenerator#generate}
     */
    @Bean
    public OAuth2TokenGenerator<OAuth2Token> tokenGenerator(JwtEncoder jwtEncoder,
                                                            OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer,
                                                            CasServerSettings casServerSettings) {
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator(jwtEncoder, jwtCustomizer),
                customAccessTokenGenerator(casServerSettings),
                customRefreshTokenGenerator(casServerSettings),
                customAuthorizationCodeGenerator(casServerSettings)
        );
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
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            JwtClaimsSet.Builder claims = context.getClaims();
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                // 添加权限列表
                Optional.ofNullable(context.get(OAuth2Authorization.class)).ifPresent(o -> {
                    if (o.getAttribute(Principal.class.getName()) instanceof Authentication authentication) {
                        if (authentication.getPrincipal() instanceof OAuth2ClientAuthenticationToken client) {
                            claims.claim("openId", client.getRegisteredClient().getClientId());
                            claims.claim("clientCredentials", true);
                        }
                        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
                            claims.claim("openId", customUserDetails.getOpenId());
                            claims.claim("permissions", customUserDetails.getPermissions());
                            claims.claim("roles", customUserDetails.getRoles());
                            claims.claim("clientCredentials", false);
                        }
                    }
                });
            }
        };
    }

    /**
     * 访问令牌生成者
     */
    private OAuth2TokenGenerator<OAuth2AccessToken> customAccessTokenGenerator(CasServerSettings casServerSettings) {
        return new CustomAccessTokenGenerator(casServerSettings.getAccessTokenGenerator());
    }

    /**
     * 刷新令牌生成者
     */
    private OAuth2TokenGenerator<OAuth2RefreshToken> customRefreshTokenGenerator(CasServerSettings casServerSettings) {
        return new CustomRefreshTokenGenerator(casServerSettings.getRefreshTokenGenerator());
    }

    /**
     * 授权码生成者
     */
    private OAuth2TokenGenerator<OAuth2AuthorizationCode> customAuthorizationCodeGenerator(CasServerSettings casServerSettings) {
        return new CustomAuthorizationCodeGenerator(casServerSettings.getAuthorizationCodeGenerator());
    }

    /**
     * jwt生成者
     */
    private OAuth2TokenGenerator<Jwt> jwtGenerator(JwtEncoder jwtEncoder, OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer) {
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        jwtGenerator.setJwtCustomizer(jwtCustomizer);
        return jwtGenerator;
    }
}
