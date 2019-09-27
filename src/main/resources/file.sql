/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : file

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2019-09-27 11:18:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for codes
-- ----------------------------
DROP TABLE IF EXISTS `codes`;
CREATE TABLE `codes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`id`,`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of codes
-- ----------------------------
INSERT INTO `codes` VALUES ('7', 'ac431c0f065e47cd94874813eb1e34f2', '1447566902@qq.com');

-- ----------------------------
-- Table structure for t_file
-- ----------------------------
DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file` (
  `file_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件唯一标识',
  `type_id` int(11) DEFAULT NULL COMMENT '文件所属类别ID',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件上传后服务器返回的名称',
  `file_describer` varchar(255) DEFAULT NULL COMMENT '资源详细描述',
  `file_path` varchar(255) DEFAULT NULL COMMENT '文件路径',
  `file_label` varchar(255) DEFAULT NULL COMMENT '文件标签',
  `file_img_path` varchar(255) DEFAULT NULL COMMENT '文件配图路径',
  `create_time` datetime DEFAULT NULL,
  `last_update_time` datetime DEFAULT NULL,
  `file_suffix` varchar(255) DEFAULT NULL,
  `file_size` bigint(255) DEFAULT NULL COMMENT '文件大小，以byte为单位',
  `file_title` varchar(255) DEFAULT NULL COMMENT '资源标题',
  `exe_name` varchar(255) DEFAULT NULL COMMENT '可执行程序名称，仅当filetype为exe时填写该字段',
  `img_name` varchar(255) DEFAULT NULL COMMENT '图片上传后服务器返回的名称',
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_file
-- ----------------------------
INSERT INTO `t_file` VALUES ('2', '4', '53cdd1f7c1f21.jpg', null, 'D:\\FileUpload1569226939801.jpg', null, null, '2019-09-23 16:22:20', '2019-09-23 16:22:20', '.jpg', '235257', null, null, null);
INSERT INTO `t_file` VALUES ('3', '4', '53cdd1f7c1f21.jpg', null, 'D:\\FileUpload1569227116743.jpg', null, null, '2019-09-23 16:25:17', '2019-09-23 16:25:17', '.jpg', '235257', null, null, null);
INSERT INTO `t_file` VALUES ('4', '4', '53cdd1f7c1f21.jpg', null, 'D:\\FileUpload1569233197975.jpg', null, null, '2019-09-23 18:06:38', '2019-09-23 18:06:38', '.jpg', '235257', null, null, null);
INSERT INTO `t_file` VALUES ('5', '5', 'sys_user.sql', null, 'D:\\FileUpload\\1569289579361', null, null, null, null, '.sql', '1159', null, null, null);
INSERT INTO `t_file` VALUES ('6', '6', 'pom.xml', 'wwwwww', 'D:\\FileUpload\\1569297357349', null, null, null, null, '.xml', '2466', 'qqqqqqq', 'eeeedd', null);
INSERT INTO `t_file` VALUES ('7', null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `t_file` VALUES ('8', null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `t_file` VALUES ('9', null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `t_file` VALUES ('10', null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `t_file` VALUES ('11', null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `t_file` VALUES ('12', null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `t_file` VALUES ('13', null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `t_file` VALUES ('14', null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `t_file` VALUES ('17', '1', 'xxxxx.exe', '资源描述1', null, null, null, '2019-09-26 03:02:00', null, null, '123456', '资源1', '资源1.exe', '资源1配图');
INSERT INTO `t_file` VALUES ('18', '1', '资源2', '资源56', null, null, null, '2019-09-26 06:39:33', null, null, '123', '资源56', null, '资源2');

-- ----------------------------
-- Table structure for t_file_img
-- ----------------------------
DROP TABLE IF EXISTS `t_file_img`;
CREATE TABLE `t_file_img` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_id` int(11) NOT NULL COMMENT '上传文件唯一标识',
  `img_id` int(11) NOT NULL COMMENT '配图文件唯一标识',
  `create_time` datetime DEFAULT NULL,
  `last_update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_file_img
-- ----------------------------

-- ----------------------------
-- Table structure for t_type
-- ----------------------------
DROP TABLE IF EXISTS `t_type`;
CREATE TABLE `t_type` (
  `type_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '类别唯一标识',
  `type_name` varchar(255) DEFAULT NULL COMMENT '类别名称',
  `type_describer` varchar(2550) DEFAULT NULL COMMENT '类别描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_type
-- ----------------------------
INSERT INTO `t_type` VALUES ('1', 'exe', 'exe文件', '2019-09-25 09:24:41', null);
INSERT INTO `t_type` VALUES ('2', 'mp4', 'mp4文件', '2019-09-25 09:25:07', null);
INSERT INTO `t_type` VALUES ('3', 'doc', 'doc文件', '2019-09-25 02:44:13', null);
INSERT INTO `t_type` VALUES ('4', 'jpg', 'jpg文件', '2019-09-26 10:11:39', null);
INSERT INTO `t_type` VALUES ('5', 'sql', 'sql文件', '2019-09-26 10:39:17', null);
INSERT INTO `t_type` VALUES ('6', 'xml', 'xml文件', '2019-09-26 10:39:41', null);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `passwd` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `usertype` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('84926da17933484c90f60a1696a648f5', '123456', 'e10adc3949ba59abbe56e057f20f883e', '1447566902@qq.com', '2');
INSERT INTO `users` VALUES ('9ed2e37a95cb4962961b16a18f15f06d', '165153', '8d515e45f2066cbe6926a116105550d2', '1651654132', '2');
INSERT INTO `users` VALUES ('c267c5e2e8564e368c61a5fb59ea3426', '464135', '34a97642542b2a28abfe718fecc492f9', '46845132132', '2');
INSERT INTO `users` VALUES ('dbb482a6f1e24b118a192db8efdfcd58', 'admin', '21232f297a57a5a743894a0e4a801fc3', '1234567890@qq.com', '1');
