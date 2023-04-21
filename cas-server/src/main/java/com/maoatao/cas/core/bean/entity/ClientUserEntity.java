package com.maoatao.cas.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.base.BaseEntity;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端用户
 *
 * @author MaoAtao
 * @date 2023-04-07 21:42:38
 */
@Data
@Builder
@TableName("t_cas_client_user")
@EqualsAndHashCode(callSuper = true)
public class ClientUserEntity extends BaseEntity<ClientUserEntity> {

    @Serial
    private static final long serialVersionUID = -8037953378905637409L;

    /**
     * 用户 id
     */
    private Long userId;
    /**
     * 客户端 id
     */
    private String clientId;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 密码
     */
    private String password;

    @Tolerate
    public ClientUserEntity() {}
}
