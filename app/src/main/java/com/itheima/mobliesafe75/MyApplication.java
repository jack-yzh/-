package com.itheima.mobliesafe75;

import android.app.Application;
import android.content.Context;

/**
 * Created by jack_yzh on 2017/12/20.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        context = getApplicationContext();
    }
    public static Context getContext() {
        return context;
    }
}
