package com.maoatao.cas.core.bean.param.clientsetting;

import com.maoatao.synapse.core.bean.base.BaseSaveParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "新增CAS 客户端设置参数")
public class ClientSettingSaveParam extends BaseSaveParam {

    @Serial
    private static final long serialVersionUID = 5901057559407335155L;

    /**
     * 客户端 id
     */
    @NotNull(message = "clientId 不能为空")
    @Schema(description = "客户端 id")
    private String clientId;
    /**
     * 是否启用验证授权码参数(PKCE);false:禁用,true:启用
     */
    @NotNull(message = "requireProofKey 不能为空")
    @Schema(description = "是否启用验证授权码参数(PKCE);false:禁用,true:启用")
    private Boolean requireProofKey;
    /**
     * 是否启用授权同意;false:禁用,true:启用
     */
    @NotNull(message = "requireAuthorizationConsent 不能为空")
    @Schema(description = "是否启用授权同意;false:禁用,true:启用")
    private Boolean requireAuthorizationConsent;
    /**
     * JWK 秘钥集 URL
     */
    @NotNull(message = "jwkSetUrl 不能为空")
    @Schema(description = "JWK 秘钥集 URL")
    private String jwkSetUrl;
    /**
     * 签名算法
     */
    @NotNull(message = "signingAlgorithm 不能为空")
    @Schema(description = "签名算法")
    private String signingAlgorithm;

    @Tolerate
    public ClientSettingSaveParam() {}
}
