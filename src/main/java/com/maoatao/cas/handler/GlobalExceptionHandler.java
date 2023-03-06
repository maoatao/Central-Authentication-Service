package com.maoatao.cas.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.maoatao.cas.util.ServletUtils;
import com.maoatao.synapse.core.lang.SynaException;
import com.maoatao.synapse.core.web.HttpResponseStatus;
import com.maoatao.synapse.core.web.RestResponse;
import com.maoatao.synapse.core.web.VerifyResponseErrors;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Set;

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

    /**
     * 普通参数(非 java bean)校验出错时抛出
     * <p>
     * 关键词 : @Validated , 非Java bean
     *
     * @param request 请求
     * @param e       异常
     * @return RestResponse
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public RestResponse<Void> exceptionHandler(HttpServletRequest request, ConstraintViolationException e) {
        return verificationHandler(request, e);
    }

    /**
     * 请求体绑定到java bean上失败时抛出
     * <p>
     * 关键词 : @Valid , @RequestBody,Java bean ,表单(Content-Type: application/json,Content-Type: application/xml)
     *
     * @param request 请求
     * @param e       异常
     * @return RestResponse
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public RestResponse<Void> exceptionHandler(HttpServletRequest request, MethodArgumentNotValidException e) {
        return verificationHandler(request, e);
    }

    /**
     * 请求参数绑定到java bean上失败时抛出
     * <p>
     * 关键词 : @Valid , Java bean ,表单(Content-Type: multipart/form-data)
     *
     * @param request 请求
     * @param e       异常
     * @return RestResponse
     */
    @ExceptionHandler(value = BindException.class)
    public RestResponse<Void> exceptionHandler(HttpServletRequest request, BindException e) {
        return verificationHandler(request, e);
    }

    /**
     * 异常处理程序
     *
     * @param request 请求
     * @param e       异常
     * @return RestResponse
     */
    @ExceptionHandler(SynaException.class)
    public RestResponse<Void> exceptionHandler(HttpServletRequest request, SynaException e) {
        log.debug("--------------------=== 请求终止处理[START] ===--------------------");
        log.debug("当前请求：{}", request.getRequestURI());
        if (MapUtil.isNotEmpty(request.getParameterMap())) {
            log.debug("请求参数：{}", new JSONObject(request.getParameterMap()));
        }
        log.debug("终止原因：{}", e.getMessage());
        log.debug("--------------------=== 请求终止处理[E N D] ===--------------------");
        return RestResponse.failed(e.getStatus());
    }

    /**
     * 异常处理程序
     *
     * @param request 请求
     * @param e       异常
     * @return RestResponse
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public RestResponse<Void> exceptionHandler(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        log.error("不允许的请求方法'{}'", request.getMethod(), e);
        return RestResponse.failed("不允许的请求方法".concat(request.getMethod()));
    }

    /**
     * 异常处理程序
     *
     * @param request 请求
     * @param e       异常
     * @return RestResponse
     */
    @ExceptionHandler(Exception.class)
    public RestResponse<Void> exceptionHandler(HttpServletRequest request, Exception e) {
        log.error("报错请求：{},请求参数：{}", request.getRequestURI(), new JSONObject(request.getParameterMap()), e);
        return RestResponse.failed("服务器遇到错误，无法完成请求！");
    }

    private RestResponse<Void> verificationHandler(HttpServletRequest request, Exception e) {
        log.debug("--------------------=== 参数验证失败[START] ===--------------------");
        log.debug("当前请求：{}", request.getRequestURI());
        VerifyResponseErrors errors = new VerifyResponseErrors();
        if (e instanceof BindException bindException) {
            errors.addAll(buildVerification(bindException.getAllErrors()));
        }
        if (e instanceof ConstraintViolationException exception) {
            errors.addAll(buildVerification(exception.getConstraintViolations()));
        }
        if (errors.getErrors().isEmpty()) {
            log.warn("未支持的参数校验异常");
        } else {
            errors.getErrors().forEach(o -> log.debug("字段:[{}] 原因:[{}] 参数:[{}]", o.getField(), o.getMessage(), o.getValue()));
        }
        log.debug("--------------------=== 参数验证失败[E N D] ===--------------------");
        return RestResponse.failed(errors);
    }

    /**
     * 构建响应数据
     *
     * @return 响应数据封装类
     */
    private static List<VerifyResponseErrors.VerifyDetails> buildVerification(List<ObjectError> errors) {
        return errors.stream().map(objectError -> {
            if (objectError instanceof FieldError fieldError) {
                return new VerifyResponseErrors.VerifyDetails(fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
            } else {
                return new VerifyResponseErrors.VerifyDetails(objectError.getObjectName(), null, objectError.getDefaultMessage());
            }
        }).toList();
    }

    /**
     * 构建响应数据
     *
     * @return 响应数据封装类
     */
    private static List<VerifyResponseErrors.VerifyDetails> buildVerification(Set<ConstraintViolation<?>> errors) {
        return errors.stream().map(o -> {
            String field = null;
            if (o.getPropertyPath() instanceof PathImpl path) {
                field = path.getLeafNode().getName();
            }
            return new VerifyResponseErrors.VerifyDetails(field, o.getInvalidValue(), o.getMessage());
        }).toList();
    }
}