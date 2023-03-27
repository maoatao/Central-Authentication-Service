package com.maoatao.cas.security.configurer;

import com.maoatao.cas.core.service.AuthorizationService;
import com.maoatao.cas.security.filter.TokenAuthenticationFilter;
import com.maoatao.daedalus.core.context.SpringContextHolder;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * 授权过滤配置器
 * <p>
 * {@link com.maoatao.cas.config.SecutityConfig}
 *
 * @author MaoAtao
 * @date 2023-03-09 15:54:00
 */
public class TokenAuthenticationFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final OAuth2AuthorizationService oAuth2AuthorizationService = SpringContextHolder.getBean(OAuth2AuthorizationService.class);

    private final AuthorizationService authorizationService = SpringContextHolder.getBean(AuthorizationService.class);

    private final HandlerExceptionResolver handlerExceptionResolver = SpringContextHolder.getBean("handlerExceptionResolver", HandlerExceptionResolver.class);

    @Override
    public void configure(HttpSecurity http) {
        TokenAuthenticationFilter filter = new TokenAuthenticationFilter(oAuth2AuthorizationService, authorizationService, handlerExceptionResolver);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
