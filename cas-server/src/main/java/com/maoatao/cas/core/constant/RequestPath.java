package com.maoatao.cas.core.constant;

/**
 * 请求路径
 *
 * @author MaoAtao
 * @date 2023-05-10 12:15:45
 */
public final class RequestPath {

    /**
     * CAS CORE 前后端分离接口(与原版接口区分用与过滤器使用)
     */
    public static final String CAS_CORE = "/core";

    /**
     * CAS 请求授权码接口
     */
    public static final String CAS_CODE = CAS_CORE + "/code";

    /**
     * CAS 请求令牌忌口
     */
    public static final String CAS_TOKEN = CAS_CORE + "/token";
}
