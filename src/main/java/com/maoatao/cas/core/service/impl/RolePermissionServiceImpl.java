package com.maoatao.cas.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.RolePermissionEntity;
import com.maoatao.cas.core.mapper.RolePermissionMapper;
import com.maoatao.cas.core.service.RolePermissionService;
import com.maoatao.cas.core.param.PageParam;
import org.springframework.stereotype.Service;

/**
 * 角色权限关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermissionEntity> implements RolePermissionService {

    @Override
    public Page<RolePermissionEntity> getPage(PageParam pageParam, RolePermissionEntity rolePermissionEntity) {
        return page(new Page<>(pageParam.getPageNo(), pageParam.getPageSize()), Wrappers.query(rolePermissionEntity));
    }
}
