package com.maoatao.cas.core.param;

import com.maoatao.daedalus.web.bean.param.BasePaginationParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;
import java.util.List;

/**
 * 用户
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户")
public class UserParam extends BasePaginationParam {

    @Serial
    private static final long serialVersionUID = 4486807411370303523L;
    /**
     * 主键id(自增)
     */
    @Schema(description = "主键id(自增)")
    private Long id;
    /**
     * CAS 全局唯一id
     */
    @Schema(description = "CAS 全局唯一id")
    private String openId;
    /**
     * 客户端 id
     */
    @NotNull(message = "注册客户端id不能为空")
    @Schema(description = "客户端 id")
    private String clientId;
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String name;
    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;
    /**
     * 是否启用;0:禁用,1:启用
     */
    @Schema(description = "false:禁用,true:启用")
    private Boolean enabled;

    /**
     * 角色名称集合
     */
    @Schema(description = "角色名称集合")
    List<String> roles;

    public List<String> getRoles() {
        // 去重
        return roles.stream().distinct().toList();
    }

    @Tolerate
    public UserParam() {}
}
