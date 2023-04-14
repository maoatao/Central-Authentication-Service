package com.maoatao.cas.core.param;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 权限
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "权限")
public class PermissionParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = -4876422828478855700L;
    /**
     * 主键id(自增)
     */
    @Schema(description = "主键id(自增)")
    private Long id;
    /**
     * 客户端 id
     */
    @NotNull(message = "注册客户端id不能为空")
    @Schema(description = "客户端 id")
    private String clientId;
    /**
     * 权限名
     */
    @NotNull(message = "权限名不能为空")
    @Schema(description = "权限名")
    private String name;
    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;
    /**
     * 0:不可用,1:可用
     */
    @Schema(description = "0:不可用,1:可用")
    private Boolean enabled;

}
