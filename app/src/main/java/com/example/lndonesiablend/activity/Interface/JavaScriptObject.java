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

    @JavascriptInterface
    public void openLoad() {
        this.javaCallback.openLoad();
    }

    @JavascriptInterface
    public void closeLoad() {
        this.javaCallback.closeLoad();
    }

    @JavascriptInterface
    public void submitPersonalInformation() {
        this.javaCallback.submitPersonalInformation();
    }

    @JavascriptInterface
    public void personalInformationSubmittedSuccessfully() {
        this.javaCallback.personalInformationSubmittedSuccessfully();

    }

    @JavascriptInterface
    public void submitContact() {
        this.javaCallback.submitContact();

    }

    @JavascriptInterface
    public void contactSubmittedSuccessfully() {
        this.javaCallback.contactSubmittedSuccessfully();

    }

    @JavascriptInterface
    public void submitWorkInformation() {
        this.javaCallback.submitWorkInformation();

    }

    @JavascriptInterface
    public void workInformationSubmittedSuccessfully() {
        this.javaCallback.workInformationSubmittedSuccessfully();

    }

    @JavascriptInterface
    public void submitBankCardInformation() {
        this.javaCallback.submitBankCardInformation();

    }

    @JavascriptInterface
    public void bankCardInformationSubmittedSuccessfully() {
        this.javaCallback.bankCardInformationSubmittedSuccessfully();

    }

    @JavascriptInterface
    public void accountLoginSucceeded() {
        this.javaCallback.accountLoginSucceeded();

    }

    @JavascriptInterface
    public void accountLoginFailed() {
        this.javaCallback.accountLoginFailed();

    }

    @JavascriptInterface
    public void verifyLoginSuccess() {
        this.javaCallback.verifyLoginSuccess();

    }

    @JavascriptInterface
    public void failedToVerifyLogin() {
        this.javaCallback.failedToVerifyLogin();

    }

    @JavascriptInterface
    public void verificationCodeSentSuccessfully() {
        this.javaCallback.verificationCodeSentSuccessfully();
    }

    @JavascriptInterface
    public void failedToSendVerificationCode() {
        this.javaCallback.failedToSendVerificationCode();

    }


}
