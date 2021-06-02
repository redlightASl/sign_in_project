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
    public AuthService service;

    @PostMapping("/login")
    @Operation(description = "登录")
    public Object login(HttpSession httpSession, @RequestBody @Validated LoginVO loginVO) {
        Object user = httpSession.getAttribute("user");
        if (user == null) {
            String username = loginVO.getUsername();
            String password = loginVO.getPassword();
            return service.login(httpSession, username, password);
        }
        return ResultVO.fail(1002, "您已登录");
    }

    @GetMapping("/userinfo")
    @Operation(description = "查询用户信息")
    public int getUserInfo(HttpSession httpSession) {
        return service.getUserInfo(httpSession);
    }

    @GetMapping("/logout")
    @Operation(description = "注销")
    public boolean logout(HttpSession httpSession) {
        return service.logout(httpSession);
    }
}
