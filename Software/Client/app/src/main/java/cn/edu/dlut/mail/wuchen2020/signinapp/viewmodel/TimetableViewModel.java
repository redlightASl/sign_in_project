package cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;

import cn.edu.dlut.mail.wuchen2020.signinapp.model.Course;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Result;
import cn.edu.dlut.mail.wuchen2020.signinapp.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class TimetableViewModel extends ViewModel {
    MutableLiveData<List<Course>> timetable = new MutableLiveData<>();

    public MutableLiveData<List<Course>> getTimetable() {
        return timetable;
    }

    public void updateTimetable(int type) {
        if (type == 0) {
            Request request = new Request.Builder()
                    .url("https://" + HttpUtil.SIGNIN_API + "/api/student/getTimetable?week=1") // TODO 周数先写死
                    .get()
                    .build();
            HttpUtil.getClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {}
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Result<List<Course>> result = Result.fromJsonArray(response.body().string(), Course.class);
                    timetable.postValue(result.getData());
                }
            });
        } else if (type == 1) {
            Request request = new Request.Builder()
                    .url("https://" + HttpUtil.SIGNIN_API + "/api/teacher/getTimetable?week=1") // TODO 周数先写死
                    .get()
                    .build();
            HttpUtil.getClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {}
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Result<List<Course>> result = Result.fromJsonArray(response.body().string(), Course.class);
                    timetable.postValue(result.getData());
                }
            });
        }
    }
}
