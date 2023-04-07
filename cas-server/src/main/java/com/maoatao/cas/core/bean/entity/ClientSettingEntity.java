package com.maoatao.cas.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.base.BaseEntity;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端设置
 *
 * @author MaoAtao
 * @date 2023-04-07 21:42:38
 */
@Data
@Builder
@TableName("t_cas_client_setting")
@EqualsAndHashCode(callSuper = true)
public class ClientSettingEntity extends BaseEntity<ClientSettingEntity> {

    @Serial
    private static final long serialVersionUID = -8088291941086986630L;

    /**
     * 客户端 id
     */
    private String clientId;
    /**
     * 是否启用验证授权码参数(PKCE);false:禁用,true:启用
     */
    private Boolean requireProofKey;
    /**
     * 是否启用授权同意;false:禁用,true:启用
     */
    private Boolean requireAuthorizationConsent;
    /**
     * JWK 秘钥集 URL
     */
    private String jwkSetUrl;
    /**
     * 签名算法
     */
    private String signingAlgorithm;

    @Tolerate
    public ClientSettingEntity() {}
}
