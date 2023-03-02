package com.maoatao.cas;

import com.maoatao.cas.core.entity.PermissionEntity;
import com.maoatao.cas.core.entity.RoleEntity;
import com.maoatao.cas.core.entity.RolePermissionEntity;
import com.maoatao.cas.core.service.PermissionService;
import com.maoatao.cas.core.service.RolePermissionService;
import com.maoatao.cas.core.service.RoleService;
import com.maoatao.cas.core.service.UserService;
import com.maoatao.cas.security.oauth2.auth.CustomAuthorizationCodeGenerator;
import com.maoatao.cas.security.UUIDStringKeyGenerator;
import com.maoatao.cas.util.Ids;
import com.maoatao.cas.core.param.UserParam;
import com.maoatao.synapse.core.lang.SynaException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
class CasApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OAuth2AuthorizationService authorizationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;


    //------------------------------------------------ 初始化测试数据 开始 ------------------------------------------------
    // 请按步骤执行以下单元测试

    /**
     * 客户端 id
     */
    private static final String TEST_CLIENT_ID = "test-client";
    /**
     * 客户端密码
     */
    private static final String TEST_CLIENT_SECRET = "123456";
    /**
     * 角色名称
     */
    private static final String TEST_ROLE_NAME = "TEST";
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
    @Transactional(rollbackFor = Exception.class)
    void initialize_test_data() {
        // 1.先创建客户端 save_client_test
        save_client_test();
        // 2.通过客户端 id 新增角色和权限 save_role_permission_test
        save_role_permission_test();
        // 3.通过客户端 id 新增用户 save_user_test
        save_user_test();
    }


    /**
     * 步骤 1
     * <p>
     * 创建测试客户端
     */
    void save_client_test() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(TEST_CLIENT_ID)
                .clientSecret(passwordEncoder.encode(TEST_CLIENT_SECRET))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("http://127.0.0.1:8080/authorized")
                .redirectUri("https://cn.bing.com")
                .scope("test.read")
                .scope("test.write")
                // requireProofKey 需要额外参数验证,requireAuthorizationConsent 授权需要同意
                .clientSettings(ClientSettings.builder().requireProofKey(true).build())
                .tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build())
                .build();
        registeredClientRepository.save(registeredClient);
        Assert.assertEquals(registeredClientRepository.findByClientId("messaging-client").getClientId(), "messaging-client");
    }

    /**
     * 步骤 2
     * <p>
     * 新增角色权限并绑定关系
     * <p>
     * {@link AuthorityAuthorizationManager#hasRole(String)}方法可以看到角色前缀 ROLE_PREFIX 是 ROLE_
     * 所以库中的角色名一定要有前缀 ROLE_
     */
    void save_role_permission_test() {
        RoleEntity roleEntity = RoleEntity.builder().clientId(TEST_CLIENT_ID)
                .name("ROLE_".concat(TEST_ROLE_NAME))
                .enabled(true)
                .build();
        Assert.assertTrue(roleService.save(roleEntity));
        PermissionEntity permissionEntity = PermissionEntity.builder()
                .clientId(TEST_CLIENT_ID)
                .name(TEST_PERMISSION_NAME)
                .enabled(true)
                .build();
        Assert.assertTrue(permissionService.save(permissionEntity));
        Assert.assertTrue(rolePermissionService.save(RolePermissionEntity.builder()
                .roleId(roleEntity.getId())
                .permissionId(permissionEntity.getId())
                .build()));
    }

    /**
     * 步骤 3
     * <p>
     * 创建用户信息
     */
    void save_user_test() {
        UserParam param = new UserParam();
        param.setUniqueId(Ids.user());
        param.setClientId(TEST_CLIENT_ID);
        param.setName(TEST_USER_NAME);
        param.setPassword(TEST_USER_PASSWORD);
        // 角色前缀同步骤 2 说明
        param.setRoles(Collections.singletonList("ROLE_".concat(TEST_ROLE_NAME)));
        long userId = userService.save(param);
        // id 为数据库自增,添加成功一定大于 0
        Assert.assertTrue(userId > 0);
    }

    //------------------------------------------------ 初始化测试数据 结束 ------------------------------------------------

    @Test
    void generate_authorization_code_test() {
        String username = "user";
        String password = "password";
        String clientId = "messaging-client";
        Set<String> scopes = new TreeSet<>();
        scopes.add("message.read");
        scopes.add("message.write");
        Map<String, Object> additionalParameters = new HashMap<>();
        additionalParameters.put("code_challenge_method", "S256");
        additionalParameters.put("code_challenge", "3vrxycun-VbyenvO5GiFOaOBazUBX_xcFElnqbl-TXA");
        // code_challenge对应code_verifier的值: eT3Zhtr7Tmz20-qpTk9zs8EWhN63qdZd8GWiq5-h67TrujxzIg0p_tPUfWH1dXQg278ZEiMcq9ehYPvbBehNe8f4VP4o8EOnFoQY7wVwjUyG_l0ksZUUuPWg5dWKAEth

        // 通过 username 和 password 构建一个 Authentication 对象
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        Authentication principal = authenticationManager.authenticate(authRequest);

        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throw new SynaException(OAuth2ErrorCodes.INVALID_REQUEST + ":" + OAuth2ParameterNames.CLIENT_ID);
        }

        if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE)) {
            throw new SynaException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT + ":" + OAuth2ParameterNames.CLIENT_ID);
        }

        OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
                .authorizationUri("http://localhost:8080/oauth2/authorize")
                .clientId(registeredClient.getClientId())
                .scopes(scopes)
                .additionalParameters(additionalParameters)
                .build();

        OAuth2TokenContext tokenContext = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(principal)
                .tokenType(new OAuth2TokenType(OAuth2ParameterNames.CODE))
                .authorizedScopes(scopes)
                .build();

        OAuth2AuthorizationCode authorizationCode = new CustomAuthorizationCodeGenerator(new UUIDStringKeyGenerator()).generate(tokenContext);
        if (authorizationCode != null) {

            // 控制台输出授权码
            System.out.println(authorizationCode.getTokenValue());

            OAuth2Authorization authorization = OAuth2Authorization.withRegisteredClient(registeredClient)
                    .principalName(principal.getName())
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .attribute(Principal.class.getName(), principal)
                    .attribute(OAuth2AuthorizationRequest.class.getName(), authorizationRequest)
                    .authorizedScopes(scopes)
                    .token(authorizationCode)
                    .build();
            this.authorizationService.save(authorization);
        }
    }


    /**
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
