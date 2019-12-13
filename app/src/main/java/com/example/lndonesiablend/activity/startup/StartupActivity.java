package com.example.lndonesiablend.activity.startup;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import com.example.lndonesiablend.R;
import com.example.lndonesiablend.activity.MainActivity;
import com.example.lndonesiablend.base.BaseActivity;
import com.example.lndonesiablend.base.BasePresenter;
import com.example.lndonesiablend.bean.Constant;
import com.example.lndonesiablend.service.UpInstallReferrerInfoService;
import com.example.lndonesiablend.utils.SharePreUtil;

public class StartupActivity extends BaseActivity {

    private static final int times = 3000;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_startup;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        Boolean isFirstInstallation = SharePreUtil.getBoolean(this, Constant.IS_FIRST_INSTALLATION, true);
        if (isFirstInstallation) {
            Log.d("LJJ", "上传安装蓝牙wifi信息");
            SharePreUtil.putBoolean(mContext, Constant.IS_FIRST_INSTALLATION, false);
            startService(new Intent(mContext, UpInstallReferrerInfoService.class));
        }
        initMonitor();
    }

    private void initMonitor() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartupActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, times);
    }
}
