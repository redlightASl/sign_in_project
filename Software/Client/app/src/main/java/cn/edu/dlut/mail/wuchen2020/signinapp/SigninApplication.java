package cn.edu.dlut.mail.wuchen2020.signinapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cn.edu.dlut.mail.wuchen2020.signinapp.util.HttpUtil;


public class SigninApplication extends Application {
    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        HttpUtil.init();
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static void clearCookies() {
        getSharedPreferences().edit().remove("cookies").apply();
    }
}
