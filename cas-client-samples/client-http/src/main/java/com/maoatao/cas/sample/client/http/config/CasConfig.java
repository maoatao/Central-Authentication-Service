package com.maoatao.cas.sample.client.http.config;

import com.maoatao.cas.openapi.authentication.CasServerSettings;
import com.maoatao.cas.openapi.matcher.CasRequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CAS 配置
 *
 * @author LiYuanHao
 * @date 2023-03-30 18:21:18
 */
@Configuration
public class CasConfig {

    @Bean
    public CasServerSettings casServerSettings() {
        return CasServerSettings.builder()
                .permitMatchers(CasRequestMatcher.builder().antMatchers(null, "/demo").build())
                .build();
    }

}
