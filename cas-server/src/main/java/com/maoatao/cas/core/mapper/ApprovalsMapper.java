package com.maoatao.cas.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.maoatao.cas.core.bean.entity.ApprovalsEntity;

/**
 * CAS 授权批准
 *
 * @author MaoAtao
 * @date 2023-05-06 09:53:13
 */
@Mapper
public interface ApprovalsMapper extends BaseMapper<ApprovalsEntity> {}
