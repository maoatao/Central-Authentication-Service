package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientSettingEntity;
import com.maoatao.cas.core.bean.param.clientsetting.ClientSettingQueryParam;
import com.maoatao.cas.core.bean.vo.ClientSettingVO;
import com.maoatao.cas.core.mapper.ClientSettingMapper;
import com.maoatao.cas.core.service.ClientSettingService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import org.springframework.stereotype.Service;

/**
 * CAS 客户端设置
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
@Service
public class ClientSettingServiceImpl extends DaedalusServiceImpl<ClientSettingMapper, ClientSettingEntity> implements ClientSettingService {

    @Override
    public Page<ClientSettingVO> page(ClientSettingQueryParam param) {
        ClientSettingEntity entity = BeanUtil.copyProperties(param, ClientSettingEntity.class);
        Page<ClientSettingEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, ClientSettingVO.class);
    }

    @Override
    public ClientSettingVO details(Long id) {
        return BeanUtil.toBean(super.getById(id), ClientSettingVO.class);
    }

    @Override
    public ClientSettingEntity getByClientId(String clientId) {
        return super.getOne(Wrappers.<ClientSettingEntity>lambdaQuery().eq(ClientSettingEntity::getClientId, clientId));
    }
}
