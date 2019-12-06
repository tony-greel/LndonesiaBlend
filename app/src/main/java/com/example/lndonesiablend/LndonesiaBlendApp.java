package com.example.lndonesiablend;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;
import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.LogLevel;
import com.example.lndonesiablend.broadcast.NetStateReceiver;
import com.example.lndonesiablend.event.AdjustEvents;
import com.example.lndonesiablend.helper.DataBaseHelper;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.squareup.leakcanary.LeakCanary;

public class LndonesiaBlendApp extends Application{

    private static Context mContext;
    public static String VERSION_NUMBER;
    public static String VERSION;
    public static String CHANNEL;
    public static String TIMESTAMP;
    public static String APPLICATION_ID;

    public static Context getAppContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
        initLeakCanary();
        initAdjust();
        broadcast();
    }

    private void initData() {
        VERSION_NUMBER = BuildConfig.VERSION_NAME;
        VERSION = BuildConfig.VERSION;
        CHANNEL = BuildConfig.CHANNEL;
        TIMESTAMP = System.currentTimeMillis() + "";
        APPLICATION_ID = BuildConfig.APPLICATION_ID;
        mContext = getApplicationContext();
        DataBaseHelper.init(mContext);
    }

    private void broadcast() {
        //动态注册网络变化广播
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            NetStateReceiver netBroadcastReceiver = new NetStateReceiver();
            //注册广播接收
            registerReceiver(netBroadcastReceiver, filter);
        }
        /*开启网络广播监听*/
        NetStateReceiver.registerNetworkStateReceiver(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        NetStateReceiver.unRegisterNetworkStateReceiver(this);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    private void initAdjust() {
        String[] signs = BuildConfig.ADJUST_APP_SECRET.split(", ");
        Integer secretId = Integer.parseInt(signs[0]);
        Integer info1 = Integer.parseInt(signs[1]);
        Integer info2 = Integer.parseInt(signs[2]);
        Integer info3 = Integer.parseInt(signs[3]);
        Integer info4 = Integer.parseInt(signs[4]);

        String token = BuildConfig.ADJUST_TOKEN;
        String environment = AdjustConfig.ENVIRONMENT_PRODUCTION;
        AdjustConfig config = new AdjustConfig(getAppContext(), token, environment);
        config.setAppSecret(secretId, info1, info2, info3, info4);
        config.setSendInBackground(true);
        config.setLogLevel(LogLevel.VERBOSE);

        Adjust.onCreate(config);
        registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());

        new Thread(() -> getUpload()).start();
    }

    private void getUpload() {
        try {
            AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getAppContext());
            String id = adInfo.getId();
            Adjust.trackEvent(new AdjustEvent(AdjustEvents.APP_INSTALL.getCode()));
            AdjustEvent adjustEvent = new AdjustEvent(AdjustEvents.GAID.getCode());
            adjustEvent.addCallbackParameter("key", "gaid");
            adjustEvent.addCallbackParameter("value", id);
            adjustEvent.addCallbackParameter("source", BuildConfig.APP_NAME);
            Adjust.trackEvent(adjustEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static final class AdjustLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }
    }
}
