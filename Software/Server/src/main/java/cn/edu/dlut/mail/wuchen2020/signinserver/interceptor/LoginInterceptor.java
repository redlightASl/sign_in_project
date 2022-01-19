package cn.edu.dlut.mail.wuchen2020.signinserver.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.ResultVO;

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
            // response.sendError(401);
            // TODO ErrorController处理完之后好像没进Filter, 我实在没办法了
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().append(new ObjectMapper().writeValueAsString(ResultVO.fail(401, "未登录")));
            return false;
        }
        return true;
    }
}
