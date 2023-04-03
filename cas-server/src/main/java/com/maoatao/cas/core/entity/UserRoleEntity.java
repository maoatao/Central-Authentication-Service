package com.maoatao.cas.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * 用户角色关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
@Data
@Builder
@TableName("t_cas_user_role")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户角色关系")
public class UserRoleEntity extends BaseEntity<UserRoleEntity> {

    @Serial
    private static final long serialVersionUID = -4722252259023056863L;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long userId;
    /**
     * 角色id
     */
    @Schema(description = "角色id")
    private Long roleId;

    @Tolerate
    public UserRoleEntity() {}
}
