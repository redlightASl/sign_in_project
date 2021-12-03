package cn.edu.dlut.mail.wuchen2020.signinserver.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.UserSession;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.UserSession.UserRole;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.LessonVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.ResultVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.TeacherInfoVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "获取教师信息")
    @ApiResponse(description = "教师信息", content = @Content(schema = @Schema(implementation = TeacherInfoVO.class)))
    public Object getTeacherInfo(HttpSession httpSession) {
        UserSession user = (UserSession) httpSession.getAttribute("user");
        if (user.getRole() == UserRole.TEACHER) {
            return teacherService.getTeacherInfo(user.getUsername());
        }
        return ResultVO.fail(1003, "没有权限");
    }
    
    @GetMapping("/getTimetable")
    @Operation(summary = "获取教师课程表")
    @ApiResponse(description = "教师课程表", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LessonVO.class))))
    public Object getTimetable(HttpSession httpSession,
            @RequestParam(name = "week", required = false, defaultValue = "1") int week) {
        UserSession user = (UserSession) httpSession.getAttribute("user");
        if (user.getRole() == UserRole.TEACHER) {
            return teacherService.getTimetable(user.getUsername(), week);
        }
        return ResultVO.fail(1003, "没有权限");
    }
    
    @GetMapping("/getSigninStatus")
    @Operation(summary = "获取教师所在教学班签到状态")
    @ApiResponse(description = "教师所在教学班签到状态", content = @Content(schema = @Schema(example = "{\n  \"totalCount\": 0,\n  \"signinCount\": 0,\n  \"course\": LessonVO\n}")))
    public Object getSigninStatus(HttpSession httpSession) {
        UserSession user = (UserSession) httpSession.getAttribute("user");
        if (user.getRole() == UserRole.TEACHER) {
            return teacherService.getSigninStatus(user.getUsername());
        }
        return ResultVO.fail(1003, "没有权限");
    }
}
