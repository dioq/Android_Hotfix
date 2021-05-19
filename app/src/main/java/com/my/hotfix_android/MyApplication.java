package com.my.hotfix_android;

import android.app.Application;

import com.my.hotfix_android.hotfix.Hotfix;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //启动热修复
        Hotfix.dynamicUpdate(this);
    }
}
