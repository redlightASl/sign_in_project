SET NAMES utf8mb4;

-- DataBase
CREATE DATABASE `signin_db` IF NOT EXISTS `signin_db`;

-- Table of admin
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`
(
    `id` TINYINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '管理账号的序号',
    `username` VARCHAR(255) NOT NULL COMMENT '用户名',
    `password` CHAR(32) NOT NULL  COMMENT '密码',
    PRIMARY KEY (`id`)
);
-- 密码加盐后32位小写md5加密
INSERT INTO admin VALUES (1, 'admin', '');

-- Table of teacher
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`
(
    `id` char(16) NOT NULL COMMENT '工号',
    `fingerprint` char(32) NOT NULL COMMENT '人脸指纹',
    `name` varchar(24) NOT NULL COMMENT '姓名',
    `class` varchar(16) COMMENT '班主任带的班级(如果是)'
);

-- Table of student
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`
(
    `id` char(16) NOT NULL COMMENT '学号',
    `fingerprint` char(32) NOT NULL COMMENT '人脸指纹',
    `name` varchar(24) NOT NULL COMMENT '姓名',
    `class` varchar(16) NOT NULL COMMENT '班级',
    `major` varchar(32) COMMENT '专业名称(可能没啥用)',
    `department` varchar(32) COMMENT '院系名称(确实没啥用)'
);
-- TODO 如果有需要可能得把班级, 专业名称, 院系名称都抽出来作为单独的表
-- 我jio得不太需要(笑)

-- Table of course
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`
(
    `id` int(16) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '课程序号',
    `name` varchar(64) NOT NULL COMMENT '课程名称',
    `location` varchar(32) NOT NULL COMMENT '上课地点',
    `start_week` int(8) NOT NULL COMMENT '第几周开始',
    `end_week` int(8) NOT NULL COMMENT '第几周结束',
    `weekday` int(3) NOT NULL COMMENT '星期几上课',
    `start_time` int(4) NOT NULL COMMENT '第几节开始上课',
    `end_time` int(4) NOT NULL COMMENT '上到第几节课',
    `teacher` char(16) COMMENT '任课教师(可能为空因为自习课)',
    PRIMARY KEY (`id`)
);

-- Table of course records
DROP TABLE IF EXISTS `student_course`;
CREATE TABLE `student_course`
(
    `id` int UNSIGNED NOT NULL COMMENT '记录序号',
    `student` char(16) NOT NULL COMMENT '学生学号',
    `course` int(16) UNSIGNED NOT NULL COMMENT '课程序号',
    -- 用FOREIGN KEY
    PRIMARY KEY (`id`)
);

-- Table of sign in log
-- 也可能不用MySQL实现
CREATE TABLE `signin_log`
(
    `id` int UNSIGNED NOT NULL COMMENT '日志序号',
    `student` char(16) NOT NULL COMMENT '学生学号',
    `course` int(16) UNSIGNED NOT NULL COMMENT '课程序号',
    `location` varchar(32) NOT NULL COMMENT '实际签到地点',
    `time` DATETIME NOT NULL COMMENT '实际签到时间',
    `status` int(3) NOT NULL COMMENT '签到状态',
    -- 正常签到(0)/走错教室(1)/暂离(2)/返回(3)/离开(4)
    PRIMARY KEY (`id`)
);
