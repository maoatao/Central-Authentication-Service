package com.maoatao.cas.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.base.BaseEntity;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 用户角色关系
 *
 * @author MaoAtao
 * @date 2023-04-07 21:42:38
 */
@Data
@Builder
@TableName("t_cas_user_role")
@EqualsAndHashCode(callSuper = true)
public class UserRoleEntity extends BaseEntity<UserRoleEntity> {

    @Serial
    private static final long serialVersionUID = -4773054698058658959L;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户id
     */
    private Long roleId;

    @Tolerate
    public UserRoleEntity() {}
}
