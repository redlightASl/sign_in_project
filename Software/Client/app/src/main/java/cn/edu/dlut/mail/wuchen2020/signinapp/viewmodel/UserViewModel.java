package cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

import cn.edu.dlut.mail.wuchen2020.signinapp.SigninApplication;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Result;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Student;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Teacher;
import cn.edu.dlut.mail.wuchen2020.signinapp.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class UserViewModel extends ViewModel {
    private MutableLiveData<Student> student = new MutableLiveData<>();
    private MutableLiveData<Teacher> teacher = new MutableLiveData<>();

    public MutableLiveData<Student> getStudent() {
        return student;
    }

    public MutableLiveData<Teacher> getTeacher() {
        return teacher;
    }

    public void updateStudentInfo() {
        Request request = new Request.Builder()
                .url("https://" + HttpUtil.SIGNIN_API + "/api/student/getStudentInfo")
                .get()
                .build();
        HttpUtil.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {}
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Result<Student> result = Result.fromJson(response.body().string(), Student.class);
                student.postValue(result.getData());
            }
        });
    }

    public void updateTeacherInfo() {
        Request request = new Request.Builder()
                .url("https://" + HttpUtil.SIGNIN_API + "/api/teacher/getTeacherInfo")
                .get()
                .build();
        HttpUtil.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {}
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Result<Teacher> result = Result.fromJson(response.body().string(), Teacher.class);
                teacher.postValue(result.getData());
            }
        });
    }

    public void logout() {
        Request request = new Request.Builder()
                .url("https://" + HttpUtil.SIGNIN_API + "/api/logout")
                .get()
                .build();
        HttpUtil.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                SigninApplication.clearCookies();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                SigninApplication.clearCookies();
            }
        });
    }
}
