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
 * 权限
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Data
@Builder
@TableName("t_cas_permission")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "权限")
public class PermissionEntity extends BaseEntity<PermissionEntity> {

    @Serial
    private static final long serialVersionUID = 8835416927286873660L;

    /**
     * OAuth2 客户端id
     */
    @Schema(description = "OAuth2 客户端id")
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

    @Tolerate
    public PermissionEntity() {}
}
