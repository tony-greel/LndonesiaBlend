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


    public static String getDeviceModel(){
        return Build.MODEL;
    }


    public static String getDeviceManufacturer(){
        return Build.MANUFACTURER;
    }


    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

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
        if (!StringUtils.isEmpty(bluetoothAddress)){
            return bluetoothAddress;
        }
        return "";
    }


    public static String getDeviceId(Context context){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) ==
                PackageManager.PERMISSION_GRANTED){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if(null != tm){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    return tm.getImei();
                }
                return tm.getDeviceId();
            }
        }
        return "";
    }


    private static String getIMSI(Context context){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) ==
                PackageManager.PERMISSION_GRANTED){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if(null != tm){
                return tm.getSubscriberId();
            }
        }
        return "";
    }


    private static String getSimNumber(Context context){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) ==
                PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != tm) {
                return tm.getLine1Number();
            }
        }
        return "";
    }


    private static String getSimSerialNumber(Context context){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) ==
                PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != tm) {
                return tm.getSimSerialNumber();
            }
        }
        return "";
    }


    @SuppressWarnings("deprecation")
    public static String getAndroidId(Context context){
        return Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
    }


    public static String getUniqueId(Context context){
        String id = null;
        try {
            id = getDeviceId(context);
        } catch (Exception e) {

        } catch (Error e) {

        }
        if(StringUtils.isEmpty(id)){
            try {
                id = getIMSI(context);
            } catch (Exception e) {

            } catch (Error e) {

            }
            if(StringUtils.isEmpty(id)){
                try {
                    id = getSimSerialNumber(context);
                } catch (Exception e) {

                } catch (Error e) {

                }
                if(StringUtils.isEmpty(id)){
                    try {
                        id = getSimNumber(context);
                    } catch (Exception e) {

                    } catch (Error e) {

                    }
                    if(!StringUtils.isEmpty(id)){
                        return id;
                    }
                }else{
                    return id;
                }
            }else{
                return id;
            }
        }else{
            return id;
        }
        return getAndroidId(context);
    }

    /**
     * 获取用户ip
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo    = wifiManager.getConnectionInfo();
                String ipAddress   = intIP2StringIP(wifiInfo.getIpAddress());
                return ipAddress;
            }
        } else {

        }
        return null;
    }


    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    public static boolean isRoot() {
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";
        if (new File(binPath).exists() && isCanExecute(binPath)) {
            return true;
        }
        if (new File(xBinPath).exists() && isCanExecute(xBinPath)) {
            return true;
        }
        return false;
    }


    private static boolean isCanExecute(String filePath) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("ls -l " + filePath);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String str = in.readLine();
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }


    private static String[] known_pipes={ "/dev/socket/qemud", "/dev/qemu_pipe"};

    private static String[] known_qemu_drivers = {
            "goldfish"
    };

    private static String[] known_files = {
            "/system/lib/libc_malloc_debug_qemu.so",
            "/sys/qemu_trace",
            "/system/bin/qemu-props"
    };

    private static String[] known_numbers = { "15555215554", "15555215556",
            "15555215558", "15555215560", "15555215562", "15555215564",
            "15555215566", "15555215568", "15555215570", "15555215572",
            "15555215574", "15555215576", "15555215578", "15555215580",
            "15555215582", "15555215584", };

    private static String[] known_device_ids = {
            "000000000000000" // 默认ID
    };

    private static String[] known_imsi_ids = {
            "310260000000000" // 默认的 imsi id
    };

    private static boolean checkPipes(){
        for(int i = 0; i < known_pipes.length; i++){
            String pipes = known_pipes[i];
            File qemu_socket = new File(pipes);
            if(qemu_socket.exists()){
                LogUtils.e("Result: Find pipes!");
                return true;
            }
        }
        LogUtils.e("Result: Not Find pipes!");
        return false;
    }

    private static Boolean checkQEmuDriverFile(){
        File driver_file = new File("/proc/tty/drivers");
        if(driver_file.exists() && driver_file.canRead()){
            byte[] data = new byte[1024];
            try {
                InputStream inStream = new FileInputStream(driver_file);
                inStream.read(data);
                inStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String driver_data = new String(data);
            for(String known_qemu_driver : known_qemu_drivers){
                if(driver_data.contains(known_qemu_driver)){
                    LogUtils.e("Result:Find know_qemu_drivers!");
                    return true;
                }
            }
        }
        LogUtils.e("Result:Not Find known_qemu_drivers!");
        return false;
    }

    private static Boolean CheckEmulatorFiles(){
        for(String s : known_files){
            if(FileUtils.isFile(new File(s))){
                LogUtils.e("Result:Find Emulator Files!");
                return true;
            }
        }
        LogUtils.e("Result:Not Find Emulator Files!");
        return false;
    }

    private static Boolean CheckPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        try{
            String phonenumber = getSimNumber(context);

            for (String number : known_numbers) {
                if (number.equalsIgnoreCase(phonenumber)) {
                    LogUtils.e("Result:Find PhoneNumber!");
                    return true;
                }
            }
            LogUtils.e("Result:Not Find PhoneNumber!");
        }catch (SecurityException e){
            e.printStackTrace();
        }
        return false;
    }

    private static Boolean CheckDeviceIDS(Context context) {
        try{
            String device_ids = getDeviceId(context);
            for (String know_deviceid : known_device_ids) {
                if (know_deviceid.equalsIgnoreCase(device_ids)) {
                    LogUtils.e("Result:Find ids: 000000000000000!");
                    return true;
                }
            }
            LogUtils.e("Result:Not Find ids: 000000000000000!");
        }catch (SecurityException e){
            e.printStackTrace();
        }
        return false;
    }

    private static Boolean CheckImsiIDS(Context context){
        try{
            String imsi_ids = getIMSI(context);

            for (String know_imsi : known_imsi_ids) {
                if (know_imsi.equalsIgnoreCase(imsi_ids)) {
                    LogUtils.e("Result:Find imsi ids: 310260000000000!");
                    return true;
                }
            }
            LogUtils.e("Result:Not Find imsi ids: 310260000000000!");
        }catch (SecurityException e){
            e.printStackTrace();
        }
        return false;
    }

    private static Boolean CheckEmulatorBuild(Context context){
        String BOARD = Build.BOARD;
        String BOOTLOADER = Build.BOOTLOADER;
        String BRAND = Build.BRAND;
        String DEVICE = Build.DEVICE;
        String HARDWARE = Build.HARDWARE;
        String MODEL = Build.MODEL;
        String PRODUCT = Build.PRODUCT;
        if (BOARD == "unknown" || BOOTLOADER == "unknown"
                || BRAND == "generic" || DEVICE == "generic"
                || MODEL == "sdk" || PRODUCT == "sdk"
                || HARDWARE == "goldfish")
        {
            LogUtils.e("Result:Find Emulator by EmulatorBuild!");
            return true;
        }
        LogUtils.e("Result:Not Find Emulator by EmulatorBuild!");
        return false;
    }


    public static boolean isVirtualDevice(Context context){
        return checkPipes() || checkQEmuDriverFile() || CheckEmulatorFiles() || CheckPhoneNumber(context) ||
                CheckDeviceIDS(context) || CheckImsiIDS(context) || CheckEmulatorBuild(context);
    }


}
