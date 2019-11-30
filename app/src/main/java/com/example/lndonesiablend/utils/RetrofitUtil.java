package com.example.lndonesiablend.utils;
import com.example.lndonesiablend.BuildConfig;

import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    private static RetrofitUtil retrofitUtil;
    private HttpLoggingInterceptor interceptor;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;

    private RetrofitUtil() {
        HostnameVerifier verifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //默认请求任何主机名都返回true
                return true;
            }
        };
        interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new RetrofitAddInterceptorUtils())
                .addInterceptor(new RetrofitReceiveInterceptorUtils())
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .hostnameVerifier(verifier)
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.RELEASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    };
    public static RetrofitUtil getRetrofitUtil() {
        if (retrofitUtil == null) {
            synchronized (RetrofitUtil.class) {//线程锁---保证获取对象唯一
                if (retrofitUtil == null) {
                    retrofitUtil = new RetrofitUtil();
                }
            }
        }
        return retrofitUtil;
    }


    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }
}
