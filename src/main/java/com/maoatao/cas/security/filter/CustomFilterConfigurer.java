package com.maoatao.cas.security.filter;

import com.maoatao.cas.core.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * 令牌过滤配置器
 *
 * @author MaoAtao
 * @date 2022-10-24 11:16:14
 */
@Configuration
public class CustomFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private OAuth2AuthorizationService oAuth2AuthorizationService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private AuthorizationServerSettings authorizationServerSettings;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public void configure(HttpSecurity http) {
        ExceptionFilter exceptionFilter = new ExceptionFilter(resolver);
        http.addFilterAfter(exceptionFilter, ExceptionTranslationFilter.class);
        TokenFilter tokenFilter = new TokenFilter(oAuth2AuthorizationService, authorizationService);
        // 令牌过滤放在原 login 登录过滤前(登录过滤在添加了 clientId 参数后不能用了,不想重新写,且原接口禁止访问了)
        http.addFilterAfter(tokenFilter, ExceptionFilter.class);
        // 资源过滤,放在令牌过滤前(禁止原有接口的请求)
        // ResourcesFilter resourcesFilter = new ResourcesFilter();
        // http.addFilterBefore(resourcesFilter, TokenFilter.class);
        // 获取令牌和授权码时要用,放在令牌过滤后
        AuthorizationServerContextFilter authorizationServerContextFilter = new AuthorizationServerContextFilter(authorizationServerSettings);
        http.addFilterAfter(authorizationServerContextFilter, TokenFilter.class);

    }
}
