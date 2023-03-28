package com.maoatao.cas.core.service;

import com.maoatao.cas.core.entity.RoleEntity;
import com.maoatao.daedalus.data.service.DaedalusService;

import java.util.List;

/**
 * 角色
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
public interface RoleService extends DaedalusService<RoleEntity> {

    /**
     * 通过角色名称和客户端 id 查询角色
     *
     * @param roleNames 角色名称集合
     * @param clientId  客户端 id
     * @return 角色列表
     */
    List<RoleEntity> listByRolesAndClient(List<String> roleNames, String clientId);

    /**
     * 通过用户id查询列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleEntity> listByUser(Long userId);
}
