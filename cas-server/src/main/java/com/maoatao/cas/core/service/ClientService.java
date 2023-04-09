package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientEntity;
import com.maoatao.cas.core.bean.param.client.ClientQueryParam;
import com.maoatao.cas.core.bean.vo.ClientVO;
import com.maoatao.daedalus.data.service.DaedalusService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * CAS 客户端
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
public interface ClientService extends DaedalusService<ClientEntity>, RegisteredClientRepository {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<ClientVO> page(ClientQueryParam param);

    /**
     * 通过id查询
     *
     * @param id CAS 客户端id
     * @return CAS 客户端
     */
    ClientVO details(Long id);
}
