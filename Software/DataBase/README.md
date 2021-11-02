# 数据库设计
数据库使用MariaDB(MySQL)

## 数据库(signin_db)
所有的表都存在这个数据库中

**注: \*为主键**

---
### 教务账号表(admin)
| id* | username | password |
| --- | -------- | -------- |
| 序号 | 用户名 | 密码 |

---
### 教师信息表(teacher)
| id* | number | password | fingerprint | name | class |
| --- | ------ | -------- | ----------- | ---- | ----- |
| 序号 | 工号 | 密码 | 人脸指纹 | 姓名 | 如果是班主任那么所带的班级 |

---
### 学生信息表(student)
| id* | number | password | fingerprint | name | class | major | department |
| --- | ------ | -------- | ----------- | ---- | ----- | ----- | ---------- |
| 序号 | 学号 | 密码 | 人脸指纹 | 姓名 | 班级 | 专业(可能没用) | 院系(没用) |

---
### 课程信息表(course)
| id* | name | location | start_week | end_week | weekday | start_time | end_time | teacher |
| -- | ---- | -------- | -----------| -------- | ------- | ---------- | -------- | ------- |
| 课程序号 | 课程名称 | 上课地点 | 开始周 | 结束周 | 星期几上课 | 开始节数 | 结束节数 | 任课教师(可能为空因为有自习课) |

外键:
- teacher -> teacher.id

---
### 选课记录表(student_course)
| id* | student | course |
| -- | ------- | ------ |
| 序号 | 学生 | 课程 |

外键:
- student -> student.id
- course -> course.id

---
### 签到日志表(signin_log)
| id | student | course | location | time | status |
| -- | ------- | ------ | -------- | ---- | ------ |
| 日志序号 | 学生 | 课程 | 实际签到位置 | 实际签到时间 | 签到状态 |

外键:
- student -> student.id
- course -> course.id

注: 签到状态包括: 正常签到(0)/走错教室(1)/暂离(2)/返回(3)/离开(4)
