package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.common.constant.CasSeparator;
import com.maoatao.cas.core.bean.bo.ClientBo;
import com.maoatao.cas.core.bean.entity.ClientAuthenticationMethodEntity;
import com.maoatao.cas.core.bean.entity.ClientEntity;
import com.maoatao.cas.core.bean.entity.ClientGrantTypeEntity;
import com.maoatao.cas.core.bean.entity.ClientRedirectUrlEntity;
import com.maoatao.cas.core.bean.entity.ClientScopeEntity;
import com.maoatao.cas.core.bean.entity.ClientSettingEntity;
import com.maoatao.cas.core.bean.entity.ClientTokenSettingEntity;
import com.maoatao.cas.core.bean.param.client.ClientQueryParam;
import com.maoatao.cas.core.bean.param.client.ClientSaveParam;
import com.maoatao.cas.core.bean.vo.ClientVO;
import com.maoatao.cas.core.mapper.ClientMapper;
import com.maoatao.cas.core.service.ClientAuthenticationMethodService;
import com.maoatao.cas.core.service.ClientGrantTypeService;
import com.maoatao.cas.core.service.ClientRedirectUrlService;
import com.maoatao.cas.core.service.ClientScopeService;
import com.maoatao.cas.core.service.ClientService;
import com.maoatao.cas.core.service.ClientSettingService;
import com.maoatao.cas.core.service.ClientTokenSettingService;
import com.maoatao.daedalus.data.service.impl.DaedalusServiceImpl;
import com.maoatao.daedalus.data.util.PageUtils;
import com.maoatao.synapse.lang.util.SynaAssert;
import com.maoatao.synapse.lang.util.SynaChecks;
import com.maoatao.synapse.lang.util.SynaDates;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CAS 客户端
 *
 * @author MaoAtao
 * @date 2023-04-07 20:51:07
 */
@Service
public class ClientServiceImpl extends DaedalusServiceImpl<ClientMapper, ClientEntity> implements ClientService {

    @Autowired
    private ClientAuthenticationMethodService clientAuthenticationMethodService;

    @Autowired
    private ClientGrantTypeService clientGrantTypeService;

    @Autowired
    private ClientRedirectUrlService clientRedirectUrlService;

    @Autowired
    private ClientScopeService clientScopeService;

    @Autowired
    private ClientSettingService clientSettingService;

    @Autowired
    private ClientTokenSettingService clientTokenSettingService;

    @Override
    public Page<ClientVO> page(ClientQueryParam param) {
        ClientEntity entity = BeanUtil.copyProperties(param, ClientEntity.class);
        Page<ClientEntity> page = super.page(PageUtils.convert(param), Wrappers.query(entity));
        return PageUtils.convert(page, ClientVO.class);
    }

    @Override
    public ClientVO details(Long id) {
        return BeanUtil.toBean(super.getById(id), ClientVO.class);
    }

    @Override
    public ClientEntity getByClientId(String clientId) {
        return super.getOne(Wrappers.<ClientEntity>lambdaQuery().eq(ClientEntity::getClientId, clientId));
    }

    @Override
    public ClientEntity getByClientAlias(String clientAlias) {
        return super.getOne(Wrappers.<ClientEntity>lambdaQuery().eq(ClientEntity::getAlias, clientAlias));
    }

    @Override
    public List<ClientEntity> listByClientIds(List<String> clientIds) {
        return super.list(Wrappers.<ClientEntity>lambdaQuery().in(ClientEntity::getClientId, clientIds));
    }

    @Override
    public List<ClientEntity> listByClientAliases(List<String> clientAliases) {
        return super.list(Wrappers.<ClientEntity>lambdaQuery().in(ClientEntity::getAlias, clientAliases));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long save(ClientSaveParam param) {
        return -1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RegisteredClient registeredClient) {
        ClientEntity clientEntity = saveClient(registeredClient);
        saveClientAuthenticationMethod(registeredClient);
        saveClientGrantType(registeredClient);
        saveClientRedirectUrl(registeredClient);
        saveClientScope(registeredClient, clientEntity.getAlias());
        saveClientSettings(registeredClient);
        saveTokenSettings(registeredClient);
    }

    @Override
    public RegisteredClient findById(String id) {
        SynaAssert.isTrue(NumberUtil.isNumber(id), "无效的客户端主键编号");
        return convert(getClientBo(Long.parseLong(id)));
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return convert(getClientBo(clientId));
    }

    private ClientEntity saveClient(RegisteredClient registeredClient) {
        SynaAssert.notEmpty(registeredClient.getClientId(), "clientId 不能为空!");
        SynaAssert.notEmpty(registeredClient.getClientSecret(), "clientSecret 不能为空!");
        SynaAssert.notEmpty(registeredClient.getClientName(), "clientName 不能为空!");
        ClientEntity clientEntity = ClientEntity.builder()
                .clientId(registeredClient.getClientId())
                .clientIdIssuedAt(SynaDates.of(registeredClient.getClientIdIssuedAt()))
                .secret(registeredClient.getClientSecret())
                .secretExpiresAt(SynaDates.of(registeredClient.getClientSecretExpiresAt()))
                .name(registeredClient.getClientName())
                // 别名唯一,目前用于scope前缀,通过registeredClient储存时暂时设置为随机字符串
                .alias(nextRandomAlias())
                .build();
        SynaAssert.isTrue(super.save(clientEntity), "客户端保存失败!");
        return clientEntity;
    }

    private void saveClientAuthenticationMethod(RegisteredClient registeredClient) {
        SynaAssert.notEmpty(registeredClient.getClientAuthenticationMethods(), "clientAuthenticationMethods 不能为空!");
        List<ClientAuthenticationMethodEntity> clientAuthenticationMethodEntities = registeredClient.getClientAuthenticationMethods().stream()
                .map(o -> ClientAuthenticationMethodEntity.builder()
                        .clientId(registeredClient.getClientId())
                        .value(o.getValue())
                        .build())
                .toList();
        SynaAssert.isTrue(clientAuthenticationMethodService.saveBatch(clientAuthenticationMethodEntities), "客户端授权方法保存失败!");
    }

    private void saveClientGrantType(RegisteredClient registeredClient) {
        SynaAssert.notEmpty(registeredClient.getAuthorizationGrantTypes(), "clientAuthenticationGrantTypes 不能为空!");
        List<ClientGrantTypeEntity> clientGrantTypeEntities = registeredClient.getAuthorizationGrantTypes().stream()
                .map(o -> ClientGrantTypeEntity.builder()
                        .clientId(registeredClient.getClientId())
                        .value(o.getValue())
                        .build())
                .toList();
        SynaAssert.isTrue(clientGrantTypeService.saveBatch(clientGrantTypeEntities), "客户端授权类型保存失败!");
    }

    private void saveClientRedirectUrl(RegisteredClient registeredClient) {
        SynaAssert.notEmpty(registeredClient.getRedirectUris(), "redirectUris 不能为空!");
        List<ClientRedirectUrlEntity> clientGrantTypeEntities = registeredClient.getRedirectUris().stream()
                .map(o -> ClientRedirectUrlEntity.builder()
                        .clientId(registeredClient.getClientId())
                        .value(o)
                        .build())
                .toList();
        SynaAssert.isTrue(clientRedirectUrlService.saveBatch(clientGrantTypeEntities), "客户端重定向地址保存失败!");
    }

    private void saveClientScope(RegisteredClient registeredClient, String prefix) {
        SynaAssert.notEmpty(registeredClient.getScopes(), "scopes 不能为空!");
        SynaAssert.notEmpty(prefix, "prefix 不能为空!");
        List<ClientScopeEntity> clientScopeEntities = registeredClient.getScopes().stream()
                .map(o -> ClientScopeEntity.builder()
                        .clientId(registeredClient.getClientId())
                        .name(prefix.concat(CasSeparator.SCOPE).concat(o))
                        .build())
                .toList();
        SynaAssert.isTrue(clientScopeService.saveBatch(clientScopeEntities), "客户端作用域保存失败!");
    }

    private void saveClientSettings(RegisteredClient registeredClient) {
        SynaAssert.notNull(registeredClient.getClientSettings(), "clientSettings 不能为空!");
        ClientSettingEntity.ClientSettingEntityBuilder entityBuilder = ClientSettingEntity.builder()
                .clientId(registeredClient.getClientId())
                .requireAuthorizationConsent(registeredClient.getClientSettings().isRequireAuthorizationConsent())
                .requireProofKey(registeredClient.getClientSettings().isRequireProofKey());
        if (StrUtil.isNotEmpty(registeredClient.getClientSettings().getJwkSetUrl())) {
            entityBuilder.jwkSetUrl(registeredClient.getClientSettings().getJwkSetUrl());
        }
        if (ObjUtil.isNotEmpty(registeredClient.getClientSettings().getTokenEndpointAuthenticationSigningAlgorithm())) {
            entityBuilder.signingAlgorithm(registeredClient.getClientSettings().getTokenEndpointAuthenticationSigningAlgorithm().getName());
        }
        SynaAssert.isTrue(clientSettingService.save(entityBuilder.build()), "客户端设置保存失败!");
    }

    private void saveTokenSettings(RegisteredClient registeredClient) {
        SynaAssert.notNull(registeredClient.getTokenSettings(), "tokenSettings 不能为空!");
        ClientTokenSettingEntity.ClientTokenSettingEntityBuilder entityBuilder = ClientTokenSettingEntity.builder()
                .clientId(registeredClient.getClientId())
                .reuseRefreshToken(registeredClient.getTokenSettings().isReuseRefreshTokens());
        if (ObjUtil.isNotNull(registeredClient.getTokenSettings().getAuthorizationCodeTimeToLive())) {
            entityBuilder.authorizationCodeDuration(registeredClient.getTokenSettings().getAuthorizationCodeTimeToLive().toSeconds());
        }
        if (ObjUtil.isNotNull(registeredClient.getTokenSettings().getAccessTokenFormat())) {
            entityBuilder.accessTokenFormat(registeredClient.getTokenSettings().getAccessTokenFormat().getValue());
        }
        if (ObjUtil.isNotNull(registeredClient.getTokenSettings().getIdTokenSignatureAlgorithm())) {
            entityBuilder.signingAlgorithm(registeredClient.getTokenSettings().getIdTokenSignatureAlgorithm().getName());
        }
        if (ObjUtil.isNotNull(registeredClient.getTokenSettings().getAccessTokenTimeToLive())) {
            entityBuilder.authorizationCodeDuration(registeredClient.getTokenSettings().getAccessTokenTimeToLive().toSeconds());
        }
        if (ObjUtil.isNotNull(registeredClient.getTokenSettings().getRefreshTokenTimeToLive())) {
            entityBuilder.authorizationCodeDuration(registeredClient.getTokenSettings().getRefreshTokenTimeToLive().toSeconds());
        }
        SynaAssert.isTrue(clientTokenSettingService.save(entityBuilder.build()), "客户端令牌设置保存失败!");
    }

    private ClientBo getClientBo(Long id) {
        ClientEntity clientEntity = SynaChecks.checkAndGet(id, this::getById, "id");
        return getClientBo(clientEntity);
    }

    private ClientBo getClientBo(String clientId) {
        ClientEntity clientEntity = SynaChecks.checkAndGet(clientId, this::getByClientId, "clientId");
        return getClientBo(clientEntity);
    }

    private ClientBo getClientBo(ClientEntity clientEntity) {
        List<ClientAuthenticationMethodEntity> authenticationMethods = SynaChecks.checkAndGet(clientEntity.getClientId(), clientAuthenticationMethodService::listByClientId, "clientId");
        List<ClientGrantTypeEntity> grantTypes = SynaChecks.checkAndGet(clientEntity.getClientId(), clientGrantTypeService::listByClientId, "clientId");
        List<ClientRedirectUrlEntity> redirectUrls = SynaChecks.checkAndGet(clientEntity.getClientId(), clientRedirectUrlService::listByClientId, "clientId");
        List<ClientScopeEntity> scopes = SynaChecks.checkAndGet(clientEntity.getClientId(), clientScopeService::listByClientId, "clientId");
        ClientSettingEntity setting = SynaChecks.checkAndGet(clientEntity.getClientId(), clientSettingService::getByClientId, "clientId");
        ClientTokenSettingEntity tokenSetting = SynaChecks.checkAndGet(clientEntity.getClientId(), clientTokenSettingService::getByClientId, "clientId");

        ClientBo.ClientBoBuilder clientBoBuilder = ClientBo.builder()
                .content(clientEntity)
                .authenticationMethods(CollUtil.newHashSet(authenticationMethods))
                .grantTypes(CollUtil.newHashSet(grantTypes))
                .redirectUrls(CollUtil.newHashSet(redirectUrls))
                .scopes(CollUtil.newHashSet(scopes))
                .setting(setting)
                .tokenSetting(tokenSetting);

        return clientBoBuilder.build();
    }

    private RegisteredClient convert(ClientBo clientBo) {
        RegisteredClient.Builder registeredClientBuilder = RegisteredClient.withId(clientBo.getContent().getId().toString());

        ClientSettings.Builder clientSettingsBuilder = ClientSettings.builder()
                .requireAuthorizationConsent(clientBo.getSetting().getRequireAuthorizationConsent())
                .requireProofKey(clientBo.getSetting().getRequireProofKey());
        ClientAuthenticationMethodEntity clientSecretJwt = clientBo.getAuthenticationMethods().stream()
                .filter(o -> o.getValue().equals(ClientAuthenticationMethod.CLIENT_SECRET_JWT.getValue()))
                .findFirst().orElse(null);
        ClientAuthenticationMethodEntity privateKeyJwt = clientBo.getAuthenticationMethods().stream()
                .filter(o -> o.getValue().equals(ClientAuthenticationMethod.PRIVATE_KEY_JWT.getValue()))
                .findFirst().orElse(null);

        if (clientSecretJwt != null) {
            clientSettingsBuilder.tokenEndpointAuthenticationSigningAlgorithm(Optional.ofNullable(MacAlgorithm.from(clientBo.getSetting().getSigningAlgorithm())).orElse(MacAlgorithm.HS256));
        } else if (privateKeyJwt != null) {
            clientSettingsBuilder.tokenEndpointAuthenticationSigningAlgorithm(Optional.ofNullable(SignatureAlgorithm.from(clientBo.getSetting().getSigningAlgorithm())).orElse(SignatureAlgorithm.RS256));
            // jwkSetUrl 不能为空 新增客户端时 PRIVATE_KEY_JWT 客户端身份验证方法必填
            clientSettingsBuilder.jwkSetUrl(clientBo.getSetting().getJwkSetUrl());
        }

        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.builder()
                .authorizationCodeTimeToLive(Duration.ofSeconds(clientBo.getTokenSetting().getAuthorizationCodeDuration()))
                .accessTokenFormat(new OAuth2TokenFormat(clientBo.getTokenSetting().getAccessTokenFormat()))
                .accessTokenTimeToLive(Duration.ofSeconds(clientBo.getTokenSetting().getAccessTokenDuration()))
                .idTokenSignatureAlgorithm(Optional.ofNullable(SignatureAlgorithm.from(clientBo.getSetting().getSigningAlgorithm())).orElse(SignatureAlgorithm.RS256))
                .refreshTokenTimeToLive(Duration.ofSeconds(clientBo.getTokenSetting().getRefreshTokenDuration()))
                .reuseRefreshTokens(clientBo.getTokenSetting().getReuseRefreshToken());

        registeredClientBuilder
                .clientId(clientBo.getContent().getClientId())
                .clientIdIssuedAt(SynaDates.toInstant(clientBo.getContent().getClientIdIssuedAt()))
                .clientSecret(clientBo.getContent().getSecret())
                .clientSecretExpiresAt(SynaDates.toInstant(clientBo.getContent().getSecretExpiresAt()))
                .clientName(clientBo.getContent().getName())
                .clientAuthenticationMethods(authenticationMethods -> authenticationMethods.addAll(
                        clientBo.getAuthenticationMethods().stream()
                                .map(o -> new ClientAuthenticationMethod(o.getValue()))
                                .collect(Collectors.toSet())
                ))
                .authorizationGrantTypes(authorizationGrantTypes -> authorizationGrantTypes.addAll(
                        clientBo.getGrantTypes().stream()
                                .map(o -> new AuthorizationGrantType(o.getValue()))
                                .collect(Collectors.toSet())
                ))
                .redirectUris(uris -> uris.addAll(
                        clientBo.getRedirectUrls().stream()
                                .map(ClientRedirectUrlEntity::getValue)
                                .collect(Collectors.toSet())
                ))
                .scopes(scopes -> scopes.addAll(
                        clientBo.getScopes().stream()
                                .map(ClientScopeEntity::getName)
                                .collect(Collectors.toSet())
                ))
                .clientSettings(clientSettingsBuilder.build())
                .tokenSettings(tokenSettingsBuilder.build());
        // TODO: MaoAtao 2023-04-12 11:10:45 添加自定义设置(令牌值的格式)
        return registeredClientBuilder.build();
    }

    private String nextRandomAlias() {
        String alias = RandomUtil.randomString(10);
        if (ObjUtil.isNull(getByClientAlias(alias))) {
            return alias;
        }
        return nextRandomAlias();
    }
}
