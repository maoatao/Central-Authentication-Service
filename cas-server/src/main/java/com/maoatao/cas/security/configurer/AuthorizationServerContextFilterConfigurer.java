package com.maoatao.cas.security.configurer;

import com.maoatao.cas.security.filter.TokenAuthenticationFilter;
import com.maoatao.cas.security.filter.AuthorizationServerContextFilter;
import com.maoatao.daedalus.core.context.SpringContextHolder;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 * 授权服务器上下文配置
 * <p>
 * {@link com.maoatao.cas.config.SecutityConfig}
 *
 * @author MaoAtao
 * @date 2023-03-09 15:59:49
 */
public class AuthorizationServerContextFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final AuthorizationServerSettings authorizationServerSettings = SpringContextHolder.getBean(AuthorizationServerSettings.class);

    @Override
    public void configure(HttpSecurity http) {
        AuthorizationServerContextFilter filter = new AuthorizationServerContextFilter(authorizationServerSettings);
        http.addFilterAfter(filter, TokenAuthenticationFilter.class);
    }
}
