package com.example.lndonesiablend.activity.upload;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lndonesiablend.LndonesiaBlendApp;
import com.example.lndonesiablend.R;
import com.example.lndonesiablend.activity.camera.UploadPhotoActivity;
import com.example.lndonesiablend.activity.submission.FaceDistinguishActivity;
import com.example.lndonesiablend.base.BaseActivity;
import com.example.lndonesiablend.bean.BaseBean;
import com.example.lndonesiablend.bean.User;
import com.example.lndonesiablend.bean.UserBean;
import com.example.lndonesiablend.http.Api;
import com.example.lndonesiablend.http.HttpRequestClient;
import com.example.lndonesiablend.load.MainLoadView;
import com.example.lndonesiablend.utils.SharePreUtil;
import com.example.lndonesiablend.utils.UIHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

public class PictureUploadActivity extends BaseActivity {

    private static final String LJJ = "PictureUploadActivity:";

    @BindView(R.id.picture_upload_img)
    ImageView pictureUploadImg;
    @BindView(R.id.picture_upload_back)
    Button pictureUploadBack;
    @BindView(R.id.picture_upload_submit)
    Button pictureUploadSubmit;

    private MainLoadView mianLoadView;
    private MainLoadView.Builder mianLoadViewBuilder;

    private File photoFile;
    private static final String Izin_kerja = "4"; //工作证

    @Override
    protected int getLayoutId() {
        return R.layout.activity_picture_upload;
    }

    @Override
    protected void initView() {
        mianLoadViewBuilder = new MainLoadView.Builder(this);
        mianLoadView = mianLoadViewBuilder.setContent(getString(R.string.loading)).create();
    }

    @OnClick({R.id.picture_upload_img, R.id.picture_upload_back, R.id.picture_upload_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.picture_upload_img:
                jurisdictionApply(Manifest.permission.CAMERA
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
            case R.id.picture_upload_submit:
                if (photoFile != null) {
                    upload();
                } else {
                    Toast.makeText(mContext, R.string.please_complete_the_information, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.picture_upload_back:
                finish();
                break;
        }
    }

    @Override
    protected void uploadPicture() {
        UploadPhotoActivity.startForResult(getActivity(), 0, UploadPhotoActivity.PhotoType.WORK_CARD,
                -1, R.drawable.select_take_photo_button, (requestCode, resultCode, data) -> {
                    if (resultCode == Activity.RESULT_OK) {
                        String imagePath = data.getStringExtra("LJJ");
                        photoFile = new File(imagePath);
                        Log.d(LJJ,"文件长度"+ String.valueOf(photoFile.length()));
                        Log.d(LJJ,"文件名"+ photoFile.getName());

                        if (photoFile.exists() && imagePath != null) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                            pictureUploadImg.setImageBitmap(bitmap);
                        }
                    }
                });
    }

    private void upload() {
        mianLoadView.show();
        //进行数据加密
        TreeMap requestUserWorkParams = buildCommonParams();
        requestUserWorkParams.put("user_id", SharePreUtil.getString(this, UserBean.userId, ""));
        requestUserWorkParams.put("fileu_type", Izin_kerja);
        String sign = signParameter(requestUserWorkParams, SharePreUtil.getString(this, UserBean.token, ""));
        requestUserWorkParams.put("sign", sign);

        //接口上传参数
        List<MultipartBody.Part> parts = new ArrayList<>();
        parts.add(toRequestBodyOfText("user_id", SharePreUtil.getString(this, UserBean.userId, "")));
        parts.add(toRequestBodyOfText("file_type", Izin_kerja));
        parts.add(toRequestBodyOfText("sign", sign));
        parts.add(toRequestBodyOfText("app_version", LndonesiaBlendApp.VERSION_NUMBER));
        parts.add(toRequestBodyOfText("version", LndonesiaBlendApp.VERSION));
        parts.add(toRequestBodyOfText("channel", LndonesiaBlendApp.CHANNEL));
        parts.add(toRequestBodyOfText("timestamp", LndonesiaBlendApp.TIMESTAMP));
        parts.add(toRequestBodyOfText("pkg_name", LndonesiaBlendApp.APPLICATION_ID));
        parts.add(toRequestBodyOfImage("file", photoFile));

        HttpRequestClient.getRetrofitHttpClient().create(Api.class)
                .uploadSingleImg(parts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LJJ, "onSubscribe：" + d.toString());
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        mianLoadView.cancel();
                        if (baseBean.getCode().equals("200") && SharePreUtil.getString(getActivity(), UserBean.mark, "").equals("")) {
                            Toast.makeText(mContext, R.string.photos_uploaded_successfully, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), IdUploadActivity.class));
                            finish();
                        } else if (baseBean.getCode().equals("200") && SharePreUtil.getString(getActivity(), UserBean.mark, "").equals("1")) {
                            Toast.makeText(mContext, R.string.photos_uploaded_successfully, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mianLoadView.cancel();
                        Toast.makeText(mContext, R.string.photo_upload_failed, Toast.LENGTH_SHORT).show();
                        Log.d(LJJ,e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LJJ, "onComplete：");
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mianLoadView.cancel();
    }
}
