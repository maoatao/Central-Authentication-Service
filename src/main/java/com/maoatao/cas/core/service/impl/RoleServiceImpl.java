package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.RoleEntity;
import com.maoatao.cas.core.mapper.RoleMapper;
import com.maoatao.cas.core.service.RoleService;
import com.maoatao.cas.web.param.RoleParam;
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
    public Page<RoleEntity> getPage(RoleParam param) {
        return page(new Page<>(param.getPageNo(), param.getPageSize()), Wrappers.query(BeanUtil.copyProperties(param, RoleEntity.class)));
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
    public boolean save(RoleParam param) {
        param.setId(null);
        return save(BeanUtil.copyProperties(param, RoleEntity.class));
    }

    @Override
    public boolean update(RoleParam param) {
        checkExisted(param.getId());
        return updateById(BeanUtil.copyProperties(param, RoleEntity.class));
    }

    @Override
    public boolean remove(Long id) {
        checkExisted(id);
        return removeById(id);
    }

    private void checkExisted(Long id) {
        SynaAssert.notNull(getById(id), "{}号角色不存在!");
    }
}
