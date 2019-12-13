package com.example.lndonesiablend.helper;

import com.example.lndonesiablend.http.Api;
import com.example.lndonesiablend.http.RetrofitClient;

public class RequestHelper {
    public static Api getRequestApi() {
        RetrofitClient client = RetrofitClient.getInstance();
        return client.getRetrofit(Api.baseUrl).create(Api.class);
    }
}
