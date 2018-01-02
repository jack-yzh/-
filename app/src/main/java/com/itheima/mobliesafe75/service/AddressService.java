package com.itheima.mobliesafe75.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.itheima.mobliesafe75.R;
import com.itheima.mobliesafe75.db.dao.AddressDao;

/**
 * Created by jack_yzh on 2017/12/21.
 * 监听电话设置归属地提示框及其位置
 */

public class AddressService extends Service {

    private TelephonyManager telephonyManager;
    private PhoneStateListener myPhoneStateListener;
    private WindowManager windowManager;
    private View view1;
    private SharedPreferences sp;
    private MyOutGoingCallReceiver myOutGoingCallReceiver;
    private WindowManager.LayoutParams params;
    private int width;
    private int height;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class MyOutGoingCallReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //查询外拨电话的归属地
            String phone  = getResultData();
            String queryAddress = AddressDao.queryAdress(phone,getApplicationContext());
            if (!queryAddress.isEmpty()){
                showToast(queryAddress);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //代码注册监听外拨电话广播
        //1.设置外拨电话广播接收者
        myOutGoingCallReceiver = new MyOutGoingCallReceiver();
        //2.设置接收的广播事件
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        //注册广播接受者
        registerReceiver(myOutGoingCallReceiver,intentFilter);

        sp = getSharedPreferences("config",MODE_PRIVATE);
        //监听电话状态
        //1.获取电话的管理者
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //2.监听电话的状态
        myPhoneStateListener = new MyPhoneStateListener();
        //listener : 电话状态的回调监听
        //events : 监听电话的事件
        //LISTEN_NONE : 不做任务监听操作
        //LISTEN_CALL_STATE : 监听电话状态
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        //获取屏幕宽度
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();//创建一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);//给白纸设置宽高
        width = outMetrics.widthPixels;
        height = outMetrics.heightPixels;
    }
    private class MyPhoneStateListener extends PhoneStateListener{
        //监听电话状态的回调方法
        //state : 电话的状态
        //incomingNumber :　来电电话
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE://空闲 状态,挂断状态
                    hideToast();
                    break;
                case TelephonyManager.CALL_STATE_RINGING:;//响铃的状态
                    //查询号码归属地并显示
                    String queryAddress = AddressDao.queryAdress(incomingNumber, getApplicationContext());
                    //判断查询归属地是否为空
                    if (!TextUtils.isEmpty(queryAddress)) {
                        //显示号码归属地
                        showToast(queryAddress);
                        //Toast.makeText(getApplicationContext(), queryAddress,Toast.LENGTH_SHORT).show();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://通话的状态
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }



    @Override
    public void onDestroy() {
        //当服务取消的时候，注销监听
        telephonyManager.listen(myPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        //注销外拨电话广播接收者
        unregisterReceiver(myOutGoingCallReceiver);
        super.onDestroy();
    }
    //显示Toast
    public void showToast(String queryAddress) {
        //背景图片id数组
        int[] bgcolor = new int[]{R.drawable.call_locate_blue,R.drawable.call_locate_green,R.drawable.call_locate_red,R.drawable.call_locate_yellow,R.drawable.call_locate_blue};
        //1.获取windowmanager
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        view1 = View.inflate(getApplicationContext(), R.layout.toast_custom,null);
        TextView tv_toastcustom_address = (TextView) view1.findViewById(R.id.tv_toastcustom_address);
        tv_toastcustom_address.setText(queryAddress);

        //根据设置项选中的主题来设定颜色
        view1.setBackgroundResource(bgcolor[sp.getInt("which",0)]);

        //3.设置toast的属性
        //layoutparams是toast的属性,控件要添加到那个父控件中,父控件就要使用那个父控件的属性,表示控件的属性规则符合父控件的属性规则
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;//高度包裹内容
        params.width = WindowManager.LayoutParams.WRAP_CONTENT; //宽度包裹内容
        params.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | //没有焦点
                // WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | // 不可触摸
                 WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; // 保持当前屏幕
        params.format = PixelFormat.TRANSLUCENT; // 透明
        //params.type = WindowManager.LayoutParams.TYPE_TOAST; // 执行toast的类型,可是不能有触摸事件
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;//需要权限


        params.gravity = Gravity.LEFT | Gravity.TOP;//Toast显示的相对位置，此时相对于左边和顶部
        //设置保存的相对位置，他们跟上面地址的gravity密切相关，单独使用无效
        params.x = sp.getInt("x",100);
        params.y = sp.getInt("y",100);

        setTouch();

        //2.将view对象添加到windowManager中
        //params : layoutparams  控件的属性
        //将params属性设置给view对象,并添加到windowManager中
        windowManager.addView(view1, params);
    }

    private void setTouch() {
        view1.setOnTouchListener(new View.OnTouchListener() {
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
                        //重新绘制控件
                        params.x+=dX;
                        params.y+=dY;
                        //判断坐标是否移出界面
                        if (params.x < 0){
                            params.x = 0;
                        }
                        if (params.y <25){
                            params.y = 25;
                        }
                        if (params.x > width-view1.getWidth()){
                            params.x = width - view1.getWidth();
                        }
                        if (params.y > height - view1.getHeight()){
                            params.y = height - view1.getHeight();
                        }

                        windowManager.updateViewLayout(view1,params);

                        startX = newX;
                        startY = newY;
                        System.out.print("移动...");
                        break;
                    case MotionEvent.ACTION_UP:
                        System.out.print("抬起...");
                        //获取元件的左上角坐标
                        int x = params.x;
                        int y = params.y;
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

    //隐藏Toast
    public void hideToast(){
        if (windowManager !=null && view1 != null){
            windowManager.removeView(view1);//移除控件
            windowManager = null;
            view1 = null;
        }
    }
}
