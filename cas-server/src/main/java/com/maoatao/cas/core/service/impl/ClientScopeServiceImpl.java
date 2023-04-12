package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientScopeEntity;
import com.maoatao.cas.core.bean.param.clientscope.ClientScopeQueryParam;
import com.maoatao.cas.core.bean.vo.ClientScopeVO;
import com.maoatao.cas.core.mapper.ClientScopeMapper;
import com.maoatao.cas.core.service.ClientScopeService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * CAS 客户端作用域
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
@Service
public class ClientScopeServiceImpl extends DaedalusServiceImpl<ClientScopeMapper, ClientScopeEntity> implements ClientScopeService {

    @Override
    public Page<ClientScopeVO> page(ClientScopeQueryParam param) {
        ClientScopeEntity entity = BeanUtil.copyProperties(param, ClientScopeEntity.class);
        Page<ClientScopeEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, ClientScopeVO.class);
    }

    @Override
    public ClientScopeVO details(Long id) {
        return BeanUtil.toBean(super.getById(id), ClientScopeVO.class);
    }

    @Override
    public List<ClientScopeEntity> listByClientId(String clientId) {
        return super.list(Wrappers.<ClientScopeEntity>lambdaQuery().eq(ClientScopeEntity::getClientId, clientId));
    }
}
