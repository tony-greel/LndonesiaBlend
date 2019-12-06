package com.example.lndonesiablend.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.lndonesiablend.LndonesiaBlendApp;
import com.example.lndonesiablend.bean.BaseBean;
import com.example.lndonesiablend.bean.UploadDeviceInfoBean;
import com.example.lndonesiablend.bean.UserBean;
import com.example.lndonesiablend.http.Api;
import com.example.lndonesiablend.http.HttpRequestClient;
import com.example.lndonesiablend.utils.DeviceInfoFactoryUtil;
import com.example.lndonesiablend.utils.DeviceUtil;
import com.example.lndonesiablend.utils.MD5Utils;
import com.example.lndonesiablend.utils.SharePreUtil;
import com.example.lndonesiablend.utils.StringFormatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

public class UpAppService extends IntentService {

    private static final String TAG = "UpAppService";

    private DeviceInfoFactoryUtil mDeviceFactory;

    public UpAppService(){
        super("UpAppService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mDeviceFactory = DeviceInfoFactoryUtil.getInstance(this);
        putDeviceInfo();
    }


    private void putDeviceInfo(){
        UploadDeviceInfoBean uploadDeviceInfo = new UploadDeviceInfoBean();
        uploadDeviceInfo.setUser_id(SharePreUtil.getString(this, UserBean.userId, ""));
        uploadDeviceInfo.setSelf_mobile(SharePreUtil.getString(this, UserBean.phonepre, "") + ""
                + SharePreUtil.getString(this, UserBean.phone, ""));
        uploadDeviceInfo.setImei(mDeviceFactory.getIMEI());
        uploadDeviceInfo.setSim(mDeviceFactory.getSim());
        uploadDeviceInfo.setBrands(mDeviceFactory.getBrands());
        uploadDeviceInfo.setMobile_model(mDeviceFactory.getMobil());
        uploadDeviceInfo.setCpu_model(mDeviceFactory.getCpuModel());
        uploadDeviceInfo.setCpu_cores(mDeviceFactory.getCpuCores());
        uploadDeviceInfo.setResolution(mDeviceFactory.getResolution());
        uploadDeviceInfo.setVersion(mDeviceFactory.getSystemVersion());
        uploadDeviceInfo.setApplist(readApp());
        uploadDeviceInfo.setBluetoothId(DeviceUtil.getBlueTooth());
        uploadDeviceInfo.setWifi(DeviceUtil.getWifiName(this));
        Log.d("Ljj",mDeviceFactory.getIMEI()+
                "\n"+mDeviceFactory.getSim()+
                "\n"+mDeviceFactory.getBrands()+
                "\n"+mDeviceFactory.getMobil()+
                "\n"+mDeviceFactory.getCpuModel()+
                "\n"+mDeviceFactory.getCpuCores()+
                "\n"+mDeviceFactory.getResolution()+
                "\n"+mDeviceFactory.getSystemVersion());
        TreeMap requestParams = buildCommonParams();
        ArrayList<UploadDeviceInfoBean> uploadDeviceInfos = new ArrayList<>();
        uploadDeviceInfos.add(uploadDeviceInfo);
        requestParams.put("record", uploadDeviceInfos);
        requestParams.put("account_id", SharePreUtil.getString(this, UserBean.phonepre, "") + ""
                + SharePreUtil.getString(this, UserBean.phone, ""));
        requestParams.put("uuid", mDeviceFactory.getDeviceUuid() + "");
        requestParams.put("imei",mDeviceFactory.getIMEI());
        //进行加密
        String sign = signParameter(requestParams, SharePreUtil.getString(this, UserBean.token, ""));
        requestParams.put("sign", sign);

        HttpRequestClient.getRetrofitHttpClient().create(Api.class)
                .submitDevice(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        Log.d("LJJ", baseBean.getMessage());
                        Toast.makeText(UpAppService.this, baseBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(UpAppService.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("LJJ", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 获取系统应用信息
     *
     * @return
     */
    private List<UploadDeviceInfoBean.AppList> readApp() {
        List<UploadDeviceInfoBean.AppList> appLists = new ArrayList<>();
        UploadDeviceInfoBean.AppList appList = null;
        PackageManager pm = getPackageManager();
        List<PackageInfo> list = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo packageInfo : list) {
            String appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            String packageName = packageInfo.packageName;
            if (!TextUtils.isEmpty(appName) && !TextUtils.isEmpty(packageName)) {
                appList = new UploadDeviceInfoBean.AppList();
                appList.setFirstTime(StringFormatUtils.formatTime(packageInfo.firstInstallTime, StringFormatUtils.FORMAT));
                appList.setLastTime(StringFormatUtils.formatTime(packageInfo.lastUpdateTime, StringFormatUtils.FORMAT));
                appList.setName(appName) ;
                appList.setPackageName(packageName);
                appList.setVersionCode(packageInfo.versionName);
                appLists.add(appList);
            }
        }
        return appLists;
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
