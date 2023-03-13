package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.PermissionEntity;
import com.maoatao.cas.core.mapper.PermissionMapper;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.cas.core.param.PermissionParam;
import com.maoatao.daedalus.data.util.PageUtils;
import com.maoatao.synapse.lang.util.SynaAssert;
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
        return page(PageUtils.convert(param), Wrappers.query(BeanUtil.copyProperties(param, PermissionEntity.class)));
    }

    @Override
    public List<PermissionEntity> listByUser(Long userId) {
        return permissionMapper.getPermissionByUser(userId);
    }

    @Override
    public boolean save(PermissionParam param) {
        param.setId(null);
        return save(BeanUtil.copyProperties(param, PermissionEntity.class));
    }

    @Override
    public boolean update(PermissionParam param) {
        SynaAssert.notNull(getById(param.getId()), "权限不存在!");
        return updateById(BeanUtil.copyProperties(param, PermissionEntity.class));
    }
}
