package com.maoatao.cas.core.bean.param.approvalsscope;

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
 * CAS 批准作用域
 *
 * @author MaoAtao
 * @date 2023-05-06 09:53:12
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "查询CAS 批准作用域参数")
public class ApprovalsScopeQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = -66799066632471237L;

    /**
     * 审批id
     */
    @NotNull(message = "approvalsId 不能为空")
    @Schema(description = "审批id")
    private Long approvalsId;
    /**
     * 作用域
     */
    @NotNull(message = "scope 不能为空")
    @Schema(description = "作用域")
    private String scope;

    @Tolerate
    public ApprovalsScopeQueryParam() {}
}
