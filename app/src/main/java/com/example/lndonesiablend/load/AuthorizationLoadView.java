package com.example.lndonesiablend.load;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.lndonesiablend.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthorizationLoadView extends Dialog {
    @BindView(R.id.img_authorization)
    ImageView imgAuthorization;
    @BindView(R.id.but_authorization)
    Button butAuthorization;

    public AuthorizationLoadView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_authorization);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        ButterKnife.bind(this);
        imgAuthorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCloseClick(true);
            }
        });
        butAuthorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCloseClick(true);
            }
        });
    }

    private OnCloseClickListener listener;

    public void setOnCloseClickListener(OnCloseClickListener listener) {
        this.listener = listener;
    }

    public interface OnCloseClickListener {
        void onCloseClick(boolean isClick);
    }
}
