package com.maoatao.cas.core.service.impl;

import com.maoatao.cas.common.authentication.CasAccessToken;
import com.maoatao.cas.common.authentication.CasAuthorization;
import com.maoatao.cas.common.authentication.DefaultAccessToken;
import com.maoatao.cas.common.authentication.DefaultAuthorization;
import com.maoatao.cas.core.param.GenerateAccessTokenParam;
import com.maoatao.cas.security.CustomUserAuthenticationProvider;
import com.maoatao.cas.core.service.AuthorizationService;
import com.maoatao.cas.security.GrantType;
import com.maoatao.cas.security.bean.ClientUser;
import com.maoatao.cas.security.bean.CustomUserDetails;
import com.maoatao.cas.security.filter.TokenAuthenticationFilter;
import com.maoatao.cas.security.oauth2.auth.CustomAuthorizationCodeGenerator;
import com.maoatao.cas.security.UUIDStringKeyGenerator;
import com.maoatao.cas.core.param.GenerateAuthorizationCodeParam;
import com.maoatao.daedalus.web.util.ServletUtils;
import com.maoatao.synapse.lang.exception.SynaException;
import com.maoatao.synapse.lang.util.SynaAssert;
import com.maoatao.synapse.lang.util.SynaDates;
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
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2RefreshTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.ClientSecretBasicAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
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
        Authentication principal = getUserAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) principal.getPrincipal();
        RegisteredClient registeredClient = getRegisteredClient(userDetails.getClientId());
        checkParams(param, registeredClient);
        OAuth2TokenContext tokenContext = buildTokenContext(param.getScopes(), registeredClient, principal);
        OAuth2AuthorizationCode authorizationCode = buildAuthorizationCode(tokenContext);
        saveAuthorization(param, registeredClient, principal, authorizationCode);
        return authorizationCode.getTokenValue();
    }

    /**
     * 生成访问令牌
     * <p>
     * 构建客户端身份验证源码{@link ClientSecretBasicAuthenticationConverter#convert}
     *
     * @param param 参数
     * @return 访问令牌
     */
    @Override
    public CasAccessToken generateAccessToken(GenerateAccessTokenParam param) {
        CustomUserDetails userDetails = getUserDetails();
        Authentication clientAuthentication = generateClientPrincipal(userDetails.getClientId(), param.getSecret());
        return switch (param.getType()) {
            case GrantType.AUTHORIZATION_CODE -> buildAccessTokenByCode(param.getCode(), clientAuthentication);
            case GrantType.REFRESH_TOKEN -> buildAccessTokenByRefresh(param.getCode(), clientAuthentication);
            case GrantType.CLIENT_CREDENTIALS -> buildAccessTokenByClient(clientAuthentication);
            default -> throw new UnsupportedOperationException("不支持的授权类型!");
        };
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
    public CasAuthorization getAuthorizationInfo(String accessToken) {
        OAuth2Authorization authorization = findByAccessToken(accessToken);
        if (authorization == null || authorization.getAccessToken() == null
                || !authorization.getAccessToken().isActive() || authorization.getAccessToken().getToken() == null) {
            return null;
        }
        Authentication authentication = authorization.getAttribute(Principal.class.getName());
        if (authentication == null) {
            return null;
        }
        if (authentication instanceof OAuth2ClientAuthenticationToken client) {
            // 客户端令牌
            return DefaultAuthorization.builder()
                    .user(client.getRegisteredClient().getClientName())
                    // 将 openid 和 客户端 id 设置为一致用于鉴权时辨别人机接口和机机接口
                    .openId(client.getRegisteredClient().getClientId())
                    .clientId(client.getRegisteredClient().getClientId())
                    .permissions(Set.of())
                    .roles(Set.of())
                    .expiresAt(SynaDates.of(authorization.getAccessToken().getToken().getExpiresAt()))
                    .issuedAt(SynaDates.of(authorization.getAccessToken().getToken().getIssuedAt()))
                    .clientCredentials(true)
                    .build();
        }
        if (authentication.getPrincipal() instanceof CustomUserDetails principal) {
            // 访问令牌
            return DefaultAuthorization.builder()
                    .user(principal.getUsername())
                    .openId(principal.getOpenId())
                    .clientId(principal.getClientId())
                    .permissions(principal.getPermissions())
                    .roles(principal.getRoles())
                    .expiresAt(SynaDates.of(authorization.getAccessToken().getToken().getExpiresAt()))
                    .issuedAt(SynaDates.of(authorization.getAccessToken().getToken().getIssuedAt()))
                    .clientCredentials(false)
                    .build();
        }
        return null;
    }

    @Override
    public Authentication generateUserPrincipal(ClientUser clientUser) {
        return generateUserPrincipal(clientUser.clientId(), clientUser.username(), clientUser.password());
    }

    /**
     * 通过客户端获取经过授权客户端主体
     *
     * @param clientId 客户端 ID
     * @param secret   客户端密码
     * @return 客户端主体
     */
    private Authentication generateClientPrincipal(String clientId, String secret) {
        RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);
        // 构建客户端身份验证
        Authentication clientAuthentication = new OAuth2ClientAuthenticationToken(registeredClient, ClientAuthenticationMethod.CLIENT_SECRET_BASIC, secret);
        // 认证客户端令牌
        OAuth2ClientAuthenticationToken clientAuthenticationToken;
        try {
            clientAuthenticationToken = (OAuth2ClientAuthenticationToken) this.authenticationManager.authenticate(clientAuthentication);
        } catch (Exception e) {
            throw new SynaException("认证客户端失败!", e);
        }
        return clientAuthenticationToken;
    }

    /**
     * 获取用户详情
     * <p>
     * 这个值需要在拦截器中生成(正常情况下这个值生成失败拦截器不会放行)
     * <p>
     * {@link this#generateUserPrincipal(ClientUser)}该方法反过来,从上下文中获取
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
    private Authentication generateUserPrincipal(String clientId, String username, String password) {
        Authentication authentication;
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        // 设置客户端 id 供 CustomUserAuthenticationProvider#retrieveUser 方法使用
        usernamePasswordAuthenticationToken.setDetails(clientId);
        try {
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            throw new SynaException("用户身份验证失败!可能是用户名,密码或客户端ID错误.", e);
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
            // TODO: 2023-03-05 15:49:12 疑问:请求一个多客户端的令牌 scopes 不能使用如下校验
            SynaAssert.isTrue(registeredClient.getScopes().contains(s), errorMsg);
        }
        if (registeredClient.getClientSettings().isRequireProofKey()) {
            SynaAssert.isTrue(StrUtil.isNotBlank(param.getCodeChallengeMethod()), "无效的请求参数[codeChallengeMethod]");
            SynaAssert.isTrue(StrUtil.isNotBlank(param.getCodeChallenge()), "无效的请求参数[codeChallenge]");
        }
    }

    /**
     * 通过授权码生成访问令牌 (授权码模式 {@value GrantType#AUTHORIZATION_CODE})
     * <p>
     * 构建授权码认证源码{@link OAuth2AuthorizationCodeAuthenticationConverter#convert}
     *
     * @param code   授权码
     * @param client 客户端身份验证
     * @return 访问令牌
     */
    private CasAccessToken buildAccessTokenByCode(String code, Authentication client) {
        return buildAccessToken(new OAuth2AuthorizationCodeAuthenticationToken(code, client, null, null));
    }

    /**
     * 刷新访问令牌 (刷新令牌模式 {@value GrantType#REFRESH_TOKEN})
     * <p>
     * 构建刷新令牌认证源码{@link OAuth2RefreshTokenAuthenticationConverter#convert}
     *
     * @param refreshToken 授权码
     * @param client       客户端身份验证
     * @return 访问令牌
     */
    private CasAccessToken buildAccessTokenByRefresh(String refreshToken, Authentication client) {
        return buildAccessToken(new OAuth2RefreshTokenAuthenticationToken(refreshToken, client, null, null));
    }

    /**
     * 通过客户端生成访问令牌 (客户端模式 {@value GrantType#CLIENT_CREDENTIALS})
     * <p>
     * 构建刷新令牌认证源码{@link OAuth2ClientCredentialsAuthenticationConverter#convert}
     *
     * @param client 客户端身份验证
     * @return 访问令牌
     */
    private CasAccessToken buildAccessTokenByClient(Authentication client) {
        return buildAccessToken(new OAuth2ClientCredentialsAuthenticationToken(client, null, null));
    }

    /**
     * 生成访问令牌
     * <p>
     * 入口源码{@link OAuth2TokenEndpointFilter}.doFilterInternal
     *
     * @param authentication 授权认证信息(根据不同的AuthenticationToken识别授权模式)
     * @return 访问令牌
     */
    private CasAccessToken buildAccessToken(Authentication authentication) {
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication;
        try {
            // 生成访问令牌
            accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) this.authenticationManager.authenticate(authentication);
        } catch (Exception e) {
            throw new SynaException("令牌生成失败!", e);
        }
        SynaAssert.notNull(accessTokenAuthentication, "令牌生成失败!");
        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        SynaAssert.notNull(accessToken, "令牌生成失败!");
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        DefaultAccessToken.DefaultAccessTokenBuilder defaultAccessTokenBuilder = DefaultAccessToken.builder();
        long now = SynaDates.now(SynaDates.DateType.MILLI_SECOND);
        long expiresAt = accessTokenAuthentication.getAccessToken().getExpiresAt().toEpochMilli();
        // 求差值去毫秒
        long expiresIn = (expiresAt - now) / 1000;
        if (expiresIn <= 0) {
            log.warn("令牌生成成功,但已过期? issuedAt:{},expiresAt:{},now:{}", accessTokenAuthentication.getAccessToken().getIssuedAt().toEpochMilli(), expiresAt, now);
        }
        defaultAccessTokenBuilder.accessToken(accessToken.getTokenValue()).scope(accessToken.getScopes()).expiresIn(expiresIn);
        if (accessToken.getTokenType() != null) {
            defaultAccessTokenBuilder.tokenType(accessToken.getTokenType().getValue());
        }
        if (refreshToken != null) {
            defaultAccessTokenBuilder.refreshToken(refreshToken.getTokenValue());
        }
        return defaultAccessTokenBuilder.build();
    }

    /**
     * 从上下文获取用户授权主体
     * <p>
     * 设置主体位置 {@link TokenAuthenticationFilter}
     *
     * @return 用户授权主体
     */
    private Authentication getUserAuthentication() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        SynaAssert.notNull(usernamePasswordAuthenticationToken, "从用户主体获取失败!");
        return usernamePasswordAuthenticationToken;
    }
}
