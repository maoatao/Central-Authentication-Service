package com.maoatao.cas.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maoatao.cas.core.bean.entity.RoleEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleEntity> {

    /**
     * 查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleEntity> listByClientUser(Long userId);
}
