package com.maoatao.cas.core.bean.param.client;

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
 * CAS 客户端
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "新增CAS 客户端参数")
public class ClientUpdateParam extends BaseUpdateParam {

    @Serial
    private static final long serialVersionUID = 6593034541839524780L;

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
     * 客户端 id 颁发时间
     */
    @DateTimeFormat(pattern = SynaDates.DATE_TIME_FORMAT)
    @NotNull(message = "clientIdIssuedAt 不能为空")
    @Schema(description = "客户端 id 颁发时间")
    private LocalDateTime clientIdIssuedAt;
    /**
     * 客户端密码
     */
    @NotNull(message = "secret 不能为空")
    @Schema(description = "客户端密码")
    private String secret;
    /**
     * 客户端密码过期时间
     */
    @DateTimeFormat(pattern = SynaDates.DATE_TIME_FORMAT)
    @NotNull(message = "secretExpiresAt 不能为空")
    @Schema(description = "客户端密码过期时间")
    private LocalDateTime secretExpiresAt;
    /**
     * 客户端名称
     */
    @NotNull(message = "name 不能为空")
    @Schema(description = "客户端名称")
    private String name;
    /**
     * 别名(全局唯一)
     */
    @NotNull(message = "alias 不能为空")
    @Schema(description = "别名(全局唯一)")
    private String alias;
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
    public ClientUpdateParam() {}
}
