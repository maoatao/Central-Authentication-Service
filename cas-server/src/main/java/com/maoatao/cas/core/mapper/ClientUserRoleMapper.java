package com.maoatao.cas.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.maoatao.cas.core.bean.entity.ClientUserRoleEntity;

/**
 * 用户角色关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
@Mapper
public interface ClientUserRoleMapper extends BaseMapper<ClientUserRoleEntity> {
}
