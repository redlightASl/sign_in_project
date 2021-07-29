package cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import cn.edu.dlut.mail.wuchen2020.signinapp.api.StudentAPI;
import cn.edu.dlut.mail.wuchen2020.signinapp.api.TeacherAPI;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Result;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.SigninRecord;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.SigninStatus;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.SigninStatusTeacher;
import cn.edu.dlut.mail.wuchen2020.signinapp.util.HttpUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusViewModel extends ViewModel {
    private final MutableLiveData<SigninStatus> status = new MutableLiveData<>();
    private final MutableLiveData<List<SigninRecord>> records = new MutableLiveData<>();
    private final MutableLiveData<SigninStatusTeacher> statusTeacher = new MutableLiveData<>();

    private final StudentAPI studentAPI;
    private final TeacherAPI teacherAPI;

    public StatusViewModel() {
        studentAPI = HttpUtil.getRetrofit().create(StudentAPI.class);
        teacherAPI = HttpUtil.getRetrofit().create(TeacherAPI.class);
    }

    public MutableLiveData<SigninStatus> getStatus() {
        return status;
    }

    public MutableLiveData<List<SigninRecord>> getRecords() {
        return records;
    }

    public MutableLiveData<SigninStatusTeacher> getStatusTeacher() {
        return statusTeacher;
    }

    public void updateStudentStatus() {
        studentAPI.getSigninStatus().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {}

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    Result<SigninStatus> result = Result.fromJson(Objects.requireNonNull(response.body()).string(), SigninStatus.class);
                    status.postValue(result.getData());
                } catch (IOException | NullPointerException ignored) {}
            }
        });
    }

    public void updateStudentRecords() {
        studentAPI.getSigninHistory().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {}

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    Result<List<SigninRecord>> result = Result.fromJsonArray(Objects.requireNonNull(response.body()).string(), SigninRecord.class);
                    records.postValue(result.getData());
                } catch (IOException | NullPointerException ignored) {                }
            }
        });
    }

    public void updateTeacherStatus() {
        teacherAPI.getSigninStatus().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {}

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    Result<SigninStatusTeacher> result = Result.fromJson(Objects.requireNonNull(response.body()).string(), SigninStatusTeacher.class);
                    statusTeacher.postValue(result.getData());
                } catch (IOException | NullPointerException ignored) {}
            }
        });
    }
}
