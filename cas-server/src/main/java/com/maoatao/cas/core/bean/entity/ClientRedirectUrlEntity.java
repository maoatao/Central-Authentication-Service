package com.maoatao.cas.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.base.BaseEntity;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端重定向地址
 *
 * @author MaoAtao
 * @date 2023-04-07 21:42:38
 */
@Data
@Builder
@TableName("t_cas_client_redirect_url")
@EqualsAndHashCode(callSuper = true)
public class ClientRedirectUrlEntity extends BaseEntity<ClientRedirectUrlEntity> {

    @Serial
    private static final long serialVersionUID = 3096947127023223207L;

    /**
     * 客户端 id
     */
    private String clientId;
    /**
     * 重定向地址
     */
    private String value;

    @Tolerate
    public ClientRedirectUrlEntity() {}
}
