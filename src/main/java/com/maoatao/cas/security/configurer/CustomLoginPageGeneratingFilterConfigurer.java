package com.maoatao.cas.security.configurer;

import com.maoatao.cas.security.filter.CustomLoginPageGeneratingFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;

/**
 * 用户登录授权过滤配置器
 * <p>
 * {@link com.maoatao.cas.config.SecutityConfig}
 *
 * @author MaoAtao
 * @date 2023-03-10 18:35:28
 */
@Slf4j
public class CustomLoginPageGeneratingFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(new CustomLoginPageGeneratingFilter(), DefaultLoginPageGeneratingFilter.class);
    }
}
