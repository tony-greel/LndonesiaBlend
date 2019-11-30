package com.example.lndonesiablend.utils;

import java.security.MessageDigest;
import java.util.regex.Pattern;

/**
 * @Author: weiyun
 * @Time: 2019/6/12
 * @Description:MD5相关工具类
 */
public class MD5Utils {

    private static Pattern MD5_PATTERN = Pattern.compile("^[0-9a-fA-F]{32}$");

    public final static String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };

        try {
            byte[] btInput = s.getBytes("UTF-8");
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成MD5字符串
     *
     * @param str 加密原字符串
     * @return
     **/
    public static String MD5(String str, String encoding) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes(encoding));
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    public static boolean isMD5String(String md5) {
        if (md5 == null || md5.trim().length() == 0) {
            return false;
        } else {
            return MD5_PATTERN.matcher(md5).matches();
        }
    }

    /**
     *
     * 生成动态key
     * @return
     */
    public static String generateStaticKey() {
        Long currentTime = System.currentTimeMillis();
        //MD5加密转换成大写
        String staticKey = MD5(currentTime+"").toUpperCase();

        return staticKey;
    }

}
