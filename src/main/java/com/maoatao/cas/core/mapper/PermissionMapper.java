package com.maoatao.cas.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.maoatao.cas.core.entity.PermissionEntity;

import java.util.List;

/**
 * 权限
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:23
 */
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionEntity> {

    /**
     * 获取权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<PermissionEntity> getPermissionByUser(Long userId);
}
