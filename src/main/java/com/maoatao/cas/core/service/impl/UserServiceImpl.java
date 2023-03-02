package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maoatao.cas.core.entity.UserEntity;
import com.maoatao.cas.core.mapper.UserMapper;
import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.web.param.UserParam;
import com.maoatao.synapse.core.util.SynaAssert;
import org.springframework.stereotype.Service;

/**
 * 用户
 *
 * @author: MaoAtao
 * @create: 2022-03-11 16:13:35
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Override
    public Page<UserEntity> getPage(UserParam param) {
        return page(new Page<>(param.getPageNo(), param.getPageSize()), Wrappers.query(BeanUtil.copyProperties(param, UserEntity.class)));
    }

    @Override
    public UserEntity getByNameAndClient(String name, String clientId) {
        SynaAssert.isTrue(StrUtil.isNotBlank(name), "用户名称不能为空!");
        SynaAssert.isTrue(StrUtil.isNotBlank(clientId), "客户端 Id 不能为空!");
        return getOne(Wrappers.query(UserEntity.builder().name(name).clientId(clientId).build()));
    }

    @Override
    public boolean save(UserParam param) {
        param.setId(null);
        // TODO: 2023-03-01 22:35:47 设置openId
        return save(BeanUtil.copyProperties(param, UserEntity.class));
    }

    @Override
    public boolean update(UserParam param) {
        checkExisted(param.getId());
        return updateById(BeanUtil.copyProperties(param, UserEntity.class));
    }

    @Override
    public boolean remove(Long id) {
        checkExisted(id);
        return removeById(id);
    }

    private void checkExisted(Long id) {
        SynaAssert.notNull(getById(id), "{}号用户不存在!");
    }
}
