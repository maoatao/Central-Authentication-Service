package com.maoatao.cas.security.oauth2.odic;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.oidc.OidcClientRegistration;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 自定义客户端注册转换器
 * <p>
 * Customized by {@link org.springframework.security.oauth2.server.authorization.oidc.authentication.RegisteredClientOidcClientRegistrationConverter}
 *
 * @author MaoAtao
 * @date 2023-01-22 13:20:01
 */
public class CustomClientRegistrationConverter implements Converter<RegisteredClient, OidcClientRegistration> {
    @Override
    public OidcClientRegistration convert(RegisteredClient registeredClient) {
        // @formatter:off
        OidcClientRegistration.Builder builder = OidcClientRegistration.builder()
                .clientId(registeredClient.getClientId())
                .clientIdIssuedAt(registeredClient.getClientIdIssuedAt())
                .clientName(registeredClient.getClientName());

        if (registeredClient.getClientSecret() != null) {
            builder.clientSecret(registeredClient.getClientSecret());
        }

        builder.redirectUris(redirectUris ->
                redirectUris.addAll(registeredClient.getRedirectUris()));

        builder.grantTypes(grantTypes ->
                registeredClient.getAuthorizationGrantTypes().forEach(authorizationGrantType ->
                        grantTypes.add(authorizationGrantType.getValue())));

        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE)) {
            builder.responseType(OAuth2AuthorizationResponseType.CODE.getValue());
        }

        if (!CollectionUtils.isEmpty(registeredClient.getScopes())) {
            builder.scopes(scopes ->
                    scopes.addAll(registeredClient.getScopes()));
        }

        AuthorizationServerContext authorizationServerContext = AuthorizationServerContextHolder.getContext();
        String registrationClientUri = UriComponentsBuilder.fromUriString(authorizationServerContext.getIssuer())
                .path(authorizationServerContext.getAuthorizationServerSettings().getOidcClientRegistrationEndpoint())
                .queryParam(OAuth2ParameterNames.CLIENT_ID, registeredClient.getClientId())
                .toUriString();

        builder
                .tokenEndpointAuthenticationMethod(registeredClient.getClientAuthenticationMethods().iterator().next().getValue())
                .idTokenSignedResponseAlgorithm(registeredClient.getTokenSettings().getIdTokenSignatureAlgorithm().getName())
                .registrationClientUrl(registrationClientUri);

        ClientSettings clientSettings = registeredClient.getClientSettings();

        if (clientSettings.getJwkSetUrl() != null) {
            builder.jwkSetUrl(clientSettings.getJwkSetUrl());
        }

        if (clientSettings.getTokenEndpointAuthenticationSigningAlgorithm() != null) {
            builder.tokenEndpointAuthenticationSigningAlgorithm(clientSettings.getTokenEndpointAuthenticationSigningAlgorithm().getName());
        }

        return builder.build();
        // @formatter:on
    }
}
