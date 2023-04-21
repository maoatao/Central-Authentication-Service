package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.UserEntity;
import com.maoatao.cas.core.bean.param.user.UserQueryParam;
import com.maoatao.cas.core.bean.param.user.UserSaveParam;
import com.maoatao.cas.core.bean.vo.UserVO;
import com.maoatao.daedalus.data.service.DaedalusService;

/**
 * CAS 用户
 *
 * @author MaoAtao
 * @date 2023-04-21 16:09:13
 */
public interface UserService extends DaedalusService<UserEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<UserVO> page(UserQueryParam param);

    /**
     * 通过id查询
     *
     * @param id CAS 用户id
     * @return CAS 用户
     */
    UserVO details(Long id);

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回主键 id
     */
    long save(UserSaveParam param);
}
