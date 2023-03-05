package com.maoatao.cas.core.service;

import com.maoatao.cas.core.param.GenerateAccessTokenParam;
import com.maoatao.cas.core.param.GenerateAuthorizationCodeParam;
import com.maoatao.cas.security.CustomUserAuthenticationProvider;
import com.maoatao.cas.security.bean.AuthorizationInfo;
import com.maoatao.cas.security.bean.ClientUser;
import com.maoatao.cas.security.bean.CustomAccessToken;
import org.springframework.security.core.Authentication;

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
     * @param param 参数
     * @return 授权码
     */
    String generateAuthorizationCode(GenerateAuthorizationCodeParam param);

    /**
     * 生成访问令牌
     *
     * @param param 参数
     * @return 访问令牌
     */
    CustomAccessToken generateAccessToken(GenerateAccessTokenParam param);

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

    /**
     * 通过客户端用户获取经过授权用户主体
     * <p>
     * {@link CustomUserAuthenticationProvider}
     *
     * @param clientUser 客户端用户信息
     * @return 用户主体
     */
    Authentication generatePrincipal(ClientUser clientUser);
}
