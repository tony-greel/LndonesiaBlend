package com.example.lndonesiablend.http;

import android.util.Log;

import com.example.lndonesiablend.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequestClient {
    public static final String TAG = "HttpRequestClientTAG";

    private static Retrofit retrofit;

    private static OkHttpClient getOkHttpClient() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d(TAG, message);
            }
        });
        loggingInterceptor.setLevel(level);
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
//        httpClientBuilder.connectTimeout(120, TimeUnit.SECONDS);
        //OkHttp进行添加拦截器loggingInterceptor
        httpClientBuilder.addInterceptor(loggingInterceptor);
        return httpClientBuilder.build();
    }

    public static Retrofit getRetrofitHttpClient() {
        if (null == retrofit) {
            synchronized (HttpRequestClient.class) {
                if (null == retrofit) {
                    retrofit = new Retrofit.Builder()
                            .client(getOkHttpClient())
                            .baseUrl(BuildConfig.RELEASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(getOkhttpClient())
                            .build();
                }
            }
        }
        return retrofit;
    }

    public static OkHttpClient getOkhttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder.readTimeout(25, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder requestBuilder = request.newBuilder();
                        requestBuilder.addHeader("Content-Type", "application/json;charset=UTF-8")
                                .addHeader("x-versionname", BuildConfig.VERSION_NAME)
                                .addHeader("x-versioncode", BuildConfig.VERSION_CODE + "");
                        Request newRequest = requestBuilder.build();
                        return chain.proceed(newRequest);
                    }
                })
                .connectTimeout(35, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)//设置超时
                .build();
        return client;
    }

}
