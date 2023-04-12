package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientAuthenticationMethodEntity;
import com.maoatao.cas.core.bean.param.clientauthenticationmethod.ClientAuthenticationMethodQueryParam;
import com.maoatao.cas.core.bean.vo.ClientAuthenticationMethodVO;
import com.maoatao.cas.core.mapper.ClientAuthenticationMethodMapper;
import com.maoatao.cas.core.service.ClientAuthenticationMethodService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * CAS 客户端身份验证方法
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
@Service
public class ClientAuthenticationMethodServiceImpl extends DaedalusServiceImpl<ClientAuthenticationMethodMapper, ClientAuthenticationMethodEntity> implements ClientAuthenticationMethodService {

    @Override
    public Page<ClientAuthenticationMethodVO> page(ClientAuthenticationMethodQueryParam param) {
        ClientAuthenticationMethodEntity entity = BeanUtil.copyProperties(param, ClientAuthenticationMethodEntity.class);
        Page<ClientAuthenticationMethodEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, ClientAuthenticationMethodVO.class);
    }

    @Override
    public ClientAuthenticationMethodVO details(Long id) {
        return BeanUtil.toBean(super.getById(id), ClientAuthenticationMethodVO.class);
    }

    @Override
    public List<ClientAuthenticationMethodEntity> listByClientId(String clientId) {
        return super.list(Wrappers.<ClientAuthenticationMethodEntity>lambdaQuery().eq(ClientAuthenticationMethodEntity::getClientId, clientId));
    }
}
