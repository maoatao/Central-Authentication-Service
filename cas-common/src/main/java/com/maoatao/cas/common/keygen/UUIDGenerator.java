package com.maoatao.cas.common.keygen;

import cn.hutool.core.util.IdUtil;

/**
 * UUID 生成器
 *
 * @author MaoAtao
 * @date 2023-04-03 11:14:13
 */
public class UUIDGenerator implements CasStringKeyGenerator {
    @Override
    public String generate() {
        return IdUtil.fastUUID();
    }
}
