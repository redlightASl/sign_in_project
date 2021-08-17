package cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.Objects;

import cn.edu.dlut.mail.wuchen2020.signinapp.api.AuthAPI;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Result;
import cn.edu.dlut.mail.wuchen2020.signinapp.util.HttpUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<Integer> userType = new MutableLiveData<>();

    private final AuthAPI authAPI;

    public MainViewModel() {
        authAPI = HttpUtil.getRetrofit().create(AuthAPI.class);
    }

    public LiveData<Integer> getUserType() {
        return userType;
    }

    public void updateUserType() {
        authAPI.getUserType().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                userType.postValue(-1);
            }

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    Result<Integer> result = Result.fromJson(Objects.requireNonNull(response.body()).string(), Integer.class);
                    Integer type = result.getData();
                    userType.postValue(type != null ? type : -1);
                } catch (IOException | NullPointerException e) {
                    onFailure(call, e);
                }
            }
        });
    }
}
