package com.maoatao.cas.core.bean.param.client;

import com.maoatao.synapse.core.bean.base.BaseUpdateParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "新增CAS 客户端参数")
public class ClientUpdateParam extends BaseUpdateParam {

    @Serial
    private static final long serialVersionUID = 6593034541839524780L;
    /**
     * 客户端名称
     */
    @NotNull(message = "name 不能为空")
    @Schema(description = "客户端名称")
    private String name;
    /**
     * 别名(全局唯一)
     */
    @NotNull(message = "alias 不能为空")
    @Schema(description = "别名(全局唯一)")
    private String alias;
    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    @Tolerate
    public ClientUpdateParam() {}
}
