package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.param.clientuserrole.ClientUserRoleQueryParam;
import com.maoatao.cas.core.bean.vo.UserRoleVO;
import com.maoatao.cas.core.bean.entity.ClientUserRoleEntity;
import com.maoatao.daedalus.data.service.DaedalusService;

import java.util.List;

/**
 * 用户角色关系
 *
 * @author MaoAtao
 * @date 2022-12-12 14:18:22
 */
public interface ClientUserRoleService extends DaedalusService<ClientUserRoleEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<UserRoleVO> page(ClientUserRoleQueryParam param);

    /**
     * 通过id查询
     *
     * @param id CAS 客户端用户角色关系id
     * @return CAS 客户端用户角色关系
     */
    UserRoleVO details(Long id);

    /**
     * 更新用户角色
     * <p>
     * 以入参角色 id 为准,如果传入空表示删除该用户的所有角色
     *
     * @param roleIds 角色 id 集合
     * @param userId  用户 id (未作存在校验,使用前请确认是否存在)
     * @return 更新成功返回 true
     */
    boolean updateUserRole(List<Long> roleIds, long userId);
}
