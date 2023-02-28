package com.maoatao.cas.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.UserEntity;
import com.maoatao.cas.core.mapper.UserMapper;
import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.core.param.PageParam;
import org.springframework.stereotype.Service;

/**
 * 用户
 *
 * @author: MaoAtao
 * @create: 2022-03-11 16:13:35
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Override
    public Page<UserEntity> getPage(PageParam pageParam, UserEntity entity) {
        return page(new Page<>(pageParam.getPageNo(), pageParam.getPageSize()), Wrappers.query(entity));
    }
}
