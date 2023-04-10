package com.maoatao.cas.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.entity.ClientAuthenticationMethodEntity;
import com.maoatao.cas.core.bean.entity.ClientEntity;
import com.maoatao.cas.core.bean.entity.ClientGrantTypeEntity;
import com.maoatao.cas.core.bean.entity.ClientRedirectUrlEntity;
import com.maoatao.cas.core.bean.entity.ClientScopeEntity;
import com.maoatao.cas.core.bean.entity.ClientSettingEntity;
import com.maoatao.cas.core.bean.entity.ClientTokenSettingEntity;
import com.maoatao.cas.core.bean.param.client.ClientQueryParam;
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
import com.maoatao.synapse.lang.util.SynaDates;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
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
    @Transactional(rollbackFor = Exception.class)
    public void save(RegisteredClient registeredClient) {
        saveClient(registeredClient);
        saveClientAuthenticationMethod(registeredClient);
        saveClientGrantType(registeredClient);
        saveClientRedirectUrl(registeredClient);
        saveClientScope(registeredClient);
        saveClientSettings(registeredClient);
        saveTokenSettings(registeredClient);
    }

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return null;
    }

    private void saveClient(RegisteredClient registeredClient) {
        SynaAssert.notEmpty(registeredClient.getClientId(), "clientId 不能为空!");
        SynaAssert.notNull(registeredClient.getClientIdIssuedAt(), "clientIdIssuedAt 不能为空!");
        SynaAssert.notEmpty(registeredClient.getClientSecret(), "clientSecret 不能为空!");
        SynaAssert.notNull(registeredClient.getClientSecretExpiresAt(), "clientSecretExpiresAt 不能为空!");
        SynaAssert.notEmpty(registeredClient.getClientName(), "clientName 不能为空!");
        ClientEntity clientEntity = ClientEntity.builder()
                .clientId(registeredClient.getClientId())
                .clientIdIssuedAt(SynaDates.of(registeredClient.getClientIdIssuedAt()))
                .secret(registeredClient.getClientSecret())
                .secretExpiresAt(SynaDates.of(registeredClient.getClientSecretExpiresAt()))
                .name(registeredClient.getClientName())
                .build();
        SynaAssert.isTrue(super.save(clientEntity), "客户端保存失败!");
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

    private void saveClientScope(RegisteredClient registeredClient) {
        SynaAssert.notEmpty(registeredClient.getScopes(), "scopes 不能为空!");
        List<ClientScopeEntity> clientScopeEntities = registeredClient.getScopes().stream()
                .map(o -> ClientScopeEntity.builder()
                        .clientId(registeredClient.getClientId())
                        .name(o)
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
}
