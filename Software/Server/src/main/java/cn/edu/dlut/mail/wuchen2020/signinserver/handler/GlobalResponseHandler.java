package cn.edu.dlut.mail.wuchen2020.signinserver.handler;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.ResultVO;

/**
 * 全局响应处理器
 * 
 * @author Wu Chen
 */
@RestControllerAdvice("cn.edu.dlut.mail.wuchen2020.signinserver.controller")
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        if (body == null) {
            return ResultVO.success();
        }
        if (body instanceof ResultVO) {
            return (ResultVO) body;
        }
        return ResultVO.success(body);
    }
}
