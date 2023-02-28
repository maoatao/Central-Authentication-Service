package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maoatao.cas.core.entity.RolePermissionEntity;
import com.maoatao.cas.core.param.PageParam;

/**
 * 角色权限关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
public interface RolePermissionService extends IService<RolePermissionEntity> {

    /**
     * 分页查询角色权限关系列表
     *
     * @param pageParam            分页对象
     * @param rolePermissionEntity 角色权限关系
     * @return 分页角色权限关系列表
     */
    Page<RolePermissionEntity> getPage(PageParam pageParam, RolePermissionEntity rolePermissionEntity);
}
