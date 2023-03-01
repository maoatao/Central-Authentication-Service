package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maoatao.cas.core.entity.PermissionEntity;
import com.maoatao.cas.web.param.PermissionParam;

import java.util.List;

/**
 * 权限
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
public interface PermissionService extends IService<PermissionEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<PermissionEntity> getPage(PermissionParam param);

    /**
     * 通过用户id查询列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<PermissionEntity> listByUser(Long userId);

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回true
     */
    boolean save(PermissionParam param);

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    boolean update(PermissionParam param);

    /**
     * 删除
     *
     * @param id 主键id
     * @return 删除成功返回true
     */
    boolean remove(Long id);
}
