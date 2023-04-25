package com.maoatao.cas.core.bean.param.clientuser;

import com.maoatao.synapse.core.bean.base.BaseUpdateParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
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
@Schema(description = "新增CAS 客户端用户参数")
public class ClientUserUpdateParam extends BaseUpdateParam {

    @Serial
    private static final long serialVersionUID = -2003618959733752300L;

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
    /**
     * 角色名称集合
     */
    @Schema(description = "角色名称集合")
    Set<String> roles;

    @Tolerate
    public ClientUserUpdateParam() {}
}
