package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maoatao.cas.core.entity.UserEntity;
import com.maoatao.cas.web.param.UserParam;

/**
 * 用户
 *
 * @author MaoAtao
 * @date 2022-03-11 16:13:35
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<UserEntity> getPage(UserParam param);

    /**
     * 按用户名称和客户端 id 查询用户
     *
     * @param name     名称
     * @param clientId 客户端 id
     * @return 用户不存在返回 null
     */
    UserEntity getByNameAndClient(String name, String clientId);

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回true
     */
    boolean save(UserParam param);

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    boolean update(UserParam param);

    /**
     * 删除
     *
     * @param id 主键id
     * @return 删除成功返回true
     */
    boolean remove(Long id);
}
