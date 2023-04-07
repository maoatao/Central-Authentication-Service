package com.maoatao.cas.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maoatao.cas.core.bean.entity.ClientTokenSettingEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * CAS 客户端令牌设置
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
@Mapper
public interface ClientTokenSettingMapper extends BaseMapper<ClientTokenSettingEntity> {}
