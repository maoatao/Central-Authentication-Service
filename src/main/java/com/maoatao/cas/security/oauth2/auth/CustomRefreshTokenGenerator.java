package com.maoatao.cas.security.oauth2.auth;

import org.springframework.lang.Nullable;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.time.Instant;
import java.util.Base64;
import java.util.Objects;

/**
 * 自定义OAuth2刷新令牌生成器
 * <p>Customized by {@link org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator}
 *
 * @author MaoAtao
 * @date 2022-10-06 21:33:13
 */
public class CustomRefreshTokenGenerator implements OAuth2TokenGenerator<OAuth2RefreshToken> {
    private final StringKeyGenerator refreshTokenGenerator;

    public CustomRefreshTokenGenerator() {
        this(null);
    }

    public CustomRefreshTokenGenerator(StringKeyGenerator refreshTokenGenerator) {
        this.refreshTokenGenerator = Objects.requireNonNullElseGet(refreshTokenGenerator, () -> new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96));
    }

    @Nullable
    @Override
    public OAuth2RefreshToken generate(OAuth2TokenContext context) {
        if (!OAuth2TokenType.REFRESH_TOKEN.equals(context.getTokenType())) {
            return null;
        }
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(context.getRegisteredClient().getTokenSettings().getRefreshTokenTimeToLive());
        return new OAuth2RefreshToken(this.refreshTokenGenerator.generateKey(), issuedAt, expiresAt);
    }

}
