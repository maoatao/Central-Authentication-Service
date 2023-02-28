package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maoatao.cas.core.entity.UserRoleEntity;
import com.maoatao.cas.core.param.PageParam;

/**
 * 用户角色关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
public interface UserRoleService extends IService<UserRoleEntity> {

    /**
     * 分页查询用户角色关系列表
     *
     * @param pageParam 分页对象
     * @param userRoleEntity 用户角色关系
     * @return 分页用户角色关系列表
     */
    Page<UserRoleEntity> getUserRolePage(PageParam pageParam, UserRoleEntity userRoleEntity);

    /**
     * 通过id查询用户角色关系
     *
     * @param id 用户角色关系id
     * @return 用户角色关系对象
     */
    UserRoleEntity getUserRoleById(String id);

    /**
     * 新增用户角色关系
     *
     * @param userRoleEntity 用户角色关系
     * @return 新增操作结果
     */
    Boolean addUserRole(UserRoleEntity userRoleEntity);

    /**
     * 修改用户角色关系
     *
     * @param userRoleEntity 用户角色关系
     * @return 修改操作结果
     */
    Boolean updateUserRoleById(UserRoleEntity userRoleEntity);

    /**
     * 通过id删除用户角色关系
     *
     * @param id 用户角色关系id
     * @return 删除操作结果
     */
    Boolean deleteUserRoleById(String id);
}
