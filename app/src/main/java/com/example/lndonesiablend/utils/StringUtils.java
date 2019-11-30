package com.example.lndonesiablend.utils;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;



public class StringUtils {

    public static boolean isEmpty(String str){
        return TextUtils.isEmpty(str);
    }

    public static boolean isListEmpty(List<?> list){
        return list == null || list.size() == 0;
    }

    private static String replaceAction(String username, String regular) {
        return username.replaceAll(regular, "*");
    }

    public static Spanned getHtmlText(Context context, String str){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(str, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(str);
        }
    }

    public static List<String> getJsonObjectKey(JSONObject json){
        List<String> keys = new ArrayList<>();
        if(null != json){
            LinkedHashMap<String, String> linkedHashMap = JSON.parseObject(json.toString(),
                    new TypeReference<LinkedHashMap<String, String>>() {}, Feature.OrderedField);
            for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {
                if(!isEmpty(entry.getKey())){
                    keys.add(entry.getKey());
                }
            }
        }
        return keys;
    }


    public static List<String> sortList(List<String> list){
        if(null == list || list.isEmpty()){
            return null;
        }
        try{
            List<Integer> intList = new LinkedList<>();
            for(int i = 0; i < list.size(); i++){
                intList.add(Integer.parseInt(list.get(i)));
            }
            for (int i = 0; i < intList.size(); i++) {
                for(int j = 0; j < intList.size() - 1; j++){
                    if(intList.get(j) > intList.get(j + 1)){
                        int temp = intList.get(j);
                        intList.set(j, intList.get(j + 1));
                        intList.set(j + 1, temp);
                    }
                }
            }
            List<String> resultList = new LinkedList<>();
            for(int i = 0; i < intList.size(); i++){
                resultList.add(String.valueOf(intList.get(i)));
            }
            return resultList;
        }catch (NumberFormatException e){
            return null;
        }
    }

    public static String formatLong(long num){
        if(num < 10){
            return "0" + String.valueOf(num);
        }
        return String.valueOf(num);
    }

}

