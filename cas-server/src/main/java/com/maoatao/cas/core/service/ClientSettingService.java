package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientSettingEntity;
import com.maoatao.cas.core.bean.param.clientsetting.ClientSettingQueryParam;
import com.maoatao.cas.core.bean.vo.ClientSettingVO;
import com.maoatao.daedalus.data.service.DaedalusService;

/**
 * CAS 客户端设置
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
public interface ClientSettingService extends DaedalusService<ClientSettingEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<ClientSettingVO> page(ClientSettingQueryParam param);

    /**
     * 通过id查询
     *
     * @param id CAS 客户端设置id
     * @return CAS 客户端设置
     */
    ClientSettingVO details(Long id);

    /**
     * 通过id查询
     *
     * @param clientId CAS 客户端id
     * @return CAS 客户端设置
     */
    ClientSettingEntity getByClientId(String clientId);
}
