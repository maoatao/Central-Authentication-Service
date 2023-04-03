package com.maoatao.cas.security.constant;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * 授权模式
 *
 * @author MaoAtao
 * @date 2023-03-04 17:51:04
 */
public final class GrantType {

    /**
     * 授权码模式
     * <p>
     * {@link AuthorizationGrantType#AUTHORIZATION_CODE}
     */
    public static final String AUTHORIZATION_CODE = "authorization_code";

    /**
     * 刷新令牌模式
     * <p>
     * {@link AuthorizationGrantType#REFRESH_TOKEN}
     */
    public static final String REFRESH_TOKEN = "refresh_token";

    /**
     * 客户端模式
     * <p>
     * {@link AuthorizationGrantType#CLIENT_CREDENTIALS}
     */
    public static final String CLIENT_CREDENTIALS = "client_credentials";

    private GrantType() {}
}
