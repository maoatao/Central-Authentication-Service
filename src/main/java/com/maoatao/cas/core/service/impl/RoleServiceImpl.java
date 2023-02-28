package com.maoatao.cas.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.RoleEntity;
import com.maoatao.cas.core.param.PageParam;
import com.maoatao.cas.core.mapper.RoleMapper;
import com.maoatao.cas.core.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {

    @Override
    public Page<RoleEntity> getRolePage(PageParam pageParam, RoleEntity roleEntity) {
        return page(new Page<>(pageParam.getPageNo(), pageParam.getPageSize()), Wrappers.query(roleEntity));
    }

    @Override
    public RoleEntity getRoleById(String id) {
        return getById(id);
    }

    @Override
    public Boolean addRole(RoleEntity roleEntity) {
        return save(roleEntity);
    }

    @Override
    public Boolean updateRoleById(RoleEntity roleEntity) {
        return updateById(roleEntity);
    }

    @Override
    public Boolean deleteRoleById(String id) {
        return removeById(id);
    }
}
