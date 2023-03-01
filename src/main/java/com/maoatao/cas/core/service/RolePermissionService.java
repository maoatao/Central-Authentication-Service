package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maoatao.cas.core.entity.RolePermissionEntity;
import com.maoatao.cas.web.param.RolePermissionParam;

/**
 * 角色权限关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
public interface RolePermissionService extends IService<RolePermissionEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<RolePermissionEntity> getPage(RolePermissionParam param);

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回true
     */
    boolean save(RolePermissionParam param);

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    boolean update(RolePermissionParam param);

    /**
     * 删除
     *
     * @param id 主键id
     * @return 删除成功返回true
     */
    boolean remove(Long id);
}
