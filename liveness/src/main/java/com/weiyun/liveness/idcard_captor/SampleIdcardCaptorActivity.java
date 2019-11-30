package com.weiyun.liveness.idcard_captor;

import android.content.Intent;
import android.os.Bundle;

import com.oliveapp.face.idcardcaptorsdk.captor.CapturedIDCardImage;
import com.weiyun.liveness.idcard_captor.view_controller.SampleIdcardCaptorMainActivity;


public class SampleIdcardCaptorActivity extends SampleIdcardCaptorMainActivity {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onFrameResult(int status) {
        super.onFrameResult(status);
    }


    @Override
    public void onIDCardCaptured(CapturedIDCardImage data) {
		super.onIDCardCaptured(data);
	
        Intent i = new Intent();
        i.putExtra("image", data.idcardImageData);
        setResult(RESULT_OK, i);
        finish();
    }
}
