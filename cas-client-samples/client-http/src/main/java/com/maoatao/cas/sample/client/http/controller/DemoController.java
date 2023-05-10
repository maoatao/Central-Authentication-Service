package com.maoatao.cas.sample.client.http.controller;

import com.maoatao.cas.common.annotation.CasAuth;
import com.maoatao.daedalus.web.annotation.ResponseHandle;
import com.maoatao.synapse.lang.util.SynaDates;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示
 *
 * @author MaoAtao
 * @date 2023-03-30 15:07:16
 */
@ResponseHandle
@RestController
@RequestMapping(RequestPath.CAS_CORE + "/demo")
@Tag(name = "DemoController", description = "演示")
public class DemoController {

    @GetMapping
    @CasAuth("PERMISSION_TEST")
    @Operation(summary = "demo1", description = "demo1")
    public String demo1() {
        return "NOW DATE: ".concat(SynaDates.now(SynaDates.DateType.STRING_DATE_TIME));
    }
}
