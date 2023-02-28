package com.maoatao.cas.security.bean;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;

/**
 * 权限名称对象
 *
 * @author MaoAtao
 * @date 2021-05-13 15:27:51
 */
@Builder
public class CustomAuthority implements GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 8787876921227402556L;

    /**
     * 权限名称
     */
    private String authority;

    /**
     * 客户端ID
     */
    private String client;

    @Override
    public String getAuthority() {
        return this.authority;
    }

    @Override
    public String toString() {
        return this.authority;
    }
}
