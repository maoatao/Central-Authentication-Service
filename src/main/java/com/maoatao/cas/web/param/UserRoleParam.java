package com.maoatao.cas.web.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 用户角色关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户角色关系")
public class UserRoleParam extends PageParam {

    @Serial
    private static final long serialVersionUID = -7883417552375889541L;

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
    private Long userId;
    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空")
    @Schema(description = "角色id")
    private Long roleId;
}
