package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.param.rolepermission.RolePermissionQueryParam;
import com.maoatao.cas.core.bean.vo.RolePermissionVO;
import com.maoatao.cas.core.bean.entity.RolePermissionEntity;
import com.maoatao.daedalus.data.service.DaedalusService;

/**
 * 角色权限关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
public interface RolePermissionService extends DaedalusService<RolePermissionEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<RolePermissionVO> page(RolePermissionQueryParam param);

    /**
     * 通过id查询
     *
     * @param id CAS 角色权限关系id
     * @return CAS 角色权限关系
     */
    RolePermissionVO details(Long id);
}
