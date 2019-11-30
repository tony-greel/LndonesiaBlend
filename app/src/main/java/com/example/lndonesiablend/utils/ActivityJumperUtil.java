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
    private static String GP_APP_DETAIL_URL = "https://play.google.com/store/apps/details?id=";
    private Intent intent;
    private static int REQUEST_PERMISSION_CODE = 0x000AF00;

    /**
     * 发起跳转的Context
     */
    private Context context;

    private ActivityJumperUtil(Context from, Intent intent) {
        this.intent = intent;
        this.context = from;
    }

    /**
     * 打开Activity
     */
    public void start() {
        if (context == null) {
            return;
        }
        context.startActivity(intent);
    }

    /**
     * 在新的Task种打开Activity
     */
    public void startWithNewTask() {
        if (context == null)
            return;
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public Intent getIntent() {
        return intent;
    }

    /**
     * 启动拨号键盘
     */
    public static void startCallTel(Context context, String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + tel);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 发短信界面
     */
    public static void startSendTo(Context context, String tel, String smsBody) {
        Uri sms_uri = Uri.parse("smsto:13517596490");//设置号码
        Intent intent = new Intent(Intent.ACTION_SENDTO, sms_uri);//调用发短信Action
        intent.putExtra("sms_body", smsBody);
        context.startActivity(intent);
    }


    /**
     * 短信列表界面
     */
    public static void startSmsList(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("vnd.android-dir/mms-sms");
        context.startActivity(intent);
    }

    /**
     * 跳转应用设置中心
     *
     * @param activity
     */
    public static void toPermissionSetting(Activity activity, String... permissions) {
        try {
            //判断是否为小米系统
            if (TextUtils.equals(BrandUtils.getSystemInfo().getOs(), BrandUtils.SYS_MIUI)) {
                Intent miuiIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                miuiIntent.putExtra("extra_pkgname", activity.getPackageName());
                //检测是否有能接受该Intent的Activity存在
                List<ResolveInfo> resolveInfos = activity.getPackageManager().queryIntentActivities(miuiIntent, PackageManager.MATCH_DEFAULT_ONLY);
                if (resolveInfos.size() > 0) {
                    activity.startActivityForResult(miuiIntent, REQUEST_PERMISSION_CODE);
                    return;
                }
            }
            //如果不是小米系统 则打开Android系统的应用设置页
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            intent.putExtra("go settion permission", permissions);
            activity.startActivityForResult(intent, REQUEST_PERMISSION_CODE);
        } catch (Throwable e) {
            LogUtils.e("to setting permission activity error:");
        }
    }


    /**
     * 打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gps && !network) {
            Intent GPSIntent = new Intent();
            GPSIntent.setClassName("com.android.settings",
                    "com.android.settings.widget.SettingsAppWidgetProvider");
            GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
            GPSIntent.setData(Uri.parse("custom:3"));
            try {
                PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 去 GooglePlay下载页
     */
    public static void startGooglePlay(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GP_APP_DETAIL_URL));
            List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
            if (resolveInfos == null || resolveInfos.size() <= 0) {
                DialogFactory.showMessageDialog(context, context.getString(R.string.show_download_on_google_player));
            } else {
                for (ResolveInfo resolveInfo : resolveInfos) {
                    if (resolveInfo.activityInfo.packageName.toLowerCase().contains("com.android.vending")
                            || resolveInfo.activityInfo.name.toLowerCase().contains("com.android.vending")
                            || resolveInfo.activityInfo.processName.toLowerCase().contains("com.android.vending")) {
                        intent.setPackage(resolveInfo.activityInfo.packageName);
                        break;
                    }
                }
            }
            context.startActivity(intent);
        } catch (Exception e) {
            DialogFactory.showMessageDialog(context, context.getString(R.string.show_download_on_google_player));
        }
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

