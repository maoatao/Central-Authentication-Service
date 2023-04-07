package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientGrantTypeEntity;
import com.maoatao.cas.core.bean.param.clientgranttype.ClientGrantTypeQueryParam;
import com.maoatao.cas.core.bean.vo.ClientGrantTypeVO;
import com.maoatao.cas.core.mapper.ClientGrantTypeMapper;
import com.maoatao.cas.core.service.ClientGrantTypeService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import org.springframework.stereotype.Service;

/**
 * CAS 客户端授权类型
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
@Service
public class ClientGrantTypeServiceImpl extends DaedalusServiceImpl<ClientGrantTypeMapper, ClientGrantTypeEntity> implements ClientGrantTypeService {
    
    @Override
    public Page<ClientGrantTypeVO> page(ClientGrantTypeQueryParam param) {
        ClientGrantTypeEntity entity = BeanUtil.copyProperties(param, ClientGrantTypeEntity.class);
        Page<ClientGrantTypeEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, ClientGrantTypeVO.class);
    }

    @Override
    public ClientGrantTypeVO details(Long id){
        return BeanUtil.toBean(super.getById(id), ClientGrantTypeVO.class);
    }
}
