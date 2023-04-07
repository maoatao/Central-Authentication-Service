package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientAuthenticationMethodEntity;
import com.maoatao.cas.core.bean.param.clientauthenticationmethod.ClientAuthenticationMethodQueryParam;
import com.maoatao.cas.core.bean.vo.ClientAuthenticationMethodVO;
import com.maoatao.daedalus.data.service.DaedalusService;

/**
 * CAS 客户端身份验证方法
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
public interface ClientAuthenticationMethodService extends DaedalusService<ClientAuthenticationMethodEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<ClientAuthenticationMethodVO> page(ClientAuthenticationMethodQueryParam param);

    /**
     * 通过id查询
     *
     * @param id CAS 客户端身份验证方法id
     * @return CAS 客户端身份验证方法
     */
    ClientAuthenticationMethodVO details(Long id);
}
