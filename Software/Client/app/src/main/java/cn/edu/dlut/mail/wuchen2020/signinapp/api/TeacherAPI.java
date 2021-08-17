package cn.edu.dlut.mail.wuchen2020.signinapp.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TeacherAPI {
    @GET("teacher/getTeacherInfo")
    Call<ResponseBody> getTeacherInfo();

    @GET("teacher/getTimetable")
    Call<ResponseBody> getTimetable(@Query("week") int week);

    @GET("teacher/getSigninStatus")
    Call<ResponseBody> getSigninStatus();
}
