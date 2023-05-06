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
 * CAS 授权批准
 *
 * @author MaoAtao
 * @date 2023-05-06 09:53:13
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "CAS 授权批准数据")
public class ApprovalsVO extends BaseVO {

    @Serial
    private static final long serialVersionUID = -8350113131477700892L;

    /**
     * 主键id(自增)
     */
    @Schema(description = "主键id(自增)")
    private Long id;
    /**
     * 客户端ID
     */
    @Schema(description = "客户端ID")
    private String clientId;
    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long userId;
    /**
     * 到期时间
     */
    @JsonFormat(pattern = SynaDates.DATE_TIME_FORMAT, timezone = SynaDates.CN_TIME_ZONE)
    @Schema(description = "到期时间")
    private LocalDateTime expiresDate;
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
    public ApprovalsVO() {}
}
