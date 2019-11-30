package com.example.lndonesiablend.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class DeviceInfoFactoryUtil {
    public static final String DEVICE_UUID_FILE_NAME = "uuid_file";
    public static final String DEVICE_IMEI_FILE_NAME = "imei_file";
    private static final String PREFS_FILE = "device_info";
    private static final String PREFS_DEVICE_ID = "device_id";
    private static final String PREFS_DEVICE_IMEI = "device_imei";
    private TelephonyManager mTelephonyManager;
    private final SharedPreferences mSharedPreferences;
    private static DeviceInfoFactoryUtil deviceUuidFactory;
    private static UUID uuid;
    private static String imei;
    private Context mContext;


    public static DeviceInfoFactoryUtil getInstance(Context context) {
        if (deviceUuidFactory == null) {
            synchronized (DeviceInfoFactoryUtil.class) {
                if (deviceUuidFactory == null) {
                    deviceUuidFactory = new DeviceInfoFactoryUtil(context.getApplicationContext());
                }
            }
        }
        return deviceUuidFactory;
    }

    private DeviceInfoFactoryUtil(Context context) {
        mContext = context;
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mSharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        loadUuidAndImei();  //读取UUID、IMEI
    }

    /**
     * 读取UUID、IMEI
     */
    private void loadUuidAndImei() {
        String id = mSharedPreferences.getString(PREFS_DEVICE_ID, null);
        String imeiStr = mSharedPreferences.getString(PREFS_DEVICE_IMEI, null);

        if (TextUtils.isEmpty(id)) {
            if (checkPermission(mContext, permission.WRITE_EXTERNAL_STORAGE)) {
                id = recoverFromSD(DEVICE_UUID_FILE_NAME);
            }
        }

        if (TextUtils.isEmpty(imeiStr)) {
            if (checkPermission(mContext, permission.WRITE_EXTERNAL_STORAGE)) {
                imeiStr = recoverFromSD(DEVICE_IMEI_FILE_NAME);
            }
        }

        if (!TextUtils.isEmpty(imeiStr)) {
            imei = imeiStr;
            mSharedPreferences.edit().putString(PREFS_DEVICE_IMEI, imei).commit();
            if (checkPermission(mContext, permission.WRITE_EXTERNAL_STORAGE)) {
                saveToSD(imei, DEVICE_IMEI_FILE_NAME);
            }
        }

        if (id != null) {
            // Use the ids previously computed and stored in the prefs file
            uuid = UUID.fromString(id);
            mSharedPreferences.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();
            if (checkPermission(mContext, permission.WRITE_EXTERNAL_STORAGE)) {
                saveToSD(uuid.toString(), DEVICE_UUID_FILE_NAME);
            }
        } else {
            final String androidId = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
            String tmDevice, tmSerial;
            tmDevice = "";
            tmSerial = "";

            try {
                if (checkPermission(mContext, permission.READ_PHONE_STATE)) {
                    TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                    tmDevice = tm.getDeviceId();
                    tmSerial = tm.getSimSerialNumber();
                }
            } catch (SecurityException ex) {
                ex.printStackTrace();
            }

            if (TextUtils.isEmpty(tmDevice) || TextUtils.isEmpty(tmSerial) || tmDevice.equals("000000000000000")) {
                if (!TextUtils.isEmpty(androidId) && !"9774d56d682e549c".equals(androidId) && Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes());
                } else {
                    uuid = UUID.randomUUID();
                }

            } else {
                uuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            }
            if (!TextUtils.isEmpty(tmDevice)) {
                imei = tmDevice;
                mSharedPreferences.edit().putString(PREFS_DEVICE_IMEI, imei).commit();
                if (checkPermission(mContext, permission.WRITE_EXTERNAL_STORAGE)) {
                    saveToSD(imei, DEVICE_IMEI_FILE_NAME);
                }

            }

            mSharedPreferences.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();
            if (checkPermission(mContext, permission.WRITE_EXTERNAL_STORAGE)) {
                saveToSD(uuid.toString(), DEVICE_UUID_FILE_NAME);
            }
        }
    }

    /**
     * 获取设备的UUID
     *
     * @return
     */
    public UUID getDeviceUuid() {
        return uuid;
    }

    /**
     * 获取设备的Imei
     *
     * @return
     */
    public String getDeviceImei() {
        return imei;
    }

    /**
     * 获取设备的Sim
     *
     * @return
     */
    public String getSim() {
        if (mTelephonyManager != null) {
            if (checkPermission(mContext, permission.READ_PHONE_STATE)) {
                return mTelephonyManager.getSimSerialNumber();
            }
        }
        return "";
    }

    /**
     * 获取设备品牌
     *
     * @return
     */
    public String getBrands() {
        return Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public String getMobil() {
        return Build.MODEL;
    }

    /**
     * CPU型号
     *
     * @return
     */
    public String getCpuModel() {
        return Build.CPU_ABI;
    }

    /**
     * 获取IMEI
     */
    public String getIMEI() {
        if (mTelephonyManager != null) {
            if (checkPermission(mContext, permission.READ_PHONE_STATE)) {
                return mTelephonyManager.getDeviceId();
            }
        }
        return "";
    }


    /**
     * 获取当前手机系统版本号
     */
    public String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 屏幕分辨率
     * @return
     */
    public String getResolution() {
        float xdpi = mContext.getResources().getDisplayMetrics().xdpi;
        float ydpi = mContext.getResources().getDisplayMetrics().ydpi;
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int height = mContext.getResources().getDisplayMetrics().heightPixels;

        // 这样可以计算屏幕的物理尺寸
        float width2 = (width / xdpi) * (width / xdpi);
        float height2 = (height / ydpi) * (width / xdpi);

        return ((float) Math.sqrt(width2 + height2))+"";
    }

    /**
     * CPU核数
     *
     * @return
     */
    public String getCpuCores() {
        int count = getNumberOfCPUCores();
        return count + "";
    }

    public static int getNumberOfCPUCores() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            return 1;
        }
        int cores;
        try {
            cores = new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).length;
        } catch (SecurityException e) {
            cores = 0;
        }
        return cores;
    }

    private static final FileFilter CPU_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getName();
            if (path.startsWith("cpu")) {
                for (int i = 3; i < path.length(); i++) {
                    if (path.charAt(i) < '0' || path.charAt(i) > '9') {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    };

    public static void saveToSD(String uuid, String fileName) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File targetFile = new File(dirPath, fileName);
        if (targetFile != null) {
            if (!targetFile.exists()) {
                OutputStreamWriter osw;
                try {
                    osw = new OutputStreamWriter(new FileOutputStream(targetFile), "utf-8");
                    try {
                        osw.write(uuid);
                        osw.flush();
                        osw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String recoverFromSD(String fileName) {
        try {
            String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File dir = new File(dirPath);
            File uuidFile = new File(dir, fileName);
            if (!dir.exists() || !uuidFile.exists()) {
                return null;
            }
            FileReader fileReader = new FileReader(uuidFile);
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[100];
            int readCount;
            while ((readCount = fileReader.read(buffer)) > 0) {
                sb.append(buffer, 0, readCount);
            }
            fileReader.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkPermission(Context context, permission permName) {
        int perm = context.checkCallingOrSelfPermission("android.permission." + permName.toString());
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    private enum permission {
        READ_PHONE_STATE,
        WRITE_EXTERNAL_STORAGE
    }
}
