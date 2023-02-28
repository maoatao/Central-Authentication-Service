package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maoatao.cas.core.entity.RoleEntity;
import com.maoatao.cas.core.param.PageParam;

/**
 * 角色
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
public interface RoleService extends IService<RoleEntity> {

    /**
     * 分页查询角色列表
     *
     * @param pageParam  分页对象
     * @param roleEntity 角色
     * @return 分页角色列表
     */
    Page<RoleEntity> getRolePage(PageParam pageParam, RoleEntity roleEntity);

    /**
     * 通过id查询角色
     *
     * @param id 角色id
     * @return 角色对象
     */
    RoleEntity getRoleById(String id);

    /**
     * 新增角色
     *
     * @param roleEntity 角色
     * @return 新增操作结果
     */
    Boolean addRole(RoleEntity roleEntity);

    /**
     * 修改角色
     *
     * @param roleEntity 角色
     * @return 修改操作结果
     */
    Boolean updateRoleById(RoleEntity roleEntity);

    /**
     * 通过id删除角色
     *
     * @param id 角色id
     * @return 删除操作结果
     */
    Boolean deleteRoleById(String id);
}
