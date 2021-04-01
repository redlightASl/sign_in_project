package cn.edu.dlut.mail.wuchen2020.signinserver.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 终端签到接口
 * @author WC
 */
@RestController
public class SigninController {
    @PostMapping("/signin")
    public String signin() {

        return "";
    }
}
