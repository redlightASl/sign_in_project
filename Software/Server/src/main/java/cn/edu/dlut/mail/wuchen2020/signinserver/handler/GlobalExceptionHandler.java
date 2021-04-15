package cn.edu.dlut.mail.wuchen2020.signinserver.handler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cn.edu.dlut.mail.wuchen2020.signinserver.exception.CustomException;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.ResultVO;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(Exception.class)
    ResultVO handleException(Exception e){
        LOGGER.error(e.getMessage(), e);
        return ResultVO.fail(-1, e.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    ResultVO handleBusinessException(CustomException e){
        return ResultVO.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResultVO handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, Object> argumentMap = new LinkedHashMap<>();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            argumentMap.put(fieldName, message);
        }
        return ResultVO.fail(1, "请求参数错误", argumentMap);
    }
}
