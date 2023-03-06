package com.maoatao.cas.security.filter;

import com.maoatao.synapse.core.lang.SynaException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * 异常过滤器(filter 内部异常处理)
 *
 * @author MaoAtao
 * @date 2023-03-06 22:33:09
 */
public class ExceptionFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver resolver;

    public ExceptionFilter(HandlerExceptionResolver resolver) {this.resolver = resolver;}


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
            System.out.println(response);
        } catch (Exception e) {
            resolver.resolveException(request, response, null, e);
        }
    }
}
