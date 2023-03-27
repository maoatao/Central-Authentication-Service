package com.maoatao.cas.openapi.converter;

import com.maoatao.daedalus.core.context.DaedalusOperatorContext;

/**
 * Synapse 上下文转换者
 *
 * @author MaoAtao
 * @date 2023-03-26 12:44:49
 */
public interface ContextConverter {

    /**
     * 类型转换
     *
     * @param token 令牌
     * @return 操作者上下文
     */
    DaedalusOperatorContext convert(String token);
}
