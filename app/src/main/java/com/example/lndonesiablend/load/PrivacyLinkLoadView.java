package com.example.lndonesiablend.load;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.lndonesiablend.BuildConfig;
import com.example.lndonesiablend.R;
import com.example.lndonesiablend.event.OnEventClickListener;
import com.example.lndonesiablend.utils.UIHelper;
import com.example.lndonesiablend.utils.WebViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Author: weiyun
 * @Time: 2019/9/16
 * @Description: 隐私政策提示框
 */
public class PrivacyLinkLoadView extends Dialog {
    private Context mContext;
    @BindView(R.id.mRootView)
    public LinearLayout mRootView;
    @BindView(R.id.mWebView)
    public WebView mWebView;
    @BindView(R.id.mCheckbox)
    public CheckBox mCheckbox;
    @BindView(R.id.mProgressBar)
    public ProgressBar mProgressBar;
    @BindView(R.id.mBtnConfirm)
    public Button mBtnConfirm;
    public OnConfirmClickListener mOnConfirmClickListener;

    public PrivacyLinkLoadView(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_privacy_link);
        ButterKnife.bind(this);
        mContext = getContext();
        configWebView();
        initEvent();
    }

    private void initEvent() {
        mBtnConfirm.setEnabled(false);
        mCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mBtnConfirm.setEnabled(true);
            } else {
                mBtnConfirm.setEnabled(false);
            }
        });

        UIHelper.bindClickListener(mRootView, new OnEventClickListener() {
            @Override
            public void onEventClick(View v) {
                switch (v.getId()) {
                    case R.id.mBtnConfirm:
                        if (mOnConfirmClickListener != null) {
                            mOnConfirmClickListener.onConfirm(mCheckbox.isChecked());
                        }
                        break;
                }
            }
        }, R.id.mBtnConfirm);
    }

    /**
     * 初始化WebView
     */
    private void configWebView() {
        WebViewUtils.initWebView(getOwnerActivity(),getContext(),mWebView,null,BuildConfig.PRIVACY_LINK);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        clearWebViewResource();
    }

    /**
     * 回收WebView资源
     */
    private void clearWebViewResource() {
        if (mWebView != null) {
            mWebView.removeAllViews();
            //在5.1上如果不加上这句话就会出现内存泄露。这是5.1的bug
            // mComponentCallbacks导致的内存泄漏
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.setTag(null);
            mWebView.clearHistory();
            mWebView.destroy();
            mWebView = null;
        }
    }

    /**
     * 设置点击事件
     */
    public void setOnConfirmClickListener(OnConfirmClickListener onConfirmClickListener) {
        this.mOnConfirmClickListener = onConfirmClickListener;
    }

    public interface OnConfirmClickListener {
        void onConfirm(boolean checked);
    }
}
