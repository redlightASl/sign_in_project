package cn.edu.dlut.mail.wuchen2020.signinserver.service;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.edu.dlut.mail.wuchen2020.signinserver.dao.AdminDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.dao.StudentDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.dao.TeacherDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Admin;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Student;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Teacher;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.UserSession;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.UserSession.UserRole;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.ResultVO;

/**
 * 用户(学生/教师/管理员)登录和注销的业务逻辑
 * </br>
 * 另见 {@link cn.edu.dlut.mail.wuchen2020.signinserver.controller.AuthController}
 * 
 * @author Wu Chen
 */
@Service
public class AuthService {
    private static final String PASSWORD_SALT = "_W.C=2020_nb@DLUT-EDA/";

    @Autowired
    public StudentDAO studentDAO;
    @Autowired
    public TeacherDAO teacherDAO;
    @Autowired
    public AdminDAO adminDAO;

    public Object login(HttpSession httpSession, String username, String password) {
        UserSession user = null;
        String md5Password = encodeMD5(password);
        Student student = studentDAO.findByNumber(username);
        if (student != null) {
            if (student.getPassword().equals(md5Password)) {
                user = new UserSession();
                user.setRole(UserRole.STUDENT);
            }
        } else {
            Teacher teacher = teacherDAO.findByNumber(username);
            if (teacher != null) {
                if (teacher.getPassword().equals(md5Password)) {
                    user = new UserSession();
                    user.setRole(UserRole.TEACHER);
                }
            } else {
                Admin admin = adminDAO.findByUsername(username);
                if (admin != null) {
                    if (admin.getPassword().equals(md5Password)) {
                        user = new UserSession();
                        user.setRole(UserRole.ADMIN);
                    }
                }
            }
        }
        if (user != null) {
            user.setUsername(username);
            user.setSession(UUID.randomUUID().toString());
            user.setExpire(new Date());
            httpSession.setAttribute("user", user);
            return ResultVO.success("登录成功", user.getRole().ordinal());
        }
        return ResultVO.fail(1001, "账号或密码错误");
    }

    public int getUserInfo(HttpSession httpSession) {
        UserSession user = (UserSession) httpSession.getAttribute("user");
        return user.getRole().ordinal();
    }

    public boolean logout(HttpSession httpSession) {
        httpSession.removeAttribute("user");
        return true;
    }

    public static String encodeMD5(String password) {
        String saltPassword = PASSWORD_SALT + password;
        String md5Password = DigestUtils.md5DigestAsHex(saltPassword.getBytes());
        return md5Password;
    }
}
