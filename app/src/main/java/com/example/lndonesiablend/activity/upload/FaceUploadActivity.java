package com.example.lndonesiablend.activity.upload;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.lndonesiablend.R;
import com.example.lndonesiablend.base.BaseActivity;
import com.example.lndonesiablend.bean.ApplyLoanBean;
import com.example.lndonesiablend.bean.BaseBean;
import com.example.lndonesiablend.bean.Constant;
import com.example.lndonesiablend.bean.UserBean;
import com.example.lndonesiablend.http.Api;
import com.example.lndonesiablend.http.HttpRequestClient;
import com.example.lndonesiablend.utils.SharePreUtil;

import java.time.LocalDate;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FaceUploadActivity extends BaseActivity {


    @BindView(R.id.mIvSubmitStateIcon)
    ImageView mIvSubmitStateIcon;
    @BindView(R.id.mTvSubmitStateTitle)
    TextView mTvSubmitStateTitle;
    @BindView(R.id.mIvProgressState)
    ImageView mIvProgressState;
    @BindView(R.id.mTvApplyState)
    TextView mTvApplyState;
    @BindView(R.id.mTvApprovalState)
    TextView mTvApprovalState;
    @BindView(R.id.mTvLoanState)
    TextView mTvLoanState;
    @BindView(R.id.mCvSubmitProgress)
    CardView mCvSubmitProgress;
    @BindView(R.id.mTvStateDescribe)
    TextView mTvStateDescribe;
    @BindView(R.id.mBtDetermine)
    Button mBtDetermine;

    private String facePicture;
    private static final String LJJ = "FaceUploadActivity";
    private Boolean isJumpFromH5 = false;

    @Override
    protected void initView() {
        Intent intent = getIntent();
        facePicture = intent.getStringExtra(Constant.IMAGE_PATH);
        isJumpFromH5 = intent.getBooleanExtra(Constant.JUMP_TO_LIVING_BODY, false);
        Log.d(LJJ,facePicture);
        if (SharePreUtil.getString(mContext, UserBean.userId,"") != null
                && SharePreUtil.getString(mContext,UserBean.token,"") != null){
            String a = SharePreUtil.getString(mContext, UserBean.userId, "");
            String b = SharePreUtil.getString(mContext, UserBean.token, "");
            submitAll(this,a,b,facePicture);
        }
    }

    private void submitAll(Context context, String userId, String token, String mFilePath) {
        TreeMap treeMap = buildCommonParams();
        treeMap.put("user_id", userId);

        String proId = SharePreUtil.getString(context, UserBean.productId, "20");
        if (proId == null || proId.equals("")){
            treeMap.put("product_id", "20");
        }else {
            treeMap.put("product_id", SharePreUtil.getString(context, UserBean.productId, "20"));
        }
        treeMap.put("method", Constant.HUOTI_TYPE);
        treeMap.put("file", mFilePath);
        String sign = signParameter(treeMap, token);
        if (sign != null){
            treeMap.put("sign", sign);
        }

        HttpRequestClient.getRetrofitHttpClient().create(Api.class).submitLoan(treeMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<ApplyLoanBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean<ApplyLoanBean> applyLoanBeanBaseBean) {
                        if(applyLoanBeanBaseBean.getCode().equals("600934")){
                            mIvSubmitStateIcon.setImageResource(R.mipmap.submit_all);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LJJ,e.getMessage());
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

    @OnClick(R.id.mBtDetermine)
    public void onViewClicked() {
        finish();
    }
}
