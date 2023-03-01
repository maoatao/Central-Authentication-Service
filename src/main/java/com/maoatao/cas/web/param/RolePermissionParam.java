package com.maoatao.cas.web.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 角色权限关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色权限关系")
public class RolePermissionParam extends PageParam {

    @Serial
    private static final long serialVersionUID = -7036910121720283031L;
    /**
     * 主键id(自增)
     */
    @Schema(description = "主键id(自增)")
    private Long id;
    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    @Schema(description = "用户id")
    private Long roleId;
    /**
     * 权限id
     */
    @NotNull(message = "权限id不能为空")
    @Schema(description = "权限id")
    private Long permissionId;
}
