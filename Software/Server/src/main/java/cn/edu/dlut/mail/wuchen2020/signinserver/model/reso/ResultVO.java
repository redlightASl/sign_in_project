package cn.edu.dlut.mail.wuchen2020.signinserver.model.reso;

/**
 * 统一返回值格式
 * 
 * @author Wu Chen
 */
public class ResultVO {
    private int code;
    private String message;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
    public static ResultVO success() {
        return new ResultVO();
    }
    
    public static ResultVO success(String message) {
        return success(message, null);
    }
    
    public static ResultVO success(Object data) {
        return success(null, data);
    }
    
    public static ResultVO success(String message, Object data) {
        ResultVO result = new ResultVO();
        result.setCode(0);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
    
    public static ResultVO fail(int code, String message) {
        return fail(code, message, null);
    }
    
    public static ResultVO fail(int code, String message, Object data) {
        ResultVO result = new ResultVO();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
