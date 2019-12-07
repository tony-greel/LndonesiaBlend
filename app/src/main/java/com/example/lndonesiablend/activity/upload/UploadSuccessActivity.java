package com.example.lndonesiablend.activity.upload;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.lndonesiablend.R;
import com.example.lndonesiablend.base.BaseActivity;
import com.example.lndonesiablend.load.MainLoadView;
import com.example.lndonesiablend.utils.WebViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadSuccessActivity extends BaseActivity {


    @BindView(R.id.upload_success_web_view)
    WebView uploadSuccessWebView;

    private MainLoadView mianLoadView;
    private MainLoadView.Builder mianLoadViewBuilder;

    @Override
    protected void initView() {
        mianLoadViewBuilder = new MainLoadView.Builder(this);
        mianLoadView = mianLoadViewBuilder.setContent(getString(R.string.please_wait)).create();
        WebViewUtils.initWebView(this,this,uploadSuccessWebView,webChromeClient,"http://test.goferer.com/#/downApp");
    }

    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mianLoadView.hide();
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
