package cn.edu.dlut.mail.wuchen2020.signinserver.handler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @ExceptionHandler(Exception.class)
    public ResultVO handleException(Exception e) {
        LOGGER.error("An error has occurred: ", e);
        return ResultVO.fail(-1, e.getLocalizedMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResultVO handleBusinessException(BusinessException e) {
        return ResultVO.fail(e.getCode(), e.getLocalizedMessage());
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultVO handleRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResultVO.fail(1, "不支持的请求方法");
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, Object> argumentMap = new LinkedHashMap<>();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            argumentMap.put(fieldName, message);
        }
        return ResultVO.fail(2, "请求参数错误", argumentMap);
    }
    
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<ResultVO> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException e) {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(ResultVO.fail(304, "没有新消息"));
    }
}
