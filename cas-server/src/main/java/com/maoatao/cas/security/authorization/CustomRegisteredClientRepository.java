package com.maoatao.cas.security.authorization;

import com.maoatao.cas.core.service.ClientService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * 自定义注册客户端储存
 * <p>
 * Customized by {@link org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository}
 *
 * @author MaoAtao
 * @date 2023-04-08 11:17:00
 */
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientService clientService;

    public CustomRegisteredClientRepository(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void save(RegisteredClient registeredClient) {

    }

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return null;
    }
}
