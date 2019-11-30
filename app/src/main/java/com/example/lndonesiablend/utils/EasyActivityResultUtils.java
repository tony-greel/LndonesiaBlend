package com.example.lndonesiablend.utils;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * Created by SiKang on 2018/9/21.
 * 用Callback的方式实现 OnActivityResult
 */
public class EasyActivityResultUtils {
    private static final String TAG = "EasyActivityResult";
    private AvoidOnResultFragment mAvoidOnResultFragment;

    public EasyActivityResultUtils(FragmentActivity activity) {
        mAvoidOnResultFragment = getAvoidOnResultFragment(activity);
    }

    public EasyActivityResultUtils(Fragment fragment) {
        this(fragment.getActivity());
    }

    private AvoidOnResultFragment getAvoidOnResultFragment(FragmentActivity activity) {
        AvoidOnResultFragment avoidOnResultFragment = findAvoidOnResultFragment(activity);
        if (avoidOnResultFragment == null) {
            avoidOnResultFragment = new AvoidOnResultFragment();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(avoidOnResultFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return avoidOnResultFragment;
    }

    private AvoidOnResultFragment findAvoidOnResultFragment(FragmentActivity activity) {
        return (AvoidOnResultFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
    }


    public void startForResult(Intent intent, int requestCode, OnResultListener activityReesult) {
        mAvoidOnResultFragment.startForResult(intent, requestCode, activityReesult);
    }

    public void startForResult(Class<? extends Activity> activity, int requestCode, OnResultListener activityReesult) {
        mAvoidOnResultFragment.startForResult(new Intent(mAvoidOnResultFragment.getContext(), activity), requestCode, activityReesult);
    }

    public interface OnResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
