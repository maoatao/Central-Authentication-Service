package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.param.permission.PermissionQueryParam;
import com.maoatao.cas.core.bean.vo.PermissionVO;
import com.maoatao.cas.core.bean.entity.PermissionEntity;
import com.maoatao.cas.core.mapper.PermissionMapper;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
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
    public List<PermissionEntity> listByClientUser(Long userId) {
        return getBaseMapper().listByClientUser(userId);
    }

    @Override
    public List<PermissionEntity> listByClientScopes(List<Long> scopes) {
        SynaAssert.notEmpty(scopes, "scopes 不能为空!");
        return getBaseMapper().listByClientScopes(scopes);
    }
}
