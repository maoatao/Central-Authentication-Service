package com.maoatao.cas.util;

import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

import java.util.Base64;

/**
 * Id 工具类
 *
 * @author MaoAtao
 * @date 2023-03-02 11:09:29
 */
public abstract class Ids {

    private static final StringKeyGenerator SECURE_KEY_GENERATOR = new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding());

    private static final String USER_OPEN_ID_PREFIX = "uo";

    /**
     * 获取用户 open id
     *
     * @return 用户 open id
     */
    public static String nextUserOpenId() {
        return USER_OPEN_ID_PREFIX.concat(SECURE_KEY_GENERATOR.generateKey());
    }
}
