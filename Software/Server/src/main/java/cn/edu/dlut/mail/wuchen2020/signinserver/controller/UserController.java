package cn.edu.dlut.mail.wuchen2020.signinserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.reqo.LoginVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.service.AuthService;

/**
 * 管理员/学生/教师登录和注销的接口
 * @author WC
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    public AuthService service;

    @PostMapping("/login")
    public boolean login(@Validated @RequestBody LoginVO loginVO) {
        String username = loginVO.getUsername();
        String password = loginVO.getPassword();
        return service.login(username, password, 0);
    }
    
    @RequestMapping("/status")
    public boolean status() {
        return true;
    }

    @RequestMapping("/logout")
    public boolean logout() {
        return service.logout(0);
    }
}
