package com.maoatao.cas.core.controller;

import com.maoatao.cas.security.service.CasAuthorizationService;
import com.maoatao.cas.core.bean.param.authorization.GenerateAuthorizationCodeParam;
import com.maoatao.daedalus.web.annotation.ResponseHandle;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CAS 授权码管理
 *
 * @author MaoAtao
 * @date 2022-10-23 17:18:43
 */
@ResponseHandle
@RestController
@RequestMapping("/code")
@Tag(name = "AuthorizationCodeController", description = "授权码管理")
public class AuthorizationCodeController {

    @Autowired
    private CasAuthorizationService casAuthorizationService;

    /**
     * 生成授权码
     *
     * @param param 参数
     * @return 授权码
     */
    @PostMapping
    @Operation(summary = "generateAuthorizationCode", description = "生成授权码")
    public String generateAuthorizationCode(@RequestBody GenerateAuthorizationCodeParam param) {
        return casAuthorizationService.generateAuthorizationCode(param);
    }
}
