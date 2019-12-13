package com.example.lndonesiablend.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lndonesiablend.BuildConfig;
import com.example.lndonesiablend.R;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.web_main)
    WebView webMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        webMain.loadUrl(BuildConfig.WEB_URL);
    }
}
