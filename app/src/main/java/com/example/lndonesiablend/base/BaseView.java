package com.example.lndonesiablend.base;

import com.example.lndonesiablend.bean.BaseBean;
import com.example.lndonesiablend.http.ExceptionHandle;

public interface BaseView {
    void onSuccess(Object object);

    void onFail(ExceptionHandle.ResponeThrowable e);
}
