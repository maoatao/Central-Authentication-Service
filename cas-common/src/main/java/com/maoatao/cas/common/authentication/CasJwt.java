package com.maoatao.cas.common.authentication;

import com.maoatao.synapse.core.bean.base.BaseBean;
import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * CAS Jwt
 *
 * @author MaoAtao
 * @date 2023-03-24 22:57:52
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class CasJwt extends BaseBean {

    @Serial
    private static final long serialVersionUID = 7504153736906698787L;

    /**
     * jwt签发者
     */
    private String iss;
    /**
     * jwt所面向的用户
     */
    private String sub;
    /**
     * 接收jwt的一方
     */
    private String aud;
    /**
     * jwt的过期时间，这个过期时间必须要大于签发时间
     */
    private String exp;
    /**
     * 生效时间，定义在什么时间之前，该jwt都是不可用的.
     */
    private String nbf;
    /**
     * jwt的签发时间
     */
    private String iat;
    /**
     * jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
     */
    private String jti;
    /**
     * 开放ID
     */
    private String openId;
    /**
     * 权限
     */
    private Map<String, Set<String>> permissions;
    /**
     * 客户端凭据
     */
    private boolean clientCredentials;
}
