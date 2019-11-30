package com.example.lndonesiablend.utils;

import android.util.Log;

public class LogUtils {

    private LogUtils()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 是否需要打印bug，可以在application的onCreate函数里面初始化
     */
    public static boolean isDebug = true;
    private static final String TAG = "JRWD_TAG";
    private static int LOG_MAXLENGTH = 2000;


    public static void i(String msg)
    {
        if (isDebug){
            logI(msg);
        }

    }

    public static void d(String msg)
    {
        if (isDebug){
            logD(msg);
        }
    }

    public static void e(String msg)
    {
        if (isDebug){
            logE(msg);
        }
    }

    public static void v(String msg)
    {
        if (isDebug){
            logV(msg);
        }
    }

    public static void logI(String msg){
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAXLENGTH;
        for (int i = 0; i < 100; i++) {
            if (strLength > end) {
                Log.i(TAG, msg.substring(start, end));
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                Log.i(TAG, msg.substring(start, strLength));
                break;
            }
        }
    }

    public static void logD(String msg){
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAXLENGTH;
        for (int i = 0; i < 100; i++) {
            if (strLength > end) {
                Log.d(TAG, msg.substring(start, end));
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                Log.d(TAG, msg.substring(start, strLength));
                break;
            }
        }
    }

    public static void logV(String msg){
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAXLENGTH;
        for (int i = 0; i < 100; i++) {
            if (strLength > end) {
                Log.v(TAG, msg.substring(start, end));
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                Log.v(TAG, msg.substring(start, strLength));
                break;
            }
        }
    }

    public static void logE(String msg){
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAXLENGTH;
        for (int i = 0; i < 100; i++) {
            if (strLength > end) {
                Log.e(TAG, msg.substring(start, end));
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                Log.e(TAG, msg.substring(start, strLength));
                break;
            }
        }
    }

}

