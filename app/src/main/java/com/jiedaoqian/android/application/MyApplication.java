package com.jiedaoqian.android.application;

import android.app.Application;

import com.mob.MobSDK;

/**
 * Created by zenghui on 2018/3/15.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
    }
}
