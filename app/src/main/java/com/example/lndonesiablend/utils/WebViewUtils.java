package com.example.lndonesiablend.utils;

import android.os.Build;
import android.os.Handler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.lndonesiablend.BuildConfig;
import com.example.lndonesiablend.LndonesiaBlendApp;

public class WebViewUtils {

    public static void initWebView(WebView webMain ,WebChromeClient webChromeClient,String string) {
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
            webMain.loadUrl(string);
        }

    }
}
