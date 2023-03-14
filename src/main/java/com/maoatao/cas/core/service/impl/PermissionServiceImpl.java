package com.maoatao.cas.core.service.impl;

import com.maoatao.cas.core.entity.PermissionEntity;
import com.maoatao.cas.core.mapper.PermissionMapper;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Service
public class PermissionServiceImpl extends DaedalusServiceImpl<PermissionMapper, PermissionEntity> implements PermissionService {

    @Override
    public List<PermissionEntity> listByUser(Long userId) {
        return getBaseMapper().getPermissionByUser(userId);
    }
}
