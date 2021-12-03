package cn.edu.dlut.mail.wuchen2020.signinapp.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MessageAPI {
    @GET("pullMessages")
    Call<ResponseBody> pullMessages();
}
