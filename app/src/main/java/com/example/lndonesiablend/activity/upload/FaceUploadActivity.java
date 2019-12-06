package com.example.lndonesiablend.activity.upload;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.lndonesiablend.R;
import com.example.lndonesiablend.activity.camera.UploadPhotoActivity;
import com.example.lndonesiablend.base.BaseActivity;
import com.example.lndonesiablend.bean.ApplyLoanBean;
import com.example.lndonesiablend.bean.BaseBean;
import com.example.lndonesiablend.bean.Constant;
import com.example.lndonesiablend.bean.UserBean;
import com.example.lndonesiablend.http.Api;
import com.example.lndonesiablend.http.HttpRequestClient;
import com.example.lndonesiablend.load.MainLoadView;
import com.example.lndonesiablend.utils.SharePreUtil;

import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FaceUploadActivity extends BaseActivity {


    @BindView(R.id.face_upload_stateIcon)
    ImageView faceUploadStateIcon;
    @BindView(R.id.face_upload_stateTitle)
    TextView faceUploadStateTitle;
    @BindView(R.id.face_upload_progress_state)
    ImageView faceUploadProgressState;
    @BindView(R.id.face_upload_apply_state)
    TextView faceUploadApplyState;
    @BindView(R.id.face_upload_spproval_state)
    TextView faceUploadSpprovalState;
    @BindView(R.id.face_upload_loan_state)
    TextView faceUploadLoanState;
    @BindView(R.id.face_upload_progress)
    CardView faceUploadProgress;
    @BindView(R.id.face_upload_state_describe)
    TextView faceUploadStateDescribe;
    @BindView(R.id.face_upload_determine)
    Button faceUploadDetermine;

    private String facePicture;
    private static final String LJJ = "";
    private Boolean isJumpFromH5 = false;

    private MainLoadView mianLoadView;
    private MainLoadView.Builder mianLoadViewBuilder;

    @Override
    protected void initView() {
        mianLoadViewBuilder = new MainLoadView.Builder(this);
        mianLoadView = mianLoadViewBuilder.setContent(getString(R.string.please_wait)).create();

        Intent intent = getIntent();
        facePicture = intent.getStringExtra(Constant.IMAGE_PATH);
        isJumpFromH5 = intent.getBooleanExtra(Constant.JUMP_TO_LIVING_BODY, false);
        Log.d(LJJ, facePicture);
        if (SharePreUtil.getString(mContext, UserBean.userId, "") != null
                && SharePreUtil.getString(mContext, UserBean.token, "") != null) {
            String a = SharePreUtil.getString(mContext, UserBean.userId, "");
            String b = SharePreUtil.getString(mContext, UserBean.token, "");
            submitAll(this, a, b, facePicture);
            mianLoadView.show();
        }
    }

    private void submitAll(Context context, String userId, String token, String mFilePath) {
        TreeMap treeMap = buildCommonParams();
        treeMap.put("user_id", userId);

        if (SharePreUtil.getString(context, UserBean.productId, "") == null || SharePreUtil.getString(context, UserBean.productId, "").equals("")) {
            treeMap.put("product_id", "20");
        } else {
            treeMap.put("product_id", SharePreUtil.getString(context, UserBean.productId, ""));
        }
        treeMap.put("method", Constant.HUOTI_TYPE);
        treeMap.put("file", mFilePath);
        String sign = signParameter(treeMap, token);
        if (sign != null) {
            treeMap.put("sign", sign);
        }
        HttpRequestClient.getRetrofitHttpClient().create(Api.class).loanSubmit(treeMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<ApplyLoanBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean<ApplyLoanBean> applyLoanBeanBaseBean) {
                        mianLoadView.cancel();
                        if (!applyLoanBeanBaseBean.getCode().equals("200")) {
                            faceUploadStateIcon.setImageResource(R.mipmap.submit_all);
                            faceUploadStateTitle.setVisibility(View.VISIBLE);
                            faceUploadStateTitle.setText(applyLoanBeanBaseBean.getMessage());
                            faceUploadDetermine.setText(R.string.lndonesia_blend_resubmission);
                        } else {
                            faceUploadStateIcon.setImageResource(R.mipmap.submit_all_success);
                            faceUploadProgress.setVisibility(View.VISIBLE);
                            faceUploadDetermine.setText(R.string.lndonesia_blend_see);
                            faceUploadStateDescribe.setVisibility(View.VISIBLE);
                            faceUploadStateTitle.setVisibility(View.VISIBLE);
                            faceUploadDetermine.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(FaceUploadActivity.this, UploadSuccessActivity.class));
                                    finish();
                                }
                            });
                        }
                        Log.d(LJJ, "onNext：" + applyLoanBeanBaseBean.getMessage() + "------------" + applyLoanBeanBaseBean.getCode());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mianLoadView.cancel();
                        Log.d(LJJ, "onError：" + e.getMessage() + "------------" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_face_upload;
    }

    @OnClick(R.id.face_upload_determine)
    public void onViewClicked() {
        finish();
    }

}
