package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.IterUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.UserRoleEntity;
import com.maoatao.cas.core.mapper.UserRoleMapper;
import com.maoatao.cas.core.service.UserRoleService;
import com.maoatao.cas.core.param.UserRoleParam;
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
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {

    @Override
    public Page<UserRoleEntity> getPage(UserRoleParam param) {
        return page(new Page<>(param.getPageNo(), param.getPageSize()), Wrappers.query(BeanUtil.copyProperties(param, UserRoleEntity.class)));
    }

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

    @Override
    public boolean save(UserRoleParam param) {
        param.setId(null);
        return save(BeanUtil.copyProperties(param, UserRoleEntity.class));
    }

    @Override
    public boolean update(UserRoleParam param) {
        checkExisted(param.getId());
        return updateById(BeanUtil.copyProperties(param, UserRoleEntity.class));
    }

    @Override
    public boolean remove(Long id) {
        checkExisted(id);
        return removeById(id);
    }

    private void checkExisted(Long id) {
        SynaAssert.notNull(getById(id), "{}号用户角色关系不存在!");
    }
}
