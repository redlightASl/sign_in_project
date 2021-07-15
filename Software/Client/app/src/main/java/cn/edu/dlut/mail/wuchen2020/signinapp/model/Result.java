package cn.edu.dlut.mail.wuchen2020.signinapp.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

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

    public static <T> Result<T> fromJson(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Result<T>>(){}.getType();
        return gson.fromJson(json, type);
    }
}
