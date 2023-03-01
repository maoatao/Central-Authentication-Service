package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maoatao.cas.core.entity.PermissionEntity;
import com.maoatao.cas.core.param.PageParam;

import java.util.List;

/**
 * 权限
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
public interface PermissionService extends IService<PermissionEntity> {

    /**
     * 分页查询权限列表
     *
     * @param pageParam        分页对象
     * @param permissionEntity 权限
     * @return 分页权限列表
     */
    Page<PermissionEntity> getPage(PageParam pageParam, PermissionEntity permissionEntity);

    /**
     * 获取权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<PermissionEntity> getByUser(Long userId);
}
