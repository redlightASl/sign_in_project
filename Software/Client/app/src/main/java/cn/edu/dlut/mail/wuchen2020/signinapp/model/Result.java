package cn.edu.dlut.mail.wuchen2020.signinapp.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import cn.edu.dlut.mail.wuchen2020.signinapp.util.GsonUtil;

public class Result<T> {
    private int code;
    private String message;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Result<T> fromJson(String json, Class<T> clazz) {
        Gson gson = GsonUtil.gson();
        // 错误的写法, 详见: https://www.jianshu.com/p/d62c2be60617
        // Type type = new TypeToken<Result<T>>(){}.getType();
        Type type = GsonUtil.createParameterizedType(Result.class, clazz);
        return gson.fromJson(json, type);
    }
}
