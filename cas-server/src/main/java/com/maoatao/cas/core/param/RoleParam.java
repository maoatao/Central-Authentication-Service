package com.maoatao.cas.core.param;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 角色
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色")
public class RoleParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = 1233219274545424503L;
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
     * 角色名
     */
    @NotNull(message = "角色名不能为空")
    @Schema(description = "角色名")
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
