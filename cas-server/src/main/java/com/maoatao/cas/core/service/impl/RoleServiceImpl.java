package com.maoatao.cas.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.maoatao.cas.core.entity.RoleEntity;
import com.maoatao.cas.core.mapper.RoleMapper;
import com.maoatao.cas.core.service.RoleService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
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
