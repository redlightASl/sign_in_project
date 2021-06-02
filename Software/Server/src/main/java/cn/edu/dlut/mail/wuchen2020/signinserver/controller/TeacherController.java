package cn.edu.dlut.mail.wuchen2020.signinserver.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.UserSession;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.UserSession.UserRole;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.ResultVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.service.TeacherService;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 教师查询本节课程签到人数及自己班级学生签到记录接口
 * </br>
 * 另见 {@link cn.edu.dlut.mail.wuchen2020.signinserver.controller.TeacherService}
 * 
 * @author Wu Chen
 */
@RestController
@RequestMapping("/api/teacher")
@Tag(name = "teacher", description = "教师查询接口")
public class TeacherController {
    @Autowired
    public TeacherService teacherService;
    
    @GetMapping("/getTeacherInfo")
    public Object getTeacherInfo(HttpSession httpSession) {
        UserSession user = (UserSession) httpSession.getAttribute("user");
        if (user.getRole() == UserRole.TEACHER) {
            return teacherService.getTeacherInfo(user.getUsername());
        }
        return ResultVO.fail(1002, "没有权限");
    }
    
}
