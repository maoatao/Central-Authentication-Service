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
 * CAS 批准作用域
 *
 * @author MaoAtao
 * @date 2023-05-06 09:53:12
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "CAS 批准作用域数据")
public class ApprovalsScopeVO extends BaseVO {

    @Serial
    private static final long serialVersionUID = -6349019238637253430L;

    /**
     * 主键id(自增)
     */
    @Schema(description = "主键id(自增)")
    private Long id;
    /**
     * 审批id
     */
    @Schema(description = "审批id")
    private Long approvalsId;
    /**
     * 作用域
     */
    @Schema(description = "作用域")
    private String scope;
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
    public ApprovalsScopeVO() {}
}
