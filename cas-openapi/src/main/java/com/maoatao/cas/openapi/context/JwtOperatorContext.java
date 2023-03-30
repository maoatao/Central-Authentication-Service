package com.maoatao.cas.openapi.context;

import com.maoatao.daedalus.core.context.DaedalusOperatorContext;
import com.maoatao.synapse.core.bean.base.BaseBean;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * JWT 操作者上下文
 *
 * @author MaoAtao
 * @date 2023-03-24 23:10:19
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
public class JwtOperatorContext extends BaseBean implements DaedalusOperatorContext {
    @Serial
    private static final long serialVersionUID = 1750170854450023382L;
    /**
     * 操作者 ID (全局唯一)
     */
    private final String operatorId;
    /**
     * 操作者名称 (客户端唯一)
     */
    private final String operatorName;
    /**
     * 客户端 ID
     */
    private final String clientId;
    /**
     * 角色集
     */
    private final Set<String> roles;
    /**
     * 权限集
     */
    private final Set<String> permissions;
    /**
     * 过期时间
     */
    private final LocalDateTime expiresAt;
    /**
     * 签发时间
     */
    private final LocalDateTime issuedAt;
    /**
     * 签发者 (CAS URL)
     */
    private final String issuer;
    /**
     * 客户端凭据
     */
    private final boolean clientCredentials;
}
