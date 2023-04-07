package com.maoatao.cas.core.bean.param.clientsetting;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
import com.maoatao.synapse.lang.util.SynaDates;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

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
     * 主键id(自增)
     */
    @NotNull(message = "id 不能为空")
    @Schema(description = "主键id(自增)")
    private Long id;
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
    /**
     * 创建人 ID
     */
    @NotNull(message = "createdById 不能为空")
    @Schema(description = "创建人 ID")
    private String createdById;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = SynaDates.DATE_TIME_FORMAT)
    @NotNull(message = "createdDate 不能为空")
    @Schema(description = "创建时间")
    private LocalDateTime createdDate;
    /**
     * 更新人 ID
     */
    @NotNull(message = "updatedById 不能为空")
    @Schema(description = "更新人 ID")
    private String updatedById;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = SynaDates.DATE_TIME_FORMAT)
    @NotNull(message = "updatedDate 不能为空")
    @Schema(description = "更新时间")
    private LocalDateTime updatedDate;
    /**
     * 是否删除;false:未删除,true:删除
     */
    @NotNull(message = "deleted 不能为空")
    @Schema(description = "是否删除;false:未删除,true:删除")
    private Boolean deleted;

    @Tolerate
    public ClientSettingQueryParam() {}
}
