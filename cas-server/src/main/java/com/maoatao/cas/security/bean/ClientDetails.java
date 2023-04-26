package com.maoatao.cas.security.bean;

import com.maoatao.cas.common.constant.CasSeparator;
import com.maoatao.synapse.lang.util.SynaSafes;
import com.maoatao.synapse.lang.util.SynaStrings;
import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

/**
 * 客户端作用域
 * <p>
 * 根据作用域获取权限使用
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
    private Set<String> scopes;

    public Map<String, Set<String>> getClientScopes() {
        if (scopes == null) {
            return Map.of();
        }
        return scopes.stream().map(o -> o.split(CasSeparator.SCOPE_REGEX))
                .collect(Collectors.groupingBy(o -> SynaSafes.of(o[0]), Collectors.mapping(o -> o.length == 2 ? SynaSafes.of(o[1]) : SynaStrings.EMPTY, Collectors.toSet())));
    }
}
