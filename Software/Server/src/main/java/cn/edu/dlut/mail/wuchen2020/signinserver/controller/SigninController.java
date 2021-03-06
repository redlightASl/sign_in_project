package cn.edu.dlut.mail.wuchen2020.signinserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.reqo.SigninVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.service.SigninService;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 终端签到接口
 * </br>
 * 另见 {@link cn.edu.dlut.mail.wuchen2020.signinserver.controller.SigninService}
 * 
 * @author Wu Chen
 */
@RestController
@RequestMapping("/api")
@Tag(name = "signin", description = "终端签到接口")
public class SigninController {
    @Autowired
    public SigninService service;

    @PostMapping("/signin")
    public int signin(@Validated @RequestBody SigninVO value) {
        return service.signin(value.getFingerprint(), value.getLocation(), value.getTimestamp()).ordinal();
    }

    @GetMapping("/time")
    public long getTime() {
        return System.currentTimeMillis();
    }
}
