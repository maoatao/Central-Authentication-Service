package com.maoatao.cas.util;

import cn.hutool.core.util.IdUtil;

/**
 * Id 工具类
 *
 * @author MaoAtao
 * @date 2023-03-02 11:09:29
 */
public abstract class Ids {

    private static final String USER_UNIQUE_ID_PREFIX = "U";

    /**
     * 前缀{@value USER_UNIQUE_ID_PREFIX}+24位编号
     *
     * @return 用户 id
     */
    public static String user() {
        return USER_UNIQUE_ID_PREFIX.concat(IdUtil.objectId());
    }
}
