package com.maoatao.cas.util;

import cn.hutool.json.JSONUtil;
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
