package com.itheima.mobliesafe75;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by jack_yzh on 2017/12/24.
 * 设置拖拽按钮
 */

public class DragActivity extends Activity {

    private LinearLayout ll_dragview_toast;

    private SharedPreferences sp;
    private int width;
    private int height;
    long[] mHits = new long[2];
    private TextView tv_dragview_top;
    private TextView tv_dragview_buttom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragview);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        ll_dragview_toast = (LinearLayout)findViewById(R.id.ll_dragview_toast);
        tv_dragview_top = (TextView)findViewById(R.id.tv_dragview_top);
        tv_dragview_buttom = (TextView)findViewById(R.id.tv_dragview_buttom);

        //设置拖拽控件的回显操作
        int x = sp.getInt("x",0);
        int y = sp.getInt("y",0);
//        //重新绘制在onCreat方法里面不适用
//        int width = ll_dragview_toast.getWidth();
//        int height = ll_dragview_toast.getHeight();
//        ll_dragview_toast.layout(x,y,x+width,y+height);
        //初始化控件之前要重新设置控件属性，获取父控件的属性规则，父控件的layoutparams
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_dragview_toast.getLayoutParams();
        //设置相应属性
        params.leftMargin = x;
        params.topMargin = y;
        ll_dragview_toast.setLayoutParams(params);
        //获取屏幕宽度
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();//创建一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);//给白纸设置宽高
        width = outMetrics.widthPixels;
        height = outMetrics.heightPixels;

        //判断textview隐藏还是显示
        if (y >= height/2){
            tv_dragview_buttom.setVisibility(View.INVISIBLE);
            tv_dragview_top.setVisibility(View.VISIBLE);
        }else{
            tv_dragview_buttom.setVisibility(View.VISIBLE);
            tv_dragview_top.setVisibility(View.INVISIBLE);
        }


        setTouch();
        setDoubleClick();
    }

    /**
     * 设置双击居中事件
     */
    private void setDoubleClick() {
        ll_dragview_toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *  src the source array to copy the content.   拷贝的原数组
                 srcPos the starting index of the content in src.  是从源数组那个位置开始拷贝
                 dst the destination array to copy the data into.  拷贝的目标数组
                 dstPos the starting index for the copied content in dst.  是从目标数组那个位置开始去写
                 length the number of elements to be copied.   拷贝的长度
                 */
                //拷贝数组操作
                System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
                mHits[mHits.length-1] = SystemClock.uptimeMillis();  // 将离开机的时间设置给数组的第二个元素,离开机时间 :毫秒值,手机休眠不算
                if (mHits[0] >= (SystemClock.uptimeMillis()-500)) {  // 判断是否多击操作
                    System.out.println("双击了...");
                    //双击居中
                    int l = (width - ll_dragview_toast.getWidth()) / 2;
                    int t = (height - 25 - ll_dragview_toast.getHeight()) / 2;
                    ll_dragview_toast.layout(l, t, l + ll_dragview_toast.getWidth(), t + ll_dragview_toast.getHeight());
                    //保存控件的坐标
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("x", l);
                    edit.putInt("y", t);
                    edit.commit();
                }
            }
        });
    }

    //设置触摸监听
    private void setTouch() {
        ll_dragview_toast.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //按下时的坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        System.out.println("按下...");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //松开时的坐标
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();
                        //偏差量
                        int dX = newX - startX;
                        int dY = newY - startY;
                        //原位置和边距的相对距离
                        int l = ll_dragview_toast.getLeft();
                        int t = ll_dragview_toast.getTop();
                        //新位置的相对距离
                        l+=dX;
                        t+=dY;
                        //右半部分的相对距离
                        int r = l+ll_dragview_toast.getWidth();
                        int b = t+ll_dragview_toast.getHeight();

                        if(l<0 || r>width || t<0 || b>height - 25){
                            break;
                        }

                        ll_dragview_toast.layout(l,t,r,b);//重新绘制控件
                        //判断textview隐藏还是显示
                        if (ll_dragview_toast.getTop() > height/2){
                            tv_dragview_buttom.setVisibility(View.INVISIBLE);
                            tv_dragview_top.setVisibility(View.VISIBLE);
                        }else{
                            tv_dragview_buttom.setVisibility(View.VISIBLE);
                            tv_dragview_top.setVisibility(View.INVISIBLE);
                        }

                        startX = newX;
                        startY = newY;
                        System.out.print("移动...");
                        break;
                    case MotionEvent.ACTION_UP:
                        System.out.print("抬起...");
                        //获取元件的左上角坐标
                        int x = ll_dragview_toast.getLeft();
                        int y =ll_dragview_toast.getTop();
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("x",x);
                        edit.putInt("y",y);
                        edit.commit();
                        System.out.print("x:"+x+"y:"+y);
                        break;
                }
                return false;//false 表示不拦截点击事件，可以执行其他点击事件 true表示拦截点击事件只在只在触摸事件中执行
            }
        });
    }
}
