package com.example.lndonesiablend.activity.upload;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.lndonesiablend.R;
import com.example.lndonesiablend.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IdUploadActivity extends BaseActivity {

    @BindView(R.id.id_upload_one)
    ImageView idUploadOne;
    @BindView(R.id.id_upload_two)
    ImageView idUploadTwo;
    @BindView(R.id.id_upload_but)
    Button idUploadBut;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_id_upload;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.id_upload_one, R.id.id_upload_two, R.id.id_upload_but})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_upload_one:
                uploadIdOne();
                break;
            case R.id.id_upload_two:
                break;
            case R.id.id_upload_but:
                break;
        }
    }

    private void uploadIdOne() {
        
    }
}
