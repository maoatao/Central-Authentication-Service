package com.maoatao.cas.security.oauth2.auth;

import com.maoatao.cas.util.TokenSettingUtils;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;

/**
 * 自定义OAuth2授权码生成器
 * <p>
 * Customized by {@link org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider}.OAuth2AuthorizationCodeGenerator
 *
 * @author MaoAtao
 * @date 2022-10-06 21:47:14
 */
public class CustomAuthorizationCodeGenerator implements OAuth2TokenGenerator<OAuth2AuthorizationCode> {

    private final StringKeyGenerator authorizationCodeGenerator;

    public CustomAuthorizationCodeGenerator() {
        this(null);
    }

    public CustomAuthorizationCodeGenerator(StringKeyGenerator authorizationCodeGenerator) {
        this.authorizationCodeGenerator = Objects.requireNonNullElseGet(authorizationCodeGenerator, () -> new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96));
    }

    @Nullable
    @Override
    public OAuth2AuthorizationCode generate(OAuth2TokenContext context) {
        if (context.getTokenType() == null ||
                !OAuth2ParameterNames.CODE.equals(context.getTokenType().getValue())) {
            return null;
        }
        RegisteredClient registeredClient = context.getRegisteredClient();
        Instant issuedAt = Instant.now();
        Duration authorizationCodeTimeToLive = TokenSettingUtils.getAuthorizationCodeTimeToLive(registeredClient.getTokenSettings());
        Instant expiresAt = issuedAt.plus(authorizationCodeTimeToLive);
        return new OAuth2AuthorizationCode(this.authorizationCodeGenerator.generateKey(), issuedAt, expiresAt);
    }

}
