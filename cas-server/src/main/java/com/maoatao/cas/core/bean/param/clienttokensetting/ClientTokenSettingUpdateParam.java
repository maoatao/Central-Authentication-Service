package com.maoatao.cas.core.bean.param.clienttokensetting;

import com.maoatao.synapse.core.bean.base.BaseUpdateParam;
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
 * CAS 客户端令牌设置
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "新增CAS 客户端令牌设置参数")
public class ClientTokenSettingUpdateParam extends BaseUpdateParam {

    @Serial
    private static final long serialVersionUID = -6099885247645887158L;

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
     * 授权码持续时间(单位秒,默认 300 秒)
     */
    @NotNull(message = "authorizationCodeDuration 不能为空")
    @Schema(description = "授权码持续时间(单位秒,默认 300 秒)")
    private Integer authorizationCodeDuration;
    /**
     * 是否启用单一授权(签发新令牌后自动吊销旧令牌);false:禁用,true:启用
     */
    @NotNull(message = "singleAccessToken 不能为空")
    @Schema(description = "是否启用单一授权(签发新令牌后自动吊销旧令牌);false:禁用,true:启用")
    private Boolean singleAccessToken;
    /**
     * 访问令牌持续时间(单位秒,默认 1800 秒)
     */
    @NotNull(message = "accessTokenDuration 不能为空")
    @Schema(description = "访问令牌持续时间(单位秒,默认 1800 秒)")
    private Integer accessTokenDuration;
    /**
     * 访问令牌格式
     */
    @NotNull(message = "accessTokenFormat 不能为空")
    @Schema(description = "访问令牌格式")
    private String accessTokenFormat;
    /**
     * 令牌值格式
     */
    @NotNull(message = "tokenValueFormat 不能为空")
    @Schema(description = "令牌值格式")
    private String tokenValueFormat;
    /**
     * 是否启用重复使用刷新令牌;false:禁用,true:启用
     */
    @NotNull(message = "reuseRefreshToken 不能为空")
    @Schema(description = "是否启用重复使用刷新令牌;false:禁用,true:启用")
    private Boolean reuseRefreshToken;
    /**
     * 刷新令牌持续时间(单位秒,默认 3600 秒)
     */
    @NotNull(message = "refreshTokenDuration 不能为空")
    @Schema(description = "刷新令牌持续时间(单位秒,默认 3600 秒)")
    private Integer refreshTokenDuration;
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
    public ClientTokenSettingUpdateParam() {}
}
