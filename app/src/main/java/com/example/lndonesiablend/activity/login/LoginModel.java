package com.example.lndonesiablend.activity.login;

import com.example.lndonesiablend.LndonesiaBlendApp;
import com.example.lndonesiablend.bean.BaseBean;
import com.example.lndonesiablend.bean.LoginBean;
import com.example.lndonesiablend.bean.UserBean;
import com.example.lndonesiablend.bean.VerificationCodeBean;
import com.example.lndonesiablend.helper.RequestHelper;
import com.example.lndonesiablend.helper.SpCacheHelper;
import com.example.lndonesiablend.utils.MD5Utils;
import com.example.lndonesiablend.utils.SharePreUtil;

import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginModel {

    public Observable<BaseBean<LoginBean>> login(String phone,String vcode) {
        TreeMap requestUserWorkParams = buildCommonParams();
        requestUserWorkParams.put("phone",phone);
        requestUserWorkParams.put("vcode",vcode);
        String sign = signParameter(requestUserWorkParams, SharePreUtil.getString(LndonesiaBlendApp.getAppContext(), UserBean.token, ""));
        requestUserWorkParams.put("sign", sign);
        return RequestHelper.getRequestApi().smsLogin(requestUserWorkParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseBean<VerificationCodeBean>> verificationCodeLogin(String requestParams) {
        TreeMap requestUserWorkParams = buildCommonParams();
        requestUserWorkParams.put("phone",requestParams);
        requestUserWorkParams.put("type","2");
        String sign = signParameter(requestUserWorkParams, SharePreUtil.getString(LndonesiaBlendApp.getAppContext(), UserBean.token, ""));
        requestUserWorkParams.put("sign", sign);
        return RequestHelper.getRequestApi().verificationCode(requestUserWorkParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void cacheUserLocation(LoginBean loginBean) {
        SpCacheHelper.withBuilder()
                .withString("phone", loginBean.getPhone())
                .withString("phonepre", loginBean.getPhonepre())
                .withString("user_id", loginBean.getUser_id())
                .withString("userName", loginBean.getUserName())
                .withString("custId", loginBean.getCustId())
                .withString("signKeyToken", loginBean.getSignKeyToken())
                .commit();
    }


    //公共参数
    private TreeMap<String, Object> buildCommonParams() {
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
}
