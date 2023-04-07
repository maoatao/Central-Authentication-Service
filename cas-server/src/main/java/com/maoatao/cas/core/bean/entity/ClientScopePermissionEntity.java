package com.maoatao.cas.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.base.BaseEntity;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 角色权限关系
 *
 * @author MaoAtao
 * @date 2023-04-07 21:42:38
 */
@Data
@Builder
@TableName("t_cas_client_scope_permission")
@EqualsAndHashCode(callSuper = true)
public class ClientScopePermissionEntity extends BaseEntity<ClientScopePermissionEntity> {

    @Serial
    private static final long serialVersionUID = 124897359665403831L;

    /**
     * 用户id
     */
    private Integer scopeId;
    /**
     * 权限id
     */
    private Long permissionId;

    @Tolerate
    public ClientScopePermissionEntity() {}
}
