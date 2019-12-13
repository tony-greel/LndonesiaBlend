package com.example.lndonesiablend.activity;
import android.Manifest;
import android.annotation.SuppressLint;

import android.os.Build;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.lndonesiablend.BuildConfig;
import com.example.lndonesiablend.R;
import com.example.lndonesiablend.base.BaseActivity;
import com.example.lndonesiablend.base.BasePresenter;
import com.example.lndonesiablend.bean.Constant;
import com.example.lndonesiablend.bean.User;
import com.example.lndonesiablend.bean.UserBean;
import com.example.lndonesiablend.broadcast.NetUtils;
import com.example.lndonesiablend.load.AuthorizationLoadView;
import com.example.lndonesiablend.load.MainLoadView;
import com.example.lndonesiablend.load.PrivacyLinkLoadView;
import com.example.lndonesiablend.utils.PermissionUtils;
import com.example.lndonesiablend.utils.SharePreUtil;
import com.example.lndonesiablend.utils.UIHelper;
import com.example.lndonesiablend.utils.WebViewUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.lndonesiablend.broadcast.NetStateReceiver.isNetworkAvailable;

public class MainActivity extends BaseActivity {

    @BindView(R.id.img_main)
    ImageView imgMain;
    @BindView(R.id.tv_main)
    TextView tvMain;
    @BindView(R.id.web_main)
    WebView webMain;

    private MainLoadView mianLoadView;
    private MainLoadView.Builder mianLoadViewBuilder;
    private AuthorizationLoadView authorizationLoadView;
    private long time = 0;
    private JsonObject jsonObject;

    private static final String LJJ = "MainActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 初始化视图
     */
    @Override
    protected void initView() {
        mianLoadViewBuilder = new MainLoadView.Builder(this);
        mianLoadView = mianLoadViewBuilder.setContent(getString(R.string.loading)).create();
        initMonitor();
    }

    private void initMonitor() {
        String IS_FIRST_REQUEST_PERMISSION_PASS = SharePreUtil.getString(mContext, "IS_FIRST_REQUEST_PERMISSION_PASS", "");
        if (IS_FIRST_REQUEST_PERMISSION_PASS.equals("")) {
            PrivacyjurisdictionPopup();
        } else {
            if (isNetworkAvailable()) {
                imgMain.setVisibility(View.GONE);
                tvMain.setVisibility(View.GONE);
                webMain.setVisibility(View.VISIBLE);
                WebViewUtils.initWebView(this, this, webMain, webChromeClient, BuildConfig.WEB_URL);
            } else {
                imgMain.setVisibility(View.VISIBLE);
                tvMain.setVisibility(View.VISIBLE);
                webMain.setVisibility(View.GONE);
            }
        }
    }

    private void PrivacyjurisdictionPopup() {
        PrivacyLinkLoadView mDialog = new PrivacyLinkLoadView(this);
        mDialog.show();
        mDialog.setCancelable(false);
        mDialog.setOnConfirmClickListener(checked -> {
            if (checked) {
                mDialog.cancel();
                authorizationLoadView = new AuthorizationLoadView(MainActivity.this.getActivity());
                authorizationLoadView.setCancelable(false);
                authorizationLoadView.show();
                authorizationLoadView.setOnCloseClickListener(isClick -> {
                    if (isClick) {
                        jurisdictionApply();
                    }
                });
            } else {
                UIHelper.showToast(this, R.string.lndonesia_blend_privileges);
            }
        });
    }

    /**
     * 权限申请
     */
    @SuppressLint("CheckResult")
    private void jurisdictionApply() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(permission -> {
                    if (permission.granted) {
                        SharePreUtil.putString(mContext, "IS_FIRST_REQUEST_PERMISSION_PASS", Constant.IS_FIRST_REQUEST_PERMISSION_PASS);
                        imgMain.setVisibility(View.GONE);
                        tvMain.setVisibility(View.GONE);
                        webMain.setVisibility(View.VISIBLE);
                        authorizationLoadView.dismiss();
                        mianLoadView.hide();
                        WebViewUtils.initWebView(MainActivity.this, MainActivity.this, webMain, webChromeClient, BuildConfig.WEB_URL);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        UIHelper.showToast(MainActivity.this, "您有尚未通过的权限");
                    }else {
                        PermissionUtils.showNormalDialog(this);
                    }
                });
    }


    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mianLoadView.hide();
            } else {
                mianLoadView.show();
            }
            String hmtl = "window.localStorage.getItem('Pribadi');";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                view.evaluateJavascript(hmtl, value -> {
                    if (value != null){
                        if (!"null".equals(value)) {
                            String str1 = value.substring(1, value.length() - 1)
                                    .replaceAll("\\\\", "");
                            jsonObject = new JsonParser().parse(str1).getAsJsonObject();
                            if (jsonObject.get("vcode").isJsonNull()) {
                                jsonObject.addProperty("vcode", "null");
                            }
                            User user = new Gson().fromJson(jsonObject.toString(), User.class);
                            Log.d(LJJ,"user-----------------------------" + user);
                            SharePreUtil.putString(mContext, UserBean.userId,user.getUserId());
                            SharePreUtil.putString(mContext, UserBean.selfMobile, user.getSelfMobile());
                            SharePreUtil.putString(mContext, UserBean.token, user.getToken());
                            SharePreUtil.putString(mContext, UserBean.userName, user.getUserName());
                            SharePreUtil.putString(mContext, UserBean.vcode, user.getVcode());
                            SharePreUtil.putString(mContext, UserBean.phonepre, user.getPhonepre());
                            SharePreUtil.putString(mContext, UserBean.phone, user.getPhone());
                            SharePreUtil.putString(mContext, UserBean.sign, user.getSign());
                            SharePreUtil.putString(mContext, UserBean.productId, user.getProductId());
                            SharePreUtil.putString(mContext, UserBean.mark, user.getMark());
                        }
                    }
                });
            }
        }
    };

    @OnClick({R.id.img_main, R.id.tv_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_main:
            case R.id.tv_main:
                WebViewUtils.initWebView(this,this,webMain,webChromeClient,BuildConfig.WEB_URL);
                break;
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    /**
     * 连接网络时的状态
     */
    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
        imgMain.setVisibility(View.GONE);
        tvMain.setVisibility(View.GONE);
        webMain.setVisibility(View.VISIBLE);
        WebViewUtils.initWebView(this,this,webMain,webChromeClient,BuildConfig.WEB_URL);
    }

    /**
     * 未连接网络时的状态
     */
    @Override
    protected void onNetworkDisConnected() {
        imgMain.setVisibility(View.VISIBLE);
        tvMain.setVisibility(View.VISIBLE);
        webMain.setVisibility(View.GONE);
    }

    /**
     * 在返回时
     */
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time < 1000) {
            webMain.goBack();
            webMain.getSettings().setBlockNetworkImage(true);
            super.onBackPressed();
        } else {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            time = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webMain.canGoBack()) {
            webMain.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
