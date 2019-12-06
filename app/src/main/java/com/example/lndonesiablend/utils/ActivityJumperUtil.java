package com.example.lndonesiablend.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;

import com.example.lndonesiablend.R;

import java.io.Serializable;
import java.util.List;


/**
 * Created by SiKang on 2018/9/16.
 * Intent的创建和使用
 */
public class ActivityJumperUtil {
    private Intent intent;
    /**
     * 发起跳转的Context
     */
    private Context context;

    public static boolean isEmpty(String str){
        return TextUtils.isEmpty(str);
    }


    private ActivityJumperUtil(Context from, Intent intent) {
        this.intent = intent;
        this.context = from;
    }

    public Intent getIntent() {
        return intent;
    }

    public static class Builder {
        private Intent intent;
        private Context fromContext;

        public Builder() {
            intent = new Intent();
        }

        public <C extends Activity> Builder(Context from, Class<C> target) {
            this.fromContext = from;
            intent = new Intent(fromContext, target);
        }

        public Builder(Context from, String action) {
            this.fromContext = from;
            intent = new Intent(action);
        }

        public Builder put(String key, String value) {
            intent.putExtra(key, value);
            return this;
        }

        public Builder put(String key, int value) {
            intent.putExtra(key, value);
            return this;
        }

        public Builder put(String key, float value) {
            intent.putExtra(key, value);
            return this;
        }

        public Builder put(String key, double value) {
            intent.putExtra(key, value);
            return this;
        }

        public Builder put(String key, boolean value) {
            intent.putExtra(key, value);
            return this;
        }

        public Builder put(String key, Serializable obj) {
            intent.putExtra(key, obj);
            return this;
        }

        public Builder put(String key, Parcelable obj) {
            intent.putExtra(key, obj);
            return this;
        }

        public ActivityJumperUtil build() {
            return new ActivityJumperUtil(fromContext, intent);
        }

        public Intent toIntent() {
            return intent;
        }

        public Bundle toBundle() {
            return intent.getExtras();
        }
    }

}

