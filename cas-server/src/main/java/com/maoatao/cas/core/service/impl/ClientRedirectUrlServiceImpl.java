package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientRedirectUrlEntity;
import com.maoatao.cas.core.bean.param.clientredirecturl.ClientRedirectUrlQueryParam;
import com.maoatao.cas.core.bean.vo.ClientRedirectUrlVO;
import com.maoatao.cas.core.mapper.ClientRedirectUrlMapper;
import com.maoatao.cas.core.service.ClientRedirectUrlService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import org.springframework.stereotype.Service;

/**
 * CAS 客户端重定向地址
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
@Service
public class ClientRedirectUrlServiceImpl extends DaedalusServiceImpl<ClientRedirectUrlMapper, ClientRedirectUrlEntity> implements ClientRedirectUrlService {
    
    @Override
    public Page<ClientRedirectUrlVO> page(ClientRedirectUrlQueryParam param) {
        ClientRedirectUrlEntity entity = BeanUtil.copyProperties(param, ClientRedirectUrlEntity.class);
        Page<ClientRedirectUrlEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, ClientRedirectUrlVO.class);
    }

    @Override
    public ClientRedirectUrlVO details(Long id){
        return BeanUtil.toBean(super.getById(id), ClientRedirectUrlVO.class);
    }
}
