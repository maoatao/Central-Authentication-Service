package com.maoatao.cas.security.authorization;

import com.maoatao.cas.common.keygen.CasStringKeyGenerator;
import com.maoatao.cas.common.keygen.UUIDGenerator;
import com.maoatao.synapse.lang.util.SynaAssert;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.Getter;

/**
 * CAS 服务端配置
 *
 * @author MaoAtao
 * @date 2023-03-26 15:55:36
 */
@Getter
public class CasServerSettings {

    /**
     * app key (app的客户端id)
     */
    private final String appKey;
    /**
     * 访问令牌 令牌值生成器
     */
    private final CasStringKeyGenerator accessTokenGenerator;
    /**
     * 刷新令牌 令牌值生成器
     */
    private final CasStringKeyGenerator refreshTokenGenerator;
    /**
     * 授权码 令牌值生成器
     */
    private final CasStringKeyGenerator authorizationCodeGenerator;

    public CasServerSettings(String appKey,
                             CasStringKeyGenerator accessTokenGenerator,
                             CasStringKeyGenerator refreshTokenGenerator,
                             CasStringKeyGenerator authorizationCodeGenerator) {
        this.appKey = appKey;
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.authorizationCodeGenerator = authorizationCodeGenerator;
    }

    public static CasServerSettingsBuilder builder() {
        return new CasServerSettingsBuilder();
    }

    public static class CasServerSettingsBuilder {

        private String appKey;

        private Supplier<CasStringKeyGenerator> accessTokenGenerator;

        private Supplier<CasStringKeyGenerator> refreshTokenGenerator;

        private Supplier<CasStringKeyGenerator> authorizationCodeGenerator;

        private CasServerSettingsBuilder() {
        }

        public CasServerSettingsBuilder appKey(String appKey) {
            this.appKey = appKey;
            return this;
        }

        public CasServerSettingsBuilder accessTokenGenerator(Supplier<CasStringKeyGenerator> accessTokenGenerator) {
            this.accessTokenGenerator = accessTokenGenerator;
            return this;
        }

        public CasServerSettingsBuilder refreshTokenGenerator(Supplier<CasStringKeyGenerator> refreshTokenGenerator) {
            this.refreshTokenGenerator = refreshTokenGenerator;
            return this;
        }

        public CasServerSettingsBuilder authorizationCodeGenerator(Supplier<CasStringKeyGenerator> authorizationCodeGenerator) {
            this.authorizationCodeGenerator = authorizationCodeGenerator;
            return this;
        }

        public CasServerSettings build() {
            SynaAssert.notEmpty(this.appKey, "App Key 不能为空!");
            // 默认 UUID 风格
            return new CasServerSettings(
                    this.appKey,
                    Optional.ofNullable(this.accessTokenGenerator).orElse(UUIDGenerator::new).get(),
                    Optional.ofNullable(this.refreshTokenGenerator).orElse(UUIDGenerator::new).get(),
                    Optional.ofNullable(this.authorizationCodeGenerator).orElse(UUIDGenerator::new).get()
            );
        }
    }
}
