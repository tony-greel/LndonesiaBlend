package com.example.lndonesiablend.activity.login;


import com.example.lndonesiablend.base.ILoading;

import java.util.List;
import java.util.TreeMap;

import okhttp3.MultipartBody;

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
  }

}
