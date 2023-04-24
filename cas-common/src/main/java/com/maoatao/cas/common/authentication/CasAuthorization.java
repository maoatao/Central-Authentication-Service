package com.maoatao.cas.common.authentication;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoatao.synapse.lang.util.SynaDates;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * 授权详情 接口
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
     * 获取权限列表
     *
     * @return 权限集 客户端id -> 权限集合
     */
    Map<String, Set<String>> getPermissions();

    /**
     * 获取过期时间
     *
     * @return 过期时间
     */
    @JsonFormat(pattern = SynaDates.DATE_TIME_FORMAT, timezone = SynaDates.CN_TIME_ZONE)
    LocalDateTime getExpiresAt();

    /**
     * 获取签发时间
     *
     * @return 过期时间
     */
    @JsonFormat(pattern = SynaDates.DATE_TIME_FORMAT, timezone = SynaDates.CN_TIME_ZONE)
    LocalDateTime getIssuedAt();

    /**
     * 是客户端凭据
     *
     * @return 如果是客户端凭据返回true
     */
    boolean isClientCredentials();
}
