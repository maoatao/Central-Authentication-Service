package com.maoatao.cas.security.oauth2.auth.service;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.StrUtil;
import com.maoatao.daedalus.data.util.RedisUtils;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 自定义Redis储存授权码(源码未提供授权码的redis储存
 *
 * @author MaoAtao
 * @date 2022-02-17 18:28:59
 */
public class RedisAuthorizationService implements CustomAuthorizationService {
    /**
     * redis key Authorization前缀
     */
    private static final String REDIS_KEY_AUTHORIZATION = "authorization:";

    /**
     * redis key authorization_code前缀
     */
    private static final String REDIS_KEY_CODE = "code:";

    /**
     * redis key access_token前缀
     */
    private static final String REDIS_KEY_ACCESS_TOKEN = "token:access:";

    /**
     * redis key refresh_token前缀
     */
    private static final String REDIS_KEY_REFRESH_TOKEN = "token:refresh:";

    /**
     * redis key oidc_token_id前缀
     */
    private static final String REDIS_KEY_OIDC = "token:oidc:";

    /**
     * redis key state前缀
     */
    private static final String REDIS_KEY_STATE = "state:";

    /**
     * redis key state前缀
     */
    private static final String REDIS_KEY_PRINCIPAL = "principal:";

    /**
     * 过期时间5分钟
     * <p>
     * 并非客户端详情中令牌配置的令牌时间,而是储存到redis中计算储存时间使用的默认时间
     */
    private static final long DEFAULT_EXPIRATION_SECONDS = 300;

    private final String prefix;

    public RedisAuthorizationService() {
        this(StrUtil.EMPTY);
    }

    public RedisAuthorizationService(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        Instant now = Instant.now();
        Map<Instant, OAuth2Token> tokenMap = new HashMap<>();
        saveAuthorizationCode(authorization, tokenMap, now);
        saveAccessToken(authorization, tokenMap, now);
        saveRefreshToken(authorization, tokenMap, now);
        saveOidcIdToken(authorization, tokenMap, now);
        long expiresIn = getExpiresIn(now, tokenMap);
        String authorizationState = authorization.getAttribute(OAuth2ParameterNames.STATE);
        if (StrUtil.isNotEmpty(authorizationState)) {
            RedisUtils.set(getStateKey(authorizationState), authorization.getId(), expiresIn);
        }
        RedisUtils.set(getAuthorizationKey(authorization.getId()), authorization, expiresIn);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        RedisUtils.delete(getAuthorizationKey(authorization.getId()));
        removeAuthorizationCode(authorization);
        removeAccessToken(authorization);
        removeRefreshToken(authorization);
        removeOidcIdToken(authorization);
    }

    @Override
    public OAuth2Authorization findById(String key) {
        return (OAuth2Authorization) RedisUtils.getObject(getAuthorizationKey(key));
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        if (tokenType == null) {
            String key = RedisUtils.getString(getStateKey(token));
            if (key == null || "".equals(key)) {
                key = RedisUtils.getString(getCodeKey(token));
            }
            if (key == null || "".equals(key)) {
                key = RedisUtils.getString(getAccessTokenKey(token));
            }
            if (key == null || "".equals(key)) {
                key = RedisUtils.getString(getRefreshTokenKey(token));
            }
            if (key == null || "".equals(key)) {
                return null;
            }
            return getAuthorization(key);
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            return getAuthorization(RedisUtils.getString(getStateKey(token)));
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            return getAuthorization(RedisUtils.getString(getCodeKey(token)));
        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            return getAuthorization(RedisUtils.getString(getAccessTokenKey(token)));
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            return getAuthorization(RedisUtils.getString(getRefreshTokenKey(token)));
        }
        return null;
    }

    @Override
    public List<OAuth2Authorization> findByPrincipal(String principalName) {
        Set<String> keys = RedisUtils.keys(getPrincipalKey(principalName + RedisUtils.REDIS_KEY + "*"));
        List<OAuth2Authorization> authorizations = new ArrayList<>();
        if (IterUtil.isNotEmpty(keys)) {
            keys.forEach(key -> authorizations.add(getAuthorization(
                    // 删除前缀
                    key.replaceAll(this.prefix + REDIS_KEY_PRINCIPAL + principalName + RedisUtils.REDIS_KEY, ""))));
        }
        return authorizations;
    }

    /**
     * 删除授权码
     *
     * @param authorization 授权信息
     */
    @Override
    public void removeAuthorizationCode(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null && authorizationCode.getToken() != null) {
            RedisUtils.delete(getCodeKey(authorizationCode.getToken().getTokenValue()));
        }
    }

    /**
     * 删除访问令牌
     *
     * @param authorization 授权信息
     */
    @Override
    public void removeAccessToken(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        if (accessToken != null && accessToken.getToken() != null) {
            RedisUtils.delete(getAccessTokenKey(accessToken.getToken().getTokenValue()));
            RedisUtils.delete(getPrincipalKey(authorization.getPrincipalName() + RedisUtils.REDIS_KEY + authorization.getId()));
        }
    }

    /**
     * 保存授权码
     *
     * @param authorization 授权信息
     * @param tokenMap      令牌map
     * @param now           现在时间
     */
    private void saveAuthorizationCode(OAuth2Authorization authorization, Map<Instant, OAuth2Token> tokenMap, Instant now) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null && authorizationCode.getToken() != null && authorizationCode.getToken().getExpiresAt() != null) {
            tokenMap.put(authorizationCode.getToken().getExpiresAt(), authorizationCode.getToken());
            RedisUtils.set(getCodeKey(authorizationCode.getToken().getTokenValue()), authorization.getId(), getExpiresIn(now, authorizationCode.getToken()));
        }
    }

    /**
     * 保存访问令牌
     *
     * @param authorization 授权信息
     * @param tokenMap      令牌map
     * @param now           现在时间
     */
    private void saveAccessToken(OAuth2Authorization authorization, Map<Instant, OAuth2Token> tokenMap, Instant now) {
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        if (accessToken != null && accessToken.getToken() != null && accessToken.getToken().getExpiresAt() != null) {
            tokenMap.put(accessToken.getToken().getExpiresAt(), accessToken.getToken());
            long expiresIn = getExpiresIn(now, accessToken.getToken());
            RedisUtils.set(getAccessTokenKey(accessToken.getToken().getTokenValue()), authorization.getId(), expiresIn);
            RedisUtils.set(getPrincipalKey(authorization.getPrincipalName() + RedisUtils.REDIS_KEY + authorization.getId()), authorization.getRegisteredClientId(), expiresIn);
        }
    }

    /**
     * 保存刷新令牌
     *
     * @param authorization 授权信息
     * @param tokenMap      令牌map
     * @param now           现在时间
     */
    private void saveRefreshToken(OAuth2Authorization authorization, Map<Instant, OAuth2Token> tokenMap, Instant now) {
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if (refreshToken != null && refreshToken.getToken() != null && refreshToken.getToken().getExpiresAt() != null) {
            tokenMap.put(refreshToken.getToken().getExpiresAt(), refreshToken.getToken());
            RedisUtils.set(getRefreshTokenKey(refreshToken.getToken().getTokenValue()), authorization.getId(), getExpiresIn(now, refreshToken.getToken()));
        }
    }

    /**
     * 删除刷新令牌
     *
     * @param authorization 授权信息
     */
    private void removeRefreshToken(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if (refreshToken != null && refreshToken.getToken() != null) {
            RedisUtils.delete(getRefreshTokenKey(refreshToken.getToken().getTokenValue()));
        }
    }

    /**
     * 保存Oidc令牌
     *
     * @param authorization 授权信息
     * @param tokenMap      令牌map
     * @param now           现在时间
     */
    private void saveOidcIdToken(OAuth2Authorization authorization, Map<Instant, OAuth2Token> tokenMap, Instant now) {
        OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
        if (oidcIdToken != null && oidcIdToken.getToken() != null && oidcIdToken.getToken().getExpiresAt() != null) {
            tokenMap.put(oidcIdToken.getToken().getExpiresAt(), oidcIdToken.getToken());
            RedisUtils.set(getOidcTokenKey(oidcIdToken.getToken().getTokenValue()), authorization.getId(), getExpiresIn(now, oidcIdToken.getToken()));
        }
    }

    /**
     * 删除Oidc令牌
     *
     * @param authorization 授权信息
     */
    private void removeOidcIdToken(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
        if (oidcIdToken != null && oidcIdToken.getToken() != null) {
            RedisUtils.delete(getOidcTokenKey(oidcIdToken.getToken().getTokenValue()));
        }
    }

    /**
     * 获取授权信息
     *
     * @param id 授权信息id
     * @return 授权信息
     */
    private OAuth2Authorization getAuthorization(String id) {
        return (OAuth2Authorization) RedisUtils.getObject(getAuthorizationKey(id));
    }

    /**
     * 获取授权信息的key
     *
     * @param id 授权信息id
     * @return 在redis储存的key
     */
    private String getAuthorizationKey(String id) {
        return this.prefix + REDIS_KEY_AUTHORIZATION + id;
    }

    /**
     * 获取授权码的key
     *
     * @param id 授权码id
     * @return 在redis储存的key
     */
    private String getCodeKey(String id) {
        return this.prefix + REDIS_KEY_CODE + id;
    }

    /**
     * 获取访问令牌的key
     *
     * @param id 访问令牌id
     * @return 在redis储存的key
     */
    private String getAccessTokenKey(String id) {
        return this.prefix + REDIS_KEY_ACCESS_TOKEN + id;
    }

    /**
     * 获取刷新令牌的key
     *
     * @param id 刷新令牌id
     * @return 在redis储存的key
     */
    private String getRefreshTokenKey(String id) {
        return this.prefix + REDIS_KEY_REFRESH_TOKEN + id;
    }

    /**
     * 获取Oidc令牌的key
     *
     * @param id Oidc令牌id
     * @return 在redis储存的key
     */
    private String getOidcTokenKey(String id) {
        return this.prefix + REDIS_KEY_OIDC + id;
    }

    /**
     * 获取授权主体的key
     *
     * @param id 授权主体id
     * @return 在redis储存的key
     */
    private String getPrincipalKey(String id) {
        return this.prefix + REDIS_KEY_PRINCIPAL + id;
    }

    /**
     * 获取授权State的key
     *
     * @param id 授权State
     * @return 在redis储存的key
     */
    private String getStateKey(String id) {
        return this.prefix + REDIS_KEY_STATE + id;
    }

    /**
     * 获取过期时间
     *
     * @param now   现在
     * @param token 令牌
     * @return 过期时间
     */
    private long getExpiresIn(Instant now, OAuth2Token token) {
        if (token.getExpiresAt() == null) {
            return DEFAULT_EXPIRATION_SECONDS;
        }
        return ChronoUnit.SECONDS.between(now, token.getExpiresAt());
    }

    /**
     * 获取过期时间
     *
     * @param now      现在时间
     * @param tokenMap 令牌map
     * @return 过期时间
     */
    private long getExpiresIn(Instant now, Map<Instant, OAuth2Token> tokenMap) {
        List<Instant> expires = new ArrayList<>(tokenMap.keySet());
        if (expires.size() == 0) {
            return DEFAULT_EXPIRATION_SECONDS;
        }
        if (expires.size() == 1) {
            return getExpiresIn(now, tokenMap.get(expires.get(0)));
        }
        Collections.sort(expires);
        return getExpiresIn(now, tokenMap.get(expires.get(expires.size() - 1)));
    }
}
