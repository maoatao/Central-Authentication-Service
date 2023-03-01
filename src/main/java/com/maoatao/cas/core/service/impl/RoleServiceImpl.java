package com.maoatao.cas.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.RoleEntity;
import com.maoatao.cas.core.param.PageParam;
import com.maoatao.cas.core.mapper.RoleMapper;
import com.maoatao.cas.core.service.RoleService;
import com.maoatao.synapse.core.util.SynaAssert;
import com.maoatao.synapse.core.util.SynaSafes;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {

    @Override
    public Page<RoleEntity> getPage(PageParam pageParam, RoleEntity roleEntity) {
        return page(new Page<>(pageParam.getPageNo(), pageParam.getPageSize()), Wrappers.query(roleEntity));
    }

    @Override
    public List<RoleEntity> listByRolesAndClient(List<String> roleNames, String clientId) {
        SynaAssert.notEmpty(roleNames, "角色不能为空!");
        SynaAssert.isTrue(StrUtil.isNotBlank(clientId), "客户端 Id 不能为空");
        return SynaSafes.of(list(
                Wrappers.<RoleEntity>lambdaQuery().in(RoleEntity::getName, roleNames).eq(RoleEntity::getClientId, clientId)
        ));
    }
}
