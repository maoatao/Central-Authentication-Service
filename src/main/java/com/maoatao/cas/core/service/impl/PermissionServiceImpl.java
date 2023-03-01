package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.PermissionEntity;
import com.maoatao.cas.core.mapper.PermissionMapper;
import com.maoatao.cas.web.param.PageParam;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.cas.web.param.PermissionParam;
import com.maoatao.synapse.core.util.SynaAssert;
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

    public Page<PermissionEntity> getPage(PermissionParam param) {
        return page(new Page<>(param.getPageNo(), param.getPageSize()), Wrappers.query(BeanUtil.copyProperties(param, PermissionEntity.class)));
    }

    @Override
    public List<PermissionEntity> listByUser(Long userId) {
        return permissionMapper.getPermissionByUser(userId);
    }

    @Override
    public boolean save(PermissionParam param) {
        return save(BeanUtil.copyProperties(param, PermissionEntity.class));
    }

    @Override
    public boolean update(PermissionParam param) {
        checkExisted(param.getId());
        return updateById(BeanUtil.copyProperties(param, PermissionEntity.class));
    }

    @Override
    public boolean remove(Long id) {
        checkExisted(id);
        return removeById(id);
    }

    private void checkExisted(Long id) {
        SynaAssert.notNull(getById(id), "{}号权限不存在!");
    }
}
