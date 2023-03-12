package com.maoatao.cas.security.configurer;

import com.maoatao.cas.security.filter.CustomUserAuthenticationFilter;
import com.maoatao.daedalus.core.util.SpringContextUtils;
import com.maoatao.synapse.lang.util.SynaAssert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * 用户登录授权过滤配置器
 * <p>
 * {@link com.maoatao.cas.config.SecutityConfig}
 *
 * @author MaoAtao
 * @date 2023-03-10 18:35:28
 */
@Slf4j
public class CustomUserAuthenticationFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final AuthenticationManager authenticationManager = SpringContextUtils.getBean(AuthenticationManager.class);

    @Override
    public void configure(HttpSecurity http) {
        CustomUserAuthenticationFilter filter = new CustomUserAuthenticationFilter(authenticationManager);
        SessionAuthenticationStrategy sessionAuthenticationStrategy = http.getSharedObject(SessionAuthenticationStrategy.class);
        SynaAssert.notNull(sessionAuthenticationStrategy, "sessionAuthenticationStrategy cannot be null");
        filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        filter.setSecurityContextHolderStrategy(SecurityContextHolder.getContextHolderStrategy());
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
