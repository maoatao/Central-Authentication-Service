package com.maoatao.cas.security.bean;

import lombok.Builder;

/**
 * 基本身份验证
 *
 * @author MaoAtao
 * @date 2023-03-05 01:07:06
 */
@Builder
public record BasicAuthentication(String username, String password) {}
