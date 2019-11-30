package com.example.lndonesiablend.event;

import android.view.View;

//import jally.vay.utils.FireBaseHelper;

/**
 * Created by SiKang on 2018/10/11.
 * 自动埋点的 OnClickListener
 * 给View设置一个TAG，并使用OnEventClickListener作为 Click监听事件，每次点击会自动记录一次 FireBase Event
 */
public abstract class OnEventClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
//       FireBaseHelper.LogClickEventByTag(v);
        onEventClick(v);
    }

    public abstract void onEventClick(View v);


}
