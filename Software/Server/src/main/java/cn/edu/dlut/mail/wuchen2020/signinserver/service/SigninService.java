package cn.edu.dlut.mail.wuchen2020.signinserver.service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.dlut.mail.wuchen2020.signinserver.dao.CourseSelectionDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.dao.SigninRecordDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.dao.StudentDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.exception.CustomException;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Course;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.CourseSelection;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.SigninRecord;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.SigninRecord.SigninStatus;
import cn.edu.dlut.mail.wuchen2020.signinserver.util.CourseHelper;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Student;

@Service
public class SigninService {
    @Autowired
    private StudentDAO studentDAO;
    @Autowired
    private CourseSelectionDAO courseSelectionDAO;
    @Autowired
    private SigninRecordDAO signinRecordDAO;

    public SigninStatus signin(String fingerprint, String location, long timestamp) {
        // 从student表里查询学生
        Student student = studentDAO.findByFingerprint(fingerprint);
        if (student == null) throw new CustomException(1001, "未查询到该学生");
        
        // 根据时间戳或者当前时间确定是第几周, 周几和第几节课
        Instant now = Instant.ofEpochMilli(timestamp);
        LocalDate date = LocalDate.ofInstant(now, Clock.systemDefaultZone().getZone());
        LocalTime time = LocalTime.ofInstant(now, Clock.systemDefaultZone().getZone());
        int week = CourseHelper.getWeekFromDate(date);
        int day = CourseHelper.getDayOfWeekFromDate(date);
        // int cls = CourseHelper.getClassFromTime(time);
        int cls = 3;
        // 从student_course和course表里查询学生上的课的信息
        CourseSelection courseSelection = courseSelectionDAO.findCourseSelected(student.getId(), week, day, cls);
        if (courseSelection == null) return SigninStatus.WRONG_CLASS;
        Course course = courseSelection.getCourse();
        
        // 创建签到记录
        SigninRecord record = new SigninRecord();
        record.setStudent(student);
        record.setCourse(course);
        record.setLocation(location);
        record.setTime(new Date(timestamp));
        
        // 比对地点
        if (!course.getLocation().equals(location)) {
            record.setStatus(SigninStatus.WRONG_CLASS);
        } else {
            // 查询签到记录确定签到类型
//            List<SigninRecord> oldRecords = signinRecordDAO.findByStudentAndTime();
//            for (SigninRecord oldRecord : oldRecords) {}
            
            record.setStatus(SigninStatus.SUCCESS);
        }
        signinRecordDAO.save(record);
        
        return record.getStatus();
    }
}
