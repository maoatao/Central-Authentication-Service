package com.maoatao.cas.core.bean.param.rolepermission;

import com.maoatao.synapse.core.bean.base.BaseUpdateParam;
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
public class RolePermissionUpdateParam extends BaseUpdateParam {

    @Serial
    private static final long serialVersionUID = 3148076318505113848L;

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
    public RolePermissionUpdateParam() {}
}
