package com.maoatao.cas.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.base.BaseEntity;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * CAS 客户端
 *
 * @author MaoAtao
 * @date 2023-04-07 21:42:38
 */
@Data
@Builder
@TableName("t_cas_client")
@EqualsAndHashCode(callSuper = true)
public class ClientEntity extends BaseEntity<ClientEntity> {

    @Serial
    private static final long serialVersionUID = 847076044229687377L;

    /**
     * 客户端 id
     */
    private String clientId;
    /**
     * 客户端 id 颁发时间
     */
    private LocalDateTime clientIdIssuedAt;
    /**
     * 客户端密码
     */
    private String secret;
    /**
     * 客户端密码过期时间
     */
    private LocalDateTime secretExpiresAt;
    /**
     * 客户端名称
     */
    private String name;
    /**
     * 别名(全局唯一)
     */
    private String alias;
    /**
     * 描述
     */
    private String description;

    @Tolerate
    public ClientEntity() {}
}
