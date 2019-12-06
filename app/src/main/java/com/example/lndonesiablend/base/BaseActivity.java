package com.example.lndonesiablend.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lndonesiablend.LndonesiaBlendApp;
import com.example.lndonesiablend.activity.camera.UploadPhotoActivity;
import com.example.lndonesiablend.bean.UserBean;
import com.example.lndonesiablend.broadcast.NetChangeObserver;
import com.example.lndonesiablend.broadcast.NetStateReceiver;
import com.example.lndonesiablend.broadcast.NetUtils;
import com.example.lndonesiablend.utils.MD5Utils;
import com.example.lndonesiablend.utils.SharePreUtil;
import com.example.lndonesiablend.utils.UIHelper;
import com.gyf.barlibrary.ImmersionBar;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected NetChangeObserver mNetChangeObserver = null;
    public Context mContext;
    private static final String LJJ = "PictureUploadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setStatusBar();
        initView();
        broadcast();
    }

    @SuppressLint("CheckResult")
    protected void jurisdictionApply(String a , String b, String c, String d){
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(a,b,c,d).subscribe(aBoolean -> {
            if (aBoolean) {
                uploadPicture();
            } else {
                UIHelper.showToast(this, "您有尚未通过的权限");
            }
        });
    }


    @SuppressLint("CheckResult")
    protected void uploadPositive(String a , String b, String c, String d){
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(a,b,c,d).subscribe(aBoolean -> {
            if (aBoolean) {
                uploadPositive(UploadPhotoActivity.PhotoType.POSITIVE_PHOTO);
            } else {
                UIHelper.showToast(this, "您有尚未通过的权限");
            }
        });
    }

    @SuppressLint("CheckResult")
    protected void uploadTheOtherSide(String a , String b, String c, String d){
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(a,b,c,d).subscribe(aBoolean -> {
            if (aBoolean) {
                uploadTheOtherSide(UploadPhotoActivity.PhotoType.BACK_PHOTO);
            } else {
                UIHelper.showToast(this, "您有尚未通过的权限");
            }
        });
    }
    protected void uploadPicture(){}
    protected void uploadPositive(UploadPhotoActivity.PhotoType mPositive){}
    protected void uploadTheOtherSide(UploadPhotoActivity.PhotoType mTheOtherSide){}


    /**
     * 广播监听网络
     */
    protected void broadcast(){
        // 网络改变的一个回掉类
        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetConnected(NetUtils.NetType type) {
                onNetworkConnected(type);
            }

            @Override
            public void onNetDisConnect() {
                onNetworkDisConnected();
            }
        };

        //开启广播去监听 网络 改变事件
        NetStateReceiver.registerObserver(mNetChangeObserver);
    }

    /**
     * 网络连接状态
     * @param type 网络状态
     */
    protected void onNetworkConnected(NetUtils.NetType type){ }

    /**
     * 网络断开的时候调用
     */
    protected void onNetworkDisConnected(){ }

    protected abstract void initView();

    protected abstract int getLayoutId();

    protected void setStatusBar() {
        ImmersionBar.with(this)
                .transparentStatusBar()
                .navigationBarEnable(true)
                .init();
    }

    @Override
    public void onClick(View v) {

    }

    //获取当前 Activity 对象
    public <A extends BaseActivity> A getActivity() {
        return (A) this;
    }

    //公共参数
    protected TreeMap<String, Object> buildCommonParams() {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("app_version", LndonesiaBlendApp.VERSION_NUMBER);
        treeMap.put("version", LndonesiaBlendApp.VERSION);
        treeMap.put("channel", LndonesiaBlendApp.CHANNEL);
        treeMap.put("timestamp", LndonesiaBlendApp.TIMESTAMP);
        treeMap.put("pkg_name", LndonesiaBlendApp.APPLICATION_ID);
        return treeMap;
    }

    //进行数据MD5加密
    protected String signParameter(TreeMap<String, Object> paramterMap, String signKey) {
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

    //上传文字
    protected MultipartBody.Part toRequestBodyOfText(String keyStr, String value) {
        Log.d(LJJ, "key : " + keyStr + "    value : " + value);
        MultipartBody.Part body = MultipartBody.Part.createFormData(keyStr, value);
        Log.d(LJJ, "toRequestBodyOfText: " + body);
        return body;
    }

    //上传图片
    protected MultipartBody.Part toRequestBodyOfImage(String keyStr, File pFile) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), pFile);
        MultipartBody.Part filedata = MultipartBody.Part.createFormData(keyStr, pFile.getName(), requestBody);
        Log.d(LJJ, "toRequestBodyOfImage: " + filedata);
        return filedata;
    }

    protected List<MultipartBody.Part> getParts(File file, String s){
        //进行数据加密
        TreeMap requestUserWorkParams = buildCommonParams();
        requestUserWorkParams.put("user_id", SharePreUtil.getString(mContext, UserBean.userId, ""));
        requestUserWorkParams.put("file_type", s);
        String sign = signParameter(requestUserWorkParams, SharePreUtil.getString(mContext, UserBean.token, ""));
        requestUserWorkParams.put("sign", sign);

        //接口上传参数
        List<MultipartBody.Part> parts = new ArrayList<>();
        parts.add(toRequestBodyOfText("user_id", SharePreUtil.getString(mContext, UserBean.userId, "")));
        parts.add(toRequestBodyOfText("file_type", s));
        parts.add(toRequestBodyOfText("sign", sign));
        parts.add(toRequestBodyOfText("app_version", LndonesiaBlendApp.VERSION_NUMBER));
        parts.add(toRequestBodyOfText("version", LndonesiaBlendApp.VERSION));
        parts.add(toRequestBodyOfText("channel", LndonesiaBlendApp.CHANNEL));
        parts.add(toRequestBodyOfText("timestamp", LndonesiaBlendApp.TIMESTAMP));
        parts.add(toRequestBodyOfText("pkg_name", LndonesiaBlendApp.APPLICATION_ID));
        parts.add(toRequestBodyOfImage("file", file));
        return parts;
    }

}
