package com.maoatao.cas.security;

/**
 * Http 常量
 *
 * @author MaoAtao
 * @date 2023-03-04 17:51:04
 */
public final class HttpConstants {

    /**
     * CAS 服务请求 URL
     * <p>
     * {@link com.maoatao.cas.security.filter.ResourcesFilter} 资源请求过滤器只放行 /cas** 的请求
     */
    public static final String BASE_URL = "/cas";

    /**
     * 授权请求 URL
     * <p>
     * {@link com.maoatao.cas.security.filter.AuthorizationServerContextFilter} 授权服务器上下文过滤器会处理 /cas/authorization** 的请求
     */
    public static final String AUTHORIZATION_URL = "/authorization";

    /**
     * 授权码请求 URL
     */
    public static final String AUTHORIZATION_CODE_URL = "/code";

    /**
     * 授权令牌请求 URL
     */
    public static final String AUTHORIZATION_TOKEN_URL = "/token";

    /**
     * 白名单
     */
    public static final String[] WHITE_LIST = new String[]{
            "/error",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/v3/**",
            "/api/**",
            "/doc.html",
            "/favicon.ico"
    };

    private HttpConstants() {}
}
