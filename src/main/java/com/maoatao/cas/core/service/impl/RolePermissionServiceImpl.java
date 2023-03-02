package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.RolePermissionEntity;
import com.maoatao.cas.core.mapper.RolePermissionMapper;
import com.maoatao.cas.core.service.RolePermissionService;
import com.maoatao.cas.core.param.RolePermissionParam;
import com.maoatao.synapse.core.util.SynaAssert;
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
    public Page<RolePermissionEntity> getPage(RolePermissionParam param) {
        return page(new Page<>(param.getPageNo(), param.getPageSize()), Wrappers.query(BeanUtil.copyProperties(param, RolePermissionEntity.class)));
    }

    @Override
    public boolean save(RolePermissionParam param) {
        param.setId(null);
        return save(BeanUtil.copyProperties(param, RolePermissionEntity.class));
    }

    @Override
    public boolean update(RolePermissionParam param) {
        checkExisted(param.getId());
        return updateById(BeanUtil.copyProperties(param, RolePermissionEntity.class));
    }

    @Override
    public boolean remove(Long id) {
        checkExisted(id);
        return removeById(id);
    }

    private void checkExisted(Long id) {
        SynaAssert.notNull(getById(id), "{}号角色权限关系不存在!");
    }
}
