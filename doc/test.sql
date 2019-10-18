/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2019-10-18 17:57:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_base_op
-- ----------------------------
DROP TABLE IF EXISTS `t_base_op`;
CREATE TABLE `t_base_op` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `flag` int(2) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `description` varchar(64) DEFAULT NULL COMMENT '密码',
  `del_flag` int(2) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_base_op
-- ----------------------------
INSERT INTO `t_base_op` VALUES ('1', '1', '访问', '访问', '0');
INSERT INTO `t_base_op` VALUES ('2', '2', '添加', '添加', '0');

-- ----------------------------
-- Table structure for t_module
-- ----------------------------
DROP TABLE IF EXISTS `t_module`;
CREATE TABLE `t_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `description` varchar(64) DEFAULT NULL COMMENT '密码',
  `url_prefix` varchar(256) DEFAULT NULL,
  `del_flag` int(2) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_module
-- ----------------------------
INSERT INTO `t_module` VALUES ('1', '系统管理', '系统管理', 'system/', '0');
INSERT INTO `t_module` VALUES ('2', '用户管理', '用户管理', 'user/', '0');

-- ----------------------------
-- Table structure for t_module_base_ops
-- ----------------------------
DROP TABLE IF EXISTS `t_module_base_ops`;
CREATE TABLE `t_module_base_ops` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `module_id` int(11) DEFAULT NULL,
  `flag` int(2) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `description` varchar(64) DEFAULT NULL COMMENT '密码',
  `op_url` varchar(256) DEFAULT NULL,
  `del_flag` int(2) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_module_base_ops
-- ----------------------------
INSERT INTO `t_module_base_ops` VALUES ('1', '1', '1', '系统访问', '访问系统模块', 'all:/system/', '0');
INSERT INTO `t_module_base_ops` VALUES ('2', '2', '1', '用户访问', '访问用户模块', 'post:/user/list/', '0');
INSERT INTO `t_module_base_ops` VALUES ('3', '2', '2', '用户添加', '可以添加用户', 'post:/user/', '0');

-- ----------------------------
-- Table structure for t_module_extend_ops
-- ----------------------------
DROP TABLE IF EXISTS `t_module_extend_ops`;
CREATE TABLE `t_module_extend_ops` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `module_id` int(11) DEFAULT NULL,
  `flag` int(2) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `description` varchar(64) DEFAULT NULL COMMENT '密码',
  `op_url` varchar(256) DEFAULT NULL,
  `del_flag` int(2) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_module_extend_ops
-- ----------------------------
INSERT INTO `t_module_extend_ops` VALUES ('1', '1', '1', '配置策略', '配置策略', 'system/policy/', '0');
INSERT INTO `t_module_extend_ops` VALUES ('2', '2', '1', '审核', '审核', 'user/confirm/', '0');
INSERT INTO `t_module_extend_ops` VALUES ('3', '2', '2', '驳回', '驳回', 'user/reject/', '0');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `description` varchar(64) DEFAULT NULL COMMENT '密码',
  `del_flag` int(2) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '超级管理员', '超级管理员', '0');
INSERT INTO `t_role` VALUES ('2', '管理员', '管理员', '0');

-- ----------------------------
-- Table structure for t_role_perms
-- ----------------------------
DROP TABLE IF EXISTS `t_role_perms`;
CREATE TABLE `t_role_perms` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `role_id` int(11) DEFAULT NULL,
  `module_id` int(11) DEFAULT NULL,
  `ops` int(11) DEFAULT NULL,
  `extend_flag` int(2) NOT NULL DEFAULT '0' COMMENT '是否是扩展操作',
  `del_flag` int(2) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_perms
-- ----------------------------
INSERT INTO `t_role_perms` VALUES ('5', '1', '1', '7', '0', '0');
INSERT INTO `t_role_perms` VALUES ('6', '1', '2', '2', '1', '0');
INSERT INTO `t_role_perms` VALUES ('7', '2', '2', '2', '1', '0');

-- ----------------------------
-- Table structure for t_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(64) DEFAULT NULL COMMENT '分类标签名',
  `top_flag` int(2) DEFAULT '0' COMMENT '是否置顶',
  `del_flag` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_tag
-- ----------------------------
INSERT INTO `t_tag` VALUES ('1', 'new tag', '0', '0');
INSERT INTO `t_tag` VALUES ('2', 'tag2', '0', '0');
INSERT INTO `t_tag` VALUES ('3', 'tag3', '1', '0');
INSERT INTO `t_tag` VALUES ('4', 'tag4', '0', '0');
INSERT INTO `t_tag` VALUES ('5', 'tag5', '0', '0');
INSERT INTO `t_tag` VALUES ('6', 'tag6', '0', '0');
INSERT INTO `t_tag` VALUES ('7', 'tag7', '1', '1');
INSERT INTO `t_tag` VALUES ('11', 'tag_n', '0', '0');
INSERT INTO `t_tag` VALUES ('12', 'tag_n1', '0', '0');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `login_name` varchar(64) DEFAULT NULL,
  `del_flag` int(2) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'name1', 'pwd', 'name1', '0');
INSERT INTO `t_user` VALUES ('2', 'test', '2Xd6pc6wc57baK46Hb01abI3d198Qc29a9257323q57re98b', 'test', '0');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` int(11) DEFAULT NULL COMMENT '名称',
  `role_id` int(11) DEFAULT NULL COMMENT '密码',
  `del_flag` int(2) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('3', '1', '1', '0');

-- ----------------------------
-- View structure for v_role_perms
-- ----------------------------
DROP VIEW IF EXISTS `v_role_perms`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_role_perms` AS select `a`.`role_id` AS `role_id`,`a`.`module_id` AS `module_id`,`b`.`name` AS `name`,`b`.`description` AS `description`,`b`.`op_url` AS `op_url` from (`t_role_perms` `a` join `t_module_base_ops` `b` on(((`a`.`module_id` = `b`.`module_id`) and ((`a`.`ops` & `b`.`flag`) > 0) and (`a`.`extend_flag` = 0)))) union select `a`.`role_id` AS `role_id`,`a`.`module_id` AS `module_id`,`c`.`name` AS `name`,`c`.`description` AS `description`,`c`.`op_url` AS `op_url` from (`t_role_perms` `a` join `t_module_extend_ops` `c` on(((`a`.`module_id` = `c`.`module_id`) and ((`a`.`ops` & `c`.`flag`) > 0) and (`a`.`extend_flag` = 1)))) ;
