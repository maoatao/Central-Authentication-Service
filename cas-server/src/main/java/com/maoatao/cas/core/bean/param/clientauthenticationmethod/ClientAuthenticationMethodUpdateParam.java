package com.maoatao.cas.core.bean.param.clientauthenticationmethod;

import com.maoatao.synapse.core.bean.base.BaseUpdateParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端身份验证方法
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "新增CAS 客户端身份验证方法参数")
public class ClientAuthenticationMethodUpdateParam extends BaseUpdateParam {

    @Serial
    private static final long serialVersionUID = -1053930962264531316L;
    /**
     * 身份验证方法
     */
    @NotNull(message = "value 不能为空")
    @Schema(description = "身份验证方法")
    private String value;

    @Tolerate
    public ClientAuthenticationMethodUpdateParam() {}
}
