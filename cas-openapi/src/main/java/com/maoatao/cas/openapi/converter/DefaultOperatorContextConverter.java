package com.maoatao.cas.openapi.converter;

import com.maoatao.cas.common.bean.CasAuthorization;
import com.maoatao.cas.openapi.authentication.DaedalusTokenService;
import com.maoatao.daedalus.core.context.DaedalusOperatorContext;
import com.maoatao.daedalus.core.context.DefalutOperatorContext;
import com.maoatao.synapse.lang.util.SynaAssert;

/**
 * 默认 上下文转换者
 *
 * @author MaoAtao
 * @date 2023-03-26 13:56:18
 */
public class DefaultOperatorContextConverter implements ContextConverter {

    private final DaedalusTokenService daedalusTokenService;

    public DefaultOperatorContextConverter(DaedalusTokenService daedalusTokenService) {
        SynaAssert.notNull(daedalusTokenService, "daedalusTokenService cannot be null");
        this.daedalusTokenService = daedalusTokenService;
    }

    @Override
    public DaedalusOperatorContext convert(String token) {
        DaedalusOperatorContext operatorContext;
        try {
            CasAuthorization authorization = getDaedalusTokenService().getAuthorization(token);
            operatorContext = DefalutOperatorContext.builder()
                    .operatorId(authorization.getOpenId())
                    .operatorName(authorization.getUser())
                    .clientId(authorization.getClientId())
                    .roles(authorization.getRoles())
                    .permissions(authorization.getPermissions())
                    .expiresAt(authorization.getExpiresAt())
                    .build();
        } catch (Exception e) {
            return null;
        }
        return operatorContext;
    }

    public DaedalusTokenService getDaedalusTokenService() {
        return this.daedalusTokenService;
    }
}
