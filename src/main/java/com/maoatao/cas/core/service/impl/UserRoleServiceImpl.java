package com.maoatao.cas.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.UserRoleEntity;
import com.maoatao.cas.core.mapper.UserRoleMapper;
import com.maoatao.cas.core.param.PageParam;
import com.maoatao.cas.core.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户角色关系表
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {

    @Override
    public Page<UserRoleEntity> getUserRolePage(PageParam pageParam, UserRoleEntity userRoleEntity) {
        return page(new Page<>(pageParam.getPageNo(), pageParam.getPageSize()), Wrappers.query(userRoleEntity));
    }

    @Override
    public UserRoleEntity getUserRoleById(String id) {
        return getById(id);
    }

    @Override
    public Boolean addUserRole(UserRoleEntity userRoleEntity) {
        return save(userRoleEntity);
    }

    @Override
    public Boolean updateUserRoleById(UserRoleEntity userRoleEntity) {
        return updateById(userRoleEntity);
    }

    @Override
    public Boolean deleteUserRoleById(String id) {
        return removeById(id);
    }
}
