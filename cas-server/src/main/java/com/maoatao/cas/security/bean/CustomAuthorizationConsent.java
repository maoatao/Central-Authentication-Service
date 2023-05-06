package com.maoatao.cas.security.bean;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.util.SpringAuthorizationServerVersion;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * 自定义授权同意
 * <p>
 * Customized by {@link org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent}
 *
 * @author MaoAtao
 * @date 2023-05-06 10:54:46
 */
public class CustomAuthorizationConsent {
    private static final String AUTHORITIES_SCOPE_PREFIX = "SCOPE_";

    private final String registeredClientId;
    private final String principalName;
    private final Set<GrantedAuthority> authorities;

    private CustomAuthorizationConsent(String registeredClientId, String principalName, Set<GrantedAuthority> authorities) {
        this.registeredClientId = registeredClientId;
        this.principalName = principalName;
        this.authorities = Collections.unmodifiableSet(authorities);
    }

    /**
     * Returns the identifier for the {@link RegisteredClient#getId() registered client}.
     *
     * @return the {@link RegisteredClient#getId()}
     */
    public String getRegisteredClientId() {
        return this.registeredClientId;
    }

    /**
     * Returns the {@code Principal} name of the resource owner (or client).
     *
     * @return the {@code Principal} name of the resource owner (or client)
     */
    public String getPrincipalName() {
        return this.principalName;
    }

    /**
     * Returns the {@link GrantedAuthority authorities} granted to the client by the principal.
     *
     * @return the {@link GrantedAuthority authorities} granted to the client by the principal.
     */
    public Set<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * Convenience method for obtaining the {@code scope}s granted to the client by the principal,
     * extracted from the {@link #getAuthorities() authorities}.
     *
     * @return the {@code scope}s granted to the client by the principal.
     */
    public Set<String> getScopes() {
        Set<String> authorities = new HashSet<>();
        for (GrantedAuthority authority : getAuthorities()) {
            if (authority.getAuthority().startsWith(AUTHORITIES_SCOPE_PREFIX)) {
                authorities.add(authority.getAuthority().replaceFirst(AUTHORITIES_SCOPE_PREFIX, ""));
            }
        }
        return authorities;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CustomAuthorizationConsent that = (CustomAuthorizationConsent) obj;
        return Objects.equals(this.registeredClientId, that.registeredClientId) &&
                Objects.equals(this.principalName, that.principalName) &&
                Objects.equals(this.authorities, that.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.registeredClientId, this.principalName, this.authorities);
    }

    /**
     * Returns a new {@link CustomAuthorizationConsent.Builder}, initialized with the values from the provided {@code CustomAuthorizationConsent}.
     *
     * @param authorizationConsent the {@code CustomAuthorizationConsent} used for initializing the {@link CustomAuthorizationConsent.Builder}
     * @return the {@link CustomAuthorizationConsent.Builder}
     */
    public static CustomAuthorizationConsent.Builder from(CustomAuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        return new CustomAuthorizationConsent.Builder(
                authorizationConsent.getRegisteredClientId(),
                authorizationConsent.getPrincipalName(),
                authorizationConsent.getAuthorities()
        );
    }

    /**
     * Returns a new {@link CustomAuthorizationConsent.Builder}, initialized with the given {@link RegisteredClient#getClientId() registeredClientId}
     * and {@code Principal} name.
     *
     * @param registeredClientId the {@link RegisteredClient#getId()}
     * @param principalName      the  {@code Principal} name
     * @return the {@link CustomAuthorizationConsent.Builder}
     */
    public static CustomAuthorizationConsent.Builder withId(@NonNull String registeredClientId, @NonNull String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        return new CustomAuthorizationConsent.Builder(registeredClientId, principalName);
    }


    /**
     * A builder for {@link CustomAuthorizationConsent}.
     */
    public static final class Builder implements Serializable {
        private static final long serialVersionUID = SpringAuthorizationServerVersion.SERIAL_VERSION_UID;

        private final String registeredClientId;
        private final String principalName;
        private final Set<GrantedAuthority> authorities = new HashSet<>();

        private Builder(String registeredClientId, String principalName) {
            this(registeredClientId, principalName, Collections.emptySet());
        }

        private Builder(String registeredClientId, String principalName, Set<GrantedAuthority> authorities) {
            this.registeredClientId = registeredClientId;
            this.principalName = principalName;
            if (!CollectionUtils.isEmpty(authorities)) {
                this.authorities.addAll(authorities);
            }
        }

        /**
         * Adds a scope to the collection of {@code authorities} in the resulting {@link CustomAuthorizationConsent},
         * wrapping it in a {@link SimpleGrantedAuthority}, prefixed by {@code SCOPE_}. For example, a
         * {@code message.write} scope would be stored as {@code SCOPE_message.write}.
         *
         * @param scope the scope
         * @return the {@code Builder} for further configuration
         */
        public CustomAuthorizationConsent.Builder scope(String scope) {
            authority(new SimpleGrantedAuthority(AUTHORITIES_SCOPE_PREFIX + scope));
            return this;
        }

        /**
         * Adds a {@link GrantedAuthority} to the collection of {@code authorities} in the
         * resulting {@link CustomAuthorizationConsent}.
         *
         * @param authority the {@link GrantedAuthority}
         * @return the {@code Builder} for further configuration
         */
        public CustomAuthorizationConsent.Builder authority(GrantedAuthority authority) {
            this.authorities.add(authority);
            return this;
        }

        /**
         * A {@code Consumer} of the {@code authorities}, allowing the ability to add, replace or remove.
         *
         * @param authoritiesConsumer a {@code Consumer} of the {@code authorities}
         * @return the {@code Builder} for further configuration
         */
        public CustomAuthorizationConsent.Builder authorities(Consumer<Set<GrantedAuthority>> authoritiesConsumer) {
            authoritiesConsumer.accept(this.authorities);
            return this;
        }

        /**
         * Validate the authorities and build the {@link CustomAuthorizationConsent}.
         * There must be at least one {@link GrantedAuthority}.
         *
         * @return the {@link CustomAuthorizationConsent}
         */
        public CustomAuthorizationConsent build() {
            Assert.notEmpty(this.authorities, "authorities cannot be empty");
            return new CustomAuthorizationConsent(this.registeredClientId, this.principalName, this.authorities);
        }
    }
}
