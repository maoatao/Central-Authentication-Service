package com.maoatao.cas.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.UserRoleEntity;
import com.maoatao.cas.core.mapper.UserRoleMapper;
import com.maoatao.cas.core.param.PageParam;
import com.maoatao.cas.core.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户角色关系表
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {

    @Override
    public Page<UserRoleEntity> getPage(PageParam pageParam, UserRoleEntity userRoleEntity) {
        return page(new Page<>(pageParam.getPageNo(), pageParam.getPageSize()), Wrappers.query(userRoleEntity));
    }
}
