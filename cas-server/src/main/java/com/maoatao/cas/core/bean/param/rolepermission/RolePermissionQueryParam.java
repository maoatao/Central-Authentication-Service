package com.maoatao.cas.core.bean.param.rolepermission;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
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
@Schema(description = "查询CAS 角色权限关系参数")
public class RolePermissionQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = -1624130358408510067L;

    /**
     * 用户id
     */
    @NotNull(message = "roleId 不能为空")
    @Schema(description = "用户id")
    private Long roleId;
    /**
     * 权限id
     */
    @NotNull(message = "permissionId 不能为空")
    @Schema(description = "权限id")
    private Long permissionId;

    @Tolerate
    public RolePermissionQueryParam() {}
}
