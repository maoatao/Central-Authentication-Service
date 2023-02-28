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
    Page<PermissionEntity> getPermissionPage(PageParam pageParam, PermissionEntity permissionEntity);

    /**
     * 通过id查询权限
     *
     * @param id 权限id
     * @return 权限对象
     */
    PermissionEntity getPermissionById(String id);

    /**
     * 新增权限
     *
     * @param permissionEntity 权限
     * @return 新增操作结果
     */
    Boolean addPermission(PermissionEntity permissionEntity);

    /**
     * 修改权限
     *
     * @param permissionEntity 权限
     * @return 修改操作结果
     */
    Boolean updatePermissionById(PermissionEntity permissionEntity);

    /**
     * 通过id删除权限
     *
     * @param id 权限id
     * @return 删除操作结果
     */
    Boolean deletePermissionById(String id);

    /**
     * 获取权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<PermissionEntity> getPermissionByUser(String userId);
}
