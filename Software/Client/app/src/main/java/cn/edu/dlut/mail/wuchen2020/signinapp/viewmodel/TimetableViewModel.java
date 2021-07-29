package cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import cn.edu.dlut.mail.wuchen2020.signinapp.api.CourseAPI;
import cn.edu.dlut.mail.wuchen2020.signinapp.api.StudentAPI;
import cn.edu.dlut.mail.wuchen2020.signinapp.api.TeacherAPI;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Course;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.LessonTime;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Result;
import cn.edu.dlut.mail.wuchen2020.signinapp.util.HttpUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimetableViewModel extends ViewModel {
    private final MutableLiveData<Integer> totalWeeks = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentWeek = new MutableLiveData<>();
    private final MutableLiveData<List<LessonTime>> lessonTimes = new MutableLiveData<>();
    private final MutableLiveData<List<Course>> timetable = new MutableLiveData<>();

    private final CourseAPI courseAPI;
    private final StudentAPI studentAPI;
    private final TeacherAPI teacherAPI;

    public TimetableViewModel() {
        courseAPI = HttpUtil.getRetrofit().create(CourseAPI.class);
        studentAPI = HttpUtil.getRetrofit().create(StudentAPI.class);
        teacherAPI = HttpUtil.getRetrofit().create(TeacherAPI.class);
    }

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
        courseAPI.getTotalWeek().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {}

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    Result<Integer> result = Result.fromJson(Objects.requireNonNull(response.body()).string(), Integer.class);
                    totalWeeks.postValue(result.getData());
                } catch (IOException | NullPointerException ignored) {}
            }
        });
    }

    public void updateCurrentWeek() {
        courseAPI.getWeek().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {}

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    Result<Integer> result = Result.fromJson(Objects.requireNonNull(response.body()).string(), Integer.class);
                    currentWeek.postValue(result.getData());
                } catch (IOException | NullPointerException ignored) {}
            }
        });
    }

    public void updateLessonTimes() {
        courseAPI.getAllLessonTimes().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {}

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    Result<List<LessonTime>> result = Result.fromJsonArray(Objects.requireNonNull(response.body()).string(), LessonTime.class);
                    lessonTimes.postValue(result.getData());
                } catch (IOException | NullPointerException ignored) {}
            }
        });
    }

    public void updateTimetable(int type, int week) {
        Call<ResponseBody> call = type == 0 ? studentAPI.getTimetable(week) : teacherAPI.getTimetable(week);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {}

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    Result<List<Course>> result = Result.fromJsonArray(Objects.requireNonNull(response.body()).string(), Course.class);
                    timetable.postValue(result.getData());
                } catch (IOException | NullPointerException ignored) {}
            }
        });
    }
}
