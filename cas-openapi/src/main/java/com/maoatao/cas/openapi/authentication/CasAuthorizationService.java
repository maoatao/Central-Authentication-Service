package com.maoatao.cas.openapi.authentication;

import com.maoatao.cas.common.authentication.CasAuthorization;

/**
 * 令牌服务接口
 * <p>
 * 使用引用token格式时需要访问CAS服务获取授权信息,这里需要自己实现
 *
 * @author MaoAtao
 * @date 2023-03-26 14:03:41
 */
public interface CasAuthorizationService {

    /**
     * 通过令牌值获取授权信息
     *
     * @param token 令牌值
     * @return 授权信息
     */
    CasAuthorization getAuthorization(String token);
}
