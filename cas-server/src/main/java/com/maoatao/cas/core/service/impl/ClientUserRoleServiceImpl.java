package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.IterUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.param.clientuserrole.ClientUserRoleQueryParam;
import com.maoatao.cas.core.bean.vo.UserRoleVO;
import com.maoatao.cas.core.bean.entity.ClientUserRoleEntity;
import com.maoatao.cas.core.mapper.ClientUserRoleMapper;
import com.maoatao.cas.core.service.ClientUserRoleService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
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
public class ClientUserRoleServiceImpl extends DaedalusServiceImpl<ClientUserRoleMapper, ClientUserRoleEntity> implements ClientUserRoleService {

    @Override
    public Page<UserRoleVO> page(ClientUserRoleQueryParam param) {
        ClientUserRoleEntity entity = BeanUtil.copyProperties(param, ClientUserRoleEntity.class);
        Page<ClientUserRoleEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, UserRoleVO.class);
    }

    @Override
    public UserRoleVO details(Long id){
        return BeanUtil.toBean(super.getById(id), UserRoleVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserRole(List<Long> roleIds, long userId) {
        // 查询已有,排除已有,列出新增和删除
        List<Long> existed = list(Wrappers.<ClientUserRoleEntity>lambdaQuery().eq(ClientUserRoleEntity::getUserId, userId))
                .stream()
                .map(ClientUserRoleEntity::getRoleId).toList();
        if (IterUtil.isEmpty(roleIds) && IterUtil.isNotEmpty(existed)) {
            return remove(Wrappers.<ClientUserRoleEntity>lambdaQuery().eq(ClientUserRoleEntity::getUserId, userId));
        }
        // 已有不在入参为删除(已有-交集)
        List<Long> preDelete = existed.stream().filter(o -> !roleIds.contains(o)).toList();
        // 入参不在已有为新增(入参-交集)
        List<Long> preAdd = roleIds.stream().filter(o -> !existed.contains(o)).toList();
        if (IterUtil.isNotEmpty(preDelete)) {
            SynaAssert.isTrue(removeBatchByIds(preDelete), "删除用户角色关系失败!");
        }
        if (IterUtil.isNotEmpty(preAdd)) {
            List<ClientUserRoleEntity> newUserRoles = preAdd.stream()
                    .map(o -> ClientUserRoleEntity.builder().userId(userId).roleId(o).build())
                    .toList();
            SynaAssert.isTrue(saveBatch(newUserRoles), "新增用户角色关系失败!");
        }
        return true;
    }
}
