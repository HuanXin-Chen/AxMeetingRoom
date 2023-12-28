/*
 Navicat Premium Data Transfer

 Source Server         : CHX
 Source Server Type    : MySQL
 Source Server Version : 50744
 Source Host           : localhost:3306
 Source Schema         : meeting

 Target Server Type    : MySQL
 Target Server Version : 50744
 File Encoding         : 65001

 Date: 28/12/2023 16:59:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for application_record
-- ----------------------------
DROP TABLE IF EXISTS `application_record`;
CREATE TABLE `application_record`  (
  `application_id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `apply_time` datetime NOT NULL,
  `audit_time` datetime NULL DEFAULT NULL,
  `audit_status` int(1) NOT NULL DEFAULT 0,
  `reject_reason` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `apply_date` date NOT NULL,
  `apply_slot` int(11) NOT NULL,
  `meeting_theme` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `meeting_size` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`application_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of application_record
-- ----------------------------
INSERT INTO `application_record` VALUES (12, 1, 4, '2023-12-28 16:19:03', '2023-12-28 16:19:13', 1, '', '2024-01-02', 5, 'CTF复现', 30);

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `dept_id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dept_phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dept_no` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dept_password` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (1, '开发部', '15011144029', '1001', '123456');
INSERT INTO `department` VALUES (2, '运营部', '15011144029', '1002', '123456');
INSERT INTO `department` VALUES (3, '竞赛部', '15011144029', '1003', '123456');
INSERT INTO `department` VALUES (4, '科研部', '15011144029', '1004', '123456');

-- ----------------------------
-- Table structure for manager
-- ----------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager`  (
  `username` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of manager
-- ----------------------------
INSERT INTO `manager` VALUES ('admin', 'admin');

-- ----------------------------
-- Table structure for meeting_record
-- ----------------------------
DROP TABLE IF EXISTS `meeting_record`;
CREATE TABLE `meeting_record`  (
  `record_id` int(11) NOT NULL AUTO_INCREMENT,
  `meeting_theme` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `meeting_size` int(11) NOT NULL,
  `dept_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `meeting_date` date NOT NULL,
  `meeting_slot` int(11) NOT NULL,
  `apply_id` int(11) NOT NULL,
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meeting_record
-- ----------------------------

-- ----------------------------
-- Table structure for meeting_room
-- ----------------------------
DROP TABLE IF EXISTS `meeting_room`;
CREATE TABLE `meeting_room`  (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `room_no` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `room_size` int(11) NOT NULL,
  `room_status` tinyint(1) NOT NULL DEFAULT 1,
  `air` tinyint(1) NOT NULL DEFAULT 1,
  `projector` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`room_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meeting_room
-- ----------------------------
INSERT INTO `meeting_room` VALUES (11, '701', 20, 1, 1, 1);
INSERT INTO `meeting_room` VALUES (12, '702', 20, 1, 1, 1);
INSERT INTO `meeting_room` VALUES (13, '703', 40, 1, 1, 1);
INSERT INTO `meeting_room` VALUES (14, '704', 40, 1, 1, 1);
INSERT INTO `meeting_room` VALUES (15, '705', 60, 1, 1, 1);

-- ----------------------------
-- Table structure for status
-- ----------------------------
DROP TABLE IF EXISTS `status`;
CREATE TABLE `status`  (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `room_no` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `one` tinyint(1) NULL DEFAULT 1,
  `two` tinyint(1) NULL DEFAULT 1,
  `three` tinyint(1) NULL DEFAULT 1,
  `four` tinyint(1) NULL DEFAULT 1,
  `five` tinyint(1) NULL DEFAULT 1,
  `room_size` int(11) NULL DEFAULT NULL,
  `air` tinyint(1) NULL DEFAULT 1,
  `projector` tinyint(1) NULL DEFAULT 1,
  PRIMARY KEY (`room_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of status
-- ----------------------------
INSERT INTO `status` VALUES (11, '701', 1, 1, 1, 1, 1, 20, 1, 1);
INSERT INTO `status` VALUES (12, '702', 1, 1, 1, 1, 1, 20, 1, 1);
INSERT INTO `status` VALUES (13, '703', 1, 1, 1, 1, 1, 40, 1, 1);
INSERT INTO `status` VALUES (14, '704', 1, 1, 1, 1, 1, 40, 1, 1);
INSERT INTO `status` VALUES (15, '705', 1, 1, 1, 1, 1, 60, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
