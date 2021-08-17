package cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Objects;

import cn.edu.dlut.mail.wuchen2020.signinapp.api.AuthAPI;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Result;
import cn.edu.dlut.mail.wuchen2020.signinapp.util.HttpUtil;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    public String username;
    public String password;

    private final MutableLiveData<Integer> loginResult = new MutableLiveData<>();

    private final AuthAPI authAPI;

    public LoginViewModel() {
        authAPI = HttpUtil.getRetrofit().create(AuthAPI.class);
    }

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
        authAPI.login(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                loginResult.postValue(-1);
            }

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    Result<Integer> result = Result.fromJson(Objects.requireNonNull(response.body()).string(), Integer.class);
                    if (result.getCode() == 1001) {
                        loginResult.postValue(3);
                    } else if (result.getCode() == 1002) {
                        loginResult.postValue(5);
                    } else if (result.getData() == 2) {
                        loginResult.postValue(6);
                    } else {
                        loginResult.postValue(0);
                    }
                } catch (IOException | NullPointerException e) {
                    onFailure(call, e);
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
