package com.maoatao.cas.core.service.impl;

import cn.hutool.core.collection.IterUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.maoatao.cas.core.entity.UserRoleEntity;
import com.maoatao.cas.core.mapper.UserRoleMapper;
import com.maoatao.cas.core.service.UserRoleService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.synapse.lang.util.SynaAssert;
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
public class UserRoleServiceImpl extends DaedalusServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserRole(List<Long> roleIds, long userId) {
        // 查询已有,排除已有,列出新增和删除
        List<Long> existed = list(Wrappers.<UserRoleEntity>lambdaQuery().eq(UserRoleEntity::getUserId, userId))
                .stream()
                .map(UserRoleEntity::getRoleId).toList();
        if (IterUtil.isEmpty(roleIds) && IterUtil.isNotEmpty(existed)) {
            return remove(Wrappers.<UserRoleEntity>lambdaQuery().eq(UserRoleEntity::getUserId, userId));
        }
        // 已有不在入参为删除(已有-交集)
        List<Long> preDelete = existed.stream().filter(o -> !roleIds.contains(o)).toList();
        // 入参不在已有为新增(入参-交集)
        List<Long> preAdd = roleIds.stream().filter(o -> !existed.contains(o)).toList();
        if (IterUtil.isNotEmpty(preDelete)) {
            SynaAssert.isTrue(removeBatchByIds(preDelete), "删除用户角色关系失败!");
        }
        if (IterUtil.isNotEmpty(preAdd)) {
            List<UserRoleEntity> newUserRoles = preAdd.stream()
                    .map(o -> UserRoleEntity.builder().userId(userId).roleId(o).build())
                    .toList();
            SynaAssert.isTrue(saveBatch(newUserRoles), "新增用户角色关系失败!");
        }
        return true;
    }
}
