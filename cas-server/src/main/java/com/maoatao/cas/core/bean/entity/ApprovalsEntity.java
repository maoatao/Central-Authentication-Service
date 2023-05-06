package com.maoatao.cas.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maoatao.daedalus.data.bean.base.BaseEntity;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * CAS 授权批准
 *
 * @author MaoAtao
 * @date 2023-05-06 09:53:13
 */
@Data
@Builder
@TableName("t_cas_approvals")
@EqualsAndHashCode(callSuper = true)
public class ApprovalsEntity extends BaseEntity<ApprovalsEntity> {

    @Serial
    private static final long serialVersionUID = 7897502233899280134L;

    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 到期时间
     */
    private LocalDateTime expiresDate;

    @Tolerate
    public ApprovalsEntity() {}
}
