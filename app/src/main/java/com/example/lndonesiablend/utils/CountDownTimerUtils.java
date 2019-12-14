package com.example.lndonesiablend.utils;

import android.os.CountDownTimer;
import android.text.SpannableString;
import android.widget.TextView;


/**
 * Created by lijunjie on 2018/6/2.
 * 发送验证码倒计时按钮
 */

public class CountDownTimerUtils extends CountDownTimer {

    private TextView mTextView;

    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false); //设置不可点击
        mTextView.setText(millisUntilFinished / 1000 +"s"); // 设置倒计时时间
//        mTextView.setBackgroundResource(R.drawable.background_gradual_change); // 设置按钮颜色，这时是不能点击的
        SpannableString spannableString = new SpannableString(mTextView.getText().toString()); // 获取按钮上的文字
        mTextView.setText(spannableString);
    }

    @Override
    public void onFinish() {
        mTextView.setText("重新获取验证码");
        mTextView.setClickable(true);
//        mTextView.setBackgroundResource(R.drawable.dialog_box_bg_round_white); // 还原背景色
    }
}
