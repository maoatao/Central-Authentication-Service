package com.maoatao.cas.core.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 权限
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Data
@TableName("t_uac_permission")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "权限")
public class PermissionEntity extends Model<PermissionEntity> {

    @Serial
    private static final long serialVersionUID = 8835416927286873660L;

    /**
     * 权限id
     */
    @TableId
    @Schema(description = "权限id")
    private String id;
    /**
     * 客户端id
     */
    @Schema(description = "客户端id")
    private String clientId;
    /**
     * 权限名
     */
    @Schema(description = "权限名")
    private String name;
    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;
    /**
     * 0:不可用,1:可用
     */
    @Schema(description = "0:不可用,1:可用")
    private Boolean enabled;

}
