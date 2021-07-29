package cn.edu.dlut.mail.wuchen2020.signinapp.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CourseAPI {
    @GET("course/getWeek")
    Call<ResponseBody> getWeek();

    @GET("course/getTotalWeek")
    Call<ResponseBody> getTotalWeek();

    @GET("course/getPeriod")
    Call<ResponseBody> getPeriod();

    @GET("course/getTotalPeriod")
    Call<ResponseBody> getTotalPeriod();

    @GET("course/getAllLessonTimes")
    Call<ResponseBody> getAllLessonTimes();
}
