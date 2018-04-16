package com.jiedaoqian.android.utils.httputils;

import android.content.Context;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zenghui on 2017/8/4.
 */

public class RedirectObserver<T> implements Observer<T> {

    Context mContext;

    public Context getmContext() {
        return mContext;
    }

    public RedirectObserver(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T value) {

    }


    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
