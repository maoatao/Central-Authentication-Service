package com.maoatao.cas.core.bean.param.clientscopepermission;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 角色权限关系
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "查询CAS 角色权限关系参数")
public class ClientScopePermissionQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = 1523875176365284364L;

    /**
     * 作用域id
     */
    @Schema(description = "作用域id")
    private Long scopeId;
    /**
     * 权限id
     */
    @Schema(description = "权限id")
    private Long permissionId;

    @Tolerate
    public ClientScopePermissionQueryParam() {}
}
