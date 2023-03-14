package com.maoatao.cas.core.service.impl;

import com.maoatao.cas.core.entity.RolePermissionEntity;
import com.maoatao.cas.core.mapper.RolePermissionMapper;
import com.maoatao.cas.core.service.RolePermissionService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 角色权限关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Service
public class RolePermissionServiceImpl extends DaedalusServiceImpl<RolePermissionMapper, RolePermissionEntity> implements RolePermissionService {}
