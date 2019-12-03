package com.example.lndonesiablend.activity.Interface;
import android.content.Context;
import android.webkit.JavascriptInterface;

public class JavaScriptObject implements JavaCallback{

    public Context mContext;
    private JavaCallback javaCallback;

    public JavaScriptObject(Context context,JavaCallback javaCallback1) {
        mContext = context;
        this.javaCallback = javaCallback1;
    }

    @JavascriptInterface
    public void jumpPictureUpload(){
        this.javaCallback.jumpPictureUpload();
    }

    @JavascriptInterface
    public void jumpIdUpload() {
        this.javaCallback.jumpIdUpload();
    }

    @JavascriptInterface
    public void jumpLiveAuthentication() {
        this.javaCallback.jumpLiveAuthentication();
    }

}
