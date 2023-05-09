package com.maoatao.cas.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.maoatao.cas.core.bean.entity.ClientEntity;
import com.maoatao.cas.core.bean.entity.ClientScopeEntity;
import com.maoatao.cas.core.service.ApprovalsService;
import com.maoatao.cas.core.service.ClientScopeService;
import com.maoatao.cas.core.service.ClientService;
import com.maoatao.cas.core.service.IndexService;
import com.maoatao.cas.security.bean.CustomUserDetails;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * 静态页面 thymeleaf 索引
 *
 * @author LiYuanHao
 * @date 2023-05-09 17:19:09
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private ApprovalsService approvalsService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientScopeService clientScopeService;

    @Override
    public void consent(String scope, String clientId, String state, Authentication authentication, Model model) {
        // 请求的作用域
        Set<String> requestedScopes = new HashSet<>(StrUtil.split(scope, " "));
        // 作用域
        Set<Map<String, Object>> scopes = new HashSet<>();
        if (authentication instanceof UsernamePasswordAuthenticationToken token) {
            if (token.getPrincipal() instanceof CustomUserDetails userDetails) {
                model.addAttribute("userName", userDetails.getUsername());
                List<String> authorizedScopes = approvalsService.listScopeNamesByClientUser(clientId, userDetails.getUsername());
                Map<String, String> scopeNames = clientScopeService.listByScopeNames(new ArrayList<>(requestedScopes)).stream()
                        .collect(Collectors.toMap(ClientScopeEntity::getName, ClientScopeEntity::getDescription, (v1, v2) -> v1));
                for (String requestedScope : requestedScopes) {
                    if (authorizedScopes.contains(requestedScope)) {
                        scopes.add(buildScope(scopeNames, requestedScope, Boolean.TRUE));
                    } else if (!requestedScope.equals(OidcScopes.OPENID)) {
                        scopes.add(buildScope(scopeNames, requestedScope, Boolean.FALSE));
                    }
                }
            }
        }
        ClientEntity clientEntity = clientService.getByClientId(clientId);
        model.addAttribute("scopes", scopes);
        model.addAttribute("clientId", clientId);
        Optional.ofNullable(clientEntity).ifPresent(o -> model.addAttribute("clientName", o.getName()));
        model.addAttribute("state", state);
    }

    private Map<String, Object> buildScope(Map<String, String> scopeNames, String requestedScope, Boolean checked) {
        return Map.of("name", requestedScope, "description", StrUtil.isEmpty(scopeNames.get(requestedScope)) ? requestedScope : scopeNames.get(requestedScope), "checked", checked);
    }
}
