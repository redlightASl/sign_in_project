package cn.edu.dlut.mail.wuchen2020.signinapp.util;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GsonUtil {
    private final static Gson gson = new GsonBuilder().create();

    public static Gson gson() {
        return gson;
    }

    public static ParameterizedType createParameterizedType(Class<?> raw, Type... args) {
        return new ParameterizedTypeImpl(raw, args);
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class<?> raw;
        private final Type[] args;

        public ParameterizedTypeImpl(Class<?> raw, Type... args) {
            this.raw = raw;
            this.args = args != null ? args : new Type[0];
        }

        @NonNull
        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }

        @NonNull
        @Override
        public Type getRawType() {
            return raw;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
