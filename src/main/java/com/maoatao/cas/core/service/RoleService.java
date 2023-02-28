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
    Page<RoleEntity> getPage(PageParam pageParam, RoleEntity roleEntity);
}
