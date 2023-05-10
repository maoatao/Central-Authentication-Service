package com.maoatao.cas.openapi.converter;

import com.maoatao.cas.common.authentication.CasAuthorization;
import com.maoatao.cas.openapi.authentication.CasAuthorizationService;
import com.maoatao.daedalus.core.context.DaedalusOperatorContext;
import com.maoatao.daedalus.core.context.DefalutOperatorContext;
import com.maoatao.synapse.lang.util.SynaAssert;
import com.maoatao.synapse.lang.util.SynaSafes;

/**
 * 默认 上下文转换者
 *
 * @author MaoAtao
 * @date 2023-03-26 13:56:18
 */
public class DefaultOperatorContextConverter implements ContextConverter {

    private final String appKey;

    private final CasAuthorizationService casAuthorizationService;

    public DefaultOperatorContextConverter(String appKey,
                                           CasAuthorizationService casAuthorizationService) {
        SynaAssert.notNull(appKey, "appKey cannot be null");
        SynaAssert.notNull(casAuthorizationService, "casAuthorizationService cannot be null");
        // 权限支持多客户端,通过设置 appKey (客户端id)来筛选本客户端的权限
        this.appKey = appKey;
        this.casAuthorizationService = casAuthorizationService;
    }

    @Override
    public DaedalusOperatorContext convert(String token) {
        DaedalusOperatorContext operatorContext;
        try {
            CasAuthorization authorization = this.casAuthorizationService.getAuthorization(token);
            operatorContext = DefalutOperatorContext.builder()
                    .operatorId(authorization.getOpenId())
                    .operatorName(authorization.getUser())
                    .permissions(SynaSafes.of(SynaSafes.of(authorization.getPermissions()).get(this.appKey)))
                    .expiresAt(authorization.getExpiresAt())
                    .clientCredentials(authorization.isClientCredentials())
                    .build();
        } catch (Exception e) {
            return null;
        }
        return operatorContext;
    }
}
