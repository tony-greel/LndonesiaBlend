package com.example.lndonesiablend.activity.login;
import android.util.Log;
import com.example.lndonesiablend.base.BasePresenter;
import com.example.lndonesiablend.bean.LoginBean;
import com.example.lndonesiablend.bean.VerificationCodeBean;
import com.example.lndonesiablend.http.rx.BaseErrorConsumer;
import com.example.lndonesiablend.http.rx.BaseRequestConsumer;
import com.example.lndonesiablend.utils.UIHelper;

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private LoginModel loginModel = new LoginModel();

    @Override
    public void login() {
        if (mvpView.loginVerificationInput()) {
            mvpView.showLoadingView();
            addDisposable(loginModel.login(mvpView.getUsername(), mvpView.getPassword())
                    .subscribe(new BaseRequestConsumer<LoginBean>(mvpView) {
                        @Override
                        protected void onRequestSuccess(LoginBean data) {
                            Log.d("THG", data.toString());
                            loginModel.cacheUserLocation(data);
                            mvpView.onLoginSuccess();
                        }
                    }, new BaseErrorConsumer(mvpView)));
        }
    }

    @Override
    public void lerificationCode() {
        if (mvpView.lerificationCodeVerificationInput()) {
            mvpView.showLoadingView();
            addDisposable(loginModel.verificationCodeLogin(mvpView.getUsername())
                    .subscribe(new BaseRequestConsumer<VerificationCodeBean>(mvpView) {
                        @Override
                        protected void onRequestSuccess(VerificationCodeBean data) {
                            mvpView.verificationCodeSuccess();
                            UIHelper.showToast(getContext(), "验证码发送成功！！");
                        }
                    }, new BaseErrorConsumer(mvpView)));
        }
    }


}
