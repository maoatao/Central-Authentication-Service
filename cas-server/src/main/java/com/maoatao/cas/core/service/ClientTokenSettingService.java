package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientTokenSettingEntity;
import com.maoatao.cas.core.bean.param.clienttokensetting.ClientTokenSettingQueryParam;
import com.maoatao.cas.core.bean.vo.ClientTokenSettingVO;
import com.maoatao.daedalus.data.service.DaedalusService;

/**
 * CAS 客户端令牌设置
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
public interface ClientTokenSettingService extends DaedalusService<ClientTokenSettingEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<ClientTokenSettingVO> page(ClientTokenSettingQueryParam param);

    /**
     * 通过id查询
     *
     * @param id CAS 客户端令牌设置id
     * @return CAS 客户端令牌设置
     */
    ClientTokenSettingVO details(Long id);
}
