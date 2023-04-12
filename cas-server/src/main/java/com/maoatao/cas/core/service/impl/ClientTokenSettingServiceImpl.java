package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientTokenSettingEntity;
import com.maoatao.cas.core.bean.param.clienttokensetting.ClientTokenSettingQueryParam;
import com.maoatao.cas.core.bean.vo.ClientTokenSettingVO;
import com.maoatao.cas.core.mapper.ClientTokenSettingMapper;
import com.maoatao.cas.core.service.ClientTokenSettingService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import org.springframework.stereotype.Service;

/**
 * CAS 客户端令牌设置
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
@Service
public class ClientTokenSettingServiceImpl extends DaedalusServiceImpl<ClientTokenSettingMapper, ClientTokenSettingEntity> implements ClientTokenSettingService {

    @Override
    public Page<ClientTokenSettingVO> page(ClientTokenSettingQueryParam param) {
        ClientTokenSettingEntity entity = BeanUtil.copyProperties(param, ClientTokenSettingEntity.class);
        Page<ClientTokenSettingEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, ClientTokenSettingVO.class);
    }

    @Override
    public ClientTokenSettingVO details(Long id) {
        return BeanUtil.toBean(super.getById(id), ClientTokenSettingVO.class);
    }

    @Override
    public ClientTokenSettingEntity getByClientId(String clientId) {
        return super.getOne(Wrappers.<ClientTokenSettingEntity>lambdaQuery().eq(ClientTokenSettingEntity::getClientId, clientId));
    }
}
