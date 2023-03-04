package com.maoatao.cas.security.bean;

import lombok.Builder;

/**
 * 客户端用户信息
 *
 * @author MaoAtao
 * @date 2023-03-05 01:07:06
 */
@Builder
public record ClientUser(String clientId, String username, String password) {}
