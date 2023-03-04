package com.maoatao.cas.util;

import cn.hutool.json.JSONUtil;
import com.maoatao.cas.security.bean.ClientUser;
import com.maoatao.synapse.core.web.HttpResponseStatus;
import com.maoatao.synapse.core.web.RestResponse;
import jakarta.servlet.ServletResponse;
import org.springframework.http.MediaType;

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
}
