package com.maoatao.cas.core.service.impl;

import cn.hutool.core.collection.IterUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.UserRoleEntity;
import com.maoatao.cas.core.mapper.UserRoleMapper;
import com.maoatao.cas.core.param.PageParam;
import com.maoatao.cas.core.service.UserRoleService;
import com.maoatao.synapse.core.util.SynaAssert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户角色关系表
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {

    @Override
    public Page<UserRoleEntity> getPage(PageParam pageParam, UserRoleEntity userRoleEntity) {
        return page(new Page<>(pageParam.getPageNo(), pageParam.getPageSize()), Wrappers.query(userRoleEntity));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserRole(List<Long> roleIds, long userId) {
        if (IterUtil.isEmpty(roleIds)) {
            return remove(Wrappers.<UserRoleEntity>lambdaQuery().eq(UserRoleEntity::getUserId, userId));
        }
        // 查询已有,排除已有,列出新增和删除
        List<Long> existed = list(Wrappers.<UserRoleEntity>lambdaQuery().eq(UserRoleEntity::getUserId, userId))
                .stream()
                .map(UserRoleEntity::getRoleId).toList();
        // 已有不在入参为删除
        List<Long> preDelete = existed.stream().filter(o -> !roleIds.contains(o)).toList();
        // 入参不在已有为新增
        List<Long> preAdd = roleIds.stream().filter(o -> !existed.contains(o)).toList();
        SynaAssert.isTrue(removeBatchByIds(preDelete), "删除用户角色关系失败!");
        List<UserRoleEntity> newUserRoles = preAdd.stream()
                .map(o -> UserRoleEntity.builder().userId(userId).roleId(o).build())
                .toList();
        SynaAssert.isTrue(saveBatch(newUserRoles), "新增用户角色关系失败!");
        return true;
    }
}
