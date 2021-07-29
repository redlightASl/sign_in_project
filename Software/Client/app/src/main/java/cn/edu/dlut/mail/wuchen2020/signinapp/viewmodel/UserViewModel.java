package cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.Objects;

import cn.edu.dlut.mail.wuchen2020.signinapp.SigninApplication;
import cn.edu.dlut.mail.wuchen2020.signinapp.api.AuthAPI;
import cn.edu.dlut.mail.wuchen2020.signinapp.api.StudentAPI;
import cn.edu.dlut.mail.wuchen2020.signinapp.api.TeacherAPI;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Result;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Student;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Teacher;
import cn.edu.dlut.mail.wuchen2020.signinapp.util.HttpUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<Student> student = new MutableLiveData<>();
    private final MutableLiveData<Teacher> teacher = new MutableLiveData<>();

    private final AuthAPI authAPI;
    private final StudentAPI studentAPI;
    private final TeacherAPI teacherAPI;

    public UserViewModel() {
        authAPI = HttpUtil.getRetrofit().create(AuthAPI.class);
        studentAPI = HttpUtil.getRetrofit().create(StudentAPI.class);
        teacherAPI = HttpUtil.getRetrofit().create(TeacherAPI.class);
    }

    public MutableLiveData<Student> getStudent() {
        return student;
    }

    public MutableLiveData<Teacher> getTeacher() {
        return teacher;
    }

    public void updateStudentInfo() {
        studentAPI.getStudentInfo().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {}

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    Result<Student> result = Result.fromJson(Objects.requireNonNull(response.body()).string(), Student.class);
                    student.postValue(result.getData());
                } catch (IOException | NullPointerException ignored) {}
            }
        });
    }

    public void updateTeacherInfo() {
        teacherAPI.getTeacherInfo().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {}

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    Result<Teacher> result = Result.fromJson(Objects.requireNonNull(response.body()).string(), Teacher.class);
                    teacher.postValue(result.getData());
                } catch (IOException | NullPointerException ignored) {}
            }
        });
    }

    public void logout() {
        authAPI.logout().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                SigninApplication.clearCookies();
            }

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                SigninApplication.clearCookies();
            }
        });
    }
}
