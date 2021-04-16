package cn.edu.dlut.mail.wuchen2020.signinserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.reqo.LoginVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 管理员/学生/教师登录和注销的接口
 * @author WC
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "auth", description = "用户登录接口")
public class AuthController {
    @Autowired
    public AuthService service;

    @PostMapping("/login")
    @Operation(description = "用户登录")
    public boolean login(@RequestBody @Validated LoginVO loginVO) {
        String username = loginVO.getUsername();
        String password = loginVO.getPassword();
        return service.login(username, password, 0);
    }
    
    @GetMapping("/status")
    @Operation(description = "查询用户登录状态")
    public int status() {
        return 0;
    }

    @GetMapping("/logout")
    @Operation(description = "用户注销")
    public boolean logout() {
        return service.logout(0);
    }
}
