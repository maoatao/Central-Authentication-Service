package com.maoatao.cas.core.bean.param.clientscope;

import com.maoatao.synapse.core.bean.base.BaseUpdateParam;
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
 * CAS 客户端作用域
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "新增CAS 客户端作用域参数")
public class ClientScopeUpdateParam extends BaseUpdateParam {

    @Serial
    private static final long serialVersionUID = -2982913238351357392L;

    /**
     * 主键id(自增)
     */
    @NotNull(message = "id 不能为空")
    @Schema(description = "主键id(自增)")
    private Long id;
    /**
     * 客户端 id
     */
    @NotNull(message = "clientId 不能为空")
    @Schema(description = "客户端 id")
    private String clientId;
    /**
     * 作用域
     */
    @NotNull(message = "name 不能为空")
    @Schema(description = "作用域")
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
    public ClientScopeUpdateParam() {}
}
