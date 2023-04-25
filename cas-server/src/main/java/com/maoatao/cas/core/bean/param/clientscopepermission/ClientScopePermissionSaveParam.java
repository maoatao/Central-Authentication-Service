package com.maoatao.cas.core.bean.param.clientscopepermission;

import com.maoatao.synapse.core.bean.base.BaseSaveParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "新增CAS 角色权限关系参数")
public class ClientScopePermissionSaveParam extends BaseSaveParam {

    @Serial
    private static final long serialVersionUID = 4234672819857731044L;

    /**
     * 作用域id
     */
    @NotNull(message = "scopeId 不能为空")
    @Schema(description = "作用域id")
    private Long scopeId;
    /**
     * 权限id
     */
    @NotNull(message = "permissionId 不能为空")
    @Schema(description = "权限id")
    private Long permissionId;

    @Tolerate
    public ClientScopePermissionSaveParam() {}
}
