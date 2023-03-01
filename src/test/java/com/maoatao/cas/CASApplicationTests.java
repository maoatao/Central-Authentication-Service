package com.maoatao.cas;

import com.maoatao.cas.security.bean.ClientUser;
import com.maoatao.cas.security.oauth2.auth.CustomAuthorizationCodeGenerator;
import com.maoatao.cas.security.generator.UUIDStringKeyGenerator;
import com.maoatao.cas.security.service.CustomUserDetailsService;
import com.maoatao.synapse.core.lang.SynaException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

@SpringBootTest
class CASApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 初始化客户端信息
     */
    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * 创建clientId信息
     */
    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OAuth2AuthorizationService authorizationService;

    /**
     * 创建用户信息
     */
    @Test
    void save_user_test() {
        userDetailsService.createUser(ClientUser.builder()
                .clientId("messaging-client")
                .passwordEncoder(s -> passwordEncoder.encode(s))
                .username("user")
                .password("password")
                .roles("USER")
                .build());
    }

    /**
     * 创建测试客户端
     */
    @Test
    void save_client_test() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("messaging-client")
                .clientSecret(passwordEncoder.encode("123456"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("http://127.0.0.1:8080/authorized")
                // 便于调试授权码流程
                .redirectUri("https://cn.bing.com")
                .scope("message.read")
                .scope("message.write")
                .clientSettings(ClientSettings.builder().requireProofKey(true).build())
                .tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build())
                .build();
        registeredClientRepository.save(registeredClient);
    }

    /**
     * The access token in a Client Registration request REQUIRES the OAuth2 scope client.create.
     * The access token in a Client Read request REQUIRES the OAuth2 scope client.read.
     * 创建oidc测试客户端
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
                // 便于调试授权码流程
                .redirectUri("https://cn.bing.com")
                .scope(OidcScopes.OPENID)
                .scope("client.create")
                .scope("client.read")
                .clientSettings(ClientSettings.builder().requireProofKey(true).requireAuthorizationConsent(true).build())
                .tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED).build())
                .build();
        registeredClientRepository.save(registeredClient);
    }

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
}
