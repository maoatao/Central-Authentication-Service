package com.maoatao.cas.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maoatao.synapse.core.annotation.OriginResponse;
import com.maoatao.synapse.core.web.RestResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.AnnotatedElement;

/**
 * 全局响应处理程序
 *
 * @author MaoAtao
 * @date 2023-02-10 14:49:26
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.maoatao.cas.*")
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        AnnotatedElement annotatedElement = returnType.getAnnotatedElement();
        OriginResponse originResponse = AnnotationUtils.findAnnotation(annotatedElement, OriginResponse.class);
        // 没有OriginResponse注解，不是ResponseEntity(springboot web Response)类时封装数据
        return originResponse == null && !ResponseEntity.class.equals(returnType.getParameterType());
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (body instanceof String) {
            return objectMapper.writeValueAsString(RestResponse.success(body));
        }
        if (body instanceof RestResponse<?>) {
            return body;
        }
        return RestResponse.success(body);
    }
}