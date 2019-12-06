package com.example.lndonesiablend.utils;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class DeviceUtil {

    //获取WIFI名称
    public static String getWifiName(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info != null ? info.getSSID() : null;
    }

    //获取蓝牙地址
    public static String getBlueTooth(){
        BluetoothAdapter bluetoothAdapter = null;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String bluetoothAddress = bluetoothAdapter.getAddress();
        if (!ActivityJumperUtil.isEmpty(bluetoothAddress)){
            return bluetoothAddress;
        }
        return "";
    }

}
