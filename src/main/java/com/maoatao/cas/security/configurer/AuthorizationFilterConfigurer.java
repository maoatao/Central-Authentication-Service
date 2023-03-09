package com.maoatao.cas.security.configurer;

import com.maoatao.cas.security.filter.AuthorizationFilter;
import com.maoatao.daedalus.core.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

/**
 * 授权过滤配置器
 * <p>
 * {@link com.maoatao.cas.config.SecutityConfig}
 *
 * @author MaoAtao
 * @date 2023-03-09 15:54:00
 */
@Slf4j
public class AuthorizationFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Override
    public void configure(HttpSecurity http) {
        AuthorizationFilter filter = SpringContextUtils.getBean(AuthorizationFilter.class);
        if (filter != null) {
            http.addFilterAfter(filter, AnonymousAuthenticationFilter.class);
        } else {
            log.warn("AuthorizationFilter 添加失败!");
        }
    }
}
