/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : cas

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 28/02/2023 14:45:55
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth2_authorization
-- OAuth2AuthorizationService 使用 JDBC 实现时将要使用该表
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization`;
CREATE TABLE `oauth2_authorization`
(
    `id`                            varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键 id',
    `registered_client_id`          varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '注册的客户端ID',
    `principal_name`                varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主体名称',
    `authorization_grant_type`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权类型',
    `attributes`                    blob COMMENT '属性',
    `state`                         varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '状态',
    `authorization_code_value`      blob COMMENT '授权码值',
    `authorization_code_issued_at`  timestamp NULL DEFAULT NULL COMMENT '授权码颁发时间',
    `authorization_code_expires_at` timestamp NULL DEFAULT NULL COMMENT '授权码过期时间',
    `authorization_code_metadata`   blob COMMENT '授权码元数据',
    `access_token_value`            blob COMMENT '访问令牌值',
    `access_token_issued_at`        timestamp NULL DEFAULT NULL COMMENT '访问令牌颁发时间',
    `access_token_expires_at`       timestamp NULL DEFAULT NULL COMMENT '访问令牌过期时间',
    `access_token_metadata`         blob COMMENT '访问令牌元数据',
    `access_token_type`             varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '访问令牌类型',
    `access_token_scopes`           varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '访问令牌作用域',
    `oidc_id_token_value`           blob COMMENT 'OIDC ID令牌值',
    `oidc_id_token_issued_at`       timestamp NULL DEFAULT NULL COMMENT 'OIDC ID令牌颁发时间',
    `oidc_id_token_expires_at`      timestamp NULL DEFAULT NULL COMMENT 'OIDC ID令牌过期时间',
    `oidc_id_token_metadata`        blob COMMENT 'OIDC ID令牌元数据',
    `refresh_token_value`           blob COMMENT '刷新令牌值',
    `refresh_token_issued_at`       timestamp NULL DEFAULT NULL COMMENT '刷新令牌颁发时间',
    `refresh_token_expires_at`      timestamp NULL DEFAULT NULL COMMENT '刷新令牌过期时间',
    `refresh_token_metadata`        blob COMMENT '刷新令牌元数据',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='OAuth2 授权详情';

-- ----------------------------
-- Table structure for oauth2_authorization_consent
-- 储存授权同意的信息
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization_consent`;
CREATE TABLE `oauth2_authorization_consent`
(
    `registered_client_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '注册的客户端ID',
    `principal_name`       varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '主体名称',
    `authorities`          varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权内容',
    PRIMARY KEY (`registered_client_id`, `principal_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='OAuth2 授权同意';

-- ----------------------------
-- Table structure for oauth2_registered_client
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_registered_client`;
CREATE TABLE `oauth2_registered_client`
(
    `id`                            varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '主键 id',
    `client_id`                     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '客户端 id',
    `client_id_issued_at`           timestamp                                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '客户端 id 颁发时间',
    `client_secret`                 varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci           DEFAULT NULL COMMENT '客户端密码',
    `client_secret_expires_at`      timestamp NULL DEFAULT NULL COMMENT '客户端密码过期时间',
    `client_name`                   varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '客户端名称',
    `client_authentication_methods` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端身份验证方法',
    `authorization_grant_types`     varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权授权类型',
    `redirect_uris`                 varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '重定向URI',
    `scopes`                        varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作用域',
    `client_settings`               varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端设置',
    `token_settings`                varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '令牌设置',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='OAuth2 注册客户端';

SET
FOREIGN_KEY_CHECKS = 1;
