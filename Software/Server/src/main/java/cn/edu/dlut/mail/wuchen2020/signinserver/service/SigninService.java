package cn.edu.dlut.mail.wuchen2020.signinserver.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.dlut.mail.wuchen2020.signinserver.dao.CourseSelectionDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.dao.SigninRecordDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.dao.StudentDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.exception.BusinessException;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Course;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.CourseSelection;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.SigninRecord;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.SigninRecord.SigninStatus;
import cn.edu.dlut.mail.wuchen2020.signinserver.util.CourseHelper;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Student;

/**
 * 终端签到的业务逻辑
 * </br>
 * 另见 {@link cn.edu.dlut.mail.wuchen2020.signinserver.controller.SigninController}
 * 
 * @author Wu Chen
 */
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
        if (student == null) throw new BusinessException(2001, "未查询到该学生");
        
        // 根据时间戳或者当前时间确定是第几周, 周几和第几节课
        long now = timestamp; // FIXME !!TEST!!
        // long now = System.currentTimeMillis();
        // if (timestamp <= now && now - timestamp < 10000) now = timestamp;
        Instant instant = Instant.ofEpochMilli(now);
        LocalDate date = LocalDate.ofInstant(instant, ZoneId.systemDefault());
        LocalTime time = LocalTime.ofInstant(instant, ZoneId.systemDefault());
        int week = CourseHelper.getWeekFromDate(date);
        int day = CourseHelper.getDayOfWeekFromDate(date);
        CourseHelper.Lesson lesson = CourseHelper.getLessonFromTime(time);
        
        // 从student_course和course表里查询学生上课的信息
        CourseSelection courseSelection = courseSelectionDAO.findCourseSelected(student.getId(), week, day, lesson.getOrder());
        if (courseSelection == null) {
            return SigninStatus.NO_CLASS;
        }
        Course course = courseSelection.getCourse();
        if (!course.getLocation().equals(location)) {
            return SigninStatus.WRONG_CLASS;
        }
        
        // 创建签到记录
        SigninRecord record = new SigninRecord();
        record.setStudent(student);
        record.setCourse(course);
        record.setLocation(location);
        record.setTime(new Date(now));
        
        // 查询签到记录并确定签到类型
        Date startDate = Date.from(lesson.getStartTime().atDate(date).atZone(ZoneId.systemDefault()).toInstant());
        List<SigninRecord> records = signinRecordDAO.findByStudentAndTimeBetween(student, startDate, new Date());
        if (records == null || records.isEmpty()) {
            if (lesson.isBreaking()) {
                return SigninStatus.NO_CLASS;
            }
            record.setStatus(SigninStatus.SUCCESS);
        } else {
            SigninRecord lastRecord = records.get(records.size() - 1);
            if (lesson.isBreaking()) {
                if (lastRecord.getStatus() == SigninStatus.LEAVE) {
                    return SigninStatus.NO_CLASS;
                }
                record.setStatus(SigninStatus.LEAVE);
            } else {
                if (lastRecord.getStatus() == SigninStatus.SUCCESS
                        || lastRecord.getStatus() == SigninStatus.COME_BACK) {
                    record.setStatus(SigninStatus.TEMP_LEAVE);
                } else if (lastRecord.getStatus() == SigninStatus.TEMP_LEAVE) {
                    record.setStatus(SigninStatus.COME_BACK);
                } else {
                    return SigninStatus.ERROR;
                }
            }
        }
        signinRecordDAO.save(record);
        
        return record.getStatus();
    }
}
