package com.example.lndonesiablend.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.lndonesiablend.LndonesiaBlendApp;
import com.example.lndonesiablend.bean.BaseBean;
import com.example.lndonesiablend.bean.Constant;
import com.example.lndonesiablend.http.Api;
import com.example.lndonesiablend.utils.DeviceInfoFactoryUtil;
import com.example.lndonesiablend.utils.MD5Utils;
import com.example.lndonesiablend.utils.RetrofitUtil;

import com.example.lndonesiablend.utils.TimeUtils;
import com.weiyun.liveness.utils.SharePreUtil;

import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UpInstallReferrerInfoService extends IntentService {

    private static final String TAG = "UpInstallReferrerInfoSe";

    DeviceInfoFactoryUtil mDeviceFactory;

    public UpInstallReferrerInfoService() {
        super("UpInstallReferrerInfoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mDeviceFactory = DeviceInfoFactoryUtil.getInstance(this);
        putInstallReferrerInfo();
    }

    private synchronized void putInstallReferrerInfo() {
        Log.d(TAG, "putInstallReferrerInfo: " );
        boolean isRecordInstallationState = SharePreUtil.getBoolean(this, Constant.RECORD_INSTALLATION_STATE, false);
        if (!isRecordInstallationState){
            SharePreUtil.putBoolean(this, Constant.RECORD_INSTALLATION_STATE, true);
            String referrer = SharePreUtil.getString(this, Constant.GOOGLE_PLAY_REFERRER, "");
            TreeMap requestParams = buildCommonParams();
            requestParams.put("referrer", referrer);
            requestParams.put("imei", mDeviceFactory.getIMEI());
            requestParams.put("uuid", mDeviceFactory.getDeviceUuid());
            requestParams.put("download_time", TimeUtils.getCurrentDate(TimeUtils.dateFormatYMDHMS));

            //进行加密
            String sign = signParameter(requestParams, "signkey1");
            requestParams.put("sign", sign);

            RetrofitUtil.getRetrofitUtil().create(Api.class)
                    .appDownloadStatistic(requestParams)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BaseBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseBean baseBean) {
                            Log.d(TAG, "onNext: 上传安装信息成功");
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    /**
     * 进行数据MD5加密
     *
     * @param paramterMap
     * @param signKey
     * @return
     */
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

    protected TreeMap<String, Object> buildCommonParams() {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("app_version", LndonesiaBlendApp.APP_VERSION);
        treeMap.put("version", LndonesiaBlendApp.VERSION);
        treeMap.put("channel", LndonesiaBlendApp.CHANNEL);
        treeMap.put("timestamp", LndonesiaBlendApp.TIMESTAMP);
        treeMap.put("pkg_name", LndonesiaBlendApp.APPLICATION_ID);
        return treeMap;
    }

}
