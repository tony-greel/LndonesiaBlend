package com.example.lndonesiablend.load;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import com.example.lndonesiablend.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoDemonstrationLoadView extends Dialog {


    @BindView(R.id.mBtPersonInfoSubmit)
    Button mBtPersonInfoSubmit;
    private onConfirmClickListener clickListener;

    public PhotoDemonstrationLoadView(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_photo_demonstration);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.mBtPersonInfoSubmit)
    public void onViewClicked() {
        clickListener.confirmClick(true);
    }

    public void setOnSureClick(onConfirmClickListener click) {
        this.clickListener = click;
    }


    public interface onConfirmClickListener {
        void confirmClick(boolean confirm);
    }
}
