package com.maoatao.cas.config;

import com.maoatao.cas.security.UUIDStringKeyGenerator;
import com.maoatao.cas.security.bean.CustomUserDetails;
import com.maoatao.cas.security.oauth2.auth.CustomAccessTokenGenerator;
import com.maoatao.cas.security.oauth2.auth.CustomRefreshTokenGenerator;
import com.maoatao.cas.util.AuthorizationUtils;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
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
     * uuid 字符串生成者
     * <p>
     * 授权码,访问令牌,刷新令牌使用了该uuid
     */
    @Bean
    public StringKeyGenerator uuidStringKeyGenerator() {
        return new UUIDStringKeyGenerator();
    }

    /**
     * 令牌生成器(多生成方式,构造函数参数最左最先使用,如果成功生成将不再继续尝试剩下的生成发生)
     * <p>
     * {@link DelegatingOAuth2TokenGenerator#generate}
     */
    @Bean
    public OAuth2TokenGenerator<OAuth2Token> tokenGenerator(JwtEncoder jwtEncoder,
                                                            OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer,
                                                            StringKeyGenerator stringKeyGenerator) {
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator(jwtEncoder, jwtCustomizer),
                customAccessTokenGenerator(stringKeyGenerator),
                customRefreshTokenGenerator(stringKeyGenerator));
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
                        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
                            claims.claim("openId", customUserDetails.getOpenId());
                            claims.claim("permissions", customUserDetails.getPermissions());
                            claims.claim("roles", customUserDetails.getRoles());
                            claims.claim("scope", o.getAuthorizedScopes());
                        }
                    }
                });
            }
        };
    }

    /**
     * 访问令牌生成者
     */
    private OAuth2TokenGenerator<OAuth2AccessToken> customAccessTokenGenerator(StringKeyGenerator stringKeyGenerator) {
        return new CustomAccessTokenGenerator(stringKeyGenerator);
    }

    /**
     * 刷新令牌生成者
     */
    private OAuth2TokenGenerator<OAuth2RefreshToken> customRefreshTokenGenerator(StringKeyGenerator stringKeyGenerator) {
        return new CustomRefreshTokenGenerator(stringKeyGenerator);
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
