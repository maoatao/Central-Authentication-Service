package com.maoatao.cas.common.authentication;

import com.maoatao.synapse.core.bean.base.BaseBean;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * CAS 角色
 *
 * @author MaoAtao
 * @date 2023-03-24 22:58:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CasRole extends BaseBean {
    @Serial
    private static final long serialVersionUID = -7450929003727099156L;
    /**
     * 角色名
     */
    private String role;
    /**
     * 客户端 ID
     */
    private String clientId;
}
