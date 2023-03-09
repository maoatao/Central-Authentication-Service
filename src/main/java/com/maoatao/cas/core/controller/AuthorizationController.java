package com.maoatao.cas.core.controller;

import com.maoatao.cas.core.param.GenerateAccessTokenParam;
import com.maoatao.cas.core.service.AuthorizationService;
import com.maoatao.cas.core.param.GenerateAuthorizationCodeParam;
import com.maoatao.cas.security.bean.AuthorizationInfo;
import com.maoatao.cas.security.bean.CustomAccessToken;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 授权管理
 *
 * @author MaoAtao
 * @date 2022-10-23 17:17:17
 */
@RestController
@RequestMapping("/authorization")
@Tag(name = "AuthorizationController", description = "授权管理")
public class AuthorizationController {

    @Autowired
    private AuthorizationService authorizationService;

    /**
     * 生成授权码
     *
     * @param param 参数
     * @return 授权码
     */
    @PostMapping("/code")
    @Operation(summary = "generateAuthorizationCode", description = "生成授权码")
    public String generateAuthorizationCode(@RequestBody GenerateAuthorizationCodeParam param) {
        return authorizationService.generateAuthorizationCode(param);
    }

    /**
     * 生成令牌
     *
     * @param param 参数
     * @return 访问令牌
     */
    @PostMapping("/token")
    @Operation(summary = "generateAccessToken", description = "生成令牌")
    public CustomAccessToken generateAccessToken(@RequestBody GenerateAccessTokenParam param) {
        return authorizationService.generateAccessToken(param);
    }

    /**
     * 吊销token
     *
     * @param accessToken 令牌
     * @return true -> 吊销token成功,false -> 没有该token,吊销失败
     */
    @DeleteMapping("/token")
    @Operation(summary = "revokeAccessToken", description = "吊销令牌")
    public boolean revokeAccessToken(@RequestHeader(value = "Authorization") String accessToken) {
        return authorizationService.revokeAccessToken(accessToken);
    }

    /**
     * 查询授权信息(其他服务调用)
     *
     * @param accessToken 令牌
     * @return 授权信息
     */
    @GetMapping("/token")
    @Operation(summary = "getAuthorizationInfo", description = "查询授权信息")
    public AuthorizationInfo getAuthorizationInfo(@RequestHeader(value = "Authorization") String accessToken) {
        return authorizationService.getAuthorizationInfo(accessToken);
    }

}
