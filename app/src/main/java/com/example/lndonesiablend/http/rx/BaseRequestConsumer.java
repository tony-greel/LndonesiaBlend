package com.example.lndonesiablend.http.rx;

import android.widget.Toast;

import com.example.lndonesiablend.LndonesiaBlendApp;
import com.example.lndonesiablend.base.ILoading;
import com.example.lndonesiablend.bean.BaseBean;


import io.reactivex.functions.Consumer;

public abstract class BaseRequestConsumer<T> implements Consumer<BaseBean<T>> {

  private ILoading iLoading;

  public BaseRequestConsumer(ILoading iLoading){
    this.iLoading = iLoading;
  }

  public static final String REQUEST_OK = "200";

  @Override
  public void accept(BaseBean<T> baseRequestBean) throws Exception {
    if (baseRequestBean.getCode().equals(REQUEST_OK)){
      onRequestSuccess(baseRequestBean.getResult());
    }else {
      onRequestError(baseRequestBean);
    }
    if (iLoading != null){
      iLoading.closeLoadingView();
    }
  }

  protected abstract void onRequestSuccess(T data);

  public void onRequestError(BaseBean baseRequestBean){
    Toast.makeText(LndonesiaBlendApp.getAppContext(), baseRequestBean.getMessage(), Toast.LENGTH_SHORT).show();
  }

}
