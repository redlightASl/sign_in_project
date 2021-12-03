package cn.edu.dlut.mail.wuchen2020.signinserver.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.reqo.LoginVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.ResultVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 用户(学生/教师/管理员)登录和注销的接口
 * </br>
 * 另见 {@link cn.edu.dlut.mail.wuchen2020.signinserver.service.AuthService}
 * 
 * @author Wu Chen
 */
@RestController
@RequestMapping("/api")
@Tag(name = "auth", description = "用户登录接口")
public class AuthController {
    @Autowired
    public AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "登录")
    @ApiResponse(description = "登录成功的用户种类或者登录失败提示", content = @Content(schema = @Schema(implementation = Integer.class)))
    public Object login(HttpSession httpSession, @RequestBody @Validated LoginVO loginVO) {
        Object user = httpSession.getAttribute("user");
        if (user == null) {
            String username = loginVO.getUsername();
            String password = loginVO.getPassword();
            return authService.login(httpSession, username, password);
        }
        return ResultVO.fail(1002, "您已登录");
    }

    @GetMapping("/logout")
    @Operation(summary = "注销")
    @ApiResponse(description = "是否注销成功")
    public boolean logout(HttpSession httpSession) {
        return authService.logout(httpSession);
    }
    
    @GetMapping("/usertype")
    @Operation(summary = "查询用户种类")
    @ApiResponse(description = "用户种类(0: 学生, 1: 教师, 2: 管理员)")
    public int getUserType(HttpSession httpSession) {
        return authService.getUserType(httpSession);
    }
    
    @PostMapping("/changePassword")
    @Operation(summary = "修改密码")
    @ApiResponse(description = "是否成功修改密码")
    public boolean changePassword(HttpSession httpSession, @RequestBody String oldPassword, @RequestBody String newPassword) {
        return authService.changePassword(httpSession, oldPassword, newPassword);
    }
}
