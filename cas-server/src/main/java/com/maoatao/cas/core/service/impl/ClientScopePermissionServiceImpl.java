package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientScopePermissionEntity;
import com.maoatao.cas.core.bean.param.clientscopepermission.ClientScopePermissionQueryParam;
import com.maoatao.cas.core.bean.vo.ClientScopePermissionVO;
import com.maoatao.cas.core.mapper.ClientScopePermissionMapper;
import com.maoatao.cas.core.service.ClientScopePermissionService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import org.springframework.stereotype.Service;

/**
 * CAS 角色权限关系
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
@Service
public class ClientScopePermissionServiceImpl extends DaedalusServiceImpl<ClientScopePermissionMapper, ClientScopePermissionEntity> implements ClientScopePermissionService {
    
    @Override
    public Page<ClientScopePermissionVO> page(ClientScopePermissionQueryParam param) {
        ClientScopePermissionEntity entity = BeanUtil.copyProperties(param, ClientScopePermissionEntity.class);
        Page<ClientScopePermissionEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, ClientScopePermissionVO.class);
    }

    @Override
    public ClientScopePermissionVO details(Long id){
        return BeanUtil.toBean(super.getById(id), ClientScopePermissionVO.class);
    }
}
