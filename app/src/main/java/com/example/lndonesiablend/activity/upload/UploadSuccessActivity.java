package com.example.lndonesiablend.activity.upload;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.lndonesiablend.R;
import com.example.lndonesiablend.base.BaseActivity;
import com.example.lndonesiablend.utils.WebViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadSuccessActivity extends BaseActivity {


    @BindView(R.id.upload_success_web_view)
    WebView uploadSuccessWebView;

    @Override
    protected void initView() {
        WebViewUtils.initWebView(this,this,uploadSuccessWebView,null,"http://test.goferer.com/#/downApp");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_upload_success;
    }

}
