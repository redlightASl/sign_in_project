package cn.edu.dlut.mail.wuchen2020.signinserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.dlut.mail.wuchen2020.signinserver.dao.TeacherDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Teacher;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.TeacherInfoVO;

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
    public TeacherDAO teacherDAO;

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
    
    public Object getTerm(String username) {
        return null;
    }
    
    public Object getTimetable(String username, int week) {
        return null;
    }
    
    // TODO 教师查看班级签到情况

}
