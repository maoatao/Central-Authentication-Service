package com.maoatao.cas.security.configurer;

import com.maoatao.cas.core.service.AuthorizationService;
import com.maoatao.cas.security.filter.SecurityContextFilter;
import com.maoatao.daedalus.core.util.SpringContextUtils;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 授权过滤配置器
 * <p>
 * {@link com.maoatao.cas.config.SecutityConfig}
 *
 * @author MaoAtao
 * @date 2023-03-09 15:54:00
 */
public class SecurityContextConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final OAuth2AuthorizationService oAuth2AuthorizationService = SpringContextUtils.getBean(OAuth2AuthorizationService.class);

    private final AuthorizationService authorizationService = SpringContextUtils.getBean(AuthorizationService.class);

    @Override
    public void configure(HttpSecurity http) {
        SecurityContextFilter filter = new SecurityContextFilter(oAuth2AuthorizationService, authorizationService, null);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
