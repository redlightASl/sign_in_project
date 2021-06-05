package cn.edu.dlut.mail.wuchen2020.signinserver.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截未登录请求
 * 
 * @author Wu Chen
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            response.sendError(401);
            return false;
        }
        return true;
    }
}
