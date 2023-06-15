/*
 Navicat Premium Data Transfer

 Source Server         : aliyun
 Source Server Type    : MySQL
 Source Server Version : 80024
 Source Host           : 47.112.102.45:3306
 Source Schema         : nsdb

 Target Server Type    : MySQL
 Target Server Version : 80024
 File Encoding         : 65001

 Date: 01/07/2022 20:50:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ask_for_leave
-- ----------------------------
DROP TABLE IF EXISTS `ask_for_leave`;
CREATE TABLE `ask_for_leave`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '请假id',
  `user_id` int NULL DEFAULT NULL COMMENT '员工id',
  `replace_user_id` int NULL DEFAULT NULL COMMENT '顶班员工id',
  `start_date` date NULL DEFAULT NULL COMMENT '请假开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '请假结束日期',
  `leave_days` int NULL DEFAULT NULL COMMENT '请假天数',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请假原因',
  `status` tinyint NULL DEFAULT 0 COMMENT '批假状态（1：同意 0：拒绝）',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（1：是 0：否）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `replace_user_id`(`replace_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '请假表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ask_for_leave
-- ----------------------------
INSERT INTO `ask_for_leave` VALUES (1, 9, 0, '2022-08-02', '2022-08-02', 1, '照顾家人', 1, 0, '2022-05-13 10:14:19', '2022-07-01 19:52:10');
INSERT INTO `ask_for_leave` VALUES (2, 9, 7, '2022-08-05', '2022-08-05', 1, '大夜班请假', 1, 0, '2022-05-13 10:14:19', '2022-07-01 19:54:16');
INSERT INTO `ask_for_leave` VALUES (3, 10, 0, '2022-08-04', '2022-08-04', 1, '请假', 0, 0, '2022-05-15 15:30:10', '2022-07-01 19:13:05');
INSERT INTO `ask_for_leave` VALUES (4, 12, 0, '2022-08-04', '2022-08-04', 1, '不想上班', 0, 0, '2022-06-30 17:38:25', '2022-07-01 19:56:09');

-- ----------------------------
-- Table structure for change_shift
-- ----------------------------
DROP TABLE IF EXISTS `change_shift`;
CREATE TABLE `change_shift`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '换班id',
  `shift_id` int NULL DEFAULT NULL COMMENT '请求换班员工排班班次id',
  `user_id` int NULL DEFAULT NULL COMMENT '请求换班员工id',
  `schedule_date` date NULL DEFAULT NULL COMMENT '请求换班员工排班日期',
  `changed_shift_id` int NULL DEFAULT NULL COMMENT '被换班员工排班班次id',
  `changed_user_id` int NULL DEFAULT NULL COMMENT '被换班员工id',
  `changed_schedule_date` date NULL DEFAULT NULL COMMENT '被换班员工排班日期',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '换班原因',
  `status` tinyint NULL DEFAULT 0 COMMENT '换班状态（0：不同意换班 1：被换班员工同意换班 2：护士长也同意）',
  `is_delete` tinyint NULL DEFAULT 0 COMMENT '是否删除（1：是 0：否）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `changed_user_id`(`changed_user_id`) USING BTREE,
  INDEX `shift_id`(`shift_id`) USING BTREE,
  INDEX `changed_shift_id`(`changed_shift_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '换班表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of change_shift
-- ----------------------------
INSERT INTO `change_shift` VALUES (1, 1, 7, '2022-08-04', 2, 2, '2022-08-03', '关系好', 0, 0, '2022-05-16 12:25:39', '2022-07-01 19:56:19');
INSERT INTO `change_shift` VALUES (2, 1, 9, '2022-08-04', 2, 10, '2022-08-04', '换班', 0, 0, '2022-06-06 22:34:50', '2022-07-01 19:13:28');
INSERT INTO `change_shift` VALUES (3, 1, 9, '2022-08-10', 3, 12, '2022-08-10', NULL, 0, 0, '2022-06-19 18:03:45', '2022-07-01 19:13:33');
INSERT INTO `change_shift` VALUES (4, 3, 8, '2022-08-06', 1, 5, '2022-08-22', NULL, 0, 0, '2022-06-19 18:26:10', '2022-07-01 19:13:37');

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '科室id',
  `hospital_id` int NULL DEFAULT NULL COMMENT '医院id',
  `department_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '科室名称',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（1：是 0：否）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `department_name`(`department_name`) USING BTREE,
  INDEX `hospital_id`(`hospital_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '科室表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (1, 1, '未分配科室', 0, '2022-04-22 17:54:15', '2022-06-01 12:51:52');
INSERT INTO `department` VALUES (2, 1, '内科', 0, '2022-04-22 17:54:15', '2022-06-01 12:51:52');
INSERT INTO `department` VALUES (3, 1, '外科', 0, '2022-04-22 17:54:15', '2022-06-01 12:51:52');
INSERT INTO `department` VALUES (4, 1, '儿科', 0, '2022-04-22 17:54:15', '2022-06-01 12:51:52');
INSERT INTO `department` VALUES (5, 1, '妇产科', 0, '2022-04-22 17:54:15', '2022-06-01 12:51:52');
INSERT INTO `department` VALUES (6, 1, '耳鼻喉科', 0, '2022-04-22 20:14:50', '2022-06-01 12:51:52');
INSERT INTO `department` VALUES (7, 2, '儿科', 0, '2022-05-31 21:54:19', '2022-06-01 12:51:52');

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict`  (
  `dict_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据字典' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dict
-- ----------------------------
INSERT INTO `dict` VALUES ('home', 'el-icon-s-home', 'icon');
INSERT INTO `dict` VALUES ('user', 'el-icon-user', 'icon');
INSERT INTO `dict` VALUES ('user-solid', 'el-icon-user-solid', 'icon');
INSERT INTO `dict` VALUES ('office-building', 'el-icon-office-building', 'icon');
INSERT INTO `dict` VALUES ('menu', 'el-icon-menu', 'icon');
INSERT INTO `dict` VALUES ('files', 'el-icon-files', 'icon');
INSERT INTO `dict` VALUES ('s-grid', 'el-icon-s-grid', 'icon');
INSERT INTO `dict` VALUES ('message', 'el-icon-message', 'icon');
INSERT INTO `dict` VALUES ('date', 'el-icon-date', 'icon');
INSERT INTO `dict` VALUES ('order', 'el-icon-s-order', 'icon');
INSERT INTO `dict` VALUES ('claim', 'el-icon-s-claim', 'icon');
INSERT INTO `dict` VALUES ('time', 'el-icon-time', 'icon');
INSERT INTO `dict` VALUES ('tools', 'el-icon-s-tools', 'icon');
INSERT INTO `dict` VALUES ('refresh', 'el-icon-refresh', 'icon');
INSERT INTO `dict` VALUES ('school', 'el-icon-school', 'icon');
INSERT INTO `dict` VALUES ('first-aid-kit', 'el-icon-first-aid-kit', 'icon');
INSERT INTO `dict` VALUES ('chat-dot-square', 'el-icon-chat-dot-square', 'icon');
INSERT INTO `dict` VALUES ('s-opportunity', 'el-icon-s-opportunity', 'icon');
INSERT INTO `dict` VALUES ('timer', 'el-icon-timer', 'icon');

-- ----------------------------
-- Table structure for expectation
-- ----------------------------
DROP TABLE IF EXISTS `expectation`;
CREATE TABLE `expectation`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '期望id',
  `user_id` int NULL DEFAULT NULL COMMENT '员工id',
  `start_date` date NULL DEFAULT NULL COMMENT '期望开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '期望结束日期',
  `expected_days` int NULL DEFAULT NULL COMMENT '期望天数',
  `shift_sign` tinyint NULL DEFAULT NULL COMMENT '班次标识\r\n（对应三班制：1.白班 2.小夜班 3.大夜班）\r\n（对应两班制：1.早班 3.晚班 ）',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '期望原因',
  `status` tinyint NULL DEFAULT 0 COMMENT '期望状态（1：同意 0：拒绝）',
  `complete` tinyint NULL DEFAULT 0 COMMENT '完成期望状态（1：成功 0：失败）',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（1：是 0：否）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of expectation
-- ----------------------------
INSERT INTO `expectation` VALUES (1, 8, '2022-08-01', '2022-08-01', 1, 3, '', 1, 1, 0, '2022-06-13 14:19:26', '2022-07-01 19:52:44');
INSERT INTO `expectation` VALUES (2, 8, '2022-08-02', '2022-08-02', 1, 3, NULL, 1, 0, 0, '2022-06-14 10:25:48', '2022-07-01 19:52:44');
INSERT INTO `expectation` VALUES (3, 8, '2022-08-03', '2022-08-03', 1, 3, NULL, 1, 1, 0, '2022-06-14 20:02:15', '2022-07-01 19:52:44');
INSERT INTO `expectation` VALUES (4, 8, '2022-08-08', '2022-08-08', 1, 3, NULL, 1, 1, 0, '2022-06-14 20:02:15', '2022-07-01 19:52:44');
INSERT INTO `expectation` VALUES (5, 8, '2022-08-10', '2022-08-10', 1, 3, NULL, 1, 1, 0, '2022-06-14 20:02:15', '2022-07-01 19:52:44');
INSERT INTO `expectation` VALUES (6, 8, '2022-08-12', '2022-08-12', 1, 3, NULL, 1, 1, 0, '2022-06-14 20:02:15', '2022-07-01 19:52:44');
INSERT INTO `expectation` VALUES (7, 8, '2022-08-15', '2022-08-15', 1, 3, NULL, 1, 1, 0, '2022-06-14 20:02:15', '2022-07-01 19:52:44');
INSERT INTO `expectation` VALUES (8, 8, '2022-08-17', '2022-08-17', 1, 3, NULL, 1, 1, 0, '2022-06-14 20:02:15', '2022-07-01 19:52:44');
INSERT INTO `expectation` VALUES (9, 8, '2022-08-21', '2022-08-21', 1, 3, NULL, 1, 1, 0, '2022-06-14 20:02:15', '2022-07-01 19:52:44');
INSERT INTO `expectation` VALUES (10, 8, '2022-08-23', '2022-08-23', 1, 3, NULL, 1, 1, 0, '2022-06-14 20:02:15', '2022-07-01 19:52:44');
INSERT INTO `expectation` VALUES (11, 8, '2022-08-26', '2022-08-26', 1, 3, NULL, 1, 1, 0, '2022-06-14 22:20:23', '2022-07-01 19:52:44');
INSERT INTO `expectation` VALUES (12, 8, '2022-08-29', '2022-08-29', 1, 3, NULL, 1, 1, 0, '2022-06-14 22:14:35', '2022-07-01 19:52:44');
INSERT INTO `expectation` VALUES (13, 8, '2022-08-31', '2022-08-31', 1, 3, NULL, 1, 1, 0, '2022-06-14 22:14:35', '2022-07-01 19:52:44');

-- ----------------------------
-- Table structure for files
-- ----------------------------
DROP TABLE IF EXISTS `files`;
CREATE TABLE `files`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '文件id',
  `files_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名称',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件类型',
  `size` double(32, 2) NULL DEFAULT NULL COMMENT '文件大小（KB）',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '下载链接',
  `md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件md5',
  `status` tinyint NULL DEFAULT 1 COMMENT '链接是否可用（1：是 0：否）',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（1：是 0：否）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `md5`(`md5`) USING BTREE,
  INDEX `files_name`(`files_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of files
-- ----------------------------
INSERT INTO `files` VALUES (1, 'ashe.png', 'png', 18.40, 'http://cau1i.cn:8800/files/7b9d0bad501b4ee78feaadb881d7e60d.png', '6e7c651be9f443db161b376ff4974705', 1, 0, '2022-07-01 20:40:30', '2022-07-01 20:40:30');
INSERT INTO `files` VALUES (2, 'Jarvan IV.png', 'png', 398.57, 'http://cau1i.cn:8800/files/e47b3ab03ff24ec7a6ee6dad89468eff.png', 'd997e7ea9b6eeb7ff04a18365a587cdf', 1, 0, '2022-07-01 20:47:11', '2022-07-01 20:47:11');

-- ----------------------------
-- Table structure for hospital
-- ----------------------------
DROP TABLE IF EXISTS `hospital`;
CREATE TABLE `hospital`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '科室id',
  `hospital_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '医院名称',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细地址',
  `hospital_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '医院logo',
  `intro` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '医院简介',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（1：是 0：否）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `hospital_name`(`hospital_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '医院表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hospital
-- ----------------------------
INSERT INTO `hospital` VALUES (1, '北京协和医院', '大望路', 'http://localhost:8800/files/59fdb4eea6fe439e9a21bd7fa63a2936.png', '北京协和医院是集医疗、教学、科研于一体的大型三级甲等综合医院，是国家卫生计生委指定的全国疑难重症诊治指导中心，也是最早承担高干保健和外宾医疗任务的医院之一，以学科齐全、技术力量雄厚、特色专科突出、多学科综合优势强大享誉海内外。在2010、2011、2012、2013、2014年复旦大学医院管理研究所公布的“中国最佳医院排行榜”中连续五年名列榜首。\r\r医院建成于1921年，由洛克菲勒基金会创办。建院之初，就志在“建成亚洲最好的医学中心”。90余年来，医院形成了“严谨、求精、勤奋、奉献”的协和精神和兼容并蓄的特色文化风格，创立了“三基”、“三严”的现代医学教育理念，形成了以“教授、病案、图书馆”著称的协和“三宝”，培养造就了张孝骞、林巧稚等一代医学大师和多位中国现代医学的领军人物，并向全国输送了大批的医学管理人才，创建了当今知名的10余家大型综合及专科医院。2011年在总结90年发展经验的基础上，创新性提出了“待病人如亲人，提高病人满意度；待同事如家人，提高员工幸福感”新办院理念。\r\r目前，医院共有2个院区、总建筑面积53万平方米，在职职工4000余名、两院院士5人、临床和医技科室53个、国家级重点学科20个、国家临床重点专科29个、博士点16个、硕士点29个、国家级继续医学教育基地6个、二级学科住院医师培养基地18个、三级学科专科医师培养基地15个。开放住院床位2000余张，单日最高门诊量约1.5万人次、年出院病人约8万余人次。被评为“全国文明单位”、“全国创先争优先进基层党组织”、“全国卫生系统先进集体”、“首都卫生系统文明单位”、“最受欢迎三甲医院”，荣获全国五一劳动奖章。同时，医院还承担着支援老少边穷地区、国家重要活动和突发事件主力医疗队的重任，在2008年北京奥运工作中荣获“特别贡献奖”。\r\r90多年来，协和人以执着的医志、高尚的医德、精湛的医术和严谨的学风书写了辉煌的历史，今天的协和人正为打造“国际知名、国内一流”医院的目标而继续努力。', 0, '2022-05-29 11:48:18', '2022-06-01 18:15:42', '北京');
INSERT INTO `hospital` VALUES (2, '广州博爱医院', NULL, NULL, NULL, 0, '2022-05-31 19:03:55', '2022-06-01 18:58:05', '广东');
INSERT INTO `hospital` VALUES (3, '青山', NULL, NULL, '精神病院', 1, '2022-06-15 17:13:13', '2022-06-15 17:14:17', '香港');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `menu_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路径',
  `page_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '页面路径',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `pid` int NULL DEFAULT NULL COMMENT '父级id',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（1：是 0：否）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `menu_name`(`menu_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '首页', 'el-icon-s-home', '/home', 'Home', NULL, NULL, 0, '2022-05-01 12:13:58', '2022-05-04 12:07:11');
INSERT INTO `menu` VALUES (2, '任务管理', 'el-icon-s-grid', NULL, NULL, NULL, NULL, 0, '2022-05-01 12:16:53', '2022-05-15 11:36:53');
INSERT INTO `menu` VALUES (3, '系统管理', 'el-icon-s-tools', NULL, NULL, NULL, NULL, 0, '2022-05-15 11:34:40', '2022-05-15 11:44:32');
INSERT INTO `menu` VALUES (4, '聊天室', 'el-icon-chat-dot-square', '/chat', 'Chat', NULL, NULL, 0, '2022-06-05 12:58:40', '2022-06-05 13:30:54');
INSERT INTO `menu` VALUES (21, '用户管理', 'el-icon-user', '/user', 'User', NULL, 2, 0, '2022-05-01 18:14:46', '2022-05-16 12:16:45');
INSERT INTO `menu` VALUES (22, '工作量管理', 'el-icon-s-claim', '/workload', 'Workload', NULL, 2, 0, '2022-05-10 11:31:14', '2022-05-16 12:16:46');
INSERT INTO `menu` VALUES (23, '班次管理', 'el-icon-s-order', '/shift', 'Shift', NULL, 2, 0, '2022-05-06 08:42:44', '2022-05-16 12:16:48');
INSERT INTO `menu` VALUES (24, '排班管理', 'el-icon-date', '/schedule', 'Schedule', NULL, 2, 0, '2022-05-08 11:55:36', '2022-06-13 01:17:51');
INSERT INTO `menu` VALUES (25, '期望管理', 'el-icon-s-opportunity', '/expectation', 'Expectation', NULL, 2, 0, '2022-06-13 01:17:37', '2022-06-13 01:29:06');
INSERT INTO `menu` VALUES (26, '请假管理', 'el-icon-time', '/leave', 'Leave', NULL, 2, 0, '2022-05-13 08:29:16', '2022-06-13 01:17:41');
INSERT INTO `menu` VALUES (27, '换班管理', 'el-icon-refresh', '/changeShift', 'ChangeShift', NULL, 2, 0, '2022-05-16 12:18:27', '2022-06-13 01:17:39');
INSERT INTO `menu` VALUES (31, '角色管理', 'el-icon-user-solid', '/role', 'Role', NULL, 3, 0, '2022-05-01 19:07:35', '2022-05-16 12:16:57');
INSERT INTO `menu` VALUES (32, '医院管理', 'el-icon-school', '/hospital', 'Hospital', NULL, 3, 0, '2022-05-31 14:43:30', '2022-05-31 14:44:41');
INSERT INTO `menu` VALUES (33, '科室管理', 'el-icon-office-building', '/department', 'Department', NULL, 3, 0, '2022-05-01 19:11:13', '2022-05-31 14:43:07');
INSERT INTO `menu` VALUES (34, '菜单管理', 'el-icon-menu', '/menu', 'Menu', NULL, 3, 0, '2022-05-01 19:35:20', '2022-05-31 14:43:03');
INSERT INTO `menu` VALUES (35, '文件管理', 'el-icon-files', '/file', 'File', NULL, 3, 0, '2022-05-01 19:11:22', '2022-05-31 14:43:02');
INSERT INTO `menu` VALUES (99, '关于', 'el-icon-message', '/about', 'About', NULL, NULL, 0, '2022-05-01 19:09:36', '2022-05-16 12:17:16');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `level` tinyint NULL DEFAULT NULL COMMENT '等级',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（1：是 0：否）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_name`(`role_name`) USING BTREE,
  INDEX `level`(`level`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '管理员', 0, 'level 0 系统最高级权限管理员', 0, '2022-04-26 16:41:53', '2022-05-31 10:34:04');
INSERT INTO `role` VALUES (2, '总护士长', 1, 'level 1 医院最高级权限管理员，管理医院所有员工，是护士长上级', 0, '2022-05-31 10:22:58', '2022-05-31 10:34:02');
INSERT INTO `role` VALUES (3, '护士长', 2, 'level 2 科室护士长，管理护士和助手，是护士和助手的上级', 0, '2022-04-26 16:41:53', '2022-06-15 17:21:45');
INSERT INTO `role` VALUES (4, '护士', 3, 'level 3 护士', 0, '2022-04-26 16:41:53', '2022-06-15 17:21:42');
INSERT INTO `role` VALUES (5, '助手', 3, 'level 3 助手', 0, '2022-04-29 23:15:46', '2022-06-15 17:21:44');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `role_id` int NOT NULL COMMENT '角色id',
  `menu_id` int NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (1, 1);
INSERT INTO `role_menu` VALUES (1, 2);
INSERT INTO `role_menu` VALUES (1, 3);
INSERT INTO `role_menu` VALUES (1, 4);
INSERT INTO `role_menu` VALUES (1, 21);
INSERT INTO `role_menu` VALUES (1, 22);
INSERT INTO `role_menu` VALUES (1, 23);
INSERT INTO `role_menu` VALUES (1, 24);
INSERT INTO `role_menu` VALUES (1, 25);
INSERT INTO `role_menu` VALUES (1, 26);
INSERT INTO `role_menu` VALUES (1, 27);
INSERT INTO `role_menu` VALUES (1, 31);
INSERT INTO `role_menu` VALUES (1, 32);
INSERT INTO `role_menu` VALUES (1, 33);
INSERT INTO `role_menu` VALUES (1, 34);
INSERT INTO `role_menu` VALUES (1, 35);
INSERT INTO `role_menu` VALUES (1, 99);
INSERT INTO `role_menu` VALUES (2, 1);
INSERT INTO `role_menu` VALUES (2, 2);
INSERT INTO `role_menu` VALUES (2, 3);
INSERT INTO `role_menu` VALUES (2, 4);
INSERT INTO `role_menu` VALUES (2, 21);
INSERT INTO `role_menu` VALUES (2, 22);
INSERT INTO `role_menu` VALUES (2, 23);
INSERT INTO `role_menu` VALUES (2, 24);
INSERT INTO `role_menu` VALUES (2, 25);
INSERT INTO `role_menu` VALUES (2, 26);
INSERT INTO `role_menu` VALUES (2, 27);
INSERT INTO `role_menu` VALUES (2, 33);
INSERT INTO `role_menu` VALUES (2, 99);
INSERT INTO `role_menu` VALUES (3, 1);
INSERT INTO `role_menu` VALUES (3, 2);
INSERT INTO `role_menu` VALUES (3, 4);
INSERT INTO `role_menu` VALUES (3, 21);
INSERT INTO `role_menu` VALUES (3, 22);
INSERT INTO `role_menu` VALUES (3, 23);
INSERT INTO `role_menu` VALUES (3, 24);
INSERT INTO `role_menu` VALUES (3, 25);
INSERT INTO `role_menu` VALUES (3, 26);
INSERT INTO `role_menu` VALUES (3, 27);
INSERT INTO `role_menu` VALUES (3, 99);
INSERT INTO `role_menu` VALUES (4, 1);
INSERT INTO `role_menu` VALUES (4, 4);
INSERT INTO `role_menu` VALUES (4, 99);
INSERT INTO `role_menu` VALUES (5, 1);
INSERT INTO `role_menu` VALUES (5, 4);
INSERT INTO `role_menu` VALUES (5, 99);
INSERT INTO `role_menu` VALUES (6, 1);
INSERT INTO `role_menu` VALUES (6, 4);
INSERT INTO `role_menu` VALUES (6, 99);

-- ----------------------------
-- Table structure for schedule
-- ----------------------------
DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule`  (
  `schedule_date` date NOT NULL COMMENT '排班日期',
  `shift_id` int NOT NULL COMMENT '班次id',
  `user_id` int NOT NULL COMMENT '员工id',
  `leave_status` tinyint NULL DEFAULT 0 COMMENT '请假状态（1：请假 0：未请假）',
  PRIMARY KEY (`schedule_date`, `shift_id`, `user_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `shift_id`(`shift_id`) USING BTREE,
  INDEX `schedule_date`(`schedule_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '排班表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of schedule
-- ----------------------------
INSERT INTO `schedule` VALUES ('2022-07-01', 1, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-01', 1, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-01', 1, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-01', 2, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-01', 2, 21, 0);
INSERT INTO `schedule` VALUES ('2022-07-01', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-01', 3, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-01', 3, 14, 0);
INSERT INTO `schedule` VALUES ('2022-07-02', 1, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-02', 1, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-02', 1, 22, 0);
INSERT INTO `schedule` VALUES ('2022-07-02', 2, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-02', 2, 23, 0);
INSERT INTO `schedule` VALUES ('2022-07-02', 3, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-02', 3, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-03', 1, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-03', 1, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-03', 1, 14, 0);
INSERT INTO `schedule` VALUES ('2022-07-03', 2, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-03', 2, 22, 0);
INSERT INTO `schedule` VALUES ('2022-07-03', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-03', 3, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-03', 3, 23, 0);
INSERT INTO `schedule` VALUES ('2022-07-04', 1, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-04', 1, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-04', 2, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-04', 2, 14, 0);
INSERT INTO `schedule` VALUES ('2022-07-04', 3, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-04', 3, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-04', 3, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-05', 1, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-05', 1, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-05', 2, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-05', 2, 23, 0);
INSERT INTO `schedule` VALUES ('2022-07-05', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-05', 3, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-05', 3, 21, 0);
INSERT INTO `schedule` VALUES ('2022-07-06', 1, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-06', 1, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-06', 1, 23, 0);
INSERT INTO `schedule` VALUES ('2022-07-06', 2, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-06', 2, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-06', 3, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-06', 3, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-06', 3, 22, 0);
INSERT INTO `schedule` VALUES ('2022-07-07', 1, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-07', 1, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-07', 1, 21, 0);
INSERT INTO `schedule` VALUES ('2022-07-07', 2, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-07', 2, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-07', 3, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-07', 3, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-08', 1, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-08', 1, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-08', 1, 22, 0);
INSERT INTO `schedule` VALUES ('2022-07-08', 2, 14, 0);
INSERT INTO `schedule` VALUES ('2022-07-08', 2, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-08', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-08', 3, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-08', 3, 21, 0);
INSERT INTO `schedule` VALUES ('2022-07-09', 1, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-09', 1, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-09', 2, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-09', 2, 22, 0);
INSERT INTO `schedule` VALUES ('2022-07-09', 3, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-09', 3, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-09', 3, 14, 0);
INSERT INTO `schedule` VALUES ('2022-07-10', 1, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-10', 1, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-10', 1, 21, 0);
INSERT INTO `schedule` VALUES ('2022-07-10', 2, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-10', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-10', 3, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-10', 3, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-11', 1, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-11', 1, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-11', 1, 14, 0);
INSERT INTO `schedule` VALUES ('2022-07-11', 2, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-11', 3, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-11', 3, 23, 0);
INSERT INTO `schedule` VALUES ('2022-07-11', 3, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-12', 1, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-12', 1, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-12', 1, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-12', 2, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-12', 2, 21, 0);
INSERT INTO `schedule` VALUES ('2022-07-12', 3, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-12', 3, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-12', 3, 22, 0);
INSERT INTO `schedule` VALUES ('2022-07-13', 1, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-13', 1, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-13', 1, 21, 0);
INSERT INTO `schedule` VALUES ('2022-07-13', 2, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-13', 2, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-13', 3, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-13', 3, 23, 0);
INSERT INTO `schedule` VALUES ('2022-07-13', 3, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-14', 1, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-14', 1, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-14', 1, 22, 0);
INSERT INTO `schedule` VALUES ('2022-07-14', 2, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-14', 2, 14, 0);
INSERT INTO `schedule` VALUES ('2022-07-14', 3, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-14', 3, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-15', 1, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-15', 1, 23, 0);
INSERT INTO `schedule` VALUES ('2022-07-15', 1, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-15', 2, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-15', 2, 22, 0);
INSERT INTO `schedule` VALUES ('2022-07-15', 3, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-15', 3, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-15', 3, 14, 0);
INSERT INTO `schedule` VALUES ('2022-07-16', 1, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-16', 1, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-16', 2, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-16', 2, 23, 0);
INSERT INTO `schedule` VALUES ('2022-07-16', 3, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-16', 3, 21, 0);
INSERT INTO `schedule` VALUES ('2022-07-16', 3, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-17', 1, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-17', 1, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-17', 1, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-17', 2, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-17', 2, 23, 0);
INSERT INTO `schedule` VALUES ('2022-07-17', 3, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-17', 3, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-18', 1, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-18', 1, 14, 0);
INSERT INTO `schedule` VALUES ('2022-07-18', 1, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-18', 2, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-18', 2, 21, 0);
INSERT INTO `schedule` VALUES ('2022-07-18', 3, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-18', 3, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-18', 3, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-19', 1, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-19', 1, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-19', 1, 23, 0);
INSERT INTO `schedule` VALUES ('2022-07-19', 2, 21, 0);
INSERT INTO `schedule` VALUES ('2022-07-19', 2, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-19', 3, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-19', 3, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-19', 3, 22, 0);
INSERT INTO `schedule` VALUES ('2022-07-20', 1, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-20', 1, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-20', 1, 14, 0);
INSERT INTO `schedule` VALUES ('2022-07-20', 2, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-20', 2, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-20', 3, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-20', 3, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-21', 1, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-21', 1, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-21', 1, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-21', 2, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-21', 2, 14, 0);
INSERT INTO `schedule` VALUES ('2022-07-21', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-21', 3, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-21', 3, 22, 0);
INSERT INTO `schedule` VALUES ('2022-07-22', 1, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-22', 1, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-22', 2, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-22', 2, 23, 0);
INSERT INTO `schedule` VALUES ('2022-07-22', 3, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-22', 3, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-22', 3, 21, 0);
INSERT INTO `schedule` VALUES ('2022-07-23', 1, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-23', 1, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-23', 2, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-23', 2, 22, 0);
INSERT INTO `schedule` VALUES ('2022-07-23', 3, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-23', 3, 23, 0);
INSERT INTO `schedule` VALUES ('2022-07-23', 3, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-24', 1, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-24', 1, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-24', 1, 22, 0);
INSERT INTO `schedule` VALUES ('2022-07-24', 2, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-24', 3, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-24', 3, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-24', 3, 14, 0);
INSERT INTO `schedule` VALUES ('2022-07-25', 1, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-25', 1, 21, 0);
INSERT INTO `schedule` VALUES ('2022-07-25', 1, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-25', 2, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-25', 2, 22, 0);
INSERT INTO `schedule` VALUES ('2022-07-25', 3, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-25', 3, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-25', 3, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-26', 1, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-26', 1, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-26', 1, 23, 0);
INSERT INTO `schedule` VALUES ('2022-07-26', 2, 14, 0);
INSERT INTO `schedule` VALUES ('2022-07-26', 2, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-26', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-26', 3, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-26', 3, 21, 0);
INSERT INTO `schedule` VALUES ('2022-07-27', 1, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-27', 1, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-27', 1, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-27', 2, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-27', 2, 14, 0);
INSERT INTO `schedule` VALUES ('2022-07-27', 3, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-27', 3, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-28', 1, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-28', 1, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-28', 1, 21, 0);
INSERT INTO `schedule` VALUES ('2022-07-28', 2, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-28', 2, 23, 0);
INSERT INTO `schedule` VALUES ('2022-07-28', 3, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-28', 3, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-28', 3, 24, 0);
INSERT INTO `schedule` VALUES ('2022-07-29', 1, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-29', 1, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-29', 2, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-29', 2, 21, 0);
INSERT INTO `schedule` VALUES ('2022-07-29', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-29', 3, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-29', 3, 22, 0);
INSERT INTO `schedule` VALUES ('2022-07-30', 1, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-30', 1, 13, 0);
INSERT INTO `schedule` VALUES ('2022-07-30', 1, 23, 0);
INSERT INTO `schedule` VALUES ('2022-07-30', 2, 10, 0);
INSERT INTO `schedule` VALUES ('2022-07-30', 2, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-30', 3, 5, 0);
INSERT INTO `schedule` VALUES ('2022-07-30', 3, 12, 0);
INSERT INTO `schedule` VALUES ('2022-07-31', 1, 7, 0);
INSERT INTO `schedule` VALUES ('2022-07-31', 1, 11, 0);
INSERT INTO `schedule` VALUES ('2022-07-31', 1, 22, 0);
INSERT INTO `schedule` VALUES ('2022-07-31', 2, 9, 0);
INSERT INTO `schedule` VALUES ('2022-07-31', 2, 15, 0);
INSERT INTO `schedule` VALUES ('2022-07-31', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-07-31', 3, 14, 0);
INSERT INTO `schedule` VALUES ('2022-07-31', 3, 24, 0);
INSERT INTO `schedule` VALUES ('2022-08-01', 1, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-01', 1, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-01', 2, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-01', 2, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-01', 2, 17, 0);
INSERT INTO `schedule` VALUES ('2022-08-01', 3, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-01', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-01', 3, 15, 0);
INSERT INTO `schedule` VALUES ('2022-08-02', 1, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-02', 1, 18, 0);
INSERT INTO `schedule` VALUES ('2022-08-02', 2, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-02', 2, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-02', 2, 19, 0);
INSERT INTO `schedule` VALUES ('2022-08-02', 3, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-02', 3, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-03', 1, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-03', 1, 15, 0);
INSERT INTO `schedule` VALUES ('2022-08-03', 2, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-03', 2, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-03', 2, 18, 0);
INSERT INTO `schedule` VALUES ('2022-08-03', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-03', 3, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-03', 3, 19, 0);
INSERT INTO `schedule` VALUES ('2022-08-04', 1, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-04', 1, 17, 0);
INSERT INTO `schedule` VALUES ('2022-08-04', 2, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-04', 2, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-04', 3, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-04', 3, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-04', 3, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-05', 1, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-05', 1, 19, 0);
INSERT INTO `schedule` VALUES ('2022-08-05', 2, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-05', 2, 9, 1);
INSERT INTO `schedule` VALUES ('2022-08-05', 2, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-05', 2, 15, 0);
INSERT INTO `schedule` VALUES ('2022-08-05', 3, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-05', 3, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-05', 3, 17, 0);
INSERT INTO `schedule` VALUES ('2022-08-06', 1, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-06', 1, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-06', 2, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-06', 2, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-06', 2, 18, 0);
INSERT INTO `schedule` VALUES ('2022-08-06', 3, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-06', 3, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-07', 1, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-07', 1, 15, 0);
INSERT INTO `schedule` VALUES ('2022-08-07', 2, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-07', 2, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-07', 2, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-07', 3, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-07', 3, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-07', 3, 18, 0);
INSERT INTO `schedule` VALUES ('2022-08-08', 1, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-08', 2, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-08', 2, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-08', 2, 17, 0);
INSERT INTO `schedule` VALUES ('2022-08-08', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-08', 3, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-08', 3, 19, 0);
INSERT INTO `schedule` VALUES ('2022-08-09', 1, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-09', 1, 18, 0);
INSERT INTO `schedule` VALUES ('2022-08-09', 2, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-09', 2, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-09', 3, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-09', 3, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-09', 3, 17, 0);
INSERT INTO `schedule` VALUES ('2022-08-10', 1, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-10', 1, 19, 0);
INSERT INTO `schedule` VALUES ('2022-08-10', 2, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-10', 2, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-10', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-10', 3, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-10', 3, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-11', 1, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-11', 1, 17, 0);
INSERT INTO `schedule` VALUES ('2022-08-11', 2, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-11', 2, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-11', 2, 19, 0);
INSERT INTO `schedule` VALUES ('2022-08-11', 3, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-11', 3, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-11', 3, 15, 0);
INSERT INTO `schedule` VALUES ('2022-08-12', 1, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-12', 2, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-12', 2, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-12', 2, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-12', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-12', 3, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-12', 3, 18, 0);
INSERT INTO `schedule` VALUES ('2022-08-13', 1, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-13', 1, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-13', 2, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-13', 2, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-13', 3, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-13', 3, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-13', 3, 15, 0);
INSERT INTO `schedule` VALUES ('2022-08-14', 1, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-14', 1, 19, 0);
INSERT INTO `schedule` VALUES ('2022-08-14', 2, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-14', 2, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-14', 2, 17, 0);
INSERT INTO `schedule` VALUES ('2022-08-14', 3, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-14', 3, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-14', 3, 18, 0);
INSERT INTO `schedule` VALUES ('2022-08-15', 1, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-15', 1, 15, 0);
INSERT INTO `schedule` VALUES ('2022-08-15', 2, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-15', 2, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-15', 2, 19, 0);
INSERT INTO `schedule` VALUES ('2022-08-15', 3, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-15', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-16', 1, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-16', 1, 18, 0);
INSERT INTO `schedule` VALUES ('2022-08-16', 2, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-16', 2, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-16', 2, 15, 0);
INSERT INTO `schedule` VALUES ('2022-08-16', 3, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-16', 3, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-16', 3, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-17', 1, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-17', 1, 15, 0);
INSERT INTO `schedule` VALUES ('2022-08-17', 2, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-17', 2, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-17', 2, 18, 0);
INSERT INTO `schedule` VALUES ('2022-08-17', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-17', 3, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-17', 3, 17, 0);
INSERT INTO `schedule` VALUES ('2022-08-18', 1, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-18', 1, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-18', 2, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-18', 2, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-18', 2, 15, 0);
INSERT INTO `schedule` VALUES ('2022-08-18', 3, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-18', 3, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-19', 1, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-19', 1, 17, 0);
INSERT INTO `schedule` VALUES ('2022-08-19', 2, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-19', 2, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-19', 2, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-19', 3, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-19', 3, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-19', 3, 19, 0);
INSERT INTO `schedule` VALUES ('2022-08-20', 1, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-20', 1, 18, 0);
INSERT INTO `schedule` VALUES ('2022-08-20', 2, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-20', 2, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-20', 3, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-20', 3, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-20', 3, 17, 0);
INSERT INTO `schedule` VALUES ('2022-08-21', 1, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-21', 2, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-21', 2, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-21', 2, 18, 0);
INSERT INTO `schedule` VALUES ('2022-08-21', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-21', 3, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-21', 3, 19, 0);
INSERT INTO `schedule` VALUES ('2022-08-22', 1, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-22', 1, 17, 0);
INSERT INTO `schedule` VALUES ('2022-08-22', 2, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-22', 2, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-22', 2, 15, 0);
INSERT INTO `schedule` VALUES ('2022-08-22', 3, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-22', 3, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-22', 3, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-23', 1, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-23', 1, 19, 0);
INSERT INTO `schedule` VALUES ('2022-08-23', 2, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-23', 2, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-23', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-23', 3, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-23', 3, 15, 0);
INSERT INTO `schedule` VALUES ('2022-08-24', 1, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-24', 1, 19, 0);
INSERT INTO `schedule` VALUES ('2022-08-24', 2, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-24', 2, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-24', 2, 17, 0);
INSERT INTO `schedule` VALUES ('2022-08-24', 3, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-24', 3, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-25', 1, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-25', 1, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-25', 2, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-25', 2, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-25', 2, 19, 0);
INSERT INTO `schedule` VALUES ('2022-08-25', 3, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-25', 3, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-25', 3, 18, 0);
INSERT INTO `schedule` VALUES ('2022-08-26', 1, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-26', 1, 17, 0);
INSERT INTO `schedule` VALUES ('2022-08-26', 2, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-26', 2, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-26', 2, 15, 0);
INSERT INTO `schedule` VALUES ('2022-08-26', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-26', 3, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-26', 3, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-27', 1, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-27', 1, 15, 0);
INSERT INTO `schedule` VALUES ('2022-08-27', 2, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-27', 2, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-27', 3, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-27', 3, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-27', 3, 18, 0);
INSERT INTO `schedule` VALUES ('2022-08-28', 1, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-28', 1, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-28', 2, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-28', 2, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-28', 2, 17, 0);
INSERT INTO `schedule` VALUES ('2022-08-28', 3, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-28', 3, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-29', 1, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-29', 1, 18, 0);
INSERT INTO `schedule` VALUES ('2022-08-29', 2, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-29', 2, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-29', 2, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-29', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-29', 3, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-29', 3, 19, 0);
INSERT INTO `schedule` VALUES ('2022-08-30', 1, 7, 0);
INSERT INTO `schedule` VALUES ('2022-08-30', 1, 18, 0);
INSERT INTO `schedule` VALUES ('2022-08-30', 2, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-30', 2, 14, 0);
INSERT INTO `schedule` VALUES ('2022-08-30', 2, 16, 0);
INSERT INTO `schedule` VALUES ('2022-08-30', 3, 10, 0);
INSERT INTO `schedule` VALUES ('2022-08-30', 3, 12, 0);
INSERT INTO `schedule` VALUES ('2022-08-30', 3, 17, 0);
INSERT INTO `schedule` VALUES ('2022-08-31', 1, 9, 0);
INSERT INTO `schedule` VALUES ('2022-08-31', 1, 19, 0);
INSERT INTO `schedule` VALUES ('2022-08-31', 2, 5, 0);
INSERT INTO `schedule` VALUES ('2022-08-31', 2, 11, 0);
INSERT INTO `schedule` VALUES ('2022-08-31', 3, 8, 0);
INSERT INTO `schedule` VALUES ('2022-08-31', 3, 13, 0);
INSERT INTO `schedule` VALUES ('2022-08-31', 3, 15, 0);

-- ----------------------------
-- Table structure for shift
-- ----------------------------
DROP TABLE IF EXISTS `shift`;
CREATE TABLE `shift`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '班次id',
  `department_id` int NULL DEFAULT NULL COMMENT '科室id',
  `shift_sign` tinyint NULL DEFAULT NULL COMMENT '班次标识\r\n（对应三班制：1.白班 2.小夜班 3.大夜班）\r\n（对应两班制：1.早班 3.晚班 ）',
  `shift_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班次名称',
  `start_time` time(0) NULL DEFAULT NULL COMMENT '班次开始时间',
  `end_time` time(0) NULL DEFAULT NULL COMMENT '班次结束时间',
  `duration` double(10, 1) NULL DEFAULT NULL COMMENT '班次时长（小时）',
  `nurse_number` int NULL DEFAULT NULL COMMENT '需要护士人数',
  `carer_number` int NULL DEFAULT NULL COMMENT '需要护工人数',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（1：是 0：否）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `department_id`(`department_id`) USING BTREE,
  INDEX `shift_sign`(`shift_sign`) USING BTREE,
  INDEX `shift_name`(`shift_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '班次表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of shift
-- ----------------------------
INSERT INTO `shift` VALUES (1, 2, 1, '白班', '08:00:00', '16:00:00', 8.0, 1, 1, 0, '2022-05-06 10:55:35', '2022-06-30 17:29:53');
INSERT INTO `shift` VALUES (2, 2, 2, '小夜班', '16:00:00', '00:00:00', 8.0, 2, 1, 0, '2022-05-06 10:56:29', '2022-06-30 16:57:46');
INSERT INTO `shift` VALUES (3, 2, 3, '大夜班', '00:00:00', '08:00:00', 8.0, 2, 1, 0, '2022-05-06 10:55:20', '2022-06-30 17:29:55');
INSERT INTO `shift` VALUES (4, 3, 1, '早班', '08:00:00', '20:00:00', 12.0, 1, 0, 0, '2022-05-06 12:32:54', '2022-06-12 15:49:26');
INSERT INTO `shift` VALUES (5, 3, 3, '晚班', '20:00:00', '08:00:00', 12.0, 1, 0, 0, '2022-05-06 12:33:10', '2022-06-12 15:49:26');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '员工id',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `sex` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `hospital_id` int NULL DEFAULT NULL COMMENT '医院id',
  `department_id` int NULL DEFAULT NULL COMMENT '科室id',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像地址',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（1：是 0：否）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `hospital_id_and_department_id`(`hospital_id`, `department_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '1', '1', '嘉文四世', '男', 1, NULL, NULL, '16675110681', 'http://cau1i.cn:8800/files/e47b3ab03ff24ec7a6ee6dad89468eff.png', 0, '2022-04-19 18:42:56', '2022-07-01 20:47:14');
INSERT INTO `user` VALUES (2, 'lux', 'lux', '拉克丝', '女', 2, 1, NULL, '12345678910', '', 0, '2022-04-16 19:48:22', '2022-07-01 20:26:56');
INSERT INTO `user` VALUES (3, 'sw', 'sw', '斯维因', '男', 2, 2, NULL, '12345678910', '', 0, '2022-04-19 20:05:26', '2022-06-19 11:45:50');
INSERT INTO `user` VALUES (4, 'karma', 'karma', '卡尔玛', '女', 2, 1, NULL, '13713111123', '', 0, '2022-04-20 09:28:09', '2022-06-19 11:45:50');
INSERT INTO `user` VALUES (5, 'ashe', 'ashe', '艾希', '女', 3, 1, 2, '13122334466', 'http://cau1i.cn:8800/files/7b9d0bad501b4ee78feaadb881d7e60d.png', 0, '2022-04-19 20:05:40', '2022-07-01 20:40:31');
INSERT INTO `user` VALUES (6, 'sol', 'sol', '铸星龙王', '男', 3, 1, 3, '13177882233', '', 0, '2022-04-19 20:05:50', '2022-07-01 20:26:57');
INSERT INTO `user` VALUES (7, 'm', 'm', '墨菲斯', '男', 4, 1, 2, '', '', 0, '2022-04-19 20:06:11', '2022-07-01 20:26:57');
INSERT INTO `user` VALUES (8, 'k', 'k', '凯隐', '男', 4, 1, 2, '', '', 0, '2022-04-19 20:06:19', '2022-07-01 20:26:57');
INSERT INTO `user` VALUES (9, 'j', 'j', '劫', '男', 4, 1, 2, '', NULL, 0, '2022-04-19 20:06:23', '2022-06-30 16:55:08');
INSERT INTO `user` VALUES (10, 's', 's', '沙弥拉', '女', 4, 1, 2, '', NULL, 0, '2022-04-19 20:06:34', '2022-06-30 16:55:08');
INSERT INTO `user` VALUES (11, 'n', 'n', '娜美', '女', 4, 1, 2, '', NULL, 0, '2022-04-19 20:06:38', '2022-06-30 16:55:08');
INSERT INTO `user` VALUES (12, 'g', 'g', '盖伦', '男', 4, 1, 2, '', NULL, 0, '2022-04-19 20:06:57', '2022-06-30 16:55:08');
INSERT INTO `user` VALUES (13, 'e', 'e', '塞恩', '男', 4, 1, 2, '', NULL, 0, '2022-04-19 20:07:01', '2022-06-30 17:29:34');
INSERT INTO `user` VALUES (14, 'x', 'x', '赵信', '男', 4, 1, 2, '', NULL, 0, '2022-04-19 20:13:28', '2022-06-30 17:29:36');
INSERT INTO `user` VALUES (15, 'mo', 'mo', '莫甘娜', '女', 5, 1, 2, '', NULL, 0, '2022-04-19 20:18:10', '2022-06-30 16:34:50');
INSERT INTO `user` VALUES (16, 'a', 'a', '艾瑞利亚', '女', 5, 1, 2, '', NULL, 0, '2022-05-09 12:57:15', '2022-06-30 16:34:50');
INSERT INTO `user` VALUES (17, 'f', 'f', '佛耶格', '男', 5, 1, 2, '', NULL, 0, '2022-05-12 03:16:50', '2022-06-30 16:34:50');
INSERT INTO `user` VALUES (18, 'd', 'd', '德莱文', '男', 5, 1, 2, '', NULL, 0, '2022-04-20 23:02:03', '2022-06-30 16:34:38');
INSERT INTO `user` VALUES (19, 'l', 'l', '卢锡安', '男', 5, 1, 2, '', NULL, 0, '2022-04-20 23:03:31', '2022-06-30 16:34:38');
INSERT INTO `user` VALUES (20, 'sn', 'sn', '赛娜', '女', 4, 2, 7, '', NULL, 0, '2022-04-21 00:16:21', '2022-06-30 17:13:39');
INSERT INTO `user` VALUES (21, 'z', 'z', '泽拉斯', '男', 4, 2, 7, ' ', NULL, 0, '2022-05-09 12:57:26', '2022-06-30 17:13:39');

-- ----------------------------
-- Table structure for workload
-- ----------------------------
DROP TABLE IF EXISTS `workload`;
CREATE TABLE `workload`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '员工id',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `real_work_days` int NULL DEFAULT 0 COMMENT '实际上班天数',
  `work_days` int NULL DEFAULT 0 COMMENT '下个月预计上班天数',
  `real_evening_shifts` int NULL DEFAULT 0 COMMENT '实际大夜班次数',
  `evening_shifts` int NULL DEFAULT 0 COMMENT '下个月预计大夜班次数',
  `real_morning_shifts` int NULL DEFAULT 0 COMMENT '实际白班次数',
  `morning_shifts` int NULL DEFAULT 0 COMMENT '下个月预计白班次数',
  `real_middle_shifts` int NULL DEFAULT 0 COMMENT '实际小夜班次数',
  `middle_shifts` int NULL DEFAULT 0 COMMENT '下个月预计小夜班次数',
  `left_days` int NULL DEFAULT 0 COMMENT '请假天数',
  `annual_leave_days` int NULL DEFAULT 20 COMMENT '年休天数',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（1：是 0：否）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '工作量表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of workload
-- ----------------------------
INSERT INTO `workload` VALUES (1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, '2022-05-10 12:08:40', '2022-06-08 16:32:06');
INSERT INTO `workload` VALUES (2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, '2022-05-09 19:09:17', '2022-06-15 17:23:38');
INSERT INTO `workload` VALUES (3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, '2022-05-10 11:35:27', '2022-06-08 15:38:09');
INSERT INTO `workload` VALUES (4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, '2022-05-09 19:35:27', '2022-06-08 15:44:28');
INSERT INTO `workload` VALUES (5, 5, 0, 17, 0, 6, 0, 3, 0, 8, 0, 20, 0, '2022-05-10 11:35:27', '2022-07-01 19:52:44');
INSERT INTO `workload` VALUES (6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, '2022-05-10 11:35:27', '2022-06-08 15:38:09');
INSERT INTO `workload` VALUES (7, 7, 0, 17, 0, 6, 0, 4, 0, 7, 0, 20, 0, '2022-05-10 03:35:27', '2022-07-01 19:52:44');
INSERT INTO `workload` VALUES (8, 8, 0, 20, 0, 12, 0, 3, 0, 5, 0, 20, 0, '2022-05-10 11:35:27', '2022-07-01 19:52:44');
INSERT INTO `workload` VALUES (9, 9, 0, 17, 0, 6, 0, 4, 0, 7, 1, 20, 0, '2022-05-10 11:35:27', '2022-07-01 19:53:55');
INSERT INTO `workload` VALUES (10, 10, 0, 16, 0, 6, 0, 3, 0, 7, 0, 20, 0, '2022-05-10 11:35:27', '2022-07-01 19:53:45');
INSERT INTO `workload` VALUES (11, 11, 0, 18, 0, 6, 0, 4, 0, 8, 0, 20, 0, '2022-05-10 11:35:27', '2022-07-01 19:52:44');
INSERT INTO `workload` VALUES (12, 12, 0, 16, 0, 7, 0, 3, 0, 6, 0, 20, 0, '2022-05-10 11:35:27', '2022-07-01 19:56:09');
INSERT INTO `workload` VALUES (13, 13, 0, 17, 0, 7, 0, 3, 0, 7, 0, 20, 0, '2022-05-10 11:35:27', '2022-07-01 19:52:44');
INSERT INTO `workload` VALUES (14, 14, 0, 17, 0, 6, 0, 4, 0, 7, 0, 20, 0, '2022-05-10 11:35:27', '2022-07-01 19:52:44');
INSERT INTO `workload` VALUES (15, 15, 0, 15, 0, 5, 0, 5, 0, 5, 0, 20, 0, '2022-05-10 11:35:27', '2022-07-01 19:52:44');
INSERT INTO `workload` VALUES (16, 16, 0, 16, 0, 5, 0, 6, 0, 5, 0, 20, 0, '2022-05-10 11:35:27', '2022-07-01 19:52:44');
INSERT INTO `workload` VALUES (17, 17, 0, 15, 0, 5, 0, 5, 0, 5, 0, 20, 0, '2022-05-10 11:35:27', '2022-07-01 19:52:44');
INSERT INTO `workload` VALUES (18, 18, 0, 15, 0, 5, 0, 6, 0, 4, 0, 20, 0, '2022-05-10 11:35:27', '2022-07-01 19:52:44');
INSERT INTO `workload` VALUES (19, 19, 0, 15, 0, 5, 0, 6, 0, 4, 0, 20, 0, '2022-05-10 11:35:27', '2022-07-01 19:52:44');
INSERT INTO `workload` VALUES (20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, '2022-05-10 11:35:27', '2022-06-30 17:14:02');
INSERT INTO `workload` VALUES (21, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, '2022-05-12 11:17:31', '2022-06-30 17:14:02');

SET FOREIGN_KEY_CHECKS = 1;
