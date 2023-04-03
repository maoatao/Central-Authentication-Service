package com.maoatao.cas.openapi.converter;

import cn.hutool.jwt.JWTUtil;
import com.maoatao.cas.common.authentication.CasJwt;
import com.maoatao.cas.openapi.context.JwtOperatorContext;
import com.maoatao.daedalus.core.context.DaedalusOperatorContext;
import com.maoatao.synapse.lang.util.SynaAssert;
import com.maoatao.synapse.lang.util.SynaDates;

/**
 * Jwt 上下文转换者
 *
 * @author MaoAtao
 * @date 2023-03-26 13:31:27
 */
public class JwtOperatorContextConverter implements ContextConverter {
    @Override
    public DaedalusOperatorContext convert(String token) {
        SynaAssert.notNull(token, "token为空,不能转换为Jwt上下文!");
        DaedalusOperatorContext operatorContext;
        try {
            CasJwt jwt = JWTUtil.parseToken(token).getPayloads().toBean(CasJwt.class);
            operatorContext = JwtOperatorContext.builder()
                    .operatorId(jwt.getOpenId())
                    .operatorName(jwt.getSub())
                    .clientId(jwt.getAud())
                    .roles(jwt.getRoles())
                    .permissions(jwt.getPermissions())
                    .expiresAt(SynaDates.of(1000L * Long.parseLong(jwt.getExp())))
                    .issuedAt(SynaDates.of(1000L * Long.parseLong(jwt.getIat())))
                    .issuer(jwt.getIss())
                    .clientCredentials(jwt.isClientCredentials())
                    .build();
        } catch (Exception e) {
            return null;
        }
        return operatorContext;
    }
}
