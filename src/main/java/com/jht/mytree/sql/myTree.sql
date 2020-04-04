/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : tree

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2020-03-30 21:30:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tree
-- ----------------------------
DROP TABLE IF EXISTS `tree`;
CREATE TABLE `tree` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(256) NOT NULL,
  `PARENT_ID` varchar(32) NOT NULL,
  `TREE_LEFT` int(11) DEFAULT NULL,
  `TREE_RIGHT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `I_TREE_PARENT_ID` (`PARENT_ID`),
  KEY `I_TREE_TREE_LEFT` (`TREE_LEFT`),
  KEY `I_TREE_TREE_RIGHT` (`TREE_RIGHT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tree';

-- ----------------------------
-- Table structure for tree_id
-- ----------------------------
DROP TABLE IF EXISTS `tree_id`;
CREATE TABLE `tree_id` (
  `ID` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tree_Id';