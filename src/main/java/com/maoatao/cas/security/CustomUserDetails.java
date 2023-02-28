package com.maoatao.cas.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 自定义用户详情 UserDetails
 * <p>
 * Customized by {@link org.springframework.security.core.userdetails.UserDetails}
 *
 * @author MaoAtao
 * @date 2023-02-28 22:04:00
 */
public interface CustomUserDetails extends UserDetails {
    /**
     * 获取客户端 Id
     *
     * @return 客户端 Id
     */
    String getClientId();
}
