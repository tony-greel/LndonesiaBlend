package com.example.lndonesiablend.utils;

import android.content.SharedPreferences;

import com.example.lndonesiablend.LndonesiaBlendApp;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @Author: mosquito
 * @ClassName: RetrofitReceiveInterceptorUtils
 * @Description:
 * @CreateDate: 2019/6/19 13:50
 */
public class RetrofitReceiveInterceptorUtils implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            SharedPreferences.Editor config = LndonesiaBlendApp.getAppContext().getSharedPreferences("config", LndonesiaBlendApp.getAppContext().MODE_PRIVATE)
                    .edit();
            config.putStringSet("cookie", cookies);
            config.apply();
        }

        return originalResponse;
    }
}
