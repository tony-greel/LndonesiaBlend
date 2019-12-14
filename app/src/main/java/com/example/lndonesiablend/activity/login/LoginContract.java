package com.example.lndonesiablend.activity.login;
import com.example.lndonesiablend.base.ILoading;


public interface LoginContract {

  interface Presenter{
    void login();
    void lerificationCode();
  }

  interface View extends ILoading {
    String getUsername();
    String getPassword();
    boolean loginVerificationInput();
    boolean lerificationCodeVerificationInput();
    void onLoginSuccess();
    void verificationCodeSuccess();
  }

}
