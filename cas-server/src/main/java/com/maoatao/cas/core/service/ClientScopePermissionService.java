package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientScopePermissionEntity;
import com.maoatao.cas.core.bean.param.clientscopepermission.ClientScopePermissionQueryParam;
import com.maoatao.cas.core.bean.vo.ClientScopePermissionVO;
import com.maoatao.daedalus.data.service.DaedalusService;

/**
 * CAS 角色权限关系
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
public interface ClientScopePermissionService extends DaedalusService<ClientScopePermissionEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<ClientScopePermissionVO> page(ClientScopePermissionQueryParam param);

    /**
     * 通过id查询
     *
     * @param id CAS 角色权限关系id
     * @return CAS 角色权限关系
     */
    ClientScopePermissionVO details(Long id);
}
