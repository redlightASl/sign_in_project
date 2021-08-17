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
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.SigninRecordVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.StudentInfoVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 学生查询签到状态和历史记录接口
 * </br>
 * 另见 {@link cn.edu.dlut.mail.wuchen2020.signinserver.controller.StudentService}
 * 
 * @author Wu Chen
 */
@RestController
@RequestMapping("/api/student")
@Tag(name = "student", description = "学生查询接口")
public class StudentController {
    @Autowired
    public StudentService studentService;
    
    @GetMapping("/getStudentInfo")
    @Operation(summary = "获取学生信息")
    @ApiResponse(description = "学生信息", content = @Content(schema = @Schema(implementation = StudentInfoVO.class)))
    public Object getStudentInfo(HttpSession httpSession) {
        UserSession user = (UserSession) httpSession.getAttribute("user");
        if (user.getRole() == UserRole.STUDENT) {
            return studentService.getStudentInfo(user.getUsername());
        }
        return ResultVO.fail(1003, "没有权限");
    }
    
    @GetMapping("/getTimetable")
    @Operation(summary = "获取学生课程表")
    @ApiResponse(description = "学生课程表", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LessonVO.class))))
    public Object getTimetable(HttpSession httpSession,
            @RequestParam(name = "week", defaultValue = "1") int week) {
        UserSession user = (UserSession) httpSession.getAttribute("user");
        if (user.getRole() == UserRole.STUDENT) {
            return studentService.getTimetable(user.getUsername(), week);
        }
        return ResultVO.fail(1003, "没有权限");
    }
    
    @GetMapping("/getSigninStatus")
    @Operation(summary = "获取学生当前签到状态")
    @ApiResponse(description = "学生签到状态", content = @Content(schema = @Schema(example = "{\n  \"status\": 0,\n  \"course\": LessonVO\n}")))
    public Object getSigninStatus(HttpSession httpSession) {
        UserSession user = (UserSession) httpSession.getAttribute("user");
        if (user.getRole() == UserRole.STUDENT) {
            return studentService.getSigninStatus(user.getUsername());
        }
        return ResultVO.fail(1003, "没有权限");
    }
    
    @GetMapping("/getSigninHistory")
    @Operation(summary = "获取学生签到历史记录")
    @ApiResponse(description = "存储了学生签到历史记录的数组", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SigninRecordVO.class))))
    public Object getSigninHistory(HttpSession httpSession,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "count", required = false, defaultValue = "20") int count) {
        UserSession user = (UserSession) httpSession.getAttribute("user");
        if (user.getRole() == UserRole.STUDENT) {
            return studentService.getSigninHistory(user.getUsername(), page, count);
        }
        return ResultVO.fail(1003, "没有权限");
    }
}
