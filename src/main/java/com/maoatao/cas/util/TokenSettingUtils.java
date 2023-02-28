package com.maoatao.cas.util;

import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.Objects;


/**
 * @author MaoAtao
 * @date 2022-10-19 13:18:42
 */
public abstract class TokenSettingUtils {

    /**
     * 授权码有效时间
     */
    private static final String AUTHORIZATION_CODE_TIME_TO_LIVE = "settings.token.authorization-code-time-to-live";

    /**
     * 单一授权 Single authorization (踢人下线功能)
     */
    private static final String SINGLE_ACCESS_TOKEN = "settings.token.single-access-token";

    /**
     * 过期时间5分钟
     */
    private static final long DEFAULT_EXPIRATION_SECONDS = 300;

    public static void setAuthorizationCodeTimeToLive(TokenSettings settings, Duration authorizationCodeTimeToLive) {
        settings.getSettings().put(AUTHORIZATION_CODE_TIME_TO_LIVE, authorizationCodeTimeToLive);
    }

    public static Duration getAuthorizationCodeTimeToLive(TokenSettings settings) {
        return Objects.requireNonNullElse(settings.getSetting(AUTHORIZATION_CODE_TIME_TO_LIVE), Duration.ofSeconds(DEFAULT_EXPIRATION_SECONDS));
    }

    public static void setSingleAccessToken(TokenSettings settings, boolean isSingleAccessToken) {
        settings.getSettings().put(SINGLE_ACCESS_TOKEN, isSingleAccessToken);
    }

    public static boolean getSingleAccessToken(TokenSettings settings) {
        return Objects.requireNonNullElse(settings.getSetting(SINGLE_ACCESS_TOKEN), false);
    }
}
