package com.maoatao.cas.core.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.IdEntity;
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
public class RolePermissionEntity extends IdEntity<RolePermissionEntity> {

    @Serial
    private static final long serialVersionUID = 1296674573072507831L;

    /**
     * 主键id(自增)
     */
    @TableId
    @Schema(description = "主键id(自增)")
    private Long id;
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
