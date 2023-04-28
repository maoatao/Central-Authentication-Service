package com.maoatao.cas.security.bean;

import java.io.Serial;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

/**
 * 授予的客户权限
 *
 * @author LiYuanHao
 * @date 2023-04-27 12:14:17
 */
@Getter
@Builder
@ToString
public class ClientAuthority implements GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 790178260257298823L;

    /**
     * 客户端 id
     */
    private final String clientId;

    /**
     * 权限
     */
    private final String permission;

    @Override
    public String getAuthority() {
        return this.permission;
    }
}
