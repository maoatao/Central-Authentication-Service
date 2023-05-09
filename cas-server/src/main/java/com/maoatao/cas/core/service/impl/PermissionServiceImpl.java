package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.param.permission.PermissionQueryParam;
import com.maoatao.cas.core.bean.vo.PermissionVO;
import com.maoatao.cas.core.bean.entity.PermissionEntity;
import com.maoatao.cas.core.mapper.PermissionMapper;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import com.maoatao.synapse.core.bean.base.BaseSaveParam;
import com.maoatao.synapse.lang.util.SynaAssert;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Service
public class PermissionServiceImpl extends DaedalusServiceImpl<PermissionMapper, PermissionEntity> implements PermissionService {

    @Override
    public Page<PermissionVO> page(PermissionQueryParam param) {
        PermissionEntity entity = BeanUtil.copyProperties(param, PermissionEntity.class);
        Page<PermissionEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, PermissionVO.class);
    }

    @Override
    public PermissionVO details(Long id) {
        return BeanUtil.toBean(super.getById(id), PermissionVO.class);
    }

    @Override
    public long save(BaseSaveParam param) {
        PermissionEntity permissionEntity = BeanUtil.copyProperties(param, PermissionEntity.class);
        permissionEntity.setCode(IdUtil.getSnowflakeNextIdStr());
        SynaAssert.isTrue(super.save(permissionEntity), "新增失败!");
        return permissionEntity.getId();
    }

    @Override
    public List<PermissionEntity> listByClientUser(Long clientUserIds) {
        return getBaseMapper().listByClientUser(clientUserIds);
    }

    @Override
    public List<PermissionEntity> listByClientUsers(List<Long> clientUserIds) {
        return getBaseMapper().listByClientUsers(clientUserIds);
    }

    @Override
    public List<PermissionEntity> listByClientScopes(List<Long> scopes) {
        SynaAssert.notEmpty(scopes, "scopes 不能为空!");
        return getBaseMapper().listByClientScopes(scopes);
    }
}
