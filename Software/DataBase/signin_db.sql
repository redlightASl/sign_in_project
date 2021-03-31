SET NAMES utf8mb4

-- 数据库 signin_db
CREATE DATABASE IF NOT EXISTS `signin_db`;
USE `signin_db`;

-- 表 admin
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` TINYINT(3) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '序号',
  `username` VARCHAR(32) NOT NULL COMMENT '用户名',
  `password` CHAR(32) CHARACTER SET ascii NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`),
  KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='管理账号表';

-- 密码加盐后32位小写md5加密
INSERT INTO admin VALUES (1, 'admin', '');

-- 表 teacher
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '序号',
  `number` CHAR(16) CHARACTER SET ascii NOT NULL COMMENT '工号',
  `password` CHAR(32) CHARACTER SET ascii NOT NULL COMMENT '密码',
  `fingerprint` CHAR(32) CHARACTER SET ascii NOT NULL COMMENT '人脸指纹',
  `name` VARCHAR(24) NOT NULL COMMENT '姓名',
  `class` VARCHAR(16) DEFAULT NULL COMMENT '班主任带的班级(如果是)',
  PRIMARY KEY (`id`),
  KEY `number` (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师信息表';

-- 表 student
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '序号',
  `number` CHAR(16) CHARACTER SET ascii NOT NULL COMMENT '学号',
  `password` CHAR(32) CHARACTER SET ascii NOT NULL COMMENT '密码',
  `fingerprint` CHAR(32) CHARACTER SET ascii NOT NULL COMMENT '人脸指纹',
  `name` VARCHAR(24) NOT NULL COMMENT '姓名',
  `class` VARCHAR(16) NOT NULL COMMENT '班级',
  `major` VARCHAR(32) DEFAULT NULL COMMENT '专业名称(可能没啥用)',
  `department` VARCHAR(32) DEFAULT NULL COMMENT '院系名称(确实没啥用)',
  PRIMARY KEY (`id`),
  KEY `number` (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生信息表';

-- 表 course
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '序号',
  `name` VARCHAR(32) NOT NULL COMMENT '课程名称',
  `location` VARCHAR(16) NOT NULL COMMENT '上课地点',
  `start_week` TINYINT(3) UNSIGNED NOT NULL COMMENT '第几周开始',
  `end_week` TINYINT(3) UNSIGNED NOT NULL COMMENT '第几周结束',
  `weekday` TINYINT(3) UNSIGNED NOT NULL COMMENT '星期几上课',
  `start_time` TINYINT(3) UNSIGNED NOT NULL COMMENT '第几节开始上课',
  `end_time` TINYINT(3) UNSIGNED NOT NULL COMMENT '上到第几节课',
  `teacher` INT(10) UNSIGNED DEFAULT NULL COMMENT '任课教师序号(可能为空因为自习课)',
  PRIMARY KEY (`id`),
  KEY `FK_course_teacher` (`teacher`),
  CONSTRAINT `FK_course_teacher` FOREIGN KEY (`teacher`) REFERENCES `teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程信息表';

-- 表 student_course
DROP TABLE IF EXISTS `student_course`;
CREATE TABLE `student_course` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '记录序号',
  `student` INT(10) UNSIGNED NOT NULL COMMENT '学生序号',
  `course` INT(10) UNSIGNED NOT NULL COMMENT '课程序号',
  PRIMARY KEY (`id`),
  KEY `FK_student_course_student` (`student`),
  KEY `FK_student_course_course` (`course`),
  CONSTRAINT `FK_student_course_course` FOREIGN KEY (`course`) REFERENCES `course` (`id`),
  CONSTRAINT `FK_student_course_student` FOREIGN KEY (`student`) REFERENCES `student` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生选课记录表';

-- 表 signin_log
DROP TABLE IF EXISTS `signin_log`;
CREATE TABLE `signin_log` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '序号',
  `student` INT(10) UNSIGNED NOT NULL COMMENT '学生序号',
  `course` INT(10) UNSIGNED NOT NULL COMMENT '课程序号',
  `location` VARCHAR(16) NOT NULL COMMENT '实际签到地点',
  `time` DATETIME NOT NULL COMMENT '实际签到时间',
  `status` BIT(3) NOT NULL COMMENT '签到状态',
  -- 正常签到(0)/走错教室(1)/暂离(2)/返回(3)/离开(4)
  PRIMARY KEY (`id`),
  KEY `FK_signin_log_student` (`student`),
  KEY `FK_signin_log_course` (`course`),
  CONSTRAINT `FK_signin_log_course` FOREIGN KEY (`course`) REFERENCES `course` (`id`),
  CONSTRAINT `FK_signin_log_student` FOREIGN KEY (`student`) REFERENCES `student` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签到记录表';
