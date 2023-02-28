package com.maoatao.cas.core.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 用户表
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
@Data
@TableName("t_uac_user")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户表")
public class UserEntity extends Model<UserEntity> {

    @Serial
    private static final long serialVersionUID = -5231977144633077127L;

    /**
     * 用户id
     */
    @TableId
    @Schema(description = "用户id")
    private String id;
    /**
     * 开放平台id
     */
    @Schema(description = "开放平台id")
    private String openId;
    /**
     * 用户名(唯一)
     */
    @Schema(description = "用户名(唯一)")
    private String name;
    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;
    /**
     * 0:不可用,1:可用
     */
    @Schema(description = "false:不可用,true:可用")
    private boolean enabled;

}
