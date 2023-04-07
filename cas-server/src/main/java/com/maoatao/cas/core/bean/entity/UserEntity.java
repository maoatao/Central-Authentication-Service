package com.maoatao.cas.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.base.BaseEntity;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 用户
 *
 * @author MaoAtao
 * @date 2023-04-07 21:42:38
 */
@Data
@Builder
@TableName("t_cas_user")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity<UserEntity> {

    @Serial
    private static final long serialVersionUID = -8037953378905637409L;

    /**
     * CAS 全局唯一id
     */
    private String openId;
    /**
     * OAuth2 客户端id
     */
    private String clientId;
    /**
     * 用户名
     */
    private String name;
    /**
     * 密码
     */
    private String password;

    @Tolerate
    public UserEntity() {}
}
