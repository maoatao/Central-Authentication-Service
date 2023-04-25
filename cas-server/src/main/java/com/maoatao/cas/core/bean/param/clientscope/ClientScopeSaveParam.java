package com.maoatao.cas.core.bean.param.clientscope;

import com.maoatao.synapse.core.bean.base.BaseSaveParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端作用域
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "新增CAS 客户端作用域参数")
public class ClientScopeSaveParam extends BaseSaveParam {

    @Serial
    private static final long serialVersionUID = 4273091310257018023L;

    /**
     * 客户端 id
     */
    @NotNull(message = "clientId 不能为空")
    @Schema(description = "客户端 id")
    private String clientId;
    /**
     * 作用域
     */
    @NotNull(message = "name 不能为空")
    @Schema(description = "作用域")
    private String name;
    /**
     * 描述
     */
    @NotNull(message = "description 不能为空")
    @Schema(description = "描述")
    private String description;

    @Tolerate
    public ClientScopeSaveParam() {}
}
