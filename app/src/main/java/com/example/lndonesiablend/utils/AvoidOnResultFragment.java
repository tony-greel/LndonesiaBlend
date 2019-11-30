package com.example.lndonesiablend.utils;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SiKang on 2018/9/21.
 */
public class AvoidOnResultFragment extends Fragment {
    private Map<Integer, EasyActivityResultUtils.OnResultListener> mCallbacks = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startForResult(Intent intent, int requestCode, EasyActivityResultUtils.OnResultListener callback) {
        mCallbacks.put(requestCode, callback);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyActivityResultUtils.OnResultListener callback = mCallbacks.remove(requestCode);
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, data);
        }
    }
}
