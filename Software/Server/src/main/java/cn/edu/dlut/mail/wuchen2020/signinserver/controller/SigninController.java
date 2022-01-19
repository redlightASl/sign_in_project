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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    public SigninService signinService;

    @PostMapping("/signin")
    @Operation(summary = "终端签到(未加密)")
    @ApiResponse(description = "签到结果")
    public int signin(@Validated @RequestBody SigninVO value) {
        return signinService.signin(value.getFingerprint(), value.getLocation(), value.getTimestamp()).ordinal();
    }

    @GetMapping("/time")
    @Operation(summary = "终端获取当前时间")
    @ApiResponse(description = "当前时间戳(毫秒)")
    public long getTime() {
        return System.currentTimeMillis();
    }
}
