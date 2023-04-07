package com.maoatao.cas.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.base.BaseEntity;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端身份验证方法
 *
 * @author MaoAtao
 * @date 2023-04-07 21:42:38
 */
@Data
@Builder
@TableName("t_cas_client_authentication_method")
@EqualsAndHashCode(callSuper = true)
public class ClientAuthenticationMethodEntity extends BaseEntity<ClientAuthenticationMethodEntity> {

    @Serial
    private static final long serialVersionUID = 2116217269832830357L;

    /**
     * 客户端 id
     */
    private String clientId;
    /**
     * 客户端对应设定的值
     */
    private String value;

    @Tolerate
    public ClientAuthenticationMethodEntity() {}
}
