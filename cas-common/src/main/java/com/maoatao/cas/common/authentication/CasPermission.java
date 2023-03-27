package com.maoatao.cas.common.authentication;

import com.maoatao.synapse.core.bean.base.BaseBean;
import java.io.Serial;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * CAS 权限
 *
 * @author MaoAtao
 * @date 2023-03-24 22:58:40
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
public final class CasPermission extends BaseBean {
    @Serial
    private static final long serialVersionUID = 2391323743263950690L;
    /**
     * 权限名
     */
    private final String permission;
    /**
     * 客户端 ID
     */
    private final String clientId;
}
