package com.maoatao.cas.core.service;

import com.maoatao.cas.core.entity.PermissionEntity;
import com.maoatao.daedalus.data.service.DaedalusService;

import java.util.List;

/**
 * 权限
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
public interface PermissionService extends DaedalusService<PermissionEntity> {

    /**
     * 通过用户id查询列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<PermissionEntity> listByUser(Long userId);
}
