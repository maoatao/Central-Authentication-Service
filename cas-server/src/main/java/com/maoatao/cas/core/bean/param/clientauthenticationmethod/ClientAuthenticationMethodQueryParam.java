package com.maoatao.cas.core.bean.param.clientauthenticationmethod;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "查询CAS 客户端身份验证方法参数")
public class ClientAuthenticationMethodQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = 4149037833902391965L;
    /**
     * 客户端 id
     */
    @Schema(description = "客户端 id")
    private String clientId;
    /**
     * 身份验证方法
     */
    @Schema(description = "身份验证方法")
    private String value;

    @Tolerate
    public ClientAuthenticationMethodQueryParam() {}
}
