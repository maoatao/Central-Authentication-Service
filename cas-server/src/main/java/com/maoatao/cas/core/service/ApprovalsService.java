package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ApprovalsEntity;
import com.maoatao.cas.core.bean.param.approvals.ApprovalsQueryParam;
import com.maoatao.cas.core.bean.vo.ApprovalsVO;
import com.maoatao.daedalus.data.service.DaedalusService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;

/**
 * CAS 授权批准
 *
 * @author MaoAtao
 * @date 2023-05-06 09:53:13
 */
public interface ApprovalsService extends DaedalusService<ApprovalsEntity>, OAuth2AuthorizationConsentService {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<ApprovalsVO> page(ApprovalsQueryParam param);

    /**
     * 通过id查询
     *
     * @param id CAS 授权批准id
     * @return CAS 授权批准
     */
    ApprovalsVO details(Long id);
}
