package com.maoatao.cas.util;

import cn.hutool.core.util.IdUtil;

/**
 * Id 工具类
 *
 * @author MaoAtao
 * @date 2023-03-02 11:09:29
 */
public abstract class Ids {

    private static final String USER_OPEN_ID_PREFIX = "UO";

    /**
     * 获取用户 open id
     *
     * @return 用户 open id
     */
    public static String nextUserOpenId() {
        return USER_OPEN_ID_PREFIX.concat(IdUtil.objectId());
    }
}
