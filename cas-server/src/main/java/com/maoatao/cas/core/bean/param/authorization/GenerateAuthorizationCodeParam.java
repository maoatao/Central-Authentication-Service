package com.maoatao.cas.core.bean.param.authorization;

import com.maoatao.synapse.core.bean.base.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 生成授权码请求参数
 *
 * @author MaoAtao
 * @date 2022-02-26 20:18:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "生成授权码请求参数")
public class GenerateAuthorizationCodeParam extends BaseParam {

    @Serial
    private static final long serialVersionUID = -7286826147280035652L;
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String username;
    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;
    /**
     * 授权范围
     */
    @Schema(description = "授权范围")
    private Set<String> scopes;
    /**
     * PKCE协议额外参数:校验方法
     * <p>
     * {@link ClientSettings#isRequireProofKey} 为 true 时必传且不能为空
     */
    @Schema(description = "PKCE协议额外参数:校验方法")
    private String codeChallengeMethod;
    /**
     * PKCE协议额外参数:校验值
     * <p>
     * {@link ClientSettings#isRequireProofKey} 为 true 时必传且不能为空
     */
    @Schema(description = "PKCE协议额外参数:校验值")
    private String codeChallenge;

    public Map<String, Object> getAdditionalParameters() {
        Map<String, Object> additionalParameters = new HashMap<>(2);
        additionalParameters.put(PkceParameterNames.CODE_CHALLENGE_METHOD, getCodeChallengeMethod());
        additionalParameters.put(PkceParameterNames.CODE_CHALLENGE, getCodeChallenge());
        return additionalParameters;
    }
}
