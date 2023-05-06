package com.maoatao.cas.core.bean.param.client;

import com.maoatao.cas.core.bean.param.clientsetting.ClientSettingSaveParam;
import com.maoatao.cas.core.bean.param.clienttokensetting.ClientTokenSettingSaveParam;
import com.maoatao.synapse.core.bean.base.BaseSaveParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 客户端
 *
 * @author MaoAtao
 * @date 2023-04-07 21:23:37
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "新增CAS 客户端参数")
public class ClientSaveParam extends BaseSaveParam {

    @Serial
    private static final long serialVersionUID = 994477286842508955L;
    /**
     * 客户端密码
     */
    @NotNull(message = "secret 不能为空")
    @Schema(description = "客户端密码")
    private String secret;
    /**
     * 客户端名称
     */
    @NotNull(message = "name 不能为空")
    @Schema(description = "客户端名称")
    private String name;
    /**
     * 别名(全局唯一)
     */
    @NotNull(message = "alias 不能为空")
    @Schema(description = "别名(全局唯一)")
    private String alias;
    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;
    /**
     * 验证方法
     */
    @NotEmpty(message = "authenticationMethods 不能为空")
    @Schema(description = "验证方法")
    private Set<String> authenticationMethods;
    /**
     * 授权类型
     */
    @NotEmpty(message = "grantTypes 不能为空")
    @Schema(description = "授权类型")
    private Set<String> grantTypes;
    /**
     * 重定向地址
     */
    @NotEmpty(message = "redirectUrls 不能为空")
    @Schema(description = "重定向地址")
    private Set<String> redirectUrls;
    /**
     * 作用域
     */
    @NotEmpty(message = "scopes 不能为空")
    @Schema(description = "作用域")
    private Set<String> scopes;
    /**
     * 配置
     */
    @NotNull(message = "setting 不能为空")
    @Schema(description = "配置")
    private ClientSettingSaveParam setting;
    /**
     * 令牌配置
     */
    @NotNull(message = "tokenSetting 不能为空")
    @Schema(description = "令牌配置")
    private ClientTokenSettingSaveParam tokenSetting;

    @Tolerate
    public ClientSaveParam() {}
}
