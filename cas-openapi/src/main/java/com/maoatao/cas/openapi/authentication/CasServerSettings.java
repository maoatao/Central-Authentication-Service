package com.maoatao.cas.openapi.authentication;

import cn.hutool.core.collection.IterUtil;
import com.maoatao.cas.openapi.converter.ContextConverter;
import com.maoatao.cas.openapi.converter.JwtOperatorContextConverter;
import com.maoatao.cas.openapi.matcher.CasRequestMatcher;
import java.util.List;
import lombok.Getter;

/**
 * CAS 服务配置
 *
 * @author MaoAtao
 * @date 2023-03-26 15:55:36
 */
@Getter
public class CasServerSettings {

    /**
     * 上下文转换者
     */
    private final ContextConverter contextConverter;

    /**
     * 白名单适配器
     */
    private final List<CasRequestMatcher> permitMatchers;

    private CasServerSettings(ContextConverter contextConverter, List<CasRequestMatcher> permitMatchers) {
        this.contextConverter = contextConverter;
        this.permitMatchers = permitMatchers;
    }

    public static DaedalusSecuritySettingsBuilder builder() {
        return new DaedalusSecuritySettingsBuilder();
    }

    public static class DaedalusSecuritySettingsBuilder {
        private ContextConverter contextConverter;
        private List<CasRequestMatcher> permitMatchers;

        private DaedalusSecuritySettingsBuilder() {
        }

        public DaedalusSecuritySettingsBuilder contextConverter(ContextConverter contextConverter) {
            this.contextConverter = contextConverter;
            return this;
        }

        public DaedalusSecuritySettingsBuilder permitMatchers(List<CasRequestMatcher> permitMatchers) {
            this.permitMatchers = permitMatchers;
            return this;
        }

        public CasServerSettings build() {
            // 默认jwt转换者和空白名单
            return new CasServerSettings(
                    this.contextConverter == null ? new JwtOperatorContextConverter() : this.contextConverter,
                    IterUtil.isEmpty(this.permitMatchers) ? CasRequestMatcher.builder().build() : this.permitMatchers
            );
        }
    }
}
