package com.maoatao.cas.core.bean.param.permission;

import com.maoatao.synapse.core.bean.base.BaseUpdateParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 权限
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "新增CAS 权限参数")
public class PermissionUpdateParam extends BaseUpdateParam {

    @Serial
    private static final long serialVersionUID = 3427584364823405L;

    /**
     * 权限名
     */
    @NotNull(message = "name 不能为空")
    @Schema(description = "权限名")
    private String name;
    /**
     * 描述
     */
    @NotNull(message = "description 不能为空")
    @Schema(description = "描述")
    private String description;

    @Tolerate
    public PermissionUpdateParam() {}
}
