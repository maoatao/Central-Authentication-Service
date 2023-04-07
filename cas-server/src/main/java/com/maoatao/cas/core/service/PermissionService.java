package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.param.permission.PermissionQueryParam;
import com.maoatao.cas.core.bean.vo.PermissionVO;
import com.maoatao.cas.core.bean.entity.PermissionEntity;
import com.maoatao.daedalus.data.service.DaedalusService;

import java.util.List;

/**
 * 权限
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
public interface PermissionService extends DaedalusService<PermissionEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<PermissionVO> page(PermissionQueryParam param);

    /**
     * 通过id查询
     *
     * @param id CAS 权限id
     * @return CAS 权限
     */
    PermissionVO details(Long id);

    /**
     * 通过用户id查询列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<PermissionEntity> listByUser(Long userId);
}
