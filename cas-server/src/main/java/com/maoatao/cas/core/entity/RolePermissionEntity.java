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
 * 角色权限关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Data
@Builder
@TableName("t_cas_role_permission")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色权限关系")
public class RolePermissionEntity extends BaseEntity<RolePermissionEntity> {

    @Serial
    private static final long serialVersionUID = 1296674573072507831L;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long roleId;
    /**
     * 权限id
     */
    @Schema(description = "权限id")
    private Long permissionId;

    @Tolerate
    public RolePermissionEntity() {}
}
