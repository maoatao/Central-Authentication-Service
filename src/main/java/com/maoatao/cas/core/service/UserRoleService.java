package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maoatao.cas.core.entity.UserRoleEntity;
import com.maoatao.cas.web.param.UserRoleParam;

import java.util.List;

/**
 * 用户角色关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
public interface UserRoleService extends IService<UserRoleEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<UserRoleEntity> getPage(UserRoleParam param);

    /**
     * 更新用户角色
     * <p>
     * 以入参角色 id 为准,如果传入空表示删除该用户的所有角色
     *
     * @param roleIds 角色 id 集合
     * @param userId  用户 id
     * @return 更新成功返回 true
     */
    boolean updateUserRole(List<Long> roleIds, long userId);

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回true
     */
    boolean save(UserRoleParam param);

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    boolean update(UserRoleParam param);

    /**
     * 删除
     *
     * @param id 主键id
     * @return 删除成功返回true
     */
    boolean remove(Long id);
}
