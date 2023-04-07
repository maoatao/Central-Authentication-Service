package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.param.rolepermission.RolePermissionQueryParam;
import com.maoatao.cas.core.bean.vo.RolePermissionVO;
import com.maoatao.cas.core.bean.entity.RolePermissionEntity;
import com.maoatao.cas.core.mapper.RolePermissionMapper;
import com.maoatao.cas.core.service.RolePermissionService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import com.maoatao.synapse.core.definition.interaction.Pagination;
import org.springframework.stereotype.Service;

/**
 * 角色权限关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Service
public class RolePermissionServiceImpl extends DaedalusServiceImpl<RolePermissionMapper, RolePermissionEntity> implements RolePermissionService {

    @Override
    public Page<RolePermissionVO> page(RolePermissionQueryParam param) {
        RolePermissionEntity entity = BeanUtil.copyProperties(param, RolePermissionEntity.class);
        Page<RolePermissionEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, RolePermissionVO.class);
    }

    @Override
    public RolePermissionVO details(Long id){
        return BeanUtil.toBean(super.getById(id), RolePermissionVO.class);
    }
}
