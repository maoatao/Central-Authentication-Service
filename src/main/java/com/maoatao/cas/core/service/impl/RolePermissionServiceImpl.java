package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.RolePermissionEntity;
import com.maoatao.cas.core.mapper.RolePermissionMapper;
import com.maoatao.cas.core.service.RolePermissionService;
import com.maoatao.cas.core.param.RolePermissionParam;
import com.maoatao.daedalus.data.util.PageUtils;
import com.maoatao.synapse.lang.util.SynaAssert;
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
        return page(PageUtils.convert(param), Wrappers.query(BeanUtil.copyProperties(param, RolePermissionEntity.class)));
    }

    @Override
    public boolean save(RolePermissionParam param) {
        param.setId(null);
        return save(BeanUtil.copyProperties(param, RolePermissionEntity.class));
    }

    @Override
    public boolean update(RolePermissionParam param) {
        SynaAssert.notNull(getById(param.getId()), "角色权限关系不存在!");
        return updateById(BeanUtil.copyProperties(param, RolePermissionEntity.class));
    }
}
