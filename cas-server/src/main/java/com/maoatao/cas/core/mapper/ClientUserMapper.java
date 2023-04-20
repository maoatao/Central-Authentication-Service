package com.maoatao.cas.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maoatao.cas.core.bean.entity.ClientUserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户
 *
 * @author MaoAtao
 * @date 2022-03-12 20:11:12
 */
@Mapper
public interface ClientUserMapper extends BaseMapper<ClientUserEntity> {
}
