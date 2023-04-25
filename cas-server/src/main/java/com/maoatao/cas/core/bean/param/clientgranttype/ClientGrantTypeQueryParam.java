package com.maoatao.cas.core.bean.param.clientgranttype;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
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
@Schema(description = "查询CAS 客户端授权类型参数")
public class ClientGrantTypeQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = 2141161174967906403L;
    /**
     * 客户端 id
     */
    @NotNull(message = "clientId 不能为空")
    @Schema(description = "客户端 id")
    private String clientId;
    /**
     * 授权类型
     */
    @NotNull(message = "value 不能为空")
    @Schema(description = "授权类型")
    private String value;

    @Tolerate
    public ClientGrantTypeQueryParam() {}
}
