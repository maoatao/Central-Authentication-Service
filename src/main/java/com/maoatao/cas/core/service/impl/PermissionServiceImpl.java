package com.maoatao.cas.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.PermissionEntity;
import com.maoatao.cas.core.mapper.PermissionMapper;
import com.maoatao.cas.core.param.PageParam;
import com.maoatao.cas.core.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 权限
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionEntity> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Page<PermissionEntity> getPermissionPage(PageParam pageParam, PermissionEntity permissionEntity) {
        return page(new Page<>(pageParam.getPageNo(), pageParam.getPageSize()), Wrappers.query(permissionEntity));
    }

    @Override
    public PermissionEntity getPermissionById(String id) {
        return getById(id);
    }

    @Override
    public Boolean addPermission(PermissionEntity permissionEntity) {
        return save(permissionEntity);
    }

    @Override
    public Boolean updatePermissionById(PermissionEntity permissionEntity) {
        return updateById(permissionEntity);
    }

    @Override
    public Boolean deletePermissionById(String id) {
        return removeById(id);
    }

    @Override
    public List<PermissionEntity> getPermissionByUser(String userId) {
        return permissionMapper.getPermissionByUser(userId);
    }
}
