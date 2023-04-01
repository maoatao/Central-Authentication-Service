package com.maoatao.cas.sample.client.http.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maoatao.cas.common.authentication.CasAuthorization;
import com.maoatao.cas.common.authentication.DefaultAuthorization;
import com.maoatao.cas.openapi.authentication.CasAuthorizationService;
import com.maoatao.synapse.lang.exception.SynaException;
import com.maoatao.synapse.web.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CAS授权服务
 *
 * @author MaoAtao
 * @date 2023-04-01 15:27:32
 */
@Service
public class CasAuthorizationServiceImpl implements CasAuthorizationService {

    private static final String CAS_TOKEN_URL = "localhost:18080/token";

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public CasAuthorization getAuthorization(String token) {
        RestResponse<DefaultAuthorization> response;
        try (HttpResponse httpResponse = HttpRequest.get(CAS_TOKEN_URL).bearerAuth(token).timeout(5000).execute()) {
            response = objectMapper.readValue(httpResponse.body(), new TypeReference<>() {});
        } catch (Exception e) {
            throw new SynaException("获取权限信息失败!", e);
        }
        return response.getData();
    }
}
