package cn.edu.dlut.mail.wuchen2020.signinserver.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.dlut.mail.wuchen2020.signinserver.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
    public CourseService service;
    
    @GetMapping("/getWeek")
    @Operation(description = "获取当前周数")
    public Object getWeek(HttpSession httpSession) {
        return service.getWeek(httpSession);
    }
    
    @GetMapping("/getTotalWeek")
    @Operation(description = "获取本学期总共周数")
    public Object getTotalWeek(HttpSession httpSession) {
        return service.getTotalWeek(httpSession);
    }
    
    @GetMapping("/getPeriod")
    @Operation(description = "获取当前是第几节课")
    public Object getPeriod(HttpSession httpSession) {
        return service.getPeriod(httpSession);
    }
    
    @GetMapping("/getTotalPeriod")
    @Operation(description = "获取共多少节课")
    public Object getTotalPeriod(HttpSession httpSession) {
        return service.getTotalPeriod(httpSession);
    }
    
    @GetMapping("/getAllLessonTimes")
    @Operation(description = "获取所有课程的节数以及上下课时间")
    public Object getAllLessonTimes(HttpSession httpSession) {
        return service.getAllLessonTimes(httpSession);
    }
}
