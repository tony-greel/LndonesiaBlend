package com.example.lndonesiablend.activity.upload;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lndonesiablend.R;
import com.example.lndonesiablend.activity.camera.UploadPhotoActivity;
import com.example.lndonesiablend.activity.face.FaceDistinguishActivity;
import com.example.lndonesiablend.base.BaseActivity;
import com.example.lndonesiablend.base.BasePresenter;
import com.example.lndonesiablend.bean.BaseBean;
import com.example.lndonesiablend.bean.UserBean;
import com.example.lndonesiablend.http.Api;
import com.example.lndonesiablend.http.HttpRequestClient;
import com.example.lndonesiablend.load.MainLoadView;
import com.example.lndonesiablend.utils.SharePreUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
        mianLoadView = mianLoadViewBuilder.setContent(getString(R.string.loading)).create();
    }

    @OnClick({R.id.id_upload_one, R.id.id_upload_two, R.id.id_upload_but, R.id.id_button_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_upload_one:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(getString(R.string.warm_reminder))
                        .setMessage(getString(R.string.content))
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton(getString(R.string.determine), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadIdOne();
                            }
                        });
                builder.show();
                break;
            case R.id.id_upload_two:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle(getString(R.string.warm_reminder))
                        .setMessage(getString(R.string.content))
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton(getString(R.string.determine), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadIdTwo();
                            }
                        });
                builder1.show();
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
                    startActivity(new Intent(this, PictureUploadActivity.class));
                    finish();
                } else if (SharePreUtil.getString(getActivity(), UserBean.mark, "").equals("1")) {
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
        Observable.zip(obPositive, obNegative, (baseBean, baseBean2) -> {
            BaseBean bean = new BaseBean();
            if (baseBean.getCode().equals("200") && baseBean2.getCode().equals("200")) {
                bean.setCode("200");
            }
            return bean;
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
                            Toast.makeText(mContext, R.string.upload_id_card_succeeded, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), FaceDistinguishActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (baseBean.getCode().equals("200") && SharePreUtil.getString(getActivity(), UserBean.mark, "").equals("1")) {
                            Toast.makeText(mContext, R.string.upload_id_card_succeeded, Toast.LENGTH_SHORT).show();
                            mianLoadView.cancel();
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mianLoadView.cancel();
                        Toast.makeText(mContext, R.string.failed_to_upload_id_card, Toast.LENGTH_SHORT).show();
                        Log.d(LJJ, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void uploadIdOne() {
        uploadPositive(Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.READ_CONTACTS);

    }

    private void uploadIdTwo() {
        uploadTheOtherSide(Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.READ_CONTACTS);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
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

    private void projectileDisplay() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getString(R.string.warm_reminder))
                .setMessage(getString(R.string.please_go_to_the))
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "你点击了取消按钮~", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "你点击了确定按钮~", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.show();
    }
}
