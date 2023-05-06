package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ApprovalsScopeEntity;
import com.maoatao.cas.core.bean.param.approvalsscope.ApprovalsScopeQueryParam;
import com.maoatao.cas.core.bean.vo.ApprovalsScopeVO;
import com.maoatao.daedalus.data.service.DaedalusService;
import java.util.List;

/**
 * CAS 批准作用域
 *
 * @author MaoAtao
 * @date 2023-05-06 09:53:12
 */
public interface ApprovalsScopeService extends DaedalusService<ApprovalsScopeEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<ApprovalsScopeVO> page(ApprovalsScopeQueryParam param);

    /**
     * 通过id查询
     *
     * @param id CAS 批准作用域id
     * @return CAS 批准作用域
     */
    ApprovalsScopeVO details(Long id);

    /**
     * 通过批准id查询批准作用域
     *
     * @param approvalId 批准id
     * @return 批准作用域
     */
    List<ApprovalsScopeEntity> listApprovalId(Long approvalId);

    /**
     * 通过批准id删除批准作用域
     *
     * @param approvalId 批准id
     * @return 操作成功返回true
     */
    boolean removeByApprovalId(Long approvalId);
}
