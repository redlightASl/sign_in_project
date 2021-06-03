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
        String md5Password = encodeMD5(password);
        UserSession user = null;
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
    
    public boolean changePassword(HttpSession httpSession, String oldPassword, String newPassword) {
        if (isVaildPassword(oldPassword) && isVaildPassword(newPassword)) {
            String oldMd5Password = encodeMD5(oldPassword);
            String newMd5Password = encodeMD5(newPassword);
            UserSession user = (UserSession) httpSession.getAttribute("user");
            if (user.getRole() == UserRole.STUDENT) {
                Student student = studentDAO.findByNumber(user.getUsername());
                if (!student.getPassword().equals(oldMd5Password)) {
                    return false;
                }
                student.setPassword(newMd5Password);
                studentDAO.save(student);
            } else if (user.getRole() == UserRole.TEACHER) {
                Teacher teacher = teacherDAO.findByNumber(user.getUsername());
                if (!teacher.getPassword().equals(oldMd5Password)) {
                    return false;
                }
                teacher.setPassword(newMd5Password);
                teacherDAO.save(teacher);
            } else if (user.getRole() == UserRole.ADMIN) {
                Admin admin = adminDAO.findByUsername(user.getUsername());
                if (!admin.getPassword().equals(oldMd5Password)) {
                    return false;
                }
                admin.setPassword(newMd5Password);
                adminDAO.save(admin);
            } else {
                return false;
            }
            logout(httpSession);
            return true;
        }
        return false;
    }
    
    public static boolean isVaildPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static String encodeMD5(String password) {
        String saltPassword = PASSWORD_SALT + password;
        String md5Password = DigestUtils.md5DigestAsHex(saltPassword.getBytes());
        return md5Password;
    }
}
