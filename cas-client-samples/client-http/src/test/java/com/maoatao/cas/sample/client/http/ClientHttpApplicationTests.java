package com.maoatao.cas.sample.client.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maoatao.cas.common.authentication.CasAuthorization;
import com.maoatao.cas.common.authentication.DefaultAuthorization;
import com.maoatao.daedalus.web.response.RestResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClientHttpApplicationTests {

    private static final String CAS_TOKEN_URL = "localhost:18080/token";
    private static final String CAS_BEARER_TOKEN = "bf030606-31a9-4cba-b9e9-823bc6098a71";

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void contextLoads() {
        try (HttpResponse httpResponse = HttpRequest.get(CAS_TOKEN_URL).bearerAuth(CAS_BEARER_TOKEN).execute()) {
            RestResponse<DefaultAuthorization> response = objectMapper.readValue(httpResponse.body(), new TypeReference<>() {});
            CasAuthorization authorization = response.getData();
            System.out.println(authorization);
        }
    }
}
