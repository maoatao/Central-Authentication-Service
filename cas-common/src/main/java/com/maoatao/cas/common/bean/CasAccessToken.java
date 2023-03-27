package com.maoatao.cas.common.bean;

import java.io.Serializable;
import java.util.Set;

/**
 * CAS 访问令牌
 *
 * @author MaoAtao
 * @date 2023-03-05 00:58:55
 */
public interface CasAccessToken extends Serializable {

    /**
     * 获取访问令牌值
     *
     * @return 令牌值
     */
    String getAccessToken();

    /**
     * 获取刷新令牌值
     *
     * @return 刷新令牌值
     */
    String getRefreshToken();

    /**
     * 获取授权范围
     *
     * @return 授权范围
     */
    Set<String> getScope();

    /**
     * 获取令牌类型
     *
     * @return 令牌类型
     */
    String getTokenType();

    /**
     * 获取过期时间
     *
     * @return 过期时间
     */
    long getExpiresIn();
}
