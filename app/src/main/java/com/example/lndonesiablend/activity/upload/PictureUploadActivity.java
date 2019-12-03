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
import com.example.lndonesiablend.LndonesiaBlendApp;
import com.example.lndonesiablend.R;
import com.example.lndonesiablend.activity.camera.UploadPhotoActivity;
import com.example.lndonesiablend.base.BaseActivity;
import com.example.lndonesiablend.bean.BaseBean;
import com.example.lndonesiablend.bean.User;
import com.example.lndonesiablend.bean.UserBean;
import com.example.lndonesiablend.http.Api;
import com.example.lndonesiablend.http.HttpRequestClient;
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

    WebView webView;

    private File photoFile;
    private static final String Izin_kerja = "4"; //工作证

    @Override
    protected int getLayoutId() {
        return R.layout.activity_picture_upload;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.picture_upload_img, R.id.picture_upload_back, R.id.picture_upload_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.picture_upload_img:
                jurisdictionApply(Manifest.permission.CAMERA
                        ,Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ,Manifest.permission.READ_EXTERNAL_STORAGE
                        ,Manifest.permission.READ_CONTACTS);
                break;
            case R.id.picture_upload_back:
                finish();
                break;
            case R.id.picture_upload_submit:
                submission();
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
                        if (photoFile.exists() && imagePath != null) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                            pictureUploadImg.setImageBitmap(bitmap);
                        }
                    }
                });
    }

    /**
     * 权限申请
     */
    @SuppressLint("CheckResult")
    private void submission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.READ_CONTACTS).subscribe(aBoolean -> {
            if (aBoolean) {
                upload();
            } else {
                UIHelper.showToast(this, "您有尚未通过的权限");
            }
        });
    }

    private void upload() {

        //进行数据加密
        TreeMap requestUserWorkParams = buildCommonParams();
        requestUserWorkParams.put("user_id",SharePreUtil.getString(this, UserBean.userId, ""));
        requestUserWorkParams.put("fileu_type", Izin_kerja);
        String sign = signParameter(requestUserWorkParams, SharePreUtil.getString(this, UserBean.token, ""));
        requestUserWorkParams.put("sign", sign);

        //接口上传参数
        List<MultipartBody.Part> parts = new ArrayList<>();
        parts.add(toRequestBodyOfText("user_id",SharePreUtil.getString(this, UserBean.userId, "")));
        parts.add(toRequestBodyOfText("file_type", Izin_kerja));
        parts.add(toRequestBodyOfText("sign", sign));
        parts.add(toRequestBodyOfText("app_version", LndonesiaBlendApp.APP_VERSION));
        parts.add(toRequestBodyOfText("version", LndonesiaBlendApp.VERSION));
        parts.add(toRequestBodyOfText("channel", LndonesiaBlendApp.CHANNEL));
        parts.add(toRequestBodyOfText("timestamp", LndonesiaBlendApp.TIMESTAMP));
        parts.add(toRequestBodyOfText("pkg_name", LndonesiaBlendApp.APPLICATION_ID));
        parts.add(toRequestBodyOfImage("file", photoFile));

        HttpRequestClient.getRetrofitHttpClient().create(Api.class)
                .uploadSingleImage(parts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LJJ,"onSubscribe："+d.toString());
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        Log.d(LJJ,"onNext："+baseBean.getCode());

                        Log.d(LJJ,"onNexta："+SharePreUtil.getString(getActivity(), UserBean.mark,""));

                        if (baseBean.getCode().equals("200") && SharePreUtil.getString(getActivity(), UserBean.mark,"").equals("")){
                            Intent intent = new Intent(getActivity(),IdUploadActivity.class);
                            startActivity(intent);
                        }else if (baseBean.getCode().equals("200") && SharePreUtil.getString(getActivity(), UserBean.mark,"").equals("1")){
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LJJ,"onError："+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LJJ,"onComplete：");
                    }
                });

    }
}
