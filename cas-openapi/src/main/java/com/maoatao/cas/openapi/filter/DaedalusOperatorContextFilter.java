package com.maoatao.cas.openapi.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.maoatao.cas.openapi.authentication.DaedalusSecuritySettings;
import com.maoatao.cas.openapi.converter.ContextConverter;
import com.maoatao.cas.openapi.matcher.DaedalusRequestMatcher;
import com.maoatao.daedalus.core.context.AnonymousOperatorContext;
import com.maoatao.daedalus.core.context.DaedalusOperatorContext;
import com.maoatao.daedalus.core.context.OperatorContextHolder;
import com.maoatao.synapse.lang.util.SynaAssert;
import com.maoatao.synapse.lang.util.SynaStrings;
import com.maoatao.synapse.web.response.HttpResponseStatus;
import com.maoatao.synapse.web.response.RestResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/**
 * 操作者过滤器
 *
 * @author MaoAtao
 * @date 2023-03-26 15:00:29
 */
@Slf4j
@Component
@Order(DaedalusOperatorContextFilter.ORDER)
public class DaedalusOperatorContextFilter extends GenericFilterBean {

    public static final int ORDER = 1;

    private static final String TOKEM_TYPE_BEARER = "Bearer";

    private final ContextConverter contextConverter;

    private final List<DaedalusRequestMatcher> permitMatchers;

    public DaedalusOperatorContextFilter(DaedalusSecuritySettings daedalusSecuritySettings) {
        SynaAssert.notNull(daedalusSecuritySettings, "daedalusSecuritySetting cannot be null");
        this.contextConverter = daedalusSecuritySettings.getContextConverter();
        this.permitMatchers = daedalusSecuritySettings.getPermitMatchers();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        doFilterInternal((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    private void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (doPermitFilter(request) || doTokenFilter(request)) {
                filterChain.doFilter(request, response);
            } else {
                unauthorized(response);
            }
        } finally {
            OperatorContextHolder.clearContext();
        }
    }

    private boolean doPermitFilter(HttpServletRequest request) {
        for (DaedalusRequestMatcher matcher : this.permitMatchers) {
            if (matcher.isMatch(request)) {
                OperatorContextHolder.setContext(AnonymousOperatorContext::new);
                return true;
            }
        }
        return false;
    }

    private boolean doTokenFilter(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(token) && token.startsWith(TOKEM_TYPE_BEARER)) {
            return doBearerTokenFilter(token);
        }
        return false;
    }

    private boolean doBearerTokenFilter(String token) {
        token = token.replace(TOKEM_TYPE_BEARER, SynaStrings.EMPTY).trim();
        DaedalusOperatorContext operatorContext = this.contextConverter.convert(token);
        if (operatorContext != null && operatorContext.isAuthenticated()) {
            OperatorContextHolder.setContext(operatorContext);
            return true;
        }
        return false;
    }

    private void unauthorized(ServletResponse response) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JSONUtil.parseObj(RestResponse.failed(HttpResponseStatus.UNAUTHORIZED)).toString());
        } catch (IOException e) {
            log.error("response error", e);
        }
    }
}
