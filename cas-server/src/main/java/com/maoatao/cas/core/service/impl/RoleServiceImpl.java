package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.param.role.RoleQueryParam;
import com.maoatao.cas.core.bean.vo.RoleVO;
import com.maoatao.cas.core.bean.entity.RoleEntity;
import com.maoatao.cas.core.mapper.RoleMapper;
import com.maoatao.cas.core.service.RoleService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import com.maoatao.synapse.lang.util.SynaAssert;
import com.maoatao.synapse.lang.util.SynaSafes;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Service
public class RoleServiceImpl extends DaedalusServiceImpl<RoleMapper, RoleEntity> implements RoleService {

    @Override
    public Page<RoleVO> page(RoleQueryParam param) {
        RoleEntity entity = BeanUtil.copyProperties(param, RoleEntity.class);
        Page<RoleEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, RoleVO.class);
    }

    @Override
    public RoleVO details(Long id){
        return BeanUtil.toBean(super.getById(id), RoleVO.class);
    }

    @Override
    public List<RoleEntity> listByRolesAndClient(List<String> roleNames, String clientId) {
        SynaAssert.notEmpty(roleNames, "角色不能为空!");
        SynaAssert.isTrue(StrUtil.isNotBlank(clientId), "客户端 Id 不能为空");
        return SynaSafes.of(list(
                Wrappers.<RoleEntity>lambdaQuery().in(RoleEntity::getName, roleNames).eq(RoleEntity::getClientId, clientId)
        ));
    }

    @Override
    public List<RoleEntity> listByUser(Long userId) {
        return getBaseMapper().getRoleByUser(userId);
    }
}
