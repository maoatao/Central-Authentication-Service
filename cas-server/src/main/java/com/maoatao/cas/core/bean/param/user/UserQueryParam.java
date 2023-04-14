package com.maoatao.cas.core.bean.param.user;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
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
 * CAS 用户
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "查询CAS 用户参数")
public class UserQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = -5359208169352138387L;

    /**
     * 主键id(自增)
     */
    @NotNull(message = "id 不能为空")
    @Schema(description = "主键id(自增)")
    private Long id;
    /**
     * CAS 全局唯一id
     */
    @NotNull(message = "openId 不能为空")
    @Schema(description = "CAS 全局唯一id")
    private String openId;
    /**
     * 客户端 id
     */
    @NotNull(message = "clientId 不能为空")
    @Schema(description = "客户端 id")
    private String clientId;
    /**
     * 用户名
     */
    @NotNull(message = "name 不能为空")
    @Schema(description = "用户名")
    private String name;
    /**
     * 密码
     */
    @NotNull(message = "password 不能为空")
    @Schema(description = "密码")
    private String password;
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
    public UserQueryParam() {}
}
