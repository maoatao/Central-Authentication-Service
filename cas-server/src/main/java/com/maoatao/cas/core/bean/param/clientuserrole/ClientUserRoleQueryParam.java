package com.maoatao.cas.core.bean.param.clientuserrole;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端用户角色关系
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "查询CAS 客户端用户角色关系参数")
public class ClientUserRoleQueryParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = 8634930874534587933L;

    /**
     * 客户端用户id
     */
    @NotNull(message = "clientUserId 不能为空")
    @Schema(description = "客户端用户id")
    private Long clientUserId;
    /**
     * 用户id
     */
    @NotNull(message = "roleId 不能为空")
    @Schema(description = "用户id")
    private Long roleId;

    @Tolerate
    public ClientUserRoleQueryParam() {}
}
