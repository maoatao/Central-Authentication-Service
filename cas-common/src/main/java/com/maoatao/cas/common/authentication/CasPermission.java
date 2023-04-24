package com.maoatao.cas.common.authentication;

import com.maoatao.synapse.core.bean.base.BaseBean;
import java.io.Serial;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * CAS 权限
 *
 * @author MaoAtao
 * @date 2023-03-24 22:58:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CasPermission extends BaseBean {
    @Serial
    private static final long serialVersionUID = 2391323743263950690L;
    /**
     * 权限名
     */
    private List<String> permissions;
    /**
     * 客户端 ID
     */
    private String clientId;
}
