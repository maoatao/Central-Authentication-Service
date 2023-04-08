package com.maoatao.cas.security.authorization;

import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

import java.util.List;

/**
 * 自定义授权服务接口
 *
 * @author MaoAtao
 * @date 2022-11-16 21:12:04
 */
public interface CustomAuthorizationService extends OAuth2AuthorizationService {

    /**
     * 通过主体名称查询授权集合
     *
     * @param principalName 主体名称
     * @return 授权集合
     */
    @Nullable
    List<OAuth2Authorization> findByPrincipal(String principalName);

    /**
     * 删除授权码
     *
     * @param authorization 授权信息
     */
    @Nullable
    void removeAuthorizationCode(OAuth2Authorization authorization);

    /**
     * 删除访问令牌
     *
     * @param authorization 授权信息
     */
    @Nullable
    void removeAccessToken(OAuth2Authorization authorization);
}
