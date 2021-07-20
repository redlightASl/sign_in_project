package cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

import cn.edu.dlut.mail.wuchen2020.signinapp.model.Result;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.SigninStatus;
import cn.edu.dlut.mail.wuchen2020.signinapp.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class StatusViewModel extends ViewModel {
    private MutableLiveData<SigninStatus> status = new MutableLiveData<>();

    public MutableLiveData<SigninStatus> getStatus() {
        return status;
    }

    public void updateStudentSigninStatus() {
        Request request = new Request.Builder()
                .url("https://" + HttpUtil.SIGNIN_API + "/api/student/getSigninStatus")
                .get()
                .build();
        HttpUtil.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {}
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Result<SigninStatus> result = Result.fromJson(response.body().string(), SigninStatus.class);
                status.postValue(result.getData());
            }
        });
    }

    public void updateTeacherSigninStatus() {
        // TODO 教师所在班级签到状态
    }
}
