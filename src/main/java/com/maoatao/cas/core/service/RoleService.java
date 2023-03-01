package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maoatao.cas.core.entity.RoleEntity;
import com.maoatao.cas.core.param.PageParam;

import java.util.List;

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

    /**
     * 通过角色名称和客户端 id 获取角色
     *
     * @param roleNames 角色名称集合
     * @param clientId  客户端 id
     * @return 角色集合
     */
    List<RoleEntity> listByRolesAndClient(List<String> roleNames, String clientId);
}
