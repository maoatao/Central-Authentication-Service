package com.maoatao.cas.core.service;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

/**
 * 静态页面 thymeleaf 索引
 *
 * @author MaoAtao
 * @date 2023-05-09 17:15:47
 */
public interface IndexService {

    /**
     * 授权同意页面
     * <p>
     * org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter#sendAuthorizationConsent
     */
    void consent(String scope, String clientId, String state, Authentication authentication, Model model);
}
