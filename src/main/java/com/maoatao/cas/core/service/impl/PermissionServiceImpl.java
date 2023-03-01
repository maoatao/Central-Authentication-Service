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

    public Page<PermissionEntity> getPage(PageParam pageParam, PermissionEntity permissionEntity) {
        return page(new Page<>(pageParam.getPageNo(), pageParam.getPageSize()), Wrappers.query(permissionEntity));
    }

    @Override
    public List<PermissionEntity> getByUser(Long userId) {
        return permissionMapper.getPermissionByUser(userId);
    }
}
