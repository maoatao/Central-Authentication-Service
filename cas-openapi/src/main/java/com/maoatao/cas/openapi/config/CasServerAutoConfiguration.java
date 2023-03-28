package com.maoatao.cas.openapi.config;

import com.maoatao.cas.openapi.authentication.CasServerSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置
 *
 * @author MaoAtao
 * @date 2023-03-26 18:55:42
 */
@Configuration
public class CasServerAutoConfiguration {

    /**
     * 默认安全配置,使用jwt转换者,没有白名单
     * <p>
     * 自定义配置替换默认配置代码如下,注意请不要使用重复的bean名称(方法名不要使用defaultCasServerSettings)
     * <pre>
     * &#064;Bean
     * public CasServerSettings casServerSettings() {
     *     return CasServerSettings.builder()
     *             .contextConverter( xxx )
     *             .permitMatchers( xxx )
     *             .build();
     * }
     * </pre>
     */
    @Bean
    public CasServerSettings defaultCasServerSettings() {
        return CasServerSettings.builder().build();
    }
}
