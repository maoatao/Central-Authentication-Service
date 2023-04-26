package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.UserEntity;
import com.maoatao.cas.core.bean.param.user.UserQueryParam;
import com.maoatao.cas.core.bean.param.user.UserSaveParam;
import com.maoatao.cas.core.bean.vo.UserVO;
import com.maoatao.cas.core.mapper.UserMapper;
import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.util.IdUtils;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import com.maoatao.synapse.lang.util.SynaAssert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CAS 用户
 *
 * @author MaoAtao
 * @date 2023-04-21 16:09:13
 */
@Service
public class UserServiceImpl extends DaedalusServiceImpl<UserMapper, UserEntity> implements UserService {

    @Override
    public Page<UserVO> page(UserQueryParam param) {
        UserEntity entity = BeanUtil.copyProperties(param, UserEntity.class);
        Page<UserEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, UserVO.class);
    }

    @Override
    public UserVO details(Long id) {
        return BeanUtil.toBean(super.getById(id), UserVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long save(UserSaveParam param) {
        UserEntity userEntity = BeanUtil.copyProperties(param, UserEntity.class);
        userEntity.setOpenId(IdUtils.nextUserOpenId());
        SynaAssert.isTrue(super.save(userEntity), "新增用户失败!");
        return userEntity.getId();
    }

    @Override
    public UserEntity getByOpenId(String openId) {
        return super.getOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getOpenId, openId));
    }
}
