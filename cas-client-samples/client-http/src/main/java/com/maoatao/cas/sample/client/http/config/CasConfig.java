package com.maoatao.cas.sample.client.http.config;

import com.maoatao.cas.openapi.authentication.CasAuthorizationService;
import com.maoatao.cas.openapi.authentication.CasServerSettings;
import com.maoatao.cas.openapi.converter.DefaultOperatorContextConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CAS 配置
 *
 * @author MaoAtao
 * @date 2023-03-30 18:21:18
 */
@Configuration
public class CasConfig {

    @Bean
    public CasServerSettings casServerSettings(CasAuthorizationService casAuthorizationService) {
        return CasServerSettings.builder()
                .contextConverter(new DefaultOperatorContextConverter(casAuthorizationService))
                .build();
    }

}
