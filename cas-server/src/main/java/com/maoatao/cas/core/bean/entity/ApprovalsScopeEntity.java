package com.maoatao.cas.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.base.BaseEntity;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;

/**
 * CAS 批准作用域
 *
 * @author MaoAtao
 * @date 2023-05-06 09:53:12
 */
@Data
@Builder
@TableName("t_cas_approvals_scope")
@EqualsAndHashCode(callSuper = true)
public class ApprovalsScopeEntity extends BaseEntity<ApprovalsScopeEntity> {

    @Serial
    private static final long serialVersionUID = -5275047390537037687L;

    /**
     * 审批id
     */
    private Long approvalsId;
    /**
     * 作用域
     */
    private String scope;

    @Tolerate
    public ApprovalsScopeEntity() {}
}
