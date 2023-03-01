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
@TableName("t_cas_user")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户表")
public class UserEntity extends Model<UserEntity> {

    @Serial
    private static final long serialVersionUID = -5231977144633077127L;

    /**
     * 主键id(自增)
     */
    @TableId
    @Schema(description = "主键id(自增)")
    private Long id;
    /**
     * CAS 开放id(唯一)
     */
    @Schema(description = "CAS 开放id")
    private String openId;
    /**
     * OAuth2 客户端id
     */
    @Schema(description = "OAuth2 客户端id")
    private String clientId;
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String name;
    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;
    /**
     * 是否启用;0:禁用,1:启用
     */
    @Schema(description = "false:禁用,true:启用")
    private Boolean enabled;
}
