package com.example.lndonesiablend.base;

import android.content.Context;
import android.util.Log;

import com.example.lndonesiablend.LndonesiaBlendApp;
import com.example.lndonesiablend.utils.MD5Utils;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public abstract class BasePresenter<V> {

    private static final String TAG = "BasePresenter";

    public V view;

    protected Context mContext;

    //加载View,建立连接
    public void attach(V v) {
        this.view = v;
        mContext = LndonesiaBlendApp.getAppContext();
    }

    //断开连接
    public void detattch() {
        if (view != null) {
            view = null;
        }
    }

    public boolean isAttached(){
        return view != null;
    }

    public V getView(){
        return view;
    }

    //进行数据MD5加密
    public String signParameter(TreeMap<String, Object> paramterMap, String signKey) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : paramterMap.entrySet()) {// 拼接参数字符串
            if (entry.getValue() != null && entry.getValue().toString().length() > 0) {
                stringBuilder.append(entry.getValue().toString());
            }
        }
        System.out.println("明密" + stringBuilder.toString());
        String signData = MD5Utils.MD5(MD5Utils.MD5(stringBuilder.append(signKey).toString()).concat("signkey2"));
        System.out.println("加密" + signData);
        return signData;
    }

    //公共参数
    public TreeMap<String, Object> buildCommonParams() {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("app_version", LndonesiaBlendApp.APP_VERSION);
        treeMap.put("version", LndonesiaBlendApp.VERSION);
        treeMap.put("channel", LndonesiaBlendApp.CHANNEL);
        treeMap.put("timestamp", LndonesiaBlendApp.TIMESTAMP);
        treeMap.put("pkg_name", LndonesiaBlendApp.APPLICATION_ID);

        return treeMap;
    }


    //上传文字
    public MultipartBody.Part toRequestBodyOfText(String keyStr, String value) {
        Log.d(TAG, "key : " + keyStr + "    value : " + value);
        MultipartBody.Part body = MultipartBody.Part.createFormData(keyStr, value);
        Log.d(TAG, "toRequestBodyOfText: " + body);
        return body;
    }


    //上传图片
    public MultipartBody.Part toRequestBodyOfImage(String keyStr, File pFile) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), pFile);
        MultipartBody.Part filedata = MultipartBody.Part.createFormData(keyStr, pFile.getName(), requestBody);
        Log.d(TAG, "toRequestBodyOfImage: " + filedata);
        return filedata;
    }
}
