package cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

import cn.edu.dlut.mail.wuchen2020.signinapp.model.Result;
import cn.edu.dlut.mail.wuchen2020.signinapp.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class MainViewModel extends ViewModel {
    private MutableLiveData<Integer> userType = new MutableLiveData<>();

    public LiveData<Integer> getUserType() {
        return userType;
    }

    public void updateUserType() {
        Request request = new Request.Builder()
                .url("http://" + HttpUtil.SIGNIN_API + "/api/usertype")
                .get()
                .build();
        HttpUtil.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                userType.postValue(-1);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Result<Integer> result = Result.fromJson(response.body().string(), Integer.class);
                Integer type = result.getData();
                userType.postValue(type != null ? type : -1);
            }
        });
    }
}
