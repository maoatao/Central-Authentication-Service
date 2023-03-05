package com.maoatao.cas.util;

import cn.hutool.json.JSONUtil;
import com.maoatao.cas.security.bean.ClientUser;
import com.maoatao.cas.security.oauth2.auth.CustomAuthorizationServerContext;
import com.maoatao.synapse.core.web.HttpResponseStatus;
import com.maoatao.synapse.core.web.RestResponse;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

    /**
     * 未找到资源
     */
    public static void notFound(ServletResponse response) throws IOException {
        buildResponse(response, RestResponse.failed(HttpResponseStatus.NOT_FOUND));
    }

    /**
     * 未授权的请求
     */
    public static void unauthorized(ServletResponse response) throws IOException {
        buildResponse(response, RestResponse.failed(HttpResponseStatus.UNAUTHORIZED));
    }

    private static void buildResponse(ServletResponse response, Object obj) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().println(JSONUtil.parseObj(obj));
        response.getWriter().flush();
    }

    private static String resolveIssuer(AuthorizationServerSettings authorizationServerSettings, HttpServletRequest request) {
        return authorizationServerSettings.getIssuer() != null ?
                authorizationServerSettings.getIssuer() :
                getContextPath(request);
    }

    private static String getContextPath(HttpServletRequest request) {
        // @formatter:off
        return UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
                .replacePath(request.getContextPath())
                .replaceQuery(null)
                .fragment(null)
                .build()
                .toUriString();
        // @formatter:on
    }
}
