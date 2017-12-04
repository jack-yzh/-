package com.itheima.mobliesafe75.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jack_yzh on 2017/12/3.
 */

public abstract class GuideBaseActivity extends Activity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取手势识别器
        gestureDetector = new GestureDetector(this,new MyOnGestureListener());
    }
    //创建一个内部类手势监听类
    public class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //e1按下去的初始点坐标，e2为释放点的坐标
            float startX = e1.getRawX();
            float endX = e2.getRawX();
            float startY = e1.getRawY();
            float endY = e2.getRawY();
            if ((Math.abs(startY-endY)>50)){
                return false;
            }
            if ((startX-endX)>100){
                next_activity();
            }
            if ((endX-startX)>100){
                pre_activity();
            }
            return true;
        }
    }
    //在触摸时间中注册后才能生效
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    //子控件的点击事件先在父类中执行，然后再执行子类实现的抽象方法
    public void next(View v){
        next_activity();
    }
    public void pre(View v){
        pre_activity();
    }
    //因为父类不知道子类上一步，所以创建一个抽象方法，让子类实现
    //下一步的操作
    public abstract void next_activity();
    //上一步
    public abstract void pre_activity();
    //onKeyCode()监听手机的物理按键,KeyCode按键值，KeyEvent按键代表的事件
    public boolean onKeyDown(int KeyCode,KeyEvent event){
        //在该方法中返回TRUE会使得按键被屏蔽
        if (KeyCode == KeyEvent.KEYCODE_BACK){
            pre_activity();
        }
        return super.onKeyDown(KeyCode,event);
    }

}
