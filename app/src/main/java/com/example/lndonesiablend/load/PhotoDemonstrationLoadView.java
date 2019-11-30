package com.example.lndonesiablend.load;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.lndonesiablend.R;

public class PhotoDemonstrationLoadView extends Dialog {
    public PhotoDemonstrationLoadView(@NonNull Context context) {
        super(context);
    }

    public PhotoDemonstrationLoadView(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_photo_demonstration);
    }
}
