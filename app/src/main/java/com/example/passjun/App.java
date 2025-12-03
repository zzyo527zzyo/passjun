package com.example.passjun;

import android.app.Application;
import android.content.Context;
import com.tencent.mmkv.MMKV;

public class App extends Application {

    private static Context appContext;

    // 提供安全的 getter，禁止外部修改
    public static Context getAppContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this.getApplicationContext();
        MMKV.initialize(this);
    }
}
