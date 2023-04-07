package com.maoatao.cas.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.base.BaseEntity;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 角色
 *
 * @author MaoAtao
 * @date 2023-04-07 21:42:38
 */
@Data
@Builder
@TableName("t_cas_role")
@EqualsAndHashCode(callSuper = true)
public class RoleEntity extends BaseEntity<RoleEntity> {

    @Serial
    private static final long serialVersionUID = -3594168978855315116L;

    /**
     * OAuth2 客户端id
     */
    private String clientId;
    /**
     * 角色名
     */
    private String name;
    /**
     * 描述
     */
    private String description;

    @Tolerate
    public RoleEntity() {}
}
