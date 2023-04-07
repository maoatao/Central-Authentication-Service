package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientEntity;
import com.maoatao.cas.core.bean.param.client.ClientQueryParam;
import com.maoatao.cas.core.bean.vo.ClientVO;
import com.maoatao.cas.core.mapper.ClientMapper;
import com.maoatao.cas.core.service.ClientService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import org.springframework.stereotype.Service;

/**
 * CAS 客户端
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
@Service
public class ClientServiceImpl extends DaedalusServiceImpl<ClientMapper, ClientEntity> implements ClientService {
    
    @Override
    public Page<ClientVO> page(ClientQueryParam param) {
        ClientEntity entity = BeanUtil.copyProperties(param, ClientEntity.class);
        Page<ClientEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, ClientVO.class);
    }

    @Override
    public ClientVO details(Long id){
        return BeanUtil.toBean(super.getById(id), ClientVO.class);
    }
}
