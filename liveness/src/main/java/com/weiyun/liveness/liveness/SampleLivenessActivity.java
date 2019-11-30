package com.weiyun.liveness.liveness;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.oliveapp.face.livenessdetectorsdk.livenessdetector.datatype.LivenessDetectionFrames;
import com.oliveapp.face.livenessdetectorsdk.livenessdetector.datatype.LivenessSessionState;
import com.oliveapp.face.livenessdetectorsdk.livenessdetector.datatype.OliveappFaceInfo;
import com.weiyun.liveness.R;
import com.weiyun.liveness.liveness.view_controller.LivenessDetectionMainActivity;

public class SampleLivenessActivity extends LivenessDetectionMainActivity {

    public static final String TAG = SampleLivenessActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private long startMillis, endMillis, millis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        startMillis = System.currentTimeMillis();
    }

    @Override
    protected void onStop() {
        super.onStop();
        millis += System.currentTimeMillis() - startMillis;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onInitializeSucc() {
        super.onInitializeSucc();
        super.startVerification();
    }

    @Override
    public void onInitializeFail(Throwable e) {
        super.onInitializeFail(e);
        Toast.makeText(this, R.string.server_unconnect, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ((mProgressDialog != null) && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onLivenessSuccess(final LivenessDetectionFrames livenessDetectionFrames, OliveappFaceInfo faceInfo) {
        // do nothing
    }

    @Override
    public void onLivenessSuccess(OliveappFaceInfo oliveappFaceInfo) {
        super.onLivenessSuccess(null, oliveappFaceInfo);
        endMillis = System.currentTimeMillis();
        millis += endMillis - startMillis;
        Intent intent = new Intent();
        intent.putExtra("state", "success");
        intent.putExtra("result", getLivenessDetectionPackage().verificationData);
        intent.putExtra("use_time", millis);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void onLivenessFail(int result, LivenessDetectionFrames livenessDetectionFrames) {

        super.onLivenessFail(result, livenessDetectionFrames);

        Handler handler = new Handler();
        handler.post(() -> {
            Intent intent = new Intent();
            intent.putExtra("state", "failed");
            setResult(RESULT_OK, intent);
            finish();
        });
    }

}
