package com.maoatao.cas.common.authentication;

import com.maoatao.synapse.core.bean.base.BaseBean;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 授权信息
 *
 * @author MaoAtao
 * @date 2023-03-24 23:06:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DefaultAuthorization extends BaseBean implements CasAuthorization {
    @Serial
    private static final long serialVersionUID = 1009233448803558861L;
    /**
     * 用户名
     */
    private String user;
    /**
     * 开放 ID (CAS全局唯一)
     */
    private String openId;
    /**
     * 权限集
     */
    private Map<String, Set<String>> permissions;
    /**
     * 过期时间
     */
    private LocalDateTime expiresAt;
    /**
     * 签发时间
     */
    private LocalDateTime issuedAt;
    /**
     * 客户端凭据
     */
    private boolean clientCredentials;
}
