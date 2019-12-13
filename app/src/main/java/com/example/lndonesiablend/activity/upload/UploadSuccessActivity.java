package com.example.lndonesiablend.activity.upload;


import com.example.lndonesiablend.R;
import com.example.lndonesiablend.base.BaseActivity;
import com.example.lndonesiablend.base.BasePresenter;
import com.example.lndonesiablend.load.MainLoadView;
import com.example.lndonesiablend.utils.WebViewUtils;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;

public class UploadSuccessActivity extends BaseActivity {


    @BindView(R.id.upload_success_web_view)
    WebView uploadSuccessWebView;

    private MainLoadView mianLoadView;
    private MainLoadView.Builder mianLoadViewBuilder;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        mianLoadViewBuilder = new MainLoadView.Builder(this);
        mianLoadView = mianLoadViewBuilder.setContent(getString(R.string.loading)).create();
        WebViewUtils.initWebView(this,this,uploadSuccessWebView,webChromeClient,"http://test.goferer.com/#/downApp");
    }

    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mianLoadView.cancel();
            } else {
                mianLoadView.show();
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_upload_success;
    }

}
