package cn.edu.dlut.mail.wuchen2020.signinserver.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.LessonTimeVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 查询学期, 课节数和上课时间等的接口
 * </br>
 * 另见 {@link cn.edu.dlut.mail.wuchen2020.signinserver.service.CourseService}
 * 
 * @author Wu Chen
 */
@RestController
@RequestMapping("/api/course")
@Tag(name = "course", description = "课程查询接口")
public class CourseController {
    @Autowired
    public CourseService courseService;
    
    @GetMapping("/getWeek")
    @Operation(summary = "获取当前周数")
    @ApiResponse(description = "当前是第几周", content = @Content(schema = @Schema(implementation = Integer.class)))
    public Object getWeek(HttpSession httpSession) {
        return courseService.getWeek(httpSession);
    }
    
    @GetMapping("/getTotalWeek")
    @Operation(summary = "获取本学期总共周数")
    @ApiResponse(description = "本学期共几周", content = @Content(schema = @Schema(implementation = Integer.class)))
    public Object getTotalWeek(HttpSession httpSession) {
        return courseService.getTotalWeek(httpSession);
    }
    
    @GetMapping("/getPeriod")
    @Operation(summary = "获取当前课节")
    @ApiResponse(description = "当前是第几节课", content = @Content(schema = @Schema(implementation = Integer.class)))
    public Object getPeriod(HttpSession httpSession) {
        return courseService.getPeriod(httpSession);
    }
    
    @GetMapping("/getTotalPeriod")
    @Operation(summary = "获取一天总共课节数")
    @ApiResponse(description = "一天有几节课", content = @Content(schema = @Schema(implementation = Integer.class)))
    public Object getTotalPeriod(HttpSession httpSession) {
        return courseService.getTotalPeriod(httpSession);
    }
    
    @GetMapping("/getAllLessonTimes")
    @Operation(summary = "获取所有课程的节数以及上下课时间")
    @ApiResponse(description = "存储了所有课程的节数以及上下课时间的数组", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LessonTimeVO.class))))
    public Object getAllLessonTimes(HttpSession httpSession) {
        return courseService.getAllLessonTimes(httpSession);
    }
}
