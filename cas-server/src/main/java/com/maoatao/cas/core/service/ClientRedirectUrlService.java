package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientRedirectUrlEntity;
import com.maoatao.cas.core.bean.param.clientredirecturl.ClientRedirectUrlQueryParam;
import com.maoatao.cas.core.bean.vo.ClientRedirectUrlVO;
import com.maoatao.daedalus.data.service.DaedalusService;

/**
 * CAS 客户端重定向地址
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
public interface ClientRedirectUrlService extends DaedalusService<ClientRedirectUrlEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<ClientRedirectUrlVO> page(ClientRedirectUrlQueryParam param);

    /**
     * 通过id查询
     *
     * @param id CAS 客户端重定向地址id
     * @return CAS 客户端重定向地址
     */
    ClientRedirectUrlVO details(Long id);
}
