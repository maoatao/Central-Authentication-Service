package com.maoatao.cas.core.bean.param.permission;

import com.maoatao.synapse.core.bean.base.BaseSaveParam;
import com.maoatao.synapse.lang.util.SynaDates;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * CAS 权限
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "新增CAS 权限参数")
public class PermissionSaveParam extends BaseSaveParam {

    @Serial
    private static final long serialVersionUID = -7267509369397971146L;

    /**
     * OAuth2 客户端id
     */
    @NotNull(message = "clientId 不能为空")
    @Schema(description = "OAuth2 客户端id")
    private String clientId;
    /**
     * 权限名
     */
    @NotNull(message = "name 不能为空")
    @Schema(description = "权限名")
    private String name;
    /**
     * 描述
     */
    @NotNull(message = "description 不能为空")
    @Schema(description = "描述")
    private String description;
    /**
     * 创建人 ID
     */
    @NotNull(message = "createdById 不能为空")
    @Schema(description = "创建人 ID")
    private String createdById;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = SynaDates.DATE_TIME_FORMAT)
    @NotNull(message = "createdDate 不能为空")
    @Schema(description = "创建时间")
    private LocalDateTime createdDate;
    /**
     * 更新人 ID
     */
    @NotNull(message = "updatedById 不能为空")
    @Schema(description = "更新人 ID")
    private String updatedById;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = SynaDates.DATE_TIME_FORMAT)
    @NotNull(message = "updatedDate 不能为空")
    @Schema(description = "更新时间")
    private LocalDateTime updatedDate;
    /**
     * 是否删除;false:未删除,true:删除
     */
    @NotNull(message = "deleted 不能为空")
    @Schema(description = "是否删除;false:未删除,true:删除")
    private Boolean deleted;

    @Tolerate
    public PermissionSaveParam() {}
}
