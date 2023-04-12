package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientScopeEntity;
import com.maoatao.cas.core.bean.param.clientscope.ClientScopeQueryParam;
import com.maoatao.cas.core.bean.vo.ClientScopeVO;
import com.maoatao.daedalus.data.service.DaedalusService;
import java.util.List;

/**
 * CAS 客户端作用域
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
public interface ClientScopeService extends DaedalusService<ClientScopeEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<ClientScopeVO> page(ClientScopeQueryParam param);

    /**
     * 通过id查询
     *
     * @param id CAS 客户端作用域id
     * @return CAS 客户端作用域
     */
    ClientScopeVO details(Long id);

    /**
     * 通过id查询
     *
     * @param clientId CAS 客户端id
     * @return CAS 客户端作用域
     */
    List<ClientScopeEntity> listByClientId(String clientId);
}
