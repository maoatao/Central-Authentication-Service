package com.maoatao.cas.core.bean.param.role;

import com.maoatao.synapse.core.bean.base.BaseUpdateParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 角色
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "新增CAS 角色参数")
public class RoleUpdateParam extends BaseUpdateParam {

    @Serial
    private static final long serialVersionUID = -427655622185138390L;

    /**
     * 角色名
     */
    @NotNull(message = "name 不能为空")
    @Schema(description = "角色名")
    private String name;
    /**
     * 描述
     */
    @NotNull(message = "description 不能为空")
    @Schema(description = "描述")
    private String description;

    @Tolerate
    public RoleUpdateParam() {}
}
