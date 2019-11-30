package com.example.lndonesiablend.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import com.example.lndonesiablend.adapter.RecyclerViewHolderAdapter;

import java.util.Stack;

import io.reactivex.annotations.NonNull;

public class UIHelper {

    public static Stack<Activity> activities = new Stack<>();

    /**
     * 为View添加ClickListener
     */
    public static <T extends View.OnClickListener> RecyclerViewHolderAdapter bindClickListener(ViewGroup viewGroup, T listener, @IdRes int... idSet) {
        if (idSet.length == 0 && listener == null) {
            return null;
        }
        RecyclerViewHolderAdapter holder = new RecyclerViewHolderAdapter(viewGroup);
        for (int id : idSet) {
            View view = holder.getView(id);
            if (view != null) {
                view.setOnClickListener(listener);
            }
        }
        return holder;
    }

    /**
     * 显示Toast
     */
    public static void showToast(@NonNull Context context, @StringRes int str) {
        showToast(context, context.getString(str), Toast.LENGTH_SHORT);
    }

    public static void showToast(@NonNull Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(@NonNull Context context, String msg, int time) {
        Toast.makeText(context, msg, time).show();
    }

    /**
     * 新增代码
     * @param context 上一个Activity的上下文对象
     * @param url 参数1 url
     * @param title 参数2 标题
     */
    public static void biography(Context context, String url, String title ,Class<?> object) {
        Intent data = new Intent(context, object);
        data.putExtra("url", url);
        data.putExtra("title", title);
        context.startActivity(data);
    }

    /**
     * 根据给定的class进行Aactivity跳转
     * @param targetActivity
     */
    public static void intentActivity(Class<? extends Activity> targetActivity){
        intentActivity(targetActivity, false);
    }

    /**
     * 根据给定的class进行Aactivity跳转
     * @param targetActivity      要跳转的Activity
     * @param isFinish            是否关闭Activity对象
     */
    public static void intentActivity(Class<? extends Activity> targetActivity, boolean isFinish){
        if(null != currentActivity() && null != targetActivity){
            currentActivity().startActivity(new Intent(currentActivity(), targetActivity));
            if(isFinish){
                currentActivity().finish();
            }
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        if (activities.isEmpty()) {
            return null;
        }
        Activity activity = activities.lastElement();
        return activity;
    }

}