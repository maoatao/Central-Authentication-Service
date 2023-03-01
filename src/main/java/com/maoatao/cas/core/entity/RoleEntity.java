package com.maoatao.cas.core.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * 角色
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Data
@Builder
@TableName("t_cas_role")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色")
public class RoleEntity extends Model<RoleEntity> {

    @Serial
    private static final long serialVersionUID = 5019116569323676861L;

    /**
     * 主键id(自增)
     */
    @TableId
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "主键id(自增)")
    private Long id;
    /**
     * OAuth2 客户端id
     */
    @Schema(description = "OAuth2 客户端id")
    private String clientId;
    /**
     * 角色名
     */
    @Schema(description = "角色名")
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

    @Tolerate
    public RoleEntity() {}
}
