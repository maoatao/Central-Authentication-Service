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
 * 用户表
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
@Data
@Builder
@TableName("t_cas_user")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户表")
public class UserEntity extends BaseEntity<UserEntity> {

    @Serial
    private static final long serialVersionUID = -5231977144633077127L;

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

    @Tolerate
    public UserEntity() {}
}
