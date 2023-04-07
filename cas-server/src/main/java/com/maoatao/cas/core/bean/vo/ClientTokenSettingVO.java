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
 * CAS 客户端令牌设置
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "CAS 客户端令牌设置数据")
public class ClientTokenSettingVO extends BaseVO {

    @Serial
    private static final long serialVersionUID = -1985308287022426925L;

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
     * 授权码持续时间(单位秒,默认 300 秒)
     */
    @Schema(description = "授权码持续时间(单位秒,默认 300 秒)")
    private Integer authorizationCodeDuration;
    /**
     * 是否启用单一授权(签发新令牌后自动吊销旧令牌);false:禁用,true:启用
     */
    @Schema(description = "是否启用单一授权(签发新令牌后自动吊销旧令牌);false:禁用,true:启用")
    private Boolean singleAccessToken;
    /**
     * 访问令牌持续时间(单位秒,默认 1800 秒)
     */
    @Schema(description = "访问令牌持续时间(单位秒,默认 1800 秒)")
    private Integer accessTokenDuration;
    /**
     * 访问令牌格式
     */
    @Schema(description = "访问令牌格式")
    private String accessTokenFormat;
    /**
     * 令牌值格式
     */
    @Schema(description = "令牌值格式")
    private String tokenValueFormat;
    /**
     * 是否启用重复使用刷新令牌;false:禁用,true:启用
     */
    @Schema(description = "是否启用重复使用刷新令牌;false:禁用,true:启用")
    private Boolean reuseRefreshToken;
    /**
     * 刷新令牌持续时间(单位秒,默认 3600 秒)
     */
    @Schema(description = "刷新令牌持续时间(单位秒,默认 3600 秒)")
    private Integer refreshTokenDuration;
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
    /**
     * 是否删除;false:未删除,true:删除
     */
    @Schema(description = "是否删除;false:未删除,true:删除")
    private Boolean deleted;

    @Tolerate
    public ClientTokenSettingVO() {}
}
