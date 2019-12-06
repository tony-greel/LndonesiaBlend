package com.example.lndonesiablend.activity.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.lndonesiablend.R;
import com.example.lndonesiablend.base.BaseActivity;
import com.example.lndonesiablend.load.CameraLoadView;
import com.example.lndonesiablend.utils.ActivityJumperUtil;
import com.example.lndonesiablend.utils.BitmapUtils;
import com.example.lndonesiablend.utils.EasyActivityResultUtils;
import com.example.lndonesiablend.utils.FileUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadPhotoActivity extends AppCompatActivity {

    @BindView(R.id.mButtonShoot)
    public ImageView mButtonShoot;
    @BindView(R.id.mButtonCancel)
    public Button mButtonCancel;
    @BindView(R.id.mCameraView)
    public CameraLoadView mCameraView;
    @BindView(R.id.mTakePhotoMask)
    public ImageView mTakePhotoMask;
    private String mType;

    public static String KTP_IMAGE = "ljj.jpg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick({R.id.mButtonShoot, R.id.mButtonCancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mButtonShoot:
                captureImage();
                break;
            case R.id.mButtonCancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    public enum PhotoType {
        POSITIVE_PHOTO,
        BACK_PHOTO,
        WORK_CARD
    }

    public static void startForResult(FragmentActivity activity, int requestCode, PhotoType type,
                                      @DrawableRes int previewMask, @DrawableRes int shootBtnImg,
                                      EasyActivityResultUtils.OnResultListener result) {
        Intent intent = new ActivityJumperUtil.Builder(activity, UploadPhotoActivity.class)
                .put("photo_type", type.name())
                .put("preview_mask", previewMask)
                .put("shoot_btn_img", shootBtnImg)
                .toIntent();
        new EasyActivityResultUtils(activity).startForResult(intent, requestCode, result);
    }

//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_upload_photo;
//    }


    private void initView() {

        mCameraView.setTargetPreviewSize(1280, 720);
        int mask = getIntent().getIntExtra("preview_mask", 0);
        int btnImg = getIntent().getIntExtra("shoot_btn_img", 0);
        if (mask > 0) {
            mTakePhotoMask.setImageResource(mask);
        }
        if (btnImg > 0) {
            mButtonShoot.setImageResource(btnImg);
        }
        mType = getIntent().getStringExtra("photo_type");
        if (TextUtils.equals(mType, PhotoType.POSITIVE_PHOTO.name())) {
            findViewById(R.id.mLlImageMark).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 拍照
     */
    private void captureImage() {
        if (TextUtils.equals(mType, PhotoType.POSITIVE_PHOTO.name())) {
            KTP_IMAGE = "positive_photo.jpg";
        } else if (TextUtils.equals(mType, PhotoType.BACK_PHOTO.name())) {
            KTP_IMAGE = "back_photo.jpg";
        } else if (TextUtils.equals(mType, PhotoType.WORK_CARD.name())) {
            KTP_IMAGE = "work_card.jpg";
        }
        mCameraView.captureImage(bitmap -> {
            File image = FileUtil.getImageFile(getApplicationContext(), KTP_IMAGE);
            if (image.exists()) {
                image.delete();
            }

            if (!image.exists()) {
                image.mkdir();
            }

            File file = BitmapUtils.saveBitmapToSDCard(bitmap, image, 100);
            //如果图片过大，则压缩
            long size = FileUtil.getFileSize(file) / 1024;
            if (FileUtil.getFileSize(file) / 1024 > 1024) {
                int quality = (int) (100 * (1024f / size));
                file = BitmapUtils.saveBitmapToSDCard(bitmap, image, quality);
            }
            Intent intent = new Intent();
            intent.putExtra("LJJ", image.getAbsolutePath());
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    public void onResume() {
        mCameraView.startPreview();
        super.onResume();
    }

    @Override
    public void onStop() {
        mCameraView.stopPreview();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mCameraView.release();
        super.onDestroy();
    }
}
