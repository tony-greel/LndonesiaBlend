package com.example.lndonesiablend.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.example.lndonesiablend.LndonesiaBlendApp;
import com.example.lndonesiablend.activity.Interface.JavaCallback;
import com.example.lndonesiablend.activity.Interface.JavaScriptObject;
import com.example.lndonesiablend.activity.submission.FaceDistinguishActivity;
import com.example.lndonesiablend.activity.upload.FaceUploadActivity;
import com.example.lndonesiablend.activity.upload.IdUploadActivity;
import com.example.lndonesiablend.activity.upload.PictureUploadActivity;

public class WebViewUtils {

    @SuppressLint("JavascriptInterface")
    public static void initWebView(Context context ,WebView webMain ,WebChromeClient webChromeClient,String string) {
            WebSettings mSettings = webMain.getSettings();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            mSettings.setDomStorageEnabled(true);
            mSettings.setJavaScriptEnabled(true);
            mSettings.setAllowContentAccess(true);
            mSettings.setAllowFileAccessFromFileURLs(true);
            mSettings.setAllowUniversalAccessFromFileURLs(true);
            mSettings.setLoadWithOverviewMode(true);
            mSettings.setDomStorageEnabled(true);
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

            webMain.addJavascriptInterface(new JavaScriptObject(context, new JavaCallback() {
                @Override
                public void jumpPictureUpload() {
                    Log.d("TAG","qqq");
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
            }),"android");
            webMain.loadUrl(string);
        }
    }
}
