package com.maoatao.cas.common.keygen;

/**
 * 令牌值生成器
 *
 * @author MaoAtao
 * @date 2023-04-03 11:00:57
 */
public interface CasStringKeyGenerator {

    /**
     * 生成
     *
     * @return 非空字符串
     */
    String generate();

    // TODO: MaoAtao 2023-04-14 10:11:59 支持接口,多类型委派生成器
}
