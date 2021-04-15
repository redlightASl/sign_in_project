package cn.edu.dlut.mail.wuchen2020.signinserver.exception;

public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1691418001902089117L;
    private int code;

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
}
