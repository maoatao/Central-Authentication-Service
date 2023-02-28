package com.maoatao.cas.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.maoatao.cas.core.entity.RolePermissionEntity;

/**
 * 角色权限关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermissionEntity> {
}
