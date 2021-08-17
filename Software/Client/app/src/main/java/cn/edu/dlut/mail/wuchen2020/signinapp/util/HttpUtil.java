package cn.edu.dlut.mail.wuchen2020.signinapp.util;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.edu.dlut.mail.wuchen2020.signinapp.SigninApplication;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class HttpUtil {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static OkHttpClient client;
    private static Retrofit retrofit;

    public static void init() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CustomCookieJar());
        client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://signin.dawncraft.cc/api/")
                .client(client)
                .build();
    }

    public static OkHttpClient getClient() {
        return client;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    private static class CustomCookieJar implements CookieJar {
        @NonNull
        @Override
        public List<Cookie> loadForRequest(@NonNull HttpUrl httpUrl) {
            if (retrofit.baseUrl().host().equals(httpUrl.host())) {
                Set<String> cookieSet = SigninApplication.getSharedPreferences().getStringSet("cookies", null);
                if (cookieSet != null) {
                    List<Cookie> cookies = new ArrayList<>();
                    for (String cookieStr : cookieSet) {
                        Cookie cookie = Cookie.parse(httpUrl, cookieStr);
                        if (cookie != null) cookies.add(cookie);
                    }
                    return cookies;
                }
            }
            return Collections.emptyList();
        }

        @Override
        public void saveFromResponse(@NonNull HttpUrl httpUrl, @NonNull List<Cookie> list) {
            if (retrofit.baseUrl().host().equals(httpUrl.host())) {
                SharedPreferences.Editor editor = SigninApplication.getSharedPreferences().edit();
                Set<String> cookieSet = new LinkedHashSet<>();
                for (Cookie cookie : list) {
                    cookieSet.add(cookie.toString());
                }
                editor.putStringSet("cookies", cookieSet);
                editor.apply();
            }
        }
    }
}
