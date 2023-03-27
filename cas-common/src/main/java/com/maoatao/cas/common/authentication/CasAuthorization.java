package com.maoatao.cas.common.authentication;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Synapse 授权详情 接口
 *
 * @author MaoAtao
 * @date 2023-03-16 14:42:05
 */
public interface CasAuthorization extends Serializable {

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    String getUser();

    /**
     * 获取开放 ID
     *
     * @return CAS全局唯一 ID
     */
    String getOpenId();

    /**
     * 获取客户端 ID
     *
     * @return 客户端 ID
     */
    String getClientId();

    /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    Set<String> getRoles();

    /**
     * 获取权限列表
     *
     * @return 权限列表
     */
    Set<String> getPermissions();

    /**
     * 获取作用域
     *
     * @return 授权作用域
     */
    Set<String> getScope();

    /**
     * 获取过期时间
     *
     * @return 过期时间
     */
    LocalDateTime getExpiresAt();

    /**
     * 获取签发时间
     *
     * @return 过期时间
     */
    LocalDateTime getIssuedAt();
}
