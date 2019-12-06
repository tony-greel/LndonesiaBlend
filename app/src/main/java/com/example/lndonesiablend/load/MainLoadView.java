package com.example.lndonesiablend.load;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.lndonesiablend.R;

public class MainLoadView extends Dialog{

    Context mContext;

    public MainLoadView(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public MainLoadView(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private String content;

        public Builder(Context context){
            this.context = context;
        }

        public Builder setContent(String content){
            this.content = content;
            return this;
        }

        public MainLoadView create(){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final MainLoadView dialog = new MainLoadView(context, R.style.loading_dialog_style);
            View layout = inflater.inflate(R.layout.load_network, null);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            dialog.setCancelable(false);
            ((TextView) layout.findViewById(R.id.id_tv_loading_dialog_text)).setText(content);
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
