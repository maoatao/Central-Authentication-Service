package com.maoatao.cas.core.bean.param.clientredirecturl;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "查询CAS 客户端重定向地址参数")
public class ClientRedirectUrlQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = -5046364043072690372L;
    /**
     * 客户端 id
     */
    @Schema(description = "客户端 id")
    private String clientId;
    /**
     * 重定向地址
     */
    @Schema(description = "重定向地址")
    private String value;

    @Tolerate
    public ClientRedirectUrlQueryParam() {}
}
