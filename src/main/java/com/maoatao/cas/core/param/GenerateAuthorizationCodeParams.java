package com.maoatao.cas.core.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;

import java.io.Serial;
import java.io.Serializable;
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
@Schema(description = "生成授权码请求参数")
public class GenerateAuthorizationCodeParams implements Serializable {

    @Serial
    private static final long serialVersionUID = -7286826147280035652L;
    /**
     * 客户端id
     */
    @Schema(description = "客户端ID")
    private String clientId;
    /**
     * 客户端id
     */
    @Schema(description = "用户名")
    private String username;
    /**
     * 客户端id
     */
    @Schema(description = "密码")
    private String password;
    /**
     * 范围
     */
    @Schema(description = "令牌范围")
    private Set<String> scopes;
    /**
     * 校验方法
     */
    @Schema(description = "校验授权码的方法")
    private String codeChallengeMethod;
    /**
     * 校验值
     */
    @Schema(description = "校验授权码的值")
    private String codeChallenge;

    public Map<String, Object> getAdditionalParameters() {
        Map<String, Object> additionalParameters = new HashMap<>(2);
        additionalParameters.put(PkceParameterNames.CODE_CHALLENGE_METHOD, getCodeChallengeMethod());
        additionalParameters.put(PkceParameterNames.CODE_CHALLENGE, getCodeChallenge());
        return additionalParameters;
    }
}
