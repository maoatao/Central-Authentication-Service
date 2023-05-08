package com.maoatao.cas.core.bean.param.approvals;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
import com.maoatao.synapse.lang.util.SynaDates;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "查询CAS 授权批准参数")
public class ApprovalsQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = 8886345219062160951L;

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
    @DateTimeFormat(pattern = SynaDates.DATE_TIME_FORMAT)
    @Schema(description = "到期时间")
    private LocalDateTime expiresDate;

    @Tolerate
    public ApprovalsQueryParam() {}
}
