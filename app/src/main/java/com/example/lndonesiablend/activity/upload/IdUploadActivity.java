package com.example.lndonesiablend.activity.upload;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lndonesiablend.R;
import com.example.lndonesiablend.activity.camera.UploadPhotoActivity;
import com.example.lndonesiablend.activity.submission.FaceDistinguishActivity;
import com.example.lndonesiablend.base.BaseActivity;
import com.example.lndonesiablend.bean.BaseBean;
import com.example.lndonesiablend.bean.UserBean;
import com.example.lndonesiablend.http.Api;
import com.example.lndonesiablend.http.HttpRequestClient;
import com.example.lndonesiablend.load.MainLoadView;
import com.example.lndonesiablend.utils.SharePreUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

public class IdUploadActivity extends BaseActivity {
    private static final String LJJ = "IdUploadActivity:";

    @BindView(R.id.id_upload_one)
    ImageView idUploadOne;
    @BindView(R.id.id_upload_two)
    ImageView idUploadTwo;
    @BindView(R.id.id_upload_but)
    Button idUploadBut;
    @BindView(R.id.id_button_return)
    Button idButtonReturn;

    private File mPositive = null;
    private File mTheOtherSide = null;
    private MainLoadView mianLoadView;
    private MainLoadView.Builder mianLoadViewBuilder;

    private static final String POSITIVE = "1"; //身份证正面
    private static final String THE_OTHER_SIDE = "2"; //身份证反面

    @Override
    protected int getLayoutId() {
        return R.layout.activity_id_upload;
    }

    @Override
    protected void initView() {
        mianLoadViewBuilder = new MainLoadView.Builder(this);
        mianLoadView = mianLoadViewBuilder.setContent(getString(R.string.please_wait)).create();
    }

    @OnClick({R.id.id_upload_one, R.id.id_upload_two, R.id.id_upload_but, R.id.id_button_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_upload_one:
                uploadIdOne(UploadPhotoActivity.PhotoType.POSITIVE_PHOTO);
                break;
            case R.id.id_upload_two:
                uploadIdTwo(UploadPhotoActivity.PhotoType.BACK_PHOTO);
                break;
            case R.id.id_upload_but:
                if (mPositive != null && mTheOtherSide != null) {
                    submission(mPositive, POSITIVE, mTheOtherSide, THE_OTHER_SIDE);
                } else {
                    Toast.makeText(mContext, R.string.please_complete_the_information, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.id_button_return:
                if (SharePreUtil.getString(getActivity(), UserBean.mark, "").equals("")) {
                    startActivity(new Intent(this,PictureUploadActivity.class));
                    finish();
                }
                break;

        }
    }

    @SuppressLint("CheckResult")
    private void submission(File mPositiveFile, String positiveCard, File mBackFile, String backCard) {
        mianLoadView.show();
        List<MultipartBody.Part> positiveParts = getParts(mPositiveFile, positiveCard);
        List<MultipartBody.Part> negative = getParts(mBackFile, backCard);

        Observable<BaseBean> obPositive = HttpRequestClient.getRetrofitHttpClient().create(Api.class).uploadSingleImg(positiveParts);
        Observable<BaseBean> obNegative = HttpRequestClient.getRetrofitHttpClient().create(Api.class).uploadSingleImg(negative);
        Observable.zip(obPositive, obNegative, new BiFunction<BaseBean, BaseBean, BaseBean>() {

            @Override
            public BaseBean apply(BaseBean baseBean, BaseBean baseBean2) throws Exception {
                BaseBean bean = new BaseBean();
                if (baseBean.getCode().equals("200") && baseBean2.getCode().equals("200")) {
                    bean.setCode("200");
                }
                return bean;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        Log.d(LJJ, baseBean.getCode());
                        if (baseBean.getCode().equals("200") && SharePreUtil.getString(getActivity(), UserBean.mark, "").equals("")) {
                            mianLoadView.cancel();
                            Toast.makeText(mContext, "上传身份证成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), FaceDistinguishActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (baseBean.getCode().equals("200") && SharePreUtil.getString(getActivity(), UserBean.mark, "").equals("1")) {
                            Toast.makeText(mContext, "上传身份证成功", Toast.LENGTH_SHORT).show();
                            mianLoadView.cancel();
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mianLoadView.cancel();
                        Toast.makeText(mContext, "上传身份证失败", Toast.LENGTH_SHORT).show();
                        Log.d(LJJ, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void uploadIdOne(UploadPhotoActivity.PhotoType positivePhoto) {
        uploadPositive(Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.READ_CONTACTS);

    }

    private void uploadIdTwo(UploadPhotoActivity.PhotoType backPhoto) {
        uploadTheOtherSide(Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.READ_CONTACTS);
    }

    @Override
    protected void uploadPositive(UploadPhotoActivity.PhotoType positivePhoto) {
        UploadPhotoActivity.startForResult(getActivity(), 0, positivePhoto, R.mipmap.id_picture, R.mipmap.take_photo_normal, (requestCode, resultCode, data) -> {
            //拍照成功
            if (resultCode == Activity.RESULT_OK) {
                String imagePath = data.getStringExtra("LJJ");
                File file = new File(imagePath);
                if (file.exists() && imagePath != null) {

                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    if (UploadPhotoActivity.PhotoType.POSITIVE_PHOTO.equals(positivePhoto)) {
                        mPositive = file;
                        idUploadOne.setImageBitmap(bitmap);
                    } else if (UploadPhotoActivity.PhotoType.BACK_PHOTO.equals(positivePhoto)) {
                        mTheOtherSide = file;
                        idUploadOne.setImageBitmap(bitmap);
                    }
                }
            }
        });
    }

    @Override
    protected void uploadTheOtherSide(UploadPhotoActivity.PhotoType photoType) {
        UploadPhotoActivity.startForResult(getActivity(), 0, photoType, R.mipmap.id_picture, R.mipmap.take_photo_normal, (requestCode, resultCode, data) -> {
            //拍照成功
            if (resultCode == Activity.RESULT_OK) {
                String imagePath = data.getStringExtra("LJJ");
                File file = new File(imagePath);

                if (file.exists() && imagePath != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    if (UploadPhotoActivity.PhotoType.POSITIVE_PHOTO.equals(photoType)) {
                        mPositive = file;
                        idUploadTwo.setImageBitmap(bitmap);
                    } else if (UploadPhotoActivity.PhotoType.BACK_PHOTO.equals(photoType)) {
                        mTheOtherSide = file;
                        idUploadTwo.setImageBitmap(bitmap);
                    }
                }
            }
        });
    }
}
