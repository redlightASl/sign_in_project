package cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import cn.edu.dlut.mail.wuchen2020.signinapp.model.Result;
import cn.edu.dlut.mail.wuchen2020.signinapp.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginViewModel extends ViewModel {
    public String username;
    public String password;

    private MutableLiveData<Integer> loginResult = new MutableLiveData<>();

    public LoginViewModel() {}

    public LiveData<Integer> getLoginResult() {
        return loginResult;
    }

    public void login() {
        if (!isUserNameValid(username)) {
            loginResult.postValue(1);
            return;
        }
        if (!isPasswordValid(password)) {
            loginResult.postValue(2);
            return;
        }
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);
        RequestBody body = RequestBody.create(json.toString(), HttpUtil.JSON);
        Request request = new Request.Builder()
                .url("http://" + HttpUtil.SIGNIN_API + "/api/login")
                .post(body)
                .build();
        HttpUtil.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                loginResult.postValue(-1);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Result<Double> result = Result.fromJson(response.body().string());
                if (result.getCode() == 1001) {
                    loginResult.postValue(3);
                } else if (result.getCode() == 1002) {
                    loginResult.postValue(5);
                } else if (result.getData().intValue() == 2) {
                    loginResult.postValue(6);
                } else {
                    loginResult.postValue(0);
                }
            }
        });
    }

    private boolean isUserNameValid(String username) {
        return username != null && !username.trim().isEmpty();
    }

    private boolean isPasswordValid(String password) {
        return password != null && !password.trim().isEmpty();
    }
}
