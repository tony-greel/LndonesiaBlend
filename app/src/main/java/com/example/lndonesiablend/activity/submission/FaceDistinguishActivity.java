package com.example.lndonesiablend.activity.submission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.example.lndonesiablend.LndonesiaBlendApp;
import com.example.lndonesiablend.R;
import com.example.lndonesiablend.activity.upload.FaceUploadActivity;
import com.example.lndonesiablend.base.BaseActivity;
import com.example.lndonesiablend.bean.Constant;
import com.example.lndonesiablend.event.AdjustEvents;
import com.example.lndonesiablend.service.UpAppService;
import com.example.lndonesiablend.service.UpContactsService;
import com.example.lndonesiablend.utils.EasyActivityResultUtils;
import com.example.lndonesiablend.utils.UIHelper;
import com.weiyun.liveness.liveness.SampleLivenessActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class FaceDistinguishActivity extends BaseActivity {

    @BindView(R.id.submission_loan_submit)
    Button submissionLoanSubmit;

    boolean isJumpFromH5 = false;
    private static final int LIVE_CHECK_CODE = 0x01;
    private static final String TAG = "FaceDistinguishActivity";
    private static Toast toast;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_face_distinguish;
    }

    @Override
    protected void initView() {
        startService(new Intent(mContext, UpAppService.class));
        startService(new Intent(mContext, UpContactsService.class));
    }


    @OnClick(R.id.submission_loan_submit)
    public void onViewClicked() {
        jurisdictionApply(Manifest.permission.CAMERA
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void uploadPicture() {
        Intent intent = new Intent(mContext, SampleLivenessActivity.class);
        new EasyActivityResultUtils(this).startForResult(intent, LIVE_CHECK_CODE, new EasyActivityResultUtils.OnResultListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (LIVE_CHECK_CODE == requestCode && data != null) {
                    Log.d(TAG, "initLiveness: " + data.getStringExtra("state"));
                    if (data.getStringExtra("state").equals("success")) {
                        //活体识别成功
                        Log.d(TAG, "initLiveness: successful");
                        //TODO 10-1-活体认证成功
//                        Adjust.trackEvent(new AdjustEvent(AdjustEvents.SUCCESSFUL_LIVING_CERTIFICATION.getCode()));
                        getLiveImage(data);
                    } else {
                        Log.d(TAG, "initLiveness: failed");
                        ToastBelowshow(FaceDistinguishActivity.this.getString(R.string.logcat_verification_failure));
                    }
                }
            }
        });
    }

    /**
     * 获取活体图片
     *
     * @param data
     */
    private void getLiveImage(Intent data) {
        //活体成功，并返回图片
        String liveImage = Base64.encodeToString(data.getByteArrayExtra("result"), Base64.NO_WRAP);
        Log.d(TAG, "getLiveImage: " + liveImage);
        if (!UIHelper.hasEmptyValue(liveImage)) {
            Intent intent = new Intent(mContext, FaceUploadActivity.class);
            intent.putExtra(Constant.IMAGE_PATH, liveImage)
                    .putExtra(Constant.JUMP_TO_LIVING_BODY, isJumpFromH5);
            startActivity(intent);
            finish();
        } else {
            UIHelper.showToast(mContext, "当前用户数据异常!");
        }
    }

    /**
     * 短时间显示Toast【居下】
     * @param msg 显示的内容-字符串
     */
    public static void ToastBelowshow(String msg) {
        if (LndonesiaBlendApp.getAppContext() != null) {
            if (toast == null) {
                toast = Toast.makeText(LndonesiaBlendApp.getAppContext(), msg, Toast.LENGTH_SHORT);
            } else {
                toast.setText(msg);
            }
            //1、setGravity方法必须放到这里，否则会出现toast始终按照第一次显示的位置进行显示（比如第一次是在底部显示，那么即使设置setGravity在中间，也不管用）
            //2、虽然默认是在底部显示，但是，因为这个工具类实现了中间显示，所以需要还原，还原方式如下：
            toast.setGravity(Gravity.BOTTOM, 0, commonPublicMethods(LndonesiaBlendApp.getAppContext(), 64));
            toast.show();
        }
    }

    public static int commonPublicMethods(Context c, float d) {
        final float scale = c.getResources().getDisplayMetrics().density;
        return (int) (d * scale + 0.5f);
    }
}
