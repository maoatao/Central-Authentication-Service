package com.maoatao.cas.core.service;

import com.maoatao.cas.core.param.GenerateAuthorizationCodeParams;
import com.maoatao.cas.security.bean.AuthorizationInfo;

/**
 * 自定义授权接口
 *
 * @author MaoAtao
 * @date 2022-10-23 17:16:39
 */
public interface AuthorizationService {

    /**
     * 生成授权码
     *
     * @param params 参数
     * @return 授权码
     */
    String generateAuthorizationCode(GenerateAuthorizationCodeParams params);

    /**
     * 吊销令牌
     *
     * @param accessToken 令牌
     * @return boolean true -> 吊销token成功，false -> 没有该token，吊销失败
     */
    boolean revokeAccessToken(String accessToken);

    /**
     * 查询授权信息
     *
     * @param accessToken 令牌
     * @return 授权信息
     */
    AuthorizationInfo getAuthorizationInfo(String accessToken);
}
