/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50534
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-09-02 17:12:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `account`
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(200) DEFAULT NULL,
  `Balance` double(10,4) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('1', '张三', '1000.0000');
INSERT INTO `account` VALUES ('2', '李四', '2000.0000');

CREATE TABLE tb_batch
(
	Id int not null PRIMARY KEY auto_increment,
	Name VARCHAR(200)
)

-- ----------------------------
-- Table structure for `tb_lob`
-- ----------------------------
DROP TABLE IF EXISTS `tb_lob`;
CREATE TABLE `tb_lob` (
  `Id` int(10) NOT NULL AUTO_INCREMENT,
  `Pic` longblob,
  `Txt` text,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_lob
-- ----------------------------