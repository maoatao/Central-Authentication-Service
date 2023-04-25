package com.maoatao.cas.core.bean.param.clientuser;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端用户
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "查询CAS 客户端用户参数")
public class ClientUserQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = -5359208169352138387L;
    /**
     * 用户 id
     */
    @NotNull(message = "userId 不能为空")
    @Schema(description = "用户 id")
    private Long userId;
    /**
     * 客户端 id
     */
    @NotNull(message = "clientId 不能为空")
    @Schema(description = "客户端 id")
    private String clientId;
    /**
     * 登录名
     */
    @NotNull(message = "loginName 不能为空")
    @Schema(description = "登录名")
    private String loginName;

    @Tolerate
    public ClientUserQueryParam() {}
}
