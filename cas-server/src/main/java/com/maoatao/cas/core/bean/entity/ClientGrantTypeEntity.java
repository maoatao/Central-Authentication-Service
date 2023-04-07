package com.maoatao.cas.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.base.BaseEntity;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端授权类型
 *
 * @author MaoAtao
 * @date 2023-04-07 21:42:38
 */
@Data
@Builder
@TableName("t_cas_client_grant_type")
@EqualsAndHashCode(callSuper = true)
public class ClientGrantTypeEntity extends BaseEntity<ClientGrantTypeEntity> {

    @Serial
    private static final long serialVersionUID = -1483891490224496997L;

    /**
     * 客户端 id
     */
    private String clientId;
    /**
     * 授权类型
     */
    private String value;

    @Tolerate
    public ClientGrantTypeEntity() {}
}
