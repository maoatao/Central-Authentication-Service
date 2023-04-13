package com.maoatao.cas;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.maoatao.cas.common.authentication.CasAccessToken;
import com.maoatao.cas.config.CasServerConfig;
import com.maoatao.cas.core.bean.entity.PermissionEntity;
import com.maoatao.cas.core.bean.entity.RoleEntity;
import com.maoatao.cas.core.bean.entity.RolePermissionEntity;
import com.maoatao.cas.core.bean.entity.UserEntity;
import com.maoatao.cas.core.param.GenerateAccessTokenParam;
import com.maoatao.cas.core.param.GenerateAuthorizationCodeParam;
import com.maoatao.cas.security.service.CasAuthorizationService;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.cas.core.service.RolePermissionService;
import com.maoatao.cas.core.service.RoleService;
import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.security.bean.ClientUser;
import com.maoatao.cas.util.FilterUtils;
import com.maoatao.cas.util.IdUtils;
import com.maoatao.cas.core.param.UserParam;
import com.maoatao.synapse.lang.util.SynaStrings;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class CasApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @Autowired
    private CasAuthorizationService casAuthorizationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private AuthorizationServerSettings authorizationServerSettings;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtDecoder jwtDecoder;

    /**
     * 客户端 id
     */
    private static final String TEST_CLIENT_ID = "test-client";
    /**
     * 客户端密码
     */
    private static final String TEST_CLIENT_SECRET = "123456";
    /**
     * 客户端密码
     */
    private static final String TEST_CLIENT_REDIRECT_URI = "https://cn.bing.com";
    /**
     * 客户端授权范围
     */
    private static final Set<String> TEST_CLIENT_SCOPES = Set.of("test.read", "test.write");
    /**
     * 角色名称
     * <p>
     * {@link AuthorityAuthorizationManager#hasRole(String)}方法可以看到角色前缀 ROLE_PREFIX 是 ROLE_
     * 所以库中的角色名一定要有前缀 ROLE_
     */
    private static final String TEST_ROLE_NAME = "ROLE_TEST";
    /**
     * 权限名称
     */
    private static final String TEST_PERMISSION_NAME = "PERMISSION_TEST";
    /**
     * 用户名称
     */
    private static final String TEST_USER_NAME = "user";
    /**
     * 用户密码
     */
    private static final String TEST_USER_PASSWORD = "password";

    @Test
    void test() {
        System.out.println(new Base64StringKeyGenerator(java.util.Base64.getUrlEncoder().withoutPadding(), 96).generateKey());
    }

    /**
     * 测试步骤 1
     * <p>
     * 条件:服务配置正确的数据库连接,并手动新建数据库后使用 docs/sql/CAS_DDL.sql 建表
     * <p>
     * 初始化测试数据 (推荐使用本单元测试进行初始化测试数据,可以确认服务配置是否正常,如果使用 docs/sql/CAS_DML_DEMO.sql 初始化测试数据,请跳过此单元测试直接执行测试步骤 2)
     * <p>
     * 创建一个客户端,并为其添加一个拥有角色权限的用户
     */
    @Test
    void initialize_test_data() {
        // 请按步骤初始化测试数据
        // 1.先创建客户端 save_client_test
        save_client_test();
        // 2.通过客户端 id 新增角色和权限 save_role_permission_test
        save_role_permission_test();
        // 3.通过客户端 id 新增用户 save_user_test
        save_user_test();
        System.out.println("----------------------------------------------------");
        System.out.println(SynaStrings.format("已成功创建客户端: {}", TEST_CLIENT_ID));
        System.out.println(SynaStrings.format("已成功创建角　色: {}", TEST_ROLE_NAME));
        System.out.println(SynaStrings.format("已成功创建权　限: {}", TEST_PERMISSION_NAME));
        System.out.println(SynaStrings.format("已成功创建用　户: {} 密码 {}", TEST_USER_NAME, TEST_USER_PASSWORD));
        System.out.println("----------------------------------------------------");
    }

    /**
     * 测试步骤 2
     * <p>
     * 条件:服务配置正确的redis连接(默认使用redis来储存授权信息,详情{@link CasServerConfig#oAuth2AuthorizationService })
     * <p>
     * 创建一个授权码,并使用该授权码请求令牌
     * <p>
     * 客户端设置如果 requireProofKey 为 true,就需要添加额外参数,三个参数为一组,相互对应.详情{@link com.maoatao.cas.test.CodeVerifierTest}
     * <p>
     * CodeChallenge 验证源码位置 org.springframework.security.oauth2.server.authorization.authentication.CodeVerifierAuthenticator#codeVerifierValid
     */
    @Test
    void generate_token_test() {
        mock_context_test();
        String authorizationCode = generate_authorization_code_test();
        CasAccessToken customAccessToken = generate_token_by_code_test(authorizationCode);
        String refreshToken = customAccessToken.getRefreshToken();
        CasAccessToken accessTokenByRefresh = generate_token_by_refresh_test(refreshToken);
        CasAccessToken accessTokenByClient = generate_token_by_client_test();

        System.out.println("----------------------------------------------------");
        System.out.println(SynaStrings.format("已成功生成授权码: {}", authorizationCode));
        System.out.println("----------------------------------------------------");

        System.out.println("\n----------------------------------------------------");
        System.out.println("[授权码模式]已成功获取令牌:");
        System.out.println(new JSONObject(customAccessToken).toStringPretty());
        System.out.println("----------------------------------------------------");

        System.out.println("\n----------------------------------------------------");
        System.out.println("[刷新令牌模式]已成功获取令牌:");
        System.out.println(new JSONObject(accessTokenByRefresh).toStringPretty());
        System.out.println("----------------------------------------------------");

        System.out.println("\n----------------------------------------------------");
        System.out.println("[客户端模式]已成功获取令牌:");
        System.out.println(new JSONObject(accessTokenByClient).toStringPretty());
        System.out.println("----------------------------------------------------");
    }

    /**
     * 使用原版接口 mock http request 获取令牌
     */
    @Test
    @SneakyThrows
    void request_token_test() {
        mock_context_test();
        String authorizationCode = generate_authorization_code_test();
        String result = request_token_by_code_test(authorizationCode);

        System.out.println("----------------------------------------------------");
        System.out.println(SynaStrings.format("已成功生成授权码: {}", authorizationCode));
        System.out.println("----------------------------------------------------");

        System.out.println("\n----------------------------------------------------");
        System.out.println("已成功获取令牌:");
        System.out.println(new JSONObject(result).toStringPretty());
        System.out.println("----------------------------------------------------");
    }

    //------------------------------------------------ 初始化测试数据 开始 ------------------------------------------------

    /**
     * 初始化测试数据 步骤 1
     * <p>
     * 创建测试客户端
     */
    @Test
    void save_client_test() {
        registeredClientRepository.save(RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(TEST_CLIENT_ID)
                .clientSecret(passwordEncoder.encode(TEST_CLIENT_SECRET))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("http://127.0.0.1:8080/authorized")
                .redirectUri(TEST_CLIENT_REDIRECT_URI)
                .scopes(scope -> scope.addAll(TEST_CLIENT_SCOPES))
                // requireProofKey 需要额外参数验证,requireAuthorizationConsent 授权需要同意
                .clientSettings(ClientSettings.builder().requireProofKey(true).build())
                .tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build())
                .build());
        RegisteredClient registeredClient = registeredClientRepository.findByClientId(TEST_CLIENT_ID);
        Assert.assertNotNull(registeredClient);
        Assert.assertEquals("客户端创建失败", registeredClient.getClientId(), TEST_CLIENT_ID);
    }

    /**
     * 初始化测试数据 步骤 2
     * <p>
     * 新增角色权限并绑定关系
     */
    @Test
    void save_role_permission_test() {
        RoleEntity roleEntity = RoleEntity.builder().clientId(TEST_CLIENT_ID)
                .name(TEST_ROLE_NAME)
                .build();
        Assert.assertTrue("角色创建失败", roleService.save(roleEntity));
        PermissionEntity permissionEntity = PermissionEntity.builder()
                .clientId(TEST_CLIENT_ID)
                .name(TEST_PERMISSION_NAME)
                .build();
        Assert.assertTrue("权限创建失败", permissionService.save(permissionEntity));
        Assert.assertTrue("角色权限关系绑定失败", rolePermissionService.save(RolePermissionEntity.builder()
                .roleId(roleEntity.getId())
                .permissionId(permissionEntity.getId())
                .build()));
    }

    /**
     * 初始化测试数据 步骤 3
     * <p>
     * 创建用户信息
     */
    @Test
    void save_user_test() {
        UserParam param = new UserParam();
        param.setOpenId(IdUtils.nextUserOpenId());
        param.setClientId(TEST_CLIENT_ID);
        param.setName(TEST_USER_NAME);
        param.setPassword(TEST_USER_PASSWORD);
        // 角色前缀同步骤 2 说明
        param.setRoles(Collections.singletonList(TEST_ROLE_NAME));
        UserEntity userEntity = userService.getById(userService.save(param));
        Assert.assertNotNull("用户创建失败", userEntity);
    }

    //------------------------------------------------ 初始化测试数据 结束 ------------------------------------------------

    //------------------------------------------------ 生成令牌 开始 ------------------------------------------------

    /**
     * 生成一个授权码
     *
     * @return 授权码
     */
    private String generate_authorization_code_test() {
        GenerateAuthorizationCodeParam generateAuthorizationCodeParam = new GenerateAuthorizationCodeParam();
        // scopes需要在客户端的范围内
        generateAuthorizationCodeParam.setScopes(TEST_CLIENT_SCOPES);
        generateAuthorizationCodeParam.setCodeChallengeMethod("S256");
        generateAuthorizationCodeParam.setCodeChallenge("3vrxycun-VbyenvO5GiFOaOBazUBX_xcFElnqbl-TXA");
        // 非OAuth2原获取授权码接口,原版请求成功后跳转页面 get http://localhost:8080/oauth2/authorize
        // 根据需要自己新增了一个不需要鉴权,不跳转页面,post的请求授权码接口
        String authorizationCode = casAuthorizationService.generateAuthorizationCode(generateAuthorizationCodeParam);

        Assert.assertTrue("授权码生成失败!", StrUtil.isNotBlank(authorizationCode));
        return authorizationCode;
    }

    /**
     * 授权码模式 生成令牌(原版)
     *
     * @param authorizationCode 授权码
     */
    private String request_token_by_code_test(String authorizationCode) throws Exception {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.set(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.AUTHORIZATION_CODE.getValue());
        param.set(OAuth2ParameterNames.REDIRECT_URI, TEST_CLIENT_REDIRECT_URI);
        param.set(OAuth2ParameterNames.CODE, authorizationCode);
        param.set(PkceParameterNames.CODE_VERIFIER, "eT3Zhtr7Tmz20-qpTk9zs8EWhN63qdZd8GWiq5-h67TrujxzIg0p_tPUfWH1dXQg278ZEiMcq9ehYPvbBehNe8f4VP4o8EOnFoQY7wVwjUyG_l0ksZUUuPWg5dWKAEth");
        // mock模拟了请求令牌,此接口为OAuth2原版接口 post http://localhost:8080/oauth2/token
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/oauth2/token")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .params(param)
                .header(HttpHeaders.AUTHORIZATION, "Basic ".concat(Base64.encode(TEST_CLIENT_ID.concat(":").concat(TEST_CLIENT_SECRET))))
        ).andReturn();
        MockHttpServletResponse mockResponse = mvcResult.getResponse();

        Assert.assertEquals("获取令牌请求失败!", mockResponse.getStatus(), 200);
        Assert.assertTrue("获取令牌请求失败!", StrUtil.isNotBlank(mockResponse.getContentAsString()));

        return mockResponse.getContentAsString();
    }

    /**
     * 授权码模式 生成令牌(自定义)
     *
     * @param authorizationCode 授权码
     */
    private CasAccessToken generate_token_by_code_test(String authorizationCode) {
        GenerateAccessTokenParam param = new GenerateAccessTokenParam();
        param.setType(AuthorizationGrantType.AUTHORIZATION_CODE.getValue());
        param.setCode(authorizationCode);
        param.setSecret(TEST_CLIENT_SECRET);
        param.setVerifier("eT3Zhtr7Tmz20-qpTk9zs8EWhN63qdZd8GWiq5-h67TrujxzIg0p_tPUfWH1dXQg278ZEiMcq9ehYPvbBehNe8f4VP4o8EOnFoQY7wVwjUyG_l0ksZUUuPWg5dWKAEth");

        CasAccessToken accessToken = casAuthorizationService.generateAccessToken(param);

        Assert.assertNotNull("授权码模式生成令牌请求失败!", accessToken);
        return accessToken;
    }

    /**
     * 刷新令牌模式 生成令牌
     *
     * @param refreshToken 刷新令牌
     */
    private CasAccessToken generate_token_by_refresh_test(String refreshToken) {
        GenerateAccessTokenParam param = new GenerateAccessTokenParam();
        param.setType(AuthorizationGrantType.REFRESH_TOKEN.getValue());
        param.setCode(refreshToken);
        param.setSecret(TEST_CLIENT_SECRET);

        CasAccessToken accessToken = casAuthorizationService.generateAccessToken(param);

        Assert.assertNotNull("刷新令牌模式生成令牌请求失败!", accessToken);
        return accessToken;
    }

    /**
     * 客户端模式 生成令牌
     */
    private CasAccessToken generate_token_by_client_test() {
        GenerateAccessTokenParam param = new GenerateAccessTokenParam();
        param.setType(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue());
        param.setSecret(TEST_CLIENT_SECRET);

        CasAccessToken accessToken = casAuthorizationService.generateAccessToken(param);

        Assert.assertNotNull("客户端模式生成令牌请求失败!", accessToken);
        return accessToken;
    }

    private void mock_context_test() {
        // 构建上下文
        Authentication principal = casAuthorizationService.generateUserPrincipal(ClientUser.builder()
                .clientId(TEST_CLIENT_ID)
                .username(TEST_USER_NAME)
                .password(TEST_USER_PASSWORD)
                .build()
        );
        SecurityContextHolder.getContext().setAuthentication(principal);
        AuthorizationServerContext authorizationServerContext = FilterUtils.buildAuthorizationServerContext(authorizationServerSettings, new MockHttpServletRequest());
        AuthorizationServerContextHolder.setContext(authorizationServerContext);
    }


    //------------------------------------------------ 生成令牌 结束 ------------------------------------------------

    /**
     * 此方法需要 令牌设置{@link OAuth2TokenFormat#SELF_CONTAINED}
     */
    @Test
    void read_jwt_token_test() {
        mock_context_test();
        String authorizationCode = generate_authorization_code_test();
        CasAccessToken customAccessToken = generate_token_by_code_test(authorizationCode);
        Jwt jwt = jwtDecoder.decode(customAccessToken.getAccessToken());
        JWT htJwt = JWTUtil.parseToken(customAccessToken.getAccessToken());

        System.out.println("----------------------------------------------------");
        System.out.println(SynaStrings.format("已成功生成授权码: {}", authorizationCode));
        System.out.println("----------------------------------------------------");

        System.out.println("\n----------------------------------------------------");
        System.out.println("[授权码模式]已成功获取令牌:");
        System.out.println(new JSONObject(customAccessToken).toStringPretty());
        System.out.println("----------------------------------------------------");

        System.out.println("\n----------------------------------------------------");
        System.out.println("[JWT解码]原生:");
        System.out.println(new JSONObject(jwt).toStringPretty());
        System.out.println("----------------------------------------------------");
        System.out.println("[JWT解码]Hutool:");
        System.out.println("\"header\": " + htJwt.getHeaders().toStringPretty());
        System.out.println("\"payload\": " + htJwt.getPayloads().toStringPretty());
        System.out.println("----------------------------------------------------");
    }

    /**
     * 非必要
     * <p>
     * 创建oidc测试客户端
     * <p>
     * 客户端注册请求中的访问令牌需要 scope 有 client.create.
     * <p>
     * 客户端读取请求中的访问令牌需要 scope 有 client.read.
     */
    @Test
    void save_oidc_client_test() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("oidc-client")
                .clientSecret(passwordEncoder.encode("123456"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client-oidc")
                .redirectUri("http://127.0.0.1:8080/authorized")
                .redirectUri("https://cn.bing.com")
                .scope(OidcScopes.OPENID)
                .scope("client.create")
                .scope("client.read")
                .clientSettings(ClientSettings.builder().requireProofKey(true).requireAuthorizationConsent(true).build())
                .tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED).build())
                .build();
        registeredClientRepository.save(registeredClient);
    }
}
