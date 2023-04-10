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

 Date: 28/02/2023 14:45:55
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth2_authorization
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
    `authorization_grant_types`     varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权类型',
    `redirect_uris`                 varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '重定向URI',
    `scopes`                        varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作用域',
    `client_settings`               varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端设置',
    `token_settings`                varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '令牌设置',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='OAuth2 注册客户端';

-- ----------------------------
-- Table structure for t_cas_client
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client`;
CREATE TABLE `t_cas_client`
(
    `id`                  bigint                                                  NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`           varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端 id',
    `client_id_issued_at` timestamp                                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '客户端 id 颁发时间',
    `secret`              varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '客户端密码',
    `secret_expires_at`   timestamp NULL DEFAULT NULL COMMENT '客户端密码过期时间',
    `name`                varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端名称',
    `description`         varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '描述',
    `created_by_id`       varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`        datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id`       varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`        datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`             bit(1)                                                  NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='CAS 客户端';

-- ----------------------------
-- Table structure for t_cas_client_authentication_method
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_authentication_method`;
CREATE TABLE `t_cas_client_authentication_method`
(
    `id`            bigint                                                  NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端 id',
    `value`         varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci           DEFAULT NULL COMMENT '客户端对应设定的值',
    `created_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                  NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='CAS 客户端身份验证方法';

-- ----------------------------
-- Table structure for t_cas_client_grant_type
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_grant_type`;
CREATE TABLE `t_cas_client_grant_type`
(
    `id`            bigint                                                  NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端 id',
    `value`         varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci           DEFAULT NULL COMMENT '授权类型',
    `created_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                  NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='CAS 客户端授权类型';

-- ----------------------------
-- Table structure for t_cas_client_redirect_url
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_redirect_url`;
CREATE TABLE `t_cas_client_redirect_url`
(
    `id`            bigint                                                  NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端 id',
    `value`         varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci         DEFAULT NULL COMMENT '重定向地址',
    `created_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                  NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='CAS 客户端重定向地址';

-- ----------------------------
-- Table structure for t_cas_client_scope
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_scope`;
CREATE TABLE `t_cas_client_scope`
(
    `id`            bigint                                                  NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端 id',
    `name`          varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci           DEFAULT NULL COMMENT '作用域',
    `description`   varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '描述',
    `created_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                  NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='CAS 客户端作用域';

-- ----------------------------
-- Table structure for t_cas_client_scope_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_scope_permission`;
CREATE TABLE `t_cas_client_scope_permission`
(
    `id`            bigint                                                 NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `scope_id`      int                                                    NOT NULL COMMENT '用户id',
    `permission_id` int                                                    NOT NULL COMMENT '权限id',
    `created_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                 NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='CAS 角色权限关系';

-- ----------------------------
-- Table structure for t_cas_client_setting
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_setting`;
CREATE TABLE `t_cas_client_setting`
(
    `id`                            bigint                                                  NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`                     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端 id',
    `require_proof_key`             bit(1)                                                  NOT NULL DEFAULT b'1' COMMENT '是否启用验证授权码参数(PKCE);0:禁用,1:启用',
    `require_authorization_consent` bit(1)                                                  NOT NULL DEFAULT b'1' COMMENT '是否启用授权同意;0:禁用,1:启用',
    `jwk_set_url`                   varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci         DEFAULT NULL COMMENT 'JWK 秘钥集 URL',
    `signing_algorithm`             varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci           DEFAULT NULL COMMENT '签名算法',
    `created_by_id`                 varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`                  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id`                 varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`                  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`                       bit(1)                                                  NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='CAS 客户端设置';

-- ----------------------------
-- Table structure for t_cas_client_token_setting
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_token_setting`;
CREATE TABLE `t_cas_client_token_setting`
(
    `id`                          bigint                                                  NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`                   varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端 id',
    `authorization_code_duration` bigint                                                  NOT NULL DEFAULT '300' COMMENT '授权码持续时间(单位秒,默认 300 秒)',
    `single_access_token`         bit(1)                                                  NOT NULL DEFAULT b'0' COMMENT '是否启用单一授权(签发新令牌后自动吊销旧令牌);0:禁用,1:启用',
    `access_token_duration`       bigint                                                  NOT NULL DEFAULT '1800' COMMENT '访问令牌持续时间(单位秒,默认 1800 秒)',
    `access_token_format`         varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '访问令牌格式',
    `token_value_format`          varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '令牌值格式',
    `reuse_refresh_token`         bit(1)                                                  NOT NULL DEFAULT b'0' COMMENT '是否启用重复使用刷新令牌;0:禁用,1:启用',
    `refresh_token_duration`      bigint                                                  NOT NULL DEFAULT '3600' COMMENT '刷新令牌持续时间(单位秒,默认 3600 秒)',
    `signing_algorithm`           varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '签名算法',
    `created_by_id`               varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`                datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id`               varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`                datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`                     bit(1)                                                  NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='CAS 客户端令牌设置';

-- ----------------------------
-- Table structure for t_cas_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_permission`;
CREATE TABLE `t_cas_permission`
(
    `id`            bigint                                                  NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'OAuth2 客户端id',
    `name`          varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '权限名',
    `description`   varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT '' COMMENT '描述',
    `created_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                  NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='CAS 权限';

-- ----------------------------
-- Table structure for t_cas_role
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_role`;
CREATE TABLE `t_cas_role`
(
    `id`            bigint                                                  NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'OAuth2 客户端id',
    `name`          varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '角色名',
    `description`   varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '描述',
    `created_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                  NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='CAS 角色';

-- ----------------------------
-- Table structure for t_cas_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_role_permission`;
CREATE TABLE `t_cas_role_permission`
(
    `id`            bigint                                                 NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `role_id`       bigint                                                 NOT NULL COMMENT '用户id',
    `permission_id` bigint                                                 NOT NULL COMMENT '权限id',
    `created_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                 NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='CAS 角色权限关系';

-- ----------------------------
-- Table structure for t_cas_user
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_user`;
CREATE TABLE `t_cas_user`
(
    `id`            bigint                                                  NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `open_id`       varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT 'CAS 全局唯一id',
    `client_id`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'OAuth2 客户端id',
    `name`          varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '用户名',
    `password`      varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '密码',
    `created_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                  NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='CAS 用户';

-- ----------------------------
-- Table structure for t_cas_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_user_role`;
CREATE TABLE `t_cas_user_role`
(
    `id`            bigint                                                 NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `user_id`       bigint                                                 NOT NULL COMMENT '用户id',
    `role_id`       bigint                                                 NOT NULL COMMENT '用户id',
    `created_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                 NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='CAS 用户角色关系';

SET
FOREIGN_KEY_CHECKS = 1;
