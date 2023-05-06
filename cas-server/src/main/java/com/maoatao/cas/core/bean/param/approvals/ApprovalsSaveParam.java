package com.maoatao.cas.core.bean.param.approvals;

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
 * CAS 授权批准
 *
 * @author MaoAtao
 * @date 2023-05-06 09:53:13
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "新增CAS 授权批准参数")
public class ApprovalsSaveParam extends BaseSaveParam {

    @Serial
    private static final long serialVersionUID = 4233814818427341280L;

    /**
     * 客户端ID
     */
    @NotNull(message = "clientId 不能为空")
    @Schema(description = "客户端ID")
    private String clientId;
    /**
     * 用户id
     */
    @NotNull(message = "userId 不能为空")
    @Schema(description = "用户id")
    private Long userId;
    /**
     * 到期时间
     */
    @DateTimeFormat(pattern = SynaDates.DATE_TIME_FORMAT)
    @NotNull(message = "expiresDate 不能为空")
    @Schema(description = "到期时间")
    private LocalDateTime expiresDate;

    @Tolerate
    public ApprovalsSaveParam() {}
}
