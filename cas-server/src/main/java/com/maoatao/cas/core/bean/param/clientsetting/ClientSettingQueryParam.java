package com.maoatao.cas.core.bean.param.clientsetting;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端设置
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "查询CAS 客户端设置参数")
public class ClientSettingQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = -3262588912351943382L;
    /**
     * 客户端 id
     */
    @Schema(description = "客户端 id")
    private String clientId;
    /**
     * 是否启用验证授权码参数(PKCE);false:禁用,true:启用
     */
    @Schema(description = "是否启用验证授权码参数(PKCE);false:禁用,true:启用")
    private Boolean requireProofKey;
    /**
     * 是否启用授权同意;false:禁用,true:启用
     */
    @Schema(description = "是否启用授权同意;false:禁用,true:启用")
    private Boolean requireAuthorizationConsent;
    /**
     * JWK 秘钥集 URL
     */
    @Schema(description = "JWK 秘钥集 URL")
    private String jwkSetUrl;
    /**
     * 签名算法
     */
    @Schema(description = "签名算法")
    private String signingAlgorithm;

    @Tolerate
    public ClientSettingQueryParam() {}
}
