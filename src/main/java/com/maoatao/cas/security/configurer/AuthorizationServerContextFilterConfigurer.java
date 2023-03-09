package com.maoatao.cas.security.configurer;

import com.maoatao.cas.security.filter.AuthorizationFilter;
import com.maoatao.cas.security.filter.AuthorizationServerContextFilter;
import com.maoatao.daedalus.core.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 * 授权服务器上下文配置
 * <p>
 * {@link com.maoatao.cas.config.SecutityConfig}
 *
 * @author MaoAtao
 * @date 2023-03-09 15:59:49
 */
@Slf4j
public class AuthorizationServerContextFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Override
    public void configure(HttpSecurity http) {
        AuthorizationServerContextFilter filter = SpringContextUtils.getBean(AuthorizationServerContextFilter.class);
        if (filter != null) {
            http.addFilterAfter(filter, AuthorizationFilter.class);
        } else {
            log.warn("AuthorizationServerContextFilter 添加失败!");
        }
    }
}
