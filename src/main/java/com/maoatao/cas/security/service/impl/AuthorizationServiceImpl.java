package com.maoatao.cas.security.service.impl;

import com.maoatao.cas.security.ClientUserAuthenticationProvider;
import com.maoatao.cas.security.service.AuthorizationService;
import com.maoatao.cas.security.oauth2.auth.CustomAuthorizationCodeGenerator;
import com.maoatao.cas.security.generator.UUIDStringKeyGenerator;
import com.maoatao.cas.security.bean.GenerateAuthorizationCodeParams;
import com.maoatao.cas.security.bean.AuthorizationInfo;
import com.maoatao.cas.util.ServletUtils;
import com.maoatao.synapse.core.lang.SynaException;
import com.maoatao.synapse.core.util.SynaAssert;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Set;

/**
 * 自定义授权接口实现
 *
 * @author MaoAtao
 * @date 2022-10-23 17:22:26
 */
@Slf4j
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OAuth2AuthorizationService oAuth2AuthorizationService;

    private static final OAuth2TokenGenerator<OAuth2AuthorizationCode> TOKEN_GENERATOR = new CustomAuthorizationCodeGenerator(new UUIDStringKeyGenerator());

    @Override
    public String generateAuthorizationCode(GenerateAuthorizationCodeParams params) {
        RegisteredClient registeredClient = getRegisteredClient(params.getClientId());
        checkParams(params, registeredClient);
        Authentication principal = buildPrincipal(params.getClientId(), params.getUsername(), params.getPassword());
        OAuth2TokenContext tokenContext = buildTokenContext(params, registeredClient, principal);
        OAuth2AuthorizationCode authorizationCode = buildAuthorizationCode(tokenContext);
        saveAuthorization(params, registeredClient, principal, authorizationCode);
        return authorizationCode.getTokenValue();
    }

    @Override
    public Boolean revokeAccessToken(String accessToken) {
        try {
            OAuth2Authorization authorization = findByAccessToken(accessToken);
            if (authorization == null) {
                return false;
            }
            oAuth2AuthorizationService.remove(authorization);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public AuthorizationInfo getAuthorizationInfo(String accessToken) {
        OAuth2Authorization authorization = findByAccessToken(accessToken);
        if (authorization == null) {
            return null;
        }
        Authentication principal = authorization.getAttribute(Principal.class.getName());
        boolean isActive = authorization.getAccessToken().isActive();
        return AuthorizationInfo.of(principal, isActive);
    }

    /**
     * 获取令牌
     *
     * @param accessToken 令牌的值
     * @return 令牌
     */
    private OAuth2Authorization findByAccessToken(String accessToken) {
        return oAuth2AuthorizationService.findByToken(removeBearerPrefix(accessToken), OAuth2TokenType.ACCESS_TOKEN);
    }

    /**
     * 构建身份验证
     * <p>
     * {@link ClientUserAuthenticationProvider}
     *
     * @param clientId 客户端 Id
     * @param username 用户名
     * @param password 密码
     * @return 用户身份验证
     */
    private Authentication buildPrincipal(String clientId, String username, String password) {
        Authentication authentication;
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        authenticationToken.setDetails(clientId);
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            throw new SynaException("用户身份验证失败!", e);
        }
        if (authentication == null) {
            throw new SynaException("生成用户身份验证失败!");
        }
        return authentication;
    }

    /**
     * 通过客户端id获取客户端
     *
     * @param clientId 客户端id
     * @return 客户端
     */
    private RegisteredClient getRegisteredClient(String clientId) {
        RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throw new SynaException("无效的客户端!");
        }
        if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE)) {
            throw new SynaException("无效的授权类型!");
        }
        return registeredClient;
    }

    /**
     * 构建授权请求
     *
     * @param params           请求参数
     * @param registeredClient 客户端
     * @return 授权请求
     */
    private OAuth2AuthorizationRequest buildAuthorizationRequest(GenerateAuthorizationCodeParams params,
                                                                 RegisteredClient registeredClient) {
        OAuth2AuthorizationRequest authorizationRequest;
        try {
            authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
                    .authorizationUri(ServletUtils.getRequest().getRequestURL().toString())
                    .clientId(registeredClient.getClientId())
                    .scopes(params.getScopes())
                    .additionalParameters(params.getAdditionalParameters())
                    .build();
        } catch (Exception e) {
            throw new SynaException("生成用户身份验证失败!", e);
        }
        return authorizationRequest;
    }

    /**
     * @param params           请求参数
     * @param registeredClient 客户端
     * @param principal        用户身份验证
     * @return 生成令牌上下文
     */
    private OAuth2TokenContext buildTokenContext(GenerateAuthorizationCodeParams params,
                                                 RegisteredClient registeredClient,
                                                 Authentication principal) {
        OAuth2TokenContext tokenContext;
        try {
            tokenContext = DefaultOAuth2TokenContext.builder()
                    .registeredClient(registeredClient)
                    .principal(principal)
                    .tokenType(new OAuth2TokenType(OAuth2ParameterNames.CODE))
                    .authorizedScopes(params.getScopes())
                    .build();
        } catch (Exception e) {
            throw new SynaException("生成令牌上下文失败!", e);
        }
        return tokenContext;
    }

    /**
     * 构建授权码(有效期内只能使用一次)
     *
     * @param tokenContext 生成令牌上下文
     * @return 授权码
     */
    private OAuth2AuthorizationCode buildAuthorizationCode(OAuth2TokenContext tokenContext) {
        OAuth2AuthorizationCode authorizationCode;
        try {
            authorizationCode = TOKEN_GENERATOR.generate(tokenContext);
        } catch (Exception e) {
            throw new SynaException("授权码生成失败!", e);
        }
        if (authorizationCode == null) {
            throw new SynaException("授权码生成失败!");
        }
        return authorizationCode;
    }

    /**
     * 保存授权信息
     *
     * @param params            请求参数
     * @param registeredClient  客户端
     * @param principal         用户身份验证
     * @param authorizationCode 授权码
     */
    private void saveAuthorization(GenerateAuthorizationCodeParams params,
                                   RegisteredClient registeredClient,
                                   Authentication principal,
                                   OAuth2AuthorizationCode authorizationCode) {
        OAuth2Authorization authorization;
        OAuth2AuthorizationRequest authorizationRequest = buildAuthorizationRequest(params, registeredClient);
        try {
            authorization = OAuth2Authorization.withRegisteredClient(registeredClient)
                    .principalName(principal.getName())
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .attribute(Principal.class.getName(), principal)
                    .attribute(OAuth2AuthorizationRequest.class.getName(), authorizationRequest)
                    .authorizedScopes(params.getScopes())
                    .token(authorizationCode)
                    .build();
        } catch (Exception e) {
            throw new SynaException("生成授权信息失败!", e);
        }
        try {
            oAuth2AuthorizationService.save(authorization);
        } catch (Exception e) {
            throw new SynaException("保存授权信息失败!", e);
        }
    }

    /**
     * 去掉Bearer前缀
     *
     * @param accessToken 令牌
     */
    private String removeBearerPrefix(String accessToken) {
        if (StrUtil.isBlank(accessToken)) {
            return StrUtil.EMPTY;
        }
        return accessToken.replaceAll(OAuth2AccessToken.TokenType.BEARER.getValue(), "").trim();
    }

    private void checkParams(GenerateAuthorizationCodeParams params, RegisteredClient registeredClient) {
        String errorMsg = "无效的请求参数[scopes]";
        Set<String> scopes = params.getScopes();
        SynaAssert.notEmpty(scopes, errorMsg);
        for (String s : scopes) {
            SynaAssert.isTrue(registeredClient.getScopes().contains(s), errorMsg);
        }
        if (registeredClient.getClientSettings().isRequireProofKey()) {
            SynaAssert.isTrue(StrUtil.isNotBlank(params.getCodeChallengeMethod()), "无效的请求参数[codeChallengeMethod]");
            SynaAssert.isTrue(StrUtil.isNotBlank(params.getCodeChallenge()), "无效的请求参数[codeChallenge]");
        }
    }
}
