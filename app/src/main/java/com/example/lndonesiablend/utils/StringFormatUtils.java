package com.example.lndonesiablend.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by SiKang on 2018/10/16.
 * 时间、货币、String、Bean 格式化工具
 */
public class StringFormatUtils {
    public static final String FORMAT = "yyyy-MM-dd kk:mm:ss";

    /**
     * 去除小数点
     *
     * @param money
     * @return
     */
    public static String moneyFormatInteger(String money) {
        if (!TextUtils.isEmpty(money)) {
            return money.replace(".", "");
        }
        return money;
    }

    /**
     * 格式化金额---不带货币单位
     * 中国："."为小数点，","为数字分割符
     *
     * 印尼、越南：","为小数点，"."为数字分割符
     */
    public static String moneyFormatwithoutUnit(@Nullable Context context, double money) {
        return String.format(" %,.0f", money).replace(",", ".");
    }

    /**
     * 计算日利息
     *
     * @param principal
     * @param serviceCharge
     * @param days
     * @return
     */
    public static String dailyInterestFormat(String principal, String serviceCharge, String days) {
        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(2);
        double principalDouble = Double.parseDouble(moneyFormatInteger(principal));
        double serviceChargeDouble = Double.parseDouble(moneyFormatInteger(serviceCharge));
        int daysInt = Integer.parseInt(days);
        Double interest = serviceChargeDouble / principalDouble / daysInt;
        return instance.format(interest) + "%";
    }


    /**
     * 序列化对象
     */
    public static String fromBean(Object obj) {
        if (obj == null) {
            return "";
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = null;
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            return base64(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 反序列化对象
     */
    public static Object toBean(String str) {
        try {
            byte[] bytes = Base64.decode(str, Base64.NO_WRAP);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * String转base64
     */
    public static String base64(String content) {
        if (!TextUtils.isEmpty(content))
            return Base64.encodeToString(content.getBytes(), Base64.DEFAULT);
        else
            return "";
    }

    /**
     * String转base64
     */
    public static String base64(byte[] bytes, int flag) {
        if (bytes != null && bytes.length > 0)
            return Base64.encodeToString(bytes, flag);
        else
            return "";
    }


    /**
     * MD5
     */
    public static String MD5(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 格式化时间
     */
    public static String convertTime(String srcTime, String formatStyle) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
        SimpleDateFormat simpleDateFormatLocal = new SimpleDateFormat(formatStyle);
        simpleDateFormatLocal.setTimeZone(TimeZone.getDefault());
        try {
            if (TextUtils.isEmpty(srcTime)) {
                return "0";
            }
            Date srcDate = simpleDateFormat.parse(srcTime);
            String targetTime = simpleDateFormatLocal.format(srcDate);
            return targetTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "0";
    }

    public static String getTime(Date date, String mode) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat(mode);
        return format.format(date);
    }

    /**
     * 转换时间(String -> Date)
     */
    public static Date stringToDate(String time, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            Date date = formatter.parse(time);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换时间(Date -> String)
     */
    public static String dateToString(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String time = formatter.format(date);
        return time;
    }

    /**
     * 转换时间(long->String)
     *
     * @param date
     * @return
     */
    public static String formatTime(long date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date).toString();
    }


    public static int dpToPx(Context context, float dp) {
        return (int) (context.getApplicationContext().getResources().getDisplayMetrics().densityDpi / 160 * dp);
    }


}
