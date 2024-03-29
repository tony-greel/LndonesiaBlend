package com.example.lndonesiablend.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.lndonesiablend.LndonesiaBlendApp;
import com.example.lndonesiablend.bean.BaseBean;
import com.example.lndonesiablend.bean.ContactBean;
import com.example.lndonesiablend.bean.UploadContactsBean;
import com.example.lndonesiablend.bean.UserBean;
import com.example.lndonesiablend.helper.DataBaseHelper;
import com.example.lndonesiablend.http.Api;
import com.example.lndonesiablend.http.HttpRequestClient;
import com.example.lndonesiablend.utils.DeviceInfoFactoryUtil;
import com.example.lndonesiablend.utils.LogUtils;
import com.example.lndonesiablend.utils.MD5Utils;
import com.example.lndonesiablend.utils.SharePreUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UpContactsService extends IntentService {

    private static final String TAG = "UpContactsService";

    private DeviceInfoFactoryUtil mDeviceFactory;

    public UpContactsService() {
        super("UpContactsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mDeviceFactory = DeviceInfoFactoryUtil.getInstance(this);
        putContactsInfo();
    }

    private void putContactsInfo() {
        List<ContactBean> contacts = DataBaseHelper.getContacts();  //通过数据库中获取通讯录的信息
        LogUtils.d(contacts.toString());
        LogUtils.d(Arrays.toString(contacts.toArray()));
        if (contacts != null && contacts.size() > 0) {

            UploadContactsBean uploadContactsBean = new UploadContactsBean();
            List<UploadContactsBean.OtherContactsInfo> otherContactsInfoList = new ArrayList<>();
            for (ContactBean entity : contacts) {
                UploadContactsBean.OtherContactsInfo otherContactsInfo
                        = new UploadContactsBean.OtherContactsInfo();
                otherContactsInfo.setOther_name(entity.getName());
                if (entity.getNumber() != null && entity.getNumber().size() > 0) {
                    otherContactsInfo.setOther_mobile(entity.getNumber().get(0).getNumber());
                }
                otherContactsInfoList.add(otherContactsInfo);
            }
            uploadContactsBean.setUser_id(SharePreUtil.getString(this, UserBean.userId, ""));
            uploadContactsBean.setSelf_mobile(SharePreUtil.getString(this, UserBean.phonepre,"") + "" +
                    SharePreUtil.getString(this, UserBean.phone, ""));
            uploadContactsBean.setRecord(otherContactsInfoList);
            TreeMap requestParams = buildCommonParams();
            requestParams.put("user_id", uploadContactsBean.getUser_id());
            requestParams.put("self_mobile", uploadContactsBean.getSelf_mobile());
            requestParams.put("record", uploadContactsBean.getRecord());
            requestParams.put("account_id", uploadContactsBean.getSelf_mobile());
            requestParams.put("uuid", mDeviceFactory.getDeviceUuid());
            requestParams.put("imei", mDeviceFactory.getIMEI());

            //进行加密
            String sign = signParameter(requestParams, SharePreUtil.getString(this, UserBean.token, ""));
            requestParams.put("sign", sign);

            HttpRequestClient.getRetrofitHttpClient().create(Api.class)
                    .contactsInfoUpLoad(requestParams)
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BaseBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseBean baseBean) {
                            Log.d(TAG, "onNext: 联系人信息提交完成"+baseBean.getMessage());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG,"onError"+e.getMessage());
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
        treeMap.put("app_version", LndonesiaBlendApp.VERSION_NUMBER);
        treeMap.put("version", LndonesiaBlendApp.VERSION);
        treeMap.put("channel", LndonesiaBlendApp.CHANNEL);
        treeMap.put("timestamp", LndonesiaBlendApp.TIMESTAMP);
        treeMap.put("pkg_name", LndonesiaBlendApp.APPLICATION_ID);
        return treeMap;
    }
}
