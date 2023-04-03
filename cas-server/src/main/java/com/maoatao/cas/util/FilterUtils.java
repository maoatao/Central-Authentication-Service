package com.maoatao.cas.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.IterUtil;
import com.maoatao.cas.security.bean.ClientUser;
import com.maoatao.cas.security.authorization.CustomAuthorizationServerContext;
import com.maoatao.synapse.lang.exception.SynaException;
import com.maoatao.synapse.lang.util.SynaSafes;
import com.maoatao.daedalus.web.response.HttpResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 过滤器工具类
 *
 * @author MaoAtao
 * @date 2023-03-04 19:02:52
 */
public final class FilterUtils {

    private FilterUtils() {}

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
        String[] values = SynaSafes.of(Base64.decodeStr(token)).split(BASIC_TOKEN_PARAMETERS_DELIMITER);
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
     * 请求失败时,构建响应状态
     *
     * @param response 响应
     * @return 异常
     */
    public static SynaException buildResponseStatus(HttpServletResponse response) {
        return new SynaException(switch (response.getStatus()) {
            case 404 -> HttpResponseStatus.NOT_FOUND;
            case 500 -> HttpResponseStatus.FAILED;
            default -> HttpResponseStatus.UNAUTHORIZED;
        });
    }

    /**
     * @return 请求匹配器构建器
     */
    public static RequestMatchersBuilder requestMatchersBuilder() {
        return new RequestMatchersBuilder();
    }

    /**
     * 是否存在请求匹配
     *
     * @return 有任意匹配返回 true, matchers 为空返回 false
     */
    public static boolean anyMatch(List<RequestMatcher> matchers, HttpServletRequest request) {
        if (IterUtil.isEmpty(matchers)) {
            return false;
        }
        for (RequestMatcher requestMatcher : matchers) {
            if (requestMatcher.matcher(request).isMatch()) {
                return true;
            }
        }
        return false;
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

    public static class RequestMatchersBuilder {
        private final List<RequestMatcher> matchers;

        public RequestMatchersBuilder() {
            this.matchers = new ArrayList<>();
        }

        /**
         * 构建matchers
         *
         * @param httpMethod  请求方法
         * @param antPatterns 匹配 url
         * @return RequestMatchersBuilder
         */
        public RequestMatchersBuilder antMatchers(HttpMethod httpMethod, String... antPatterns) {
            addMatchers(httpMethod, antPatterns);
            return this;
        }

        public List<RequestMatcher> build() {
            return this.matchers;
        }

        private void addMatchers(HttpMethod httpMethod, String... antPatterns) {
            String method = (httpMethod != null) ? httpMethod.toString() : null;
            for (String pattern : antPatterns) {
                this.matchers.add(new AntPathRequestMatcher(pattern, method));
            }
        }
    }
}
