package com.maoatao.cas.core.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 用户角色关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
@Data
@TableName("t_cas_user_role")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户角色关系")
public class UserRoleEntity extends Model<UserRoleEntity> {

    @Serial
    private static final long serialVersionUID = -4722252259023056863L;

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
    private Long userId;
    /**
     * 角色id
     */
    @Schema(description = "角色id")
    private Long roleId;
}
