package com.maoatao.cas.core.service;

import com.maoatao.cas.core.entity.UserRoleEntity;
import com.maoatao.daedalus.data.service.DaedalusService;

import java.util.List;

/**
 * 用户角色关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
public interface UserRoleService extends DaedalusService<UserRoleEntity> {

    /**
     * 更新用户角色
     * <p>
     * 以入参角色 id 为准,如果传入空表示删除该用户的所有角色
     *
     * @param roleIds 角色 id 集合
     * @param userId  用户 id (未作存在校验,使用前请确认是否存在)
     * @return 更新成功返回 true
     */
    boolean updateUserRole(List<Long> roleIds, long userId);
}
