package cn.edu.dlut.mail.wuchen2020.signinapp.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

public final class AndroidUtil {
    private AndroidUtil() {}

    public static void toast(Context context, String text) {
        Toast.makeText(context, text, text.length() > 15 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, @StringRes int resId) {
        toast(context, context.getResources().getString(resId));
    }
}
