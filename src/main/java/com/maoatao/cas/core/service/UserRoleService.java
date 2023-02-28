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
    Page<UserRoleEntity> getPage(PageParam pageParam, UserRoleEntity userRoleEntity);
}
