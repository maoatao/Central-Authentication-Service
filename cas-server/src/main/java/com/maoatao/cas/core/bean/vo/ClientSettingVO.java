package com.maoatao.cas.core.bean.vo;

import com.maoatao.synapse.core.bean.base.BaseVO;
import com.maoatao.synapse.lang.util.SynaDates;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * CAS 客户端设置
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "CAS 客户端设置数据")
public class ClientSettingVO extends BaseVO {

    @Serial
    private static final long serialVersionUID = 8638230331442376843L;

    /**
     * 主键id(自增)
     */
    @Schema(description = "主键id(自增)")
    private Long id;
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
    /**
     * 创建人 ID
     */
    @Schema(description = "创建人 ID")
    private String createdById;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = SynaDates.DATE_TIME_FORMAT, timezone = SynaDates.CN_TIME_ZONE)
    @Schema(description = "创建时间")
    private LocalDateTime createdDate;
    /**
     * 更新人 ID
     */
    @Schema(description = "更新人 ID")
    private String updatedById;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = SynaDates.DATE_TIME_FORMAT, timezone = SynaDates.CN_TIME_ZONE)
    @Schema(description = "更新时间")
    private LocalDateTime updatedDate;

    @Tolerate
    public ClientSettingVO() {}
}
