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
    Page<UserEntity> getPage(PageParam pageParam, UserEntity entity);
}
