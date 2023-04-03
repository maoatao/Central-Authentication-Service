package com.maoatao.cas.handler;

import cn.hutool.core.util.StrUtil;
import com.maoatao.daedalus.web.util.ServletUtils;
import com.maoatao.daedalus.web.response.HttpResponseStatus;
import com.maoatao.daedalus.web.response.RestResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理相关 全局异常处理程序
 *
 * @author MaoAtao
 * @date 2021-12-20 19:05:09
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 没有方法权限异常
     *
     * @param request 请求
     * @param e       异常
     * @return RestResponse
     */
    @ExceptionHandler(AccessDeniedException.class)
    public RestResponse<Object> exceptionHandler(HttpServletRequest request, AccessDeniedException e) {
        String token = ServletUtils.getAuthorization(request);
        log.debug("不允许访问的请求：{} , 授权信息：{}, 拦截原因：{}", request.getRequestURI(), StrUtil.isBlank(token) ? "null" : token, e.getMessage());
        return RestResponse.failed(HttpResponseStatus.UNAUTHORIZED);
    }
}
