package com.maoatao.cas.core.bean.param.clientscope;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
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
@Schema(description = "查询CAS 客户端作用域参数")
public class ClientScopeQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = 8873251698937607491L;
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

    @Tolerate
    public ClientScopeQueryParam() {}
}
