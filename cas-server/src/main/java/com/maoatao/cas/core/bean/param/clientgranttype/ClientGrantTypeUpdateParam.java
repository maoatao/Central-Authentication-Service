package com.maoatao.cas.core.bean.param.clientgranttype;

import com.maoatao.synapse.core.bean.base.BaseUpdateParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端授权类型
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "新增CAS 客户端授权类型参数")
public class ClientGrantTypeUpdateParam extends BaseUpdateParam {

    @Serial
    private static final long serialVersionUID = -4271749279159016720L;
    /**
     * 授权类型
     */
    @NotNull(message = "value 不能为空")
    @Schema(description = "授权类型")
    private String value;

    @Tolerate
    public ClientGrantTypeUpdateParam() {}
}
