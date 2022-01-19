package cn.edu.dlut.mail.wuchen2020.signinserver.handler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.http.HttpStatus;
import cn.edu.dlut.mail.wuchen2020.signinserver.exception.BusinessException;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.ResultVO;

/**
 * 全局异常处理器
 * 
 * @author Wu Chen
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResultVO handleBusinessException(BusinessException e) {
        return ResultVO.fail(e.getCode(), e.getLocalizedMessage());
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO handleException(Exception e) {
        LOGGER.error("An error has occurred: ", e);
        return ResultVO.fail(500, e.getLocalizedMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, Object> argumentMap = new LinkedHashMap<>();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            argumentMap.put(fieldName, message);
        }
        return ResultVO.fail(400, "无效请求", argumentMap);
    }
    
    @ExceptionHandler(MissingRequestValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO handleMissingPathVariableException(MissingRequestValueException e) {
        return ResultVO.fail(400, "无效请求", e.getMessage());
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResultVO handleRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResultVO.fail(405, "请求方法不允许");
    }
    
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    public ResultVO handleAsyncRequestTimeoutException(AsyncRequestTimeoutException e) {
        return ResultVO.fail(304, "没有新消息");
    }
}
