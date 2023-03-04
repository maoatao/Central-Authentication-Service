package com.maoatao.cas.security;

/**
 * Http 常量
 *
 * @author MaoAtao
 * @date 2023-03-04 17:51:04
 */
public class HttpConstants {

    /**
     * 请求根路径
     */
    public static final String BASE_URL = "/cas";

    /**
     * 白名单
     */
    public static final String[] WHITE_LIST = new String[]{
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
