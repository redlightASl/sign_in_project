package cn.edu.dlut.mail.wuchen2020.signinapp.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StudentAPI {
    @GET("student/getStudentInfo")
    Call<ResponseBody> getStudentInfo();

    @GET("student/getTimetable")
    Call<ResponseBody> getTimetable(@Query("week") int week);

    @GET("student/getSigninStatus")
    Call<ResponseBody> getSigninStatus();

    @GET("student/getSigninHistory")
    Call<ResponseBody> getSigninHistory(); // TODO 分页 page, count
}
