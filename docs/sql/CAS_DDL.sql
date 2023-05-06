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
-- Table structure for t_cas_approvals
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_approvals`;
CREATE TABLE `t_cas_approvals`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端ID',
    `user_id`       bigint unsigned NOT NULL COMMENT '用户id',
    `expires_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '到期时间',
    `created_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='CAS 授权批准';

-- ----------------------------
-- Table structure for t_cas_approvals_scope
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_approvals_scope`;
CREATE TABLE `t_cas_approvals_scope`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `approvals_id`  bigint unsigned NOT NULL COMMENT '审批id',
    `scope`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '作用域',
    `created_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                       NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='CAS 批准作用域';

-- ----------------------------
-- Table structure for t_cas_client
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client`;
CREATE TABLE `t_cas_client`
(
    `id`                  bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端编号',
    `client_id_issued_at` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '客户端编号颁发时间',
    `secret`              varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '客户端密码',
    `secret_expires_at`   timestamp NULL DEFAULT NULL COMMENT '客户端密码过期时间',
    `name`                varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端名称',
    `alias`               varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '别名(全局唯一)',
    `description`         varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述',
    `created_by_id`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`        datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`        datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`             bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='CAS 客户端';

-- ----------------------------
-- Table structure for t_cas_client_authentication_method
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_authentication_method`;
CREATE TABLE `t_cas_client_authentication_method`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端 id',
    `value`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '身份验证方法',
    `created_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='CAS 客户端身份验证方法';

-- ----------------------------
-- Table structure for t_cas_client_grant_type
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_grant_type`;
CREATE TABLE `t_cas_client_grant_type`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端 id',
    `value`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '授权类型',
    `created_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='CAS 客户端授权类型';

-- ----------------------------
-- Table structure for t_cas_client_redirect_url
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_redirect_url`;
CREATE TABLE `t_cas_client_redirect_url`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '客户端 id',
    `value`         varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '重定向地址',
    `created_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                         NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='CAS 客户端重定向地址';

-- ----------------------------
-- Table structure for t_cas_client_scope
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_scope`;
CREATE TABLE `t_cas_client_scope`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端 id',
    `name`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '作用域',
    `description`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述',
    `created_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='CAS 客户端作用域';

-- ----------------------------
-- Table structure for t_cas_client_scope_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_scope_permission`;
CREATE TABLE `t_cas_client_scope_permission`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `scope_id`      bigint unsigned NOT NULL COMMENT '作用域id',
    `permission_id` bigint unsigned NOT NULL COMMENT '权限id',
    `created_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                       NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='CAS 作用域权限关系';

-- ----------------------------
-- Table structure for t_cas_client_setting
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_setting`;
CREATE TABLE `t_cas_client_setting`
(
    `id`                            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`                     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '客户端 id',
    `require_proof_key`             bit(1)                                                         NOT NULL DEFAULT b'1' COMMENT '是否启用验证授权码参数(PKCE);0:禁用,1:启用',
    `require_authorization_consent` bit(1)                                                         NOT NULL DEFAULT b'1' COMMENT '是否启用授权同意;0:禁用,1:启用',
    `jwk_set_url`                   varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'JWK 秘钥集 URL',
    `signing_algorithm`             varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '签名算法',
    `approvals_duration`            bigint unsigned NOT NULL DEFAULT '2592000' COMMENT '授权同意持续时间(单位秒,默认 2592000 秒)',
    `created_by_id`                 varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`                  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id`                 varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`                  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`                       bit(1)                                                         NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='CAS 客户端设置';

-- ----------------------------
-- Table structure for t_cas_client_token_setting
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_token_setting`;
CREATE TABLE `t_cas_client_token_setting`
(
    `id`                          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`                   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端 id',
    `authorization_code_duration` bigint unsigned NOT NULL DEFAULT '300' COMMENT '授权码持续时间(单位秒,默认 300 秒)',
    `single_access_token`         bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否启用单一授权(签发新令牌后自动吊销旧令牌);0:禁用,1:启用',
    `access_token_duration`       bigint unsigned NOT NULL DEFAULT '1800' COMMENT '访问令牌持续时间(单位秒,默认 1800 秒)',
    `access_token_format`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '访问令牌格式;self-contained:自给(JWT),reference:引用(无信息)',
    `token_value_format`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '令牌值格式',
    `reuse_refresh_token`         bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否启用重复使用刷新令牌;0:禁用,1:启用',
    `refresh_token_duration`      bigint unsigned NOT NULL DEFAULT '3600' COMMENT '刷新令牌持续时间(单位秒,默认 3600 秒)',
    `signing_algorithm`           varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '签名算法',
    `created_by_id`               varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`                datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id`               varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`                datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`                     bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='CAS 客户端令牌设置';

-- ----------------------------
-- Table structure for t_cas_client_user
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_user`;
CREATE TABLE `t_cas_client_user`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `user_id`       bigint unsigned NOT NULL COMMENT '用户id',
    `client_id`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端 id',
    `login_name`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '登录名(客户端唯一)',
    `password`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
    `created_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='CAS 客户端用户';

-- ----------------------------
-- Table structure for t_cas_client_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_client_user_role`;
CREATE TABLE `t_cas_client_user_role`
(
    `id`             bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_user_id` bigint unsigned NOT NULL COMMENT '客户端用户id',
    `role_id`        bigint unsigned NOT NULL COMMENT '角色id',
    `created_by_id`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`   datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`   datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`        bit(1)                                                       NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='CAS 客户端用户角色关系';

-- ----------------------------
-- Table structure for t_cas_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_permission`;
CREATE TABLE `t_cas_permission`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'OAuth2 客户端id',
    `name`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '权限名',
    `description`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT '' COMMENT '描述',
    `created_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='CAS 权限';

-- ----------------------------
-- Table structure for t_cas_role
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_role`;
CREATE TABLE `t_cas_role`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'OAuth2 客户端id',
    `name`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '角色名',
    `description`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述',
    `created_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='CAS 角色';

-- ----------------------------
-- Table structure for t_cas_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_role_permission`;
CREATE TABLE `t_cas_role_permission`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `role_id`       bigint unsigned NOT NULL COMMENT '用户id',
    `permission_id` bigint unsigned NOT NULL COMMENT '权限id',
    `created_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                       NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='CAS 角色权限关系';

-- ----------------------------
-- Table structure for t_cas_user
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_user`;
CREATE TABLE `t_cas_user`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `open_id`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'CAS 全局唯一id',
    `name`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '用户名',
    `description`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述',
    `created_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 ID',
    `created_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '创建时间',
    `updated_by_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 ID',
    `updated_date`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '更新时间',
    `deleted`       bit(1)                                                        NOT NULL DEFAULT b'0' COMMENT '是否删除;0:未删除,1:删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='CAS 用户';

SET
FOREIGN_KEY_CHECKS = 1;
