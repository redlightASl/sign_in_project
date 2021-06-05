package cn.edu.dlut.mail.wuchen2020.signinserver.exception;

/**
 * 业务异常类
 * 
 * @author Wu Chen
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1691418001902089117L;

    private int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
