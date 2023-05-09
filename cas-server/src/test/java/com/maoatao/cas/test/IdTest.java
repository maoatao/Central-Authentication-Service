package com.maoatao.cas.test;

import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;

/**
 * ID
 *
 * @author MaoAtao
 * @date 2023-05-09 18:03:59
 */
public class IdTest {

    @Test
    void id_test(){
        System.out.println(IdUtil.nanoId(48));
        System.out.println(IdUtil.objectId());
        System.out.println(IdUtil.fastSimpleUUID());
        System.out.println(IdUtil.getSnowflakeNextIdStr());
    }
}
