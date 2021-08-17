package cn.edu.dlut.mail.wuchen2020.signinserver.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.ResultVO;
import io.swagger.v3.oas.annotations.Hidden;

/**
 * 自定义页面异常
 * 
 * @author Wu Chen
 */
@RestController
@Hidden
public class CommonErrorController implements ErrorController {
    @RequestMapping("/error")
    public ResultVO handleError(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        int code = response.getStatus();
        String message;
        switch (code) {
        case 400:
            message = "无效请求";
            break;
        case 401:
            message = "未登录";
            break;
        case 403:
            message = "禁止访问";
            break;
        case 404:
            message = "未找到页面";
            break;
        case 405:
            message = "请求方法不允许";
            break;
        case 500:
            message = "内部服务器错误";
            break;
        default:
            message = "未知错误";
            break;
        }
        return ResultVO.fail(code, message);
    }
}
