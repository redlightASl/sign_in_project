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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import cn.edu.dlut.mail.wuchen2020.signinserver.dao.CourseSelectionDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.dao.SigninRecordDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.dao.StudentDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.SigninRecord.SigninStatus;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Course;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.CourseSelection;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.SigninRecord;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Student;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.LessonVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.SigninRecordVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.StudentInfoVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.util.DateUtils;
import cn.edu.dlut.mail.wuchen2020.signinserver.util.LessonTimeQuerier;

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
    private StudentDAO studentDAO;
    @Autowired
    private CourseSelectionDAO courseSelectionDAO;
    @Autowired
    private SigninRecordDAO signinRecordDAO;
    @Autowired
    private LessonTimeQuerier querier;

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
    
    public List<LessonVO> getTimetable(String number, int week) {
        List<LessonVO> list = new ArrayList<>();
        Student student = studentDAO.findByNumber(number);
        if (student != null) {
            for (CourseSelection courseSelection : courseSelectionDAO.findByStudent(student)) {
                Course course = courseSelection.getCourse();
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
        Student student = studentDAO.findByNumber(number);
        if (student != null) {
            Instant instant = Instant.now();
            LocalDate date = LocalDate.ofInstant(instant, ZoneId.systemDefault());
            LocalTime time = LocalTime.ofInstant(instant, ZoneId.systemDefault());
            int week = querier.getWeek(date);
            int day = DateUtils.getDayOfWeek(date);
            int period = querier.getPeriod(time);
            CourseSelection courseSelection = courseSelectionDAO.findCourseSelected(student.getId(), week, day, period);
            if (courseSelection != null) {
                Date startDate = DateUtils.localDateTimeToDate(querier.getStartTime(period).atDate(date));
                List<SigninRecord> records = signinRecordDAO.findByStudentAndTimeAfter(student, startDate);
                if (records != null && !records.isEmpty()) {
                    SigninRecord lastRecord = records.get(records.size() - 1);
                    map.put("status", lastRecord.getStatus().ordinal());
                } else {
                    map.put("status", SigninStatus.NOT_SIGN_IN.ordinal());
                }
                Course course = courseSelection.getCourse();
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
            } else {
                map.put("status", SigninStatus.NO_LESSON.ordinal());
            }
        } else {
            map.put("status", SigninStatus.ERROR.ordinal());
        }
        return map;
    }

    public List<SigninRecordVO> getSigninHistory(String number, int page, int count) {
        List<SigninRecordVO> list = new ArrayList<>();
        Student student = studentDAO.findByNumber(number);
        if (student != null) {
            Page<SigninRecord> pages = signinRecordDAO.findByStudent(student, PageRequest.of(page, count, Sort.Direction.DESC, "id"));
            for (SigninRecord record : pages.toList()) {
                SigninRecordVO recordVO = new SigninRecordVO();
                recordVO.setStudentName(student.getName());
                recordVO.setCourseName(record.getCourse().getName());
                recordVO.setLocation(record.getLocation());
                recordVO.setTime(record.getTime());
                recordVO.setStatus(record.getStatus().ordinal());
                list.add(recordVO);
            }
        }
        return list;
    }
}
