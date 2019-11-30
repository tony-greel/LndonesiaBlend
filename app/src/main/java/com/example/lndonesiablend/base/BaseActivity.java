package com.example.lndonesiablend.base;

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
import com.example.lndonesiablend.broadcast.NetChangeObserver;
import com.example.lndonesiablend.broadcast.NetStateReceiver;
import com.example.lndonesiablend.broadcast.NetUtils;
import com.example.lndonesiablend.utils.MD5Utils;
import com.gyf.barlibrary.ImmersionBar;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private Unbinder mBinder;
    protected NetChangeObserver mNetChangeObserver = null;
    public Context mContext;
    private static final String LJJ = "PictureUploadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        setContentView(getLayoutId());
        mBinder = ButterKnife.bind(this);
        setStatusBar();
        initView();
        broadcast();
    }

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
        treeMap.put("app_version", LndonesiaBlendApp.APP_VERSION);
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

}
