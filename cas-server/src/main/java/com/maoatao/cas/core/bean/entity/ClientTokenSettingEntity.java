package com.maoatao.cas.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.base.BaseEntity;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端令牌设置
 *
 * @author MaoAtao
 * @date 2023-04-07 21:42:38
 */
@Data
@Builder
@TableName("t_cas_client_token_setting")
@EqualsAndHashCode(callSuper = true)
public class ClientTokenSettingEntity extends BaseEntity<ClientTokenSettingEntity> {

    @Serial
    private static final long serialVersionUID = 539749673626874718L;

    /**
     * 客户端 id
     */
    private String clientId;
    /**
     * 授权码持续时间(单位秒,默认 300 秒)
     */
    private Long authorizationCodeDuration;
    /**
     * 是否启用单一授权(签发新令牌后自动吊销旧令牌);false:禁用,true:启用
     */
    private Boolean singleAccessToken;
    /**
     * 访问令牌持续时间(单位秒,默认 1800 秒)
     */
    private Long accessTokenDuration;
    /**
     * 访问令牌格式
     */
    private String accessTokenFormat;
    /**
     * 令牌值格式
     */
    private String tokenValueFormat;
    /**
     * 是否启用重复使用刷新令牌;false:禁用,true:启用
     */
    private Boolean reuseRefreshToken;
    /**
     * 刷新令牌持续时间(单位秒,默认 3600 秒)
     */
    private Long refreshTokenDuration;
    /**
     * 签名算法
     */
    private String signingAlgorithm;

    @Tolerate
    public ClientTokenSettingEntity() {}
}
