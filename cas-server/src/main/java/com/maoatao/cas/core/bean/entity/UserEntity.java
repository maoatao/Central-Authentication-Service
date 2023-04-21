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
 * @date 2023-04-21 16:09:13
 */
@Data
@Builder
@TableName("t_cas_user")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity<UserEntity> {

    @Serial
    private static final long serialVersionUID = -1516869238431741705L;

    /**
     * CAS 全局唯一id
     */
    private String openId;
    /**
     * 用户名
     */
    private String name;
    /**
     * 描述
     */
    private String description;

    @Tolerate
    public UserEntity() {}
}
