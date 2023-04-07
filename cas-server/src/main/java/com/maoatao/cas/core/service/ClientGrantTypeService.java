package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientGrantTypeEntity;
import com.maoatao.cas.core.bean.param.clientgranttype.ClientGrantTypeQueryParam;
import com.maoatao.cas.core.bean.vo.ClientGrantTypeVO;
import com.maoatao.daedalus.data.service.DaedalusService;

/**
 * CAS 客户端授权类型
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
public interface ClientGrantTypeService extends DaedalusService<ClientGrantTypeEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<ClientGrantTypeVO> page(ClientGrantTypeQueryParam param);

    /**
     * 通过id查询
     *
     * @param id CAS 客户端授权类型id
     * @return CAS 客户端授权类型
     */
    ClientGrantTypeVO details(Long id);
}
