package com.maoatao.cas.security.oauth2.auth;

import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;

import java.util.function.Supplier;

/**
 * 自定义 CustomAuthorizationServerContext
 * <p>
 * 源码不能公共访问,这里复制了一边
 *
 * @author MaoAtao
 * @date 2023-03-05 15:09:02
 */
public class CustomAuthorizationServerContext implements AuthorizationServerContext {
    private final Supplier<String> issuerSupplier;
    private final AuthorizationServerSettings authorizationServerSettings;

    public CustomAuthorizationServerContext(Supplier<String> issuerSupplier, AuthorizationServerSettings authorizationServerSettings) {
        this.issuerSupplier = issuerSupplier;
        this.authorizationServerSettings = authorizationServerSettings;
    }

    @Override
    public String getIssuer() {
        return this.issuerSupplier.get();
    }

    @Override
    public AuthorizationServerSettings getAuthorizationServerSettings() {
        return this.authorizationServerSettings;
    }

}
