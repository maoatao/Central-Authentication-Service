package com.maoatao.cas.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.maoatao.cas.core.bean.entity.PermissionEntity;

import java.util.List;

/**
 * 权限
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionEntity> {

    /**
     * 查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<PermissionEntity> listByClientUser(Long userId);

    /**
     * 通过作用域查询列表
     *
     * @param scopes 作用域
     * @return 权限列表
     */
    List<PermissionEntity> listByClientScopes(List<Long> scopes);
}
