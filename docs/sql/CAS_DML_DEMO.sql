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

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

BEGIN;

-- ----------------------------
-- 步骤 1 创建客户端
-- ----------------------------
INSERT INTO `cas`.`t_cas_client`(`id`, `client_id`, `client_id_issued_at`, `secret`, `secret_expires_at`, `name`)
VALUES (1, 'test-client', '2023-04-12 10:09:49', '{bcrypt}$2a$10$QugMw1PgDES2cBZDrLVkGeBOSF/GA5OsmPehd2sgLhs51XOHMPecy',
        NULL, '1dde2723-b43b-4017-a4ff-5ee38f3fb643');

INSERT INTO `cas`.`t_cas_client_authentication_method`(`id`, `client_id`, `value`)
VALUES (1, 'test-client', 'client_secret_basic');

INSERT INTO `cas`.`t_cas_client_grant_type`(`id`, `client_id`, `value`)
VALUES (1, 'test-client', 'refresh_token'),
       (2, 'test-client', 'client_credentials'),
       (3, 'test-client', 'authorization_code');

INSERT INTO `cas`.`t_cas_client_redirect_url`(`id`, `client_id`, `value`)
VALUES (1, 'test-client', 'http://127.0.0.1:8080/authorized'),
       (2, 'test-client', 'https://cn.bing.com');

INSERT INTO `cas`.`t_cas_client_scope`(`id`, `client_id`, `name`)
VALUES (1, 'test-client', 'test.write'),
       (2, 'test-client', 'test.read');

INSERT INTO `cas`.`t_cas_client_setting`(`id`, `client_id`, `require_proof_key`, `require_authorization_consent`)
VALUES (1, 'test-client', b'1', b'0');

INSERT INTO `cas`.`t_cas_client_token_setting`(`id`, `client_id`, `authorization_code_duration`, `single_access_token`,
                                               `access_token_duration`, `access_token_format`, `token_value_format`,
                                               `reuse_refresh_token`, `refresh_token_duration`, `signing_algorithm`)
VALUES (1, 'test-client', 3600, b'0', 1800, 'reference', '', b'1', 3600, 'RS256');

-- ----------------------------
-- 步骤 2 创建权限
-- ----------------------------
INSERT INTO `cas`.`t_cas_permission`(`id`, `client_id`, `name`)
VALUES (1, 'test-client', 'PERMISSION_TEST');

-- ----------------------------
-- 步骤 3 创建角色
-- ----------------------------
INSERT INTO `cas`.`t_cas_role` (`id`, `client_id`, `name`)
VALUES (1, 'test-client', 'ROLE_TEST');

-- ----------------------------
-- 步骤 4 绑定角色和权限
-- ----------------------------
INSERT INTO `cas`.`t_cas_role_permission` (`id`, `role_id`, `permission_id`)
VALUES (1, 1, 1);

-- ----------------------------
-- 步骤 5 创建用户
-- ----------------------------
INSERT INTO `cas`.`t_cas_user` (`id`, `open_id`, `client_id`, `name`, `password`)
VALUES (1, 'UO640ada346f505467dc399155', 'test-client', 'user',
        '{bcrypt}$2a$10$stcBm3H1qfgEOqeIhJHDbeiEfas/XXE5trPJPncUcRGozT.hejzzO');

-- ----------------------------
-- 步骤 6 绑定用户和角色
-- ----------------------------
INSERT INTO `cas`.`t_cas_user_role` (`id`, `user_id`, `role_id`)
VALUES (1, 1, 1);

COMMIT;

SET
FOREIGN_KEY_CHECKS = 1;