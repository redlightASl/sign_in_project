package cn.edu.dlut.mail.wuchen2020.signinapp.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("login")
    Call<ResponseBody> login(@Body RequestBody body);

    @GET("usertype")
    Call<ResponseBody> getUserType();

    @GET("logout")
    Call<ResponseBody> logout();

    @POST("changePassword")
    Call<ResponseBody> changePassword(@Body RequestBody body);
}
