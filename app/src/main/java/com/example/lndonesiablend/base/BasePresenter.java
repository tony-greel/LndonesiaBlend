package com.example.lndonesiablend.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.lndonesiablend.http.rx.DisposableManager;

import io.reactivex.disposables.Disposable;

public class BasePresenter<V> {
    private DisposableManager disposableManager;

    public V mvpView;

    void attachView(V mvpView){
        this.mvpView = mvpView;
    }


    public void detachView() {
        this.mvpView = null;
        onUnsubscribe();
    }

    protected void onUnsubscribe(){
        if (disposableManager != null){
            disposableManager.cancelAllDisposable();
        }
    }


    public void addDisposable(Disposable disposable){
        if (disposableManager == null){
            disposableManager = new DisposableManager();
        }
        disposableManager.addDisposable(disposable);
    }

    public Context getContext(){
        if (mvpView instanceof Activity){
            return (Activity) mvpView;
        }else if (mvpView instanceof Fragment){
            return ((Fragment) mvpView).getContext();
        }else if (mvpView instanceof View){
            return ((View) mvpView).getContext();
        }else {
            throw new RuntimeException("not such this mvpView");
        }
    }
}
