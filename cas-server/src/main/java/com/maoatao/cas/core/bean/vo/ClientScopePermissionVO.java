package com.maoatao.cas.core.bean.vo;

import com.maoatao.synapse.core.bean.base.BaseVO;
import com.maoatao.synapse.lang.util.SynaDates;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * CAS 角色权限关系
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "CAS 角色权限关系数据")
public class ClientScopePermissionVO extends BaseVO {

    @Serial
    private static final long serialVersionUID = -14615099036452065L;

    /**
     * 主键id(自增)
     */
    @Schema(description = "主键id(自增)")
    private Long id;
    /**
     * 作用域id
     */
    @Schema(description = "作用域id")
    private Long scopeId;
    /**
     * 权限id
     */
    @Schema(description = "权限id")
    private Long permissionId;
    /**
     * 创建人 ID
     */
    @Schema(description = "创建人 ID")
    private String createdById;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = SynaDates.DATE_TIME_FORMAT, timezone = SynaDates.CN_TIME_ZONE)
    @Schema(description = "创建时间")
    private LocalDateTime createdDate;
    /**
     * 更新人 ID
     */
    @Schema(description = "更新人 ID")
    private String updatedById;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = SynaDates.DATE_TIME_FORMAT, timezone = SynaDates.CN_TIME_ZONE)
    @Schema(description = "更新时间")
    private LocalDateTime updatedDate;

    @Tolerate
    public ClientScopePermissionVO() {}
}
