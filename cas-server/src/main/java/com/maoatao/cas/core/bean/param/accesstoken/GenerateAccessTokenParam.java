package com.maoatao.cas.core.bean.param.accesstoken;

import com.maoatao.synapse.core.bean.base.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import com.maoatao.cas.security.constant.GrantType;

import java.io.Serial;

/**
 * 生成访问令牌请求参数
 *
 * @author MaoAtao
 * @date 2023-03-04 20:56:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "生成访问令牌请求参数")
public class GenerateAccessTokenParam extends BaseParam {

    @Serial
    private static final long serialVersionUID = -7286826147280035652L;
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
    /**
     * 重定向URL
     */
    @Schema(description = "重定向URL")
    private String redirectUrl;
}
