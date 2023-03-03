/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : cas

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 28/02/2023 14:48:23
*/

-- ----------------------------
-- 步骤 1 创建客户端
-- ----------------------------
INSERT INTO `cas`.`oauth2_registered_client` (`id`, `client_id`, `client_id_issued_at`, `client_secret`,
                                              `client_secret_expires_at`, `client_name`,
                                              `client_authentication_methods`, `authorization_grant_types`,
                                              `redirect_uris`, `scopes`, `client_settings`, `token_settings`)
VALUES ('c3f862e6-aaf0-4848-b9ea-12a9076977e2', 'test-client', '2023-03-03 20:05:40',
        '{bcrypt}$2a$10$hV3XpqAKPO/TaVMr0hmEXOuqXe7ZIRy47WU5wO.JGaaBgLPSStn1q', NULL,
        'c3f862e6-aaf0-4848-b9ea-12a9076977e2', 'client_secret_basic',
        'refresh_token,client_credentials,authorization_code', 'http://127.0.0.1:8080/authorized,https://cn.bing.com',
        'test.write,test.read',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":true,\"settings.client.require-authorization-consent\":false}',
        '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"reference\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000]}');

-- ----------------------------
-- 步骤 2 创建权限
-- ----------------------------
INSERT INTO `cas`.`t_cas_permission` (`id`, `client_id`, `name`, `description`, `enabled`)
VALUES (1, 'test-client', 'PERMISSION_TEST', '', 1);

-- ----------------------------
-- 步骤 3 创建角色
-- ----------------------------
INSERT INTO `cas`.`t_cas_role` (`id`, `client_id`, `name`, `description`, `enabled`)
VALUES (1, 'test-client', 'ROLE_TEST', '', 1);

-- ----------------------------
-- 步骤 4 绑定角色和权限
-- ----------------------------
INSERT INTO `cas`.`t_cas_role_permission` (`id`, `role_id`, `permission_id`)
VALUES (1, 1, 1);

-- ----------------------------
-- 步骤 5 创建用户
-- ----------------------------
INSERT INTO `cas`.`t_cas_user` (`id`, `unique_id`, `client_id`, `name`, `password`, `enabled`)
VALUES (1, 'U6401e2942a0cb26023ba055c', 'test-client', 'user',
        '{bcrypt}$2a$10$stcBm3H1qfgEOqeIhJHDbeiEfas/XXE5trPJPncUcRGozT.hejzzO', 1);

-- ----------------------------
-- 步骤 6 绑定用户和角色
-- ----------------------------
INSERT INTO `cas`.`t_cas_user_role` (`id`, `user_id`, `role_id`)
VALUES (1, 1, 1);

