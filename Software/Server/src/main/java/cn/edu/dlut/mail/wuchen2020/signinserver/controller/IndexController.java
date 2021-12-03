package cn.edu.dlut.mail.wuchen2020.signinserver.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.UserSession;
import cn.edu.dlut.mail.wuchen2020.signinserver.service.LongPollingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * 主页和页面模板映射
 * 
 * @author Wu Chen
 */
@Controller
public class IndexController {
    @Autowired
    public LongPollingService messageService;
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/api/pullMessages")
    @ResponseBody
    @Operation(summary = "长轮询获取新消息")
    @ApiResponse(description = "消息(超过60秒没有新消息会返回304)")
    public DeferredResult<Object> pullMessages(HttpSession httpSession) {
        UserSession user = (UserSession) httpSession.getAttribute("user");
        return messageService.goAsync(user.getUsername());
    }
}
