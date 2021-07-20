package cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;

import cn.edu.dlut.mail.wuchen2020.signinapp.model.Course;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.LessonTime;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Result;
import cn.edu.dlut.mail.wuchen2020.signinapp.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class TimetableViewModel extends ViewModel {
    MutableLiveData<Integer> totalWeeks = new MutableLiveData<>();
    MutableLiveData<Integer> currentWeek = new MutableLiveData<>();
    MutableLiveData<List<LessonTime>> lessonTimes = new MutableLiveData<>();
    MutableLiveData<List<Course>> timetable = new MutableLiveData<>();

    public MutableLiveData<Integer> getTotalWeeks() {
        return totalWeeks;
    }

    public MutableLiveData<Integer> getCurrentWeek() {
        return currentWeek;
    }

    public MutableLiveData<List<LessonTime>> getLessonTimes() {
        return lessonTimes;
    }

    public MutableLiveData<List<Course>> getTimetable() {
        return timetable;
    }

    public void updateTotalWeeks() {
        Request request = new Request.Builder()
                .url("https://" + HttpUtil.SIGNIN_API + "/api/course/getTotalWeek")
                .get()
                .build();
        HttpUtil.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {}
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Result<Integer> result = Result.fromJson(response.body().string(), Integer.class);
                totalWeeks.postValue(result.getData());
            }
        });
    }

    public void updateCurrentWeek() {
        Request request = new Request.Builder()
                .url("https://" + HttpUtil.SIGNIN_API + "/api/course/getWeek")
                .get()
                .build();
        HttpUtil.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {}
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Result<Integer> result = Result.fromJson(response.body().string(), Integer.class);
                currentWeek.postValue(result.getData());
            }
        });
    }

    public void updateLessonTimes() {
        Request request = new Request.Builder()
                .url("https://" + HttpUtil.SIGNIN_API + "/api/course/getAllLessonTimes")
                .get()
                .build();
        HttpUtil.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {}
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Result<List<LessonTime>> result = Result.fromJsonArray(response.body().string(), LessonTime.class);
                lessonTimes.postValue(result.getData());
            }
        });
    }

    public void updateTimetable(int type, int week) {
        String url = "https://" + HttpUtil.SIGNIN_API + "/api/" + (type == 0 ? "student" : "teacher")
                + "/getTimetable?week=" + week;
        Request request = new Request.Builder()
                .url(url)
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
