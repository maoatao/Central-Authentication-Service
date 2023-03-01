package com.maoatao.cas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Central Authentication Service (CAS 中央认证服务 OAuth2.1协议)
 * <p>
 * 基于Spring Authorization Server开发
 *
 * @author MaoAtao
 * @date 2022-10-23 23:31:22
 */
@SpringBootApplication
public class CASApplication {

    public static void main(String[] args) {
        SpringApplication.run(CASApplication.class, args);
    }

}
