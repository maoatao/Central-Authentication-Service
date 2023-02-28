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
    Page<RolePermissionEntity> getRolePermissionPage(PageParam pageParam, RolePermissionEntity rolePermissionEntity);

    /**
     * 通过id查询角色权限关系
     *
     * @param id 角色权限关系id
     * @return 角色权限关系对象
     */
    RolePermissionEntity getRolePermissionById(String id);

    /**
     * 新增角色权限关系
     *
     * @param rolePermissionEntity 角色权限关系
     * @return 新增操作结果
     */
    Boolean addRolePermission(RolePermissionEntity rolePermissionEntity);

    /**
     * 修改角色权限关系
     *
     * @param rolePermissionEntity 角色权限关系
     * @return 修改操作结果
     */
    Boolean updateRolePermissionById(RolePermissionEntity rolePermissionEntity);

    /**
     * 通过id删除角色权限关系
     *
     * @param id 角色权限关系id
     * @return 删除操作结果
     */
    Boolean deleteRolePermissionById(String id);
}
