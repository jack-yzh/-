package com.itheima.mobliesafe75.utils;


import android.os.Handler;
import android.os.Message;

/**
 * Created by jack_yzh on 2017/12/17.
 */

public abstract class MyAsyncTask {
    /**
     * 在子线程之前处理的事件
     */
    public abstract void preTask();

    /**
     * 在子线程之中处理的事件
     */
    public abstract void doinBack();

    /**
     * 在子线程处理完之后处理的事件
     */
    public abstract void postTask();

    /**
     * 接收子线程发来的消息，处理后续事件
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            postTask();
        }
    };

    /**
     * 定义执行方法框架，让引用类实现方法后直接执行，俗称封装
     */
    public void execute(){
        preTask();
        new Thread(){
            @Override
            public void run() {
                doinBack();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
}
