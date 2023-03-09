package com.maoatao.cas.core.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import com.maoatao.cas.security.GrantType;

import java.io.Serial;
import java.io.Serializable;

/**
 * 生成访问令牌请求参数
 *
 * @author MaoAtao
 * @date 2023-03-04 20:56:22
 */
@Data
@Schema(description = "生成访问令牌请求参数")
public class GenerateAccessTokenParam implements Serializable {

    @Serial
    private static final long serialVersionUID = -7286826147280035652L;
    /**
     * 注册客户端密码
     */
    @NotNull(message = "注册客户端密码不能为空")
    @Schema(description = "注册客户端密码")
    private String secret;
    /**
     * 授权类型
     */
    @NotNull(message = "授权类型不能为空")
    @Schema(description = "授权类型")
    private String type;
    /**
     * PKCE协议额外参数:授权码验证值
     * <p>
     * {@link GrantType#AUTHORIZATION_CODE} 授权模式和 {@link ClientSettings#isRequireProofKey} 为 true 时必传且不能为空
     */
    @Schema(description = "PKCE协议额外参数:授权码验证值")
    private String verifier;
    /**
     * 授权码,刷新令牌
     * <p>
     * {@link GrantType#AUTHORIZATION_CODE} 和 {@link GrantType#REFRESH_TOKEN} 授权模式必传且不能为空
     */
    @Schema(description = "授权码,刷新令牌")
    private String code;
}
