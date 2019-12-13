package com.example.lndonesiablend.http;

import android.util.Log;

import com.example.lndonesiablend.BuildConfig;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.TlsVersion;
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

        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
                .build();//解决在Android5.0版本以下https无法访问
        ConnectionSpec spec1 = new ConnectionSpec.Builder(ConnectionSpec.CLEARTEXT).build();//兼容http接口

        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
//        httpClientBuilder.connectTimeout(120, TimeUnit.SECONDS);
        //OkHttp进行添加拦截器loggingInterceptor
        httpClientBuilder.addInterceptor(loggingInterceptor);
        httpClientBuilder.connectionSpecs(Arrays.asList(spec, spec1));
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

    /**
     * 设置超时时间
     *
     * @return
     */
    public static OkHttpClient getOkhttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder.readTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
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
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)//设置超时
                .build();
        return client;
    }

}
