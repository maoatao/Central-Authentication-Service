package com.maoatao.cas.core.service.impl;

import cn.hutool.json.JSONObject;
import com.maoatao.cas.core.param.GenerateAccessTokenParam;
import com.maoatao.cas.security.CustomUserAuthenticationProvider;
import com.maoatao.cas.core.service.AuthorizationService;
import com.maoatao.cas.security.GrantTypeConstants;
import com.maoatao.cas.security.bean.ClientUser;
import com.maoatao.cas.security.bean.CustomUserDetails;
import com.maoatao.cas.security.oauth2.auth.CustomAuthorizationCodeGenerator;
import com.maoatao.cas.security.UUIDStringKeyGenerator;
import com.maoatao.cas.core.param.GenerateAuthorizationCodeParam;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.ClientSecretBasicAuthenticationConverter;
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
    public String generateAuthorizationCode(GenerateAuthorizationCodeParam param) {
        Authentication principal = getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) principal.getPrincipal();
        RegisteredClient registeredClient = getRegisteredClient(userDetails.getClientId());
        checkParams(param, registeredClient);
        OAuth2TokenContext tokenContext = buildTokenContext(param.getScopes(), registeredClient, principal);
        OAuth2AuthorizationCode authorizationCode = buildAuthorizationCode(tokenContext);
        saveAuthorization(param, registeredClient, principal, authorizationCode);
        return authorizationCode.getTokenValue();
    }

    @Override
    public OAuth2AccessToken generateAccessToken(GenerateAccessTokenParam param) {
        OAuth2AccessToken oAuth2AccessToken;
        switch (param.getType()) {
            case GrantTypeConstants.AUTHORIZATION_CODE -> oAuth2AccessToken = buildAccessTokenByCode(param);
            case GrantTypeConstants.REFRESH_TOKEN -> oAuth2AccessToken = buildAccessTokenByRefresh(param);
            case GrantTypeConstants.CLIENT_CREDENTIALS -> oAuth2AccessToken = buildAccessTokenByClient(param);
            default -> throw new UnsupportedOperationException("不支持的授权类型!");
        }
        return oAuth2AccessToken;
    }

    @Override
    public boolean revokeAccessToken(String accessToken) {
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

    @Override
    public Authentication generatePrincipal(ClientUser clientUser) {
        return generatePrincipal(clientUser.clientId(), clientUser.username(), clientUser.password());
    }

    /**
     * 获取用户详情
     * <p>
     * 这个值需要在拦截器中生成(正常情况下这个值生成失败拦截器不会放行)
     * <p>
     * {@link this#generatePrincipal(ClientUser)}该方法反过来,从上下文中获取
     *
     * @return 用户详情
     */
    private CustomUserDetails getUserDetails() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        SynaAssert.notNull(usernamePasswordAuthenticationToken, "从用户主体获取失败!");
        return (CustomUserDetails) usernamePasswordAuthenticationToken.getPrincipal();
    }

    /**
     * 构建身份验证
     * <p>
     * {@link CustomUserAuthenticationProvider}
     *
     * @param clientId 客户端 Id
     * @param username 用户名
     * @param password 密码
     * @return 用户身份验证
     */
    private Authentication generatePrincipal(String clientId, String username, String password) {
        Authentication authentication;
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        // 设置客户端 id 供 CustomUserAuthenticationProvider#retrieveUser 方法使用
        usernamePasswordAuthenticationToken.setDetails(clientId);
        try {
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            throw new SynaException("用户身份验证失败!", e);
        }
        if (authentication == null) {
            throw new SynaException("生成用户身份验证失败!");
        }
        return authentication;
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
     * @param param            请求参数
     * @param registeredClient 客户端
     * @return 授权请求
     */
    private OAuth2AuthorizationRequest buildAuthorizationRequest(GenerateAuthorizationCodeParam param,
                                                                 RegisteredClient registeredClient) {
        OAuth2AuthorizationRequest authorizationRequest;
        try {
            authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
                    .authorizationUri(ServletUtils.getRequest().getRequestURL().toString())
                    .clientId(registeredClient.getClientId())
                    .scopes(param.getScopes())
                    .additionalParameters(param.getAdditionalParameters())
                    .build();
        } catch (Exception e) {
            throw new SynaException("生成用户身份验证失败!", e);
        }
        return authorizationRequest;
    }

    /**
     * 生成令牌上下文
     *
     * @param scopes           授权范围
     * @param registeredClient 客户端
     * @param principal        用户身份验证
     * @return 令牌上下文
     */
    private OAuth2TokenContext buildTokenContext(Set<String> scopes,
                                                 RegisteredClient registeredClient,
                                                 Authentication principal) {
        OAuth2TokenContext tokenContext;
        try {
            tokenContext = DefaultOAuth2TokenContext.builder()
                    .registeredClient(registeredClient)
                    .principal(principal)
                    .tokenType(new OAuth2TokenType(OAuth2ParameterNames.CODE))
                    .authorizedScopes(scopes)
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
     * @param param             请求参数
     * @param registeredClient  客户端
     * @param principal         用户身份验证
     * @param authorizationCode 授权码
     */
    private void saveAuthorization(GenerateAuthorizationCodeParam param,
                                   RegisteredClient registeredClient,
                                   Authentication principal,
                                   OAuth2AuthorizationCode authorizationCode) {
        OAuth2Authorization authorization;
        OAuth2AuthorizationRequest authorizationRequest = buildAuthorizationRequest(param, registeredClient);
        try {
            authorization = OAuth2Authorization.withRegisteredClient(registeredClient)
                    .principalName(principal.getName())
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .attribute(Principal.class.getName(), principal)
                    .attribute(OAuth2AuthorizationRequest.class.getName(), authorizationRequest)
                    .authorizedScopes(param.getScopes())
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

    private void checkParams(GenerateAuthorizationCodeParam param, RegisteredClient registeredClient) {
        String errorMsg = "无效的请求参数[scopes]";
        Set<String> scopes = param.getScopes();
        SynaAssert.notEmpty(scopes, errorMsg);
        for (String s : scopes) {
            SynaAssert.isTrue(registeredClient.getScopes().contains(s), errorMsg);
        }
        if (registeredClient.getClientSettings().isRequireProofKey()) {
            SynaAssert.isTrue(StrUtil.isNotBlank(param.getCodeChallengeMethod()), "无效的请求参数[codeChallengeMethod]");
            SynaAssert.isTrue(StrUtil.isNotBlank(param.getCodeChallenge()), "无效的请求参数[codeChallenge]");
        }
    }

    /**
     * {@link OAuth2AuthorizationCodeAuthenticationConverter#convert}
     * <p>
     * {@link ClientSecretBasicAuthenticationConverter#convert}
     * <p>
     * {@link OAuth2TokenEndpointFilter}.doFilterInternal
     *
     * @param param
     * @return
     */
    private OAuth2AccessToken buildAccessTokenByCode(GenerateAccessTokenParam param) {
        CustomUserDetails userDetails = getUserDetails();
        RegisteredClient registeredClient = registeredClientRepository.findByClientId(userDetails.getClientId());
        // 构建客户端身份验证令牌
        Authentication clientAuthentication = new OAuth2ClientAuthenticationToken(registeredClient, ClientAuthenticationMethod.CLIENT_SECRET_BASIC, param.getSecret());
        // 认证客户端令牌
        OAuth2ClientAuthenticationToken clientAuthenticationToken = (OAuth2ClientAuthenticationToken) this.authenticationManager.authenticate(clientAuthentication);
        // 构建授权码认证令牌
        Authentication codeAuthentication = new OAuth2AuthorizationCodeAuthenticationToken(param.getCode(), clientAuthenticationToken, null, null);
        // 生成访问令牌
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) this.authenticationManager.authenticate(codeAuthentication);

        System.out.println(new JSONObject(accessTokenAuthentication).toStringPretty());

        return accessTokenAuthentication.getAccessToken();
    }

    private OAuth2AccessToken buildAccessTokenByClient(GenerateAccessTokenParam param) {
        return null;
    }

    private OAuth2AccessToken buildAccessTokenByRefresh(GenerateAccessTokenParam param) {
        return null;
    }

    private Authentication getAuthentication() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        SynaAssert.notNull(usernamePasswordAuthenticationToken, "从用户主体获取失败!");
        return usernamePasswordAuthenticationToken;
    }
}
