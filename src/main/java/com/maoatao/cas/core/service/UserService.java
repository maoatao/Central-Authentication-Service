package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maoatao.cas.core.entity.UserEntity;
import com.maoatao.cas.core.param.PageParam;

/**
 * 用户
 *
 * @author MaoAtao
 * @date 2022-03-11 16:13:35
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 分页查询用户列表
     *
     * @param pageParam 分页对象
     * @param entity    请求参数
     * @return 分页用户列表
     */
    Page<UserEntity> getUserPage(PageParam pageParam, UserEntity entity);

    /**
     * 通过id查询用户
     *
     * @param id 用户id
     * @return 用户对象
     */
    UserEntity getUserById(String id);

    /**
     * 新增用户
     *
     * @param entity 请求参数
     * @return 新增操作结果
     */
    Boolean addUser(UserEntity entity);

    /**
     * 修改用户
     *
     * @param entity 请求参数
     * @return 修改操作结果
     */
    Boolean updateUserById(UserEntity entity);

    /**
     * 通过id删除用户
     *
     * @param id 用户id
     * @return 删除操作结果
     */
    Boolean deleteUserById(String id);

    /**
     * 通过用户名称和客户端ID获取用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     */
    UserEntity getUserByName(String userName);
}
