package cn.edu.dlut.mail.wuchen2020.signinserver.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.dlut.mail.wuchen2020.signinserver.dao.CourseDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.dao.CourseSelectionDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.dao.SigninRecordDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.dao.TeacherDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Course;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.CourseSelection;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.SigninRecord;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.SigninRecord.SigninStatus;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Student;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Teacher;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.LessonVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.TeacherInfoVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.util.DateUtils;
import cn.edu.dlut.mail.wuchen2020.signinserver.util.LessonTimeQuerier;

/**
 * 教师的业务逻辑
 * </br>
 * 另见 {@link cn.edu.dlut.mail.wuchen2020.signinserver.controller.TeacherController}
 * 
 * @author Wu Chen
 */
@Service
public class TeacherService {
    @Autowired
    private TeacherDAO teacherDAO;
    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private CourseSelectionDAO courseSelectionDAO;
    @Autowired
    private SigninRecordDAO signinRecordDAO;
    @Autowired
    private LessonTimeQuerier querier;

    public TeacherInfoVO getTeacherInfo(String number) {
        Teacher teacher = teacherDAO.findByNumber(number);
        if (teacher != null) {
            TeacherInfoVO teacherInfo = new TeacherInfoVO();
            teacherInfo.setNumber(teacher.getNumber());
            teacherInfo.setName(teacher.getName());
            teacherInfo.setClassName(teacher.getClassName());
            return teacherInfo;
        }
        return null;
    }
    
    public List<LessonVO> getTimetable(String number, int week) {
        List<LessonVO> list = new ArrayList<>();
        Teacher teacher = teacherDAO.findByNumber(number);
        if (teacher != null) {
            for (Course course : courseDAO.findByTeacher(teacher)) {
                if (week >= course.getStartWeek() && week <= course.getEndWeek()) {
                    LessonVO lesson = new LessonVO();
                    lesson.setID(course.getId());
                    lesson.setName(course.getName());
                    lesson.setLocation(course.getLocation());
                    if (course.getTeacher() != null)
                        lesson.setTeacherName(course.getTeacher().getName());
                    lesson.setDayOfWeek(course.getDayOfWeek());
                    lesson.setStartTime(course.getStartTime());
                    lesson.setEndTime(course.getEndTime());
                    list.add(lesson);
                }
            }
        }
        return list;
    }

    public Map<String, Object> getSigninStatus(String number) {
        Map<String, Object> map = new HashMap<>();
        Teacher teacher = teacherDAO.findByNumber(number);
        if (teacher != null) {
            Instant instant = Instant.now();
            LocalDate date = LocalDate.ofInstant(instant, ZoneId.systemDefault());
            LocalTime time = LocalTime.ofInstant(instant, ZoneId.systemDefault());
            int week = querier.getWeek(date);
            int day = DateUtils.getDayOfWeek(date);
            int period = querier.getPeriod(time);
            Course course = courseDAO.findCourseTeached(teacher.getId(), week, day, period);
            if (course != null) {
                List<CourseSelection> courseSelections = courseSelectionDAO.findByCourse(course);
                map.put("totalCount", courseSelections.size());
                int signinCount = 0;
                for (CourseSelection courseSelection : courseSelections) {
                    Student student = courseSelection.getStudent();
                    Date startDate = DateUtils.localDateTimeToDate(querier.getStartTime(period).atDate(date));
                    List<SigninRecord> records = signinRecordDAO.findByStudentAndTimeAfter(student, startDate);
                    for (SigninRecord record : records) {
                        if (record.getStatus() == SigninStatus.SUCCESS) {
                            signinCount++;
                            break;
                        }
                    }
                }
                map.put("signinCount", signinCount);
                LessonVO lesson = new LessonVO();
                lesson.setID(course.getId());
                lesson.setName(course.getName());
                lesson.setLocation(course.getLocation());
                if (course.getTeacher() != null)
                    lesson.setTeacherName(course.getTeacher().getName());
                lesson.setDayOfWeek(course.getDayOfWeek());
                lesson.setStartTime(course.getStartTime());
                lesson.setEndTime(course.getEndTime());
                map.put("course", lesson);
            }
        }
        return map;
    }
}
