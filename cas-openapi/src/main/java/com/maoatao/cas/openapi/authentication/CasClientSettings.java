package com.maoatao.cas.openapi.authentication;

import cn.hutool.core.collection.IterUtil;
import com.maoatao.cas.openapi.converter.ContextConverter;
import com.maoatao.cas.openapi.converter.JwtOperatorContextConverter;
import com.maoatao.cas.openapi.matcher.CasRequestMatcher;
import com.maoatao.synapse.lang.util.SynaAssert;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

/**
 * CAS 客户端配置
 *
 * @author MaoAtao
 * @date 2023-03-26 15:55:36
 */
@Getter
public class CasClientSettings {

    /**
     * app key (app的客户端id)
     */
    private final String appKey;
    /**
     * 上下文转换者
     */
    private final ContextConverter contextConverter;

    /**
     * 白名单适配器
     */
    private final List<CasRequestMatcher> permitMatchers;

    private CasClientSettings(String appKey, ContextConverter contextConverter, List<CasRequestMatcher> permitMatchers) {
        this.appKey = appKey;
        this.contextConverter = contextConverter;
        this.permitMatchers = permitMatchers;
    }

    public static CasClientSettingsBuilder builder() {
        return new CasClientSettingsBuilder();
    }

    public static class CasClientSettingsBuilder {
        private String appKey;
        private ContextConverter contextConverter;
        private List<CasRequestMatcher> permitMatchers;

        private CasClientSettingsBuilder() {
        }

        public CasClientSettingsBuilder appKey(String appKey) {
            this.appKey = appKey;
            return this;
        }

        public CasClientSettingsBuilder contextConverter(ContextConverter contextConverter) {
            this.contextConverter = contextConverter;
            return this;
        }

        public CasClientSettingsBuilder permitMatchers(List<CasRequestMatcher> permitMatchers) {
            this.permitMatchers = permitMatchers;
            return this;
        }

        public CasClientSettings build() {
            SynaAssert.notEmpty(this.appKey, "App Key 不能为空!");
            // 默认 jwt 转换者,无放行请求
            return new CasClientSettings(
                    this.appKey,
                    Objects.requireNonNullElse(this.contextConverter, new JwtOperatorContextConverter(this.appKey)),
                    IterUtil.isEmpty(this.permitMatchers) ? CasRequestMatcher.builder().build() : this.permitMatchers);
        }
    }
}
