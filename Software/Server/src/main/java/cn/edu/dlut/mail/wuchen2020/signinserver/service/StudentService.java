package cn.edu.dlut.mail.wuchen2020.signinserver.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.dlut.mail.wuchen2020.signinserver.dao.SigninRecordDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.dao.StudentDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.SigninRecord.SigninStatus;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.SigninRecord;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Student;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.LessonVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.SigninRecordVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.StudentInfoVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.TermVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.util.CourseHelper;

/**
 * 学生的业务逻辑
 * </br>
 * 另见 {@link cn.edu.dlut.mail.wuchen2020.signinserver.controller.StudentController}
 * 
 * @author Wu Chen
 */
@Service
public class StudentService {
    @Autowired
    public StudentDAO studentDAO;
    @Autowired
    private SigninRecordDAO signinRecordDAO;

    public StudentInfoVO getStudentInfo(String number) {
        Student student = studentDAO.findByNumber(number);
        if (student != null) {
            StudentInfoVO studentInfo = new StudentInfoVO();
            studentInfo.setNumber(student.getNumber());
            studentInfo.setName(student.getName());
            studentInfo.setClassName(student.getClassName());
            studentInfo.setMajor(student.getMajor());
            studentInfo.setDepartment(student.getDepartment());
            return studentInfo;
        }
        return null;
    }
    
    public TermVO getTerm(String username) {
        // TODO 获取学期信息
        return null;
    }
    
    public List<LessonVO> getTimetable(String username, int week) {
        // TODO 获取课程表
        return null;
    }

    public SigninStatus getSigninStatus(String number) {
        Student student = studentDAO.findByNumber(number);
        if (student != null) {
            Instant instant = Instant.now();
            LocalDate date = LocalDate.ofInstant(instant, ZoneId.systemDefault());
            LocalTime time = LocalTime.ofInstant(instant, ZoneId.systemDefault());
            CourseHelper.Lesson lesson = CourseHelper.getLessonFromTime(time);
            Date startDate = Date.from(lesson.getStartTime().atDate(date).atZone(ZoneId.systemDefault()).toInstant());
            List<SigninRecord> records = signinRecordDAO.findByStudentAndTimeBetween(student, startDate, new Date());
            if (records != null && !records.isEmpty()) {
                SigninRecord lastRecord = records.get(records.size() - 1);
                return lastRecord.getStatus();
            } else {
                return SigninStatus.NOT_SIGN_IN;
            }
        }
        return SigninStatus.ERROR;
    }

    public List<SigninRecordVO> getSigninHistory(String username, int page, int count) {
        // TODO 获取签到历史记录
        return null;
    }
}
