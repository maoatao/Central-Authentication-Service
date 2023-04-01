package com.maoatao.cas.common.authentication;

import com.maoatao.synapse.core.bean.base.BaseBean;
import java.io.Serial;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 默认访问令牌
 *
 * @author MaoAtao
 * @date 2023-03-05 00:58:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DefaultAccessToken extends BaseBean implements CasAccessToken {

    @Serial
    private static final long serialVersionUID = -5495750866870845743L;

    /**
     * 访问令牌值
     */
    private String accessToken;
    /**
     * 刷新令牌值
     */
    private String refreshToken;
    /**
     * 授权范围
     */
    private Set<String> scope;
    /**
     * 令牌类型
     */
    private String tokenType;
    /**
     * 过期时间
     */
    private long expiresIn;
}
