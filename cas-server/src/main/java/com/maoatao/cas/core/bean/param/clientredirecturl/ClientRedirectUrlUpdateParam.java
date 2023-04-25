package com.maoatao.cas.core.bean.param.clientredirecturl;

import com.maoatao.synapse.core.bean.base.BaseUpdateParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端重定向地址
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "新增CAS 客户端重定向地址参数")
public class ClientRedirectUrlUpdateParam extends BaseUpdateParam {

    @Serial
    private static final long serialVersionUID = -5891990480265896988L;
    /**
     * 重定向地址
     */
    @NotNull(message = "value 不能为空")
    @Schema(description = "重定向地址")
    private String value;

    @Tolerate
    public ClientRedirectUrlUpdateParam() {}
}
