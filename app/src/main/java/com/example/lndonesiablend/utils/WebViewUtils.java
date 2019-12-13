package com.example.lndonesiablend.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.example.lndonesiablend.LndonesiaBlendApp;
import com.example.lndonesiablend.R;
import com.example.lndonesiablend.activity.Interface.JavaCallback;
import com.example.lndonesiablend.activity.Interface.JavaScriptObject;
import com.example.lndonesiablend.activity.face.FaceDistinguishActivity;
import com.example.lndonesiablend.activity.upload.IdUploadActivity;
import com.example.lndonesiablend.activity.upload.PictureUploadActivity;
import com.example.lndonesiablend.bean.UserBean;
import com.example.lndonesiablend.event.BuryingPointEvent;
import com.example.lndonesiablend.load.MainLoadView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class WebViewUtils {

    private static final String LJJ = "WebViewUtils";
    private static MainLoadView mianLoadView;
    private static MainLoadView.Builder mianLoadViewBuilder;

    @SuppressLint("SetJavaScriptEnabled")
    public static void initWebView(Activity activity, Context context, WebView webMain, WebChromeClient webChromeClient, String string) {
        WebSettings mSettings = webMain.getSettings();

        mianLoadViewBuilder = new MainLoadView.Builder(context);
        mianLoadView = mianLoadViewBuilder.setContent(context.getString(R.string.loading)).create();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(webMain,true);
        }

        mSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        mSettings.setPluginsEnabled(true);
        mSettings.setSaveFormData(false);
        webMain.refreshPlugins(true);
        mSettings.setLoadsImagesAutomatically(true);//支持自动加载图片

        mSettings.setJavaScriptEnabled(true);
        mSettings.setAllowContentAccess(true);
        mSettings.setAllowFileAccessFromFileURLs(true);
        mSettings.setAllowUniversalAccessFromFileURLs(true);
        mSettings.setLoadWithOverviewMode(true);
        mSettings.setDomStorageEnabled(true);
        mSettings.setDatabaseEnabled(true);
        mSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        mSettings.setDefaultTextEncodingName("utf-8");
        String appCachePath = LndonesiaBlendApp.getAppContext().getCacheDir().getAbsolutePath();
        mSettings.setAppCachePath(appCachePath);
        mSettings.setAllowFileAccess(true);
        mSettings.setAppCacheEnabled(true);
        mSettings.setSupportZoom(false);
        mSettings.setBuiltInZoomControls(false);
        mSettings.setUseWideViewPort(true);
        mSettings.setNeedInitialFocus(false);
        mSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        mSettings.setBlockNetworkImage(false);
        mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webMain.setWebChromeClient(webChromeClient);

        webMain.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
            }
        });

        webMain.addJavascriptInterface(new JavaScriptObject(context, new JavaCallback() {
            @Override
            public void jumpPictureUpload() {
                Log.d("TAG", "qqq");
                Intent intent = new Intent(context, PictureUploadActivity.class);
                context.startActivity(intent);
            }

            @Override
            public void jumpIdUpload() {
                Intent intent = new Intent(context, IdUploadActivity.class);
                context.startActivity(intent);
            }

            @Override
            public void jumpLiveAuthentication() {
                Intent intent = new Intent(context, FaceDistinguishActivity.class);
                context.startActivity(intent);
            }

            @Override
            public void openLoad() {
                activity.runOnUiThread(() -> mianLoadView.show());
            }

            @Override
            public void closeLoad() {
                activity.runOnUiThread(() -> mianLoadView.cancel());
            }


            @Override
            public void submitPersonalInformation() {
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.SUBMIT_PERSONAL_INFORMATION.getCode()));
                Log.d(LJJ, "提交个人信息：" + BuryingPointEvent.SUBMIT_PERSONAL_INFORMATION.getCode());
            }

            @Override
            public void personalInformationSubmittedSuccessfully() {
                Log.d(LJJ, "已成功提交个人信息");
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.PERSONAL_INFORMATION_SUBMITTED_SUCCESSFULLY.getCode()));
            }

            @Override
            public void submitContact() {
                Log.d(LJJ, "提交联系方式");
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.SUBMIT_CONTACT.getCode()));
            }

            @Override
            public void contactSubmittedSuccessfully() {
                Log.d(LJJ, "成功提交联系");
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.CONTACT_SUBMITTED_SUCCESSFULLY.getCode()));

            }

            @Override
            public void submitWorkInformation() {
                Log.d(LJJ, "提交工作信息");
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.SUBMIT_WORK_INFORMATION.getCode()));

            }

            @Override
            public void workInformationSubmittedSuccessfully() {
                Log.d(LJJ, "已成功提交工作信息");
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.WORK_INFORMATION_SUBMITTED_SUCCESSFULLY.getCode()));

            }

            @Override
            public void submitBankCardInformation() {
                Log.d(LJJ, "提交银行卡信息");
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.SUBMIT_BANK_CARD_INFORMATION.getCode()));

            }

            @Override
            public void bankCardInformationSubmittedSuccessfully() {
                Log.d(LJJ, "银行卡信息已成功提交");
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.BANK_CARD_INFORMATION_SUBMITTED_SUCCESSFULLY.getCode()));
            }

            @Override
            public void accountLoginSucceeded() {
                Log.d(LJJ, "帐户登录成功");
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.ACCOUNT_LOGIN_SUCCEEDED.getCode()));
                AdjustEvent adjustEvent = new AdjustEvent(BuryingPointEvent.UPLOAD_DATA.getCode());
                adjustEvent.addCallbackParameter("event_code", "login");
                adjustEvent.addCallbackParameter("mobile", SharePreUtil.getString(context, UserBean.phone, ""));
                Adjust.trackEvent(adjustEvent);
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.ACCOUNT_LOGIN_SUCCEEDED.getCode()));
            }

            @Override
            public void accountLoginFailed() {
                Log.d(LJJ, "帐户登录失败");
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.ACCOUNT_LOGIN_FAILED.getCode()));
            }

            @Override
            public void verifyLoginSuccess() {
                Log.d(LJJ, "验证登录成功");
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.VERIFY_LOGIN_SUCCESS.getCode()));
                AdjustEvent adjustEvent = new AdjustEvent(BuryingPointEvent.UPLOAD_DATA.getCode());
                adjustEvent.addCallbackParameter("event_code", "login");
                adjustEvent.addCallbackParameter("mobile", SharePreUtil.getString(context, UserBean.phone, ""));
                Adjust.trackEvent(adjustEvent);
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.VERIFY_LOGIN_SUCCESS.getCode()));
            }

            @Override
            public void failedToVerifyLogin() {
                Log.d(LJJ, "验证登录失败");
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.FAILED_TO_VERIFY_LOGIN.getCode()));

            }

            @Override
            public void verificationCodeSentSuccessfully() {
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.VERIFICATION_CODE_SENT_SUCCESSFULLY.getCode()));

            }

            @Override
            public void failedToSendVerificationCode() {
                Adjust.trackEvent(new AdjustEvent(BuryingPointEvent.FAILED_TO_SEND_VERIFICATION_CODE.getCode()));

            }

        }), "android");
        webMain.loadUrl(string);
    }
}
