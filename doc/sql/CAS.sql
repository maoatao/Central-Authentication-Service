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

 Date: 28/02/2023 14:48:23
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_cas_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_permission`;
CREATE TABLE `t_cas_permission`
(
    `id`          int                                                     NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`   varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'OAuth2 客户端id',
    `name`        varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '权限名',
    `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT '' COMMENT '描述',
    `enabled`     tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否启用;0:禁用,1:启用',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='CAS 权限';

-- ----------------------------
-- Table structure for t_cas_role
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_role`;
CREATE TABLE `t_cas_role`
(
    `id`          int                                                     NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `client_id`   varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'OAuth2 客户端id',
    `name`        varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '角色名',
    `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '描述',
    `enabled`     tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否启用;0:禁用,1:启用',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='CAS 角色';

-- ----------------------------
-- Table structure for t_cas_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_role_permission`;
CREATE TABLE `t_cas_role_permission`
(
    `id`            int NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `role_id`       int NOT NULL COMMENT '用户id',
    `permission_id` int NOT NULL COMMENT '权限id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='CAS 角色权限关系';

-- ----------------------------
-- Table structure for t_cas_user
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_user`;
CREATE TABLE `t_cas_user`
(
    `id`        int                                                     NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `unique_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT 'CAS 全局唯一id',
    `client_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'OAuth2 客户端id',
    `name`      varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '' COMMENT '用户名',
    `password`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '密码',
    `enabled`   tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否启用;0:禁用,1:启用',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='CAS 用户表';

-- ----------------------------
-- Table structure for t_cas_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_cas_user_role`;
CREATE TABLE `t_cas_user_role`
(
    `id`      int NOT NULL AUTO_INCREMENT COMMENT '主键id(自增)',
    `user_id` int NOT NULL COMMENT '用户id',
    `role_id` int NOT NULL COMMENT '用户id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='CAS 用户角色关系';

SET
FOREIGN_KEY_CHECKS = 1;
