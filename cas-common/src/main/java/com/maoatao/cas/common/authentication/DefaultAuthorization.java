package com.maoatao.cas.common.authentication;

import com.maoatao.synapse.core.bean.base.BaseBean;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 授权信息
 *
 * @author MaoAtao
 * @date 2023-03-24 23:06:36
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
public final class DefaultAuthorization extends BaseBean implements CasAuthorization {
    @Serial
    private static final long serialVersionUID = 1009233448803558861L;
    /**
     * 用户名
     */
    private final String user;
    /**
     * 开放 ID (CAS全局唯一)
     */
    private final String openId;
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
     * 作用域
     */
    private final Set<String> scope;
    /**
     * 过期时间
     */
    private final LocalDateTime expiresAt;
    /**
     * 签发时间
     */
    private final LocalDateTime issuedAt;
}
