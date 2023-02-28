package com.maoatao.cas.security.generator;

import org.springframework.security.crypto.keygen.StringKeyGenerator;

import java.util.UUID;

/**
 * UUID生成器
 *
 * @author MaoAtao
 * @date 2022-10-13 12:30:34
 */
public class UUIDStringKeyGenerator implements StringKeyGenerator {
    @Override
    public String generateKey() {
        return UUID.randomUUID().toString();
    }
}
