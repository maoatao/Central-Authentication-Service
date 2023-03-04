package com.maoatao.cas.security.filter;

import com.maoatao.cas.security.HttpConstants;
import com.maoatao.cas.util.FilterUtils;
import com.maoatao.synapse.core.util.SynaSafes;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资源过滤器
 * <p>
 * OAuth2原有的接口不允许访问
 * <p>
 * 放行白名单和根路径为{@value HttpConstants#BASE_URL}的请求
 *
 * @author MaoAtao
 * @date 2022-10-24 11:17:31
 */
public class ResourcesFilter extends GenericFilterBean {

    private static final Set<String> WHITE_LIST = Arrays.stream(HttpConstants.WHITE_LIST).map(o -> o.replace("/**", "")).collect(Collectors.toSet());

    public ResourcesFilter() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (doFilterInternal(request)) {
            chain.doFilter(request, response);
        } else {
            FilterUtils.notFound(response);
        }
    }

    private boolean doFilterInternal(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = SynaSafes.of(httpServletRequest.getRequestURI());
        if (url.startsWith(HttpConstants.BASE_URL)) {
            return true;
        }
        for (String s : WHITE_LIST) {
            if (url.startsWith(s)) {
                return true;
            }
        }
        return false;
    }
}
