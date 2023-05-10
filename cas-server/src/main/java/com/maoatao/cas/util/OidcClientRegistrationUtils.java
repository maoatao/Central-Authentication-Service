package com.maoatao.cas.util;

import org.springframework.security.oauth2.server.authorization.oidc.OidcClientRegistration;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

import java.time.Duration;

/**
 * oidc客户端注册工具类
 *
 * @author MaoAtao
 * @date 2022-10-17 00:35:25
 */
public final class OidcClientRegistrationUtils {

    private OidcClientRegistrationUtils() {}

    private static final String REFERENCE = "reference";

    /**
     * {@code require_proof_key} - 需要证明键code_challenge的值
     */
    private static final String REQUIRE_PROOF_KEY = "require_proof_key";

    /**
     * {@code require_authorization_consent} - 要求授权同意
     */
    private static final String REQUIRE_AUTHORIZATION_CONSENT = "require_authorization_consent";

    /**
     * {@code access_token_time_to_tive} - 令牌有效时间
     */
    private static final String ACCESS_TOKEN_TIME_TO_LIVE = "access_token_time_to_tive";

    /**
     * {@code access_token_time_to_tive} - 令牌有效时间
     */
    private static final String REFRESH_TOKEN_TIME_TO_LIVE = "refresh_token_time_to_live";

    /**
     * {@code access_token_time_to_tive} - 令牌有效时间
     */
    private static final String TOKEN_FORMAT = "token_format";

    /**
     * {@code authorization_code_time_to_live} - 授权码有效时间
     */
    private static final String AUTHORIZATION_CODE_TIME_TO_LIVE = "authorization_code_time_to_live";

    /**
     * {@code single-access-token} - 单一授权
     */
    private static final String SINGLE_ACCESS_TOKEN = "single-access-token";

    public static boolean getRequireProofKey(OidcClientRegistration clientRegistration) {
        Object o = clientRegistration.getClaims().get(REQUIRE_PROOF_KEY);
        // 不设置,默认true
        return !(o instanceof Boolean) || (boolean) o;
    }

    public static boolean getRequireAuthorizationConsent(OidcClientRegistration clientRegistration) {
        Object o = clientRegistration.getClaims().get(REQUIRE_AUTHORIZATION_CONSENT);
        // 不设置,默认true
        return !(o instanceof Boolean) || (boolean) o;
    }

    public static Duration getAccessTokenTimeToLive(OidcClientRegistration clientRegistration) {
        Object o = clientRegistration.getClaims().get(ACCESS_TOKEN_TIME_TO_LIVE);
        return o instanceof Duration ? (Duration) o : Duration.ofMinutes(5);
    }

    public static Duration getRefreshTokenTimeToLive(OidcClientRegistration clientRegistration) {
        Object o = clientRegistration.getClaims().get(REFRESH_TOKEN_TIME_TO_LIVE);
        return o instanceof Duration ? (Duration) o : Duration.ofMinutes(60);
    }

    public static Duration getAuthorizationCodeTimeToLive(OidcClientRegistration clientRegistration) {
        Object o = clientRegistration.getClaims().get(AUTHORIZATION_CODE_TIME_TO_LIVE);
        return o instanceof Duration ? (Duration) o : Duration.ofMinutes(5);
    }

    public static OAuth2TokenFormat getAccessTokenFormat(OidcClientRegistration clientRegistration) {
        Object o = clientRegistration.getClaims().get(TOKEN_FORMAT);
        return (o != null && REFERENCE.equals(o.toString())) ? OAuth2TokenFormat.REFERENCE : OAuth2TokenFormat.SELF_CONTAINED;
    }

    public static boolean getSingleAccessToken(OidcClientRegistration clientRegistration) {
        Object o = clientRegistration.getClaims().get(SINGLE_ACCESS_TOKEN);
        // 不设置,默认false
        return (o instanceof Boolean) || (boolean) o;
    }
}
