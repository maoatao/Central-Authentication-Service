package com.maoatao.cas.util;

import cn.hutool.core.util.IdUtil;

/**
 * Id 工具类
 *
 * @author MaoAtao
 * @date 2023-03-02 11:09:29
 */
public final class IdUtils {

    private IdUtils() {}

    /**
     * OPEN ID 前缀
     */
    private static final String USER_OPEN_ID_PREFIX = "UO";
    /**
     * OPEN ID 不含前缀长度
     */
    private static final int USER_OPEN_ID_LENGTH = 48;

    /**
     * 获取用户 open id
     *
     * @return 用户 open id
     */
    public static String nextUserOpenId() {
        return USER_OPEN_ID_PREFIX.concat(IdUtil.nanoId(USER_OPEN_ID_LENGTH));
    }
}
