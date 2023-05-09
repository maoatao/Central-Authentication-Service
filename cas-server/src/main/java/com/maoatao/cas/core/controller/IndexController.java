package com.maoatao.cas.core.controller;

import com.maoatao.cas.core.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 静态页面 thymeleaf 索引
 *
 * @author MaoAtao
 * @date 2023-05-08 18:29:12
 */
@Slf4j
@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;

    /**
     * 登录页
     */
    @GetMapping("login")
    public String login() {
        return "login";
    }

    /**
     * 授权同意页
     */
    @GetMapping("/oauth2/consent")
    public String consent(@RequestParam String scope, @RequestParam String client_id, @RequestParam String state, Authentication authentication, Model model) {
        indexService.consent(scope, client_id, state, authentication, model);
        return "consent";
    }
}
