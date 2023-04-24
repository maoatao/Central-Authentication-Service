package com.maoatao.cas.security.bean;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

/**
 * 客户端作用域
 *
 * @author MaoAtao
 * @date 2023-03-05 01:07:06
 */
@Data
@Builder
public class ClientDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = -721695029432930229L;

    /**
     * 主客户端id (请求令牌时的客户端)
     */
    private String clientId;

    /**
     * 客户端作用域 (包括主客户端以及其他客户端的作用域)
     */
    private Map<String, Set<String>> scopes;
}
