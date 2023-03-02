package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maoatao.cas.core.entity.RoleEntity;
import com.maoatao.cas.core.param.RoleParam;

import java.util.List;

/**
 * 角色
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
public interface RoleService extends IService<RoleEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<RoleEntity> getPage(RoleParam param);

    /**
     * 通过角色名称和客户端 id 查询角色
     *
     * @param roleNames 角色名称集合
     * @param clientId  客户端 id
     * @return 角色集合
     */
    List<RoleEntity> listByRolesAndClient(List<String> roleNames, String clientId);

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回true
     */
    boolean save(RoleParam param);

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    boolean update(RoleParam param);

    /**
     * 删除
     *
     * @param id 主键id
     * @return 删除成功返回true
     */
    boolean remove(Long id);
}
