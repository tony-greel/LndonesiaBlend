package com.example.lndonesiablend.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.lndonesiablend.LndonesiaBlendApp;
import com.example.lndonesiablend.R;
import com.example.lndonesiablend.activity.MainActivity;
import com.example.lndonesiablend.base.BaseActivity;
import com.example.lndonesiablend.base.BasePresenter;
import com.example.lndonesiablend.bean.ApplyLoanBean;
import com.example.lndonesiablend.bean.BaseBean;
import com.example.lndonesiablend.bean.Constant;
import com.example.lndonesiablend.bean.LoginBean;
import com.example.lndonesiablend.bean.UserBean;
import com.example.lndonesiablend.bean.VerificationCodeBean;
import com.example.lndonesiablend.http.Api;
import com.example.lndonesiablend.http.HttpRequestClient;
import com.example.lndonesiablend.utils.CountDownTimerUtils;
import com.example.lndonesiablend.utils.SharePreUtil;
import com.example.lndonesiablend.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.et_name) EditText etName;
    @BindView(R.id.et_password) EditText etPassword;

    @BindView(R.id.verification_code_but) Button verificationCodeBut;
    @BindView(R.id.login_but) Button loginBut;

    @BindView(R.id.mCvBorrow) CardView mCvBorrow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.verification_code_but, R.id.login_but})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.verification_code_but:
                presenter.lerificationCode();
                break;
            case R.id.login_but:
                presenter.login();
                break;
        }
    }

    @Override
    public String getUsername() {
        return etName.getText().toString();
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString();
    }

    @Override
    public boolean loginVerificationInput() {
        if (TextUtils.isEmpty(getUsername()) || TextUtils.isEmpty(getPassword())) {
            UIHelper.showToast(this, "账号或验证码不能为空");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean lerificationCodeVerificationInput() {
        if (TextUtils.isEmpty(getUsername()) ) {
            UIHelper.showToast(this, "手机号不能为空");
            return false;
        } else if (getUsername().length() == 12) {
            return true;
        }else {
            UIHelper.showToast(this, "手机号不能低于12位数字");
            return false;
        }
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void verificationCodeSuccess() {
        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(verificationCodeBut, 60000, 1000);
        mCountDownTimerUtils.start();
    }
}
