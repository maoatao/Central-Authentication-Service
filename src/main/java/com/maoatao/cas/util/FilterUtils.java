package com.maoatao.cas.util;

import com.maoatao.cas.security.bean.ClientUser;
import com.maoatao.cas.security.oauth2.auth.CustomAuthorizationServerContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 过滤器工具类
 *
 * @author MaoAtao
 * @date 2023-03-04 19:02:52
 */
public abstract class FilterUtils {

    /**
     * Basic 令牌参数分隔符
     */
    private static final String BASIC_TOKEN_PARAMETERS_DELIMITER = ":";
    /**
     * Basic 令牌参数个数
     */
    private static final int BASIC_TOKEN_PARAMETERS_NUM = 3;

    /**
     * 通过令牌构建用户详情
     *
     * @param token Basic 令牌(clientId:username:password)
     * @return 用户详情
     */
    public static ClientUser buildClientUserByToken(String token) {
        String[] values = token.split(BASIC_TOKEN_PARAMETERS_DELIMITER);
        if (values.length == BASIC_TOKEN_PARAMETERS_NUM) {
            return ClientUser.builder().clientId(values[0]).username(values[1]).password(values[2]).build();
        }
        return null;
    }

    /**
     * 构建授权服务器上下文
     *
     * @param settings 授权服务器配置
     * @return 授权服务器上下文
     */
    public static AuthorizationServerContext buildAuthorizationServerContext(AuthorizationServerSettings settings, HttpServletRequest request) {
        return new CustomAuthorizationServerContext(() -> resolveIssuer(settings, request), settings);
    }

    private static String resolveIssuer(AuthorizationServerSettings authorizationServerSettings, HttpServletRequest request) {
        return authorizationServerSettings.getIssuer() != null ?
                authorizationServerSettings.getIssuer() :
                getContextPath(request);
    }

    private static String getContextPath(HttpServletRequest request) {
        return UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
                .replacePath(request.getContextPath())
                .replaceQuery(null)
                .fragment(null)
                .build()
                .toUriString();
    }
}
