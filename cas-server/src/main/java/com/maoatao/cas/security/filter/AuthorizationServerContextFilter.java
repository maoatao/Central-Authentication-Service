package com.maoatao.cas.security.filter;

import com.maoatao.cas.util.FilterUtils;
import com.maoatao.synapse.lang.util.SynaAssert;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * 授权服务器上下文过滤
 * <p>
 * org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.AuthorizationServerContextFilter 私有
 * 新增的部分授权接口需要AuthorizationServerContext,这里提前配置
 *
 * @author MaoAtao
 * @date 2023-03-05 15:13:40
 */
public class AuthorizationServerContextFilter extends GenericFilterBean {

    private final AuthorizationServerSettings authorizationServerSettings;

    public AuthorizationServerContextFilter(AuthorizationServerSettings authorizationServerSettings) {
        SynaAssert.notNull(authorizationServerSettings, "authorizationServerSettings cannot be null");
        this.authorizationServerSettings = authorizationServerSettings;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilterInternal((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // TODO: 2023-03-09 16:35:16 特定请求执行
        try {
            AuthorizationServerContext authorizationServerContext = FilterUtils.buildAuthorizationServerContext(this.authorizationServerSettings, request);
            AuthorizationServerContextHolder.setContext(authorizationServerContext);
            filterChain.doFilter(request, response);
        } finally {
            AuthorizationServerContextHolder.resetContext();
        }
    }
}
