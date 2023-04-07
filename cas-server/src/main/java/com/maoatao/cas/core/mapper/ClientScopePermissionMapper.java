package com.maoatao.cas.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maoatao.cas.core.bean.entity.ClientScopePermissionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * CAS 角色权限关系
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
@Mapper
public interface ClientScopePermissionMapper extends BaseMapper<ClientScopePermissionEntity> {}
