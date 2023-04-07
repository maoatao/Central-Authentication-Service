package com.maoatao.cas.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.base.BaseEntity;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端作用域
 *
 * @author MaoAtao
 * @date 2023-04-07 21:42:38
 */
@Data
@Builder
@TableName("t_cas_client_scope")
@EqualsAndHashCode(callSuper = true)
public class ClientScopeEntity extends BaseEntity<ClientScopeEntity> {

    @Serial
    private static final long serialVersionUID = -1609640331155193266L;

    /**
     * 客户端 id
     */
    private String clientId;
    /**
     * 作用域
     */
    private String name;
    /**
     * 描述
     */
    private String description;

    @Tolerate
    public ClientScopeEntity() {}
}
