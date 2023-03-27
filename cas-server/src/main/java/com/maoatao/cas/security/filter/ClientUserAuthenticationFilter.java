package com.maoatao.cas.security.filter;

import com.maoatao.synapse.lang.util.SynaSafes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 自定义用户权限过滤器(添加clientId参数)
 * <p>
 * Customized by {@link UsernamePasswordAuthenticationFilter}
 *
 * @author MaoAtao
 * @date 2023-03-10 17:25:11
 */
public class ClientUserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SPRING_SECURITY_FORM_CLIENT_ID_KEY = "clientId";

    public ClientUserAuthenticationFilter() {
        super();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = SynaSafes.of(obtainUsername(request)).trim();
        String password = SynaSafes.of(obtainPassword(request));
        String clientId = SynaSafes.of(obtainClientId(request));
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
                password);
        authRequest.setDetails(clientId);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Nullable
    protected String obtainClientId(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_CLIENT_ID_KEY);
    }
}
