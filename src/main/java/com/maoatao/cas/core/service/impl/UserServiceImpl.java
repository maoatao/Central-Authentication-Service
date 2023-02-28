package com.maoatao.cas.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.UserEntity;
import com.maoatao.cas.core.mapper.UserMapper;
import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.core.param.PageParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户
 *
 * @author: MaoAtao
 * @create: 2022-03-11 16:13:35
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Override
    public Page<UserEntity> getUserPage(PageParam pageParam, UserEntity entity) {
        return page(new Page<>(pageParam.getPageNo(), pageParam.getPageSize()), Wrappers.query(entity));
    }

    @Override
    public UserEntity getUserById(String id) {
        return getById(id);
    }

    @Override
    public Boolean addUser(UserEntity entity) {
        return save(entity);
    }

    @Override
    public Boolean updateUserById(UserEntity entity) {
        return updateById(entity);
    }

    @Override
    public Boolean deleteUserById(String id) {
        return removeById(id);
    }

    @Override
    public UserEntity getUserByName(String userName) {
        return getOne(Wrappers.<UserEntity>lambdaQuery()
                .eq(UserEntity::getName, userName)
                .eq(UserEntity::isEnabled, true)
        );
    }
}
