package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ApprovalsScopeEntity;
import com.maoatao.cas.core.bean.param.approvalsscope.ApprovalsScopeQueryParam;
import com.maoatao.cas.core.bean.vo.ApprovalsScopeVO;
import com.maoatao.cas.core.mapper.ApprovalsScopeMapper;
import com.maoatao.cas.core.service.ApprovalsScopeService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * CAS 批准作用域
 *
 * @author MaoAtao
 * @date 2023-05-06 09:53:12
 */
@Service
public class ApprovalsScopeServiceImpl extends DaedalusServiceImpl<ApprovalsScopeMapper, ApprovalsScopeEntity> implements ApprovalsScopeService {

    @Override
    public Page<ApprovalsScopeVO> page(ApprovalsScopeQueryParam param) {
        ApprovalsScopeEntity entity = BeanUtil.copyProperties(param, ApprovalsScopeEntity.class);
        Page<ApprovalsScopeEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, ApprovalsScopeVO.class);
    }

    @Override
    public ApprovalsScopeVO details(Long id) {
        return BeanUtil.toBean(super.getById(id), ApprovalsScopeVO.class);
    }

    @Override
    public List<ApprovalsScopeEntity> listByApprovalId(Long approvalId) {
        return list(Wrappers.<ApprovalsScopeEntity>lambdaQuery().eq(ApprovalsScopeEntity::getApprovalsId, approvalId));
    }

    @Override
    public boolean removeByApprovalId(Long approvalId) {
        return remove(Wrappers.<ApprovalsScopeEntity>lambdaQuery().eq(ApprovalsScopeEntity::getApprovalsId, approvalId));
    }
}
