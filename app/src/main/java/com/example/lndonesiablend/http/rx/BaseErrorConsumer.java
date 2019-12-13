package com.example.lndonesiablend.http.rx;

import android.util.Log;
import android.widget.Toast;

import com.example.lndonesiablend.LndonesiaBlendApp;
import com.example.lndonesiablend.base.ILoading;

import io.reactivex.functions.Consumer;

public class BaseErrorConsumer implements Consumer<Throwable> {

  ILoading iLoading;

  public BaseErrorConsumer(ILoading iLoading){
    this.iLoading = iLoading;
  }

  @Override
  public void accept(Throwable throwable) throws Exception {
    if (iLoading != null){
      iLoading.closeLoadingView();
    }
    Log.d("yangjing",throwable.getMessage());
    Toast.makeText(LndonesiaBlendApp.getAppContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
  }
}
