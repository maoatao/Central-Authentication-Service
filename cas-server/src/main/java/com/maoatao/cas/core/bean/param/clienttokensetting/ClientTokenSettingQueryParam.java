package com.maoatao.cas.core.bean.param.clienttokensetting;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端令牌设置
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "查询CAS 客户端令牌设置参数")
public class ClientTokenSettingQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = 5815957845500226135L;
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

    @Tolerate
    public ClientTokenSettingQueryParam() {}
}
