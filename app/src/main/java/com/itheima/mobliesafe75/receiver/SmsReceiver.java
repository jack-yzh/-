package com.itheima.mobliesafe75.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.itheima.mobliesafe75.R;
import com.itheima.mobliesafe75.service.GPSService;

/**
 * Created by jack_yzh on 2017/12/18.
 */

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //获取超级管理员管理器
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //获取组件名称
        ComponentName componentName = new ComponentName(context,Admin.class);
        SmsMessage msg;
        //Bundle bundle = intent.getExtras();
        //if (bundle != null) {
            Object[] pdusObj = (Object[]) intent.getExtras().get("pdus");
            for (Object p : pdusObj) {
                msg = SmsMessage.createFromPdu((byte[]) p);
                String body = msg.getMessageBody();//得到消息的内容
                String sender = msg.getOriginatingAddress();//获取发件人
                System.out.println("发件人：" + sender + "信息：" + body);

                if("#*location*#".equals(body)){
                    //GPS追踪
                    System.out.println("GPS追踪GPS追踪GPS追踪GPS追踪GPS追踪GPS追踪GPS追踪");
                    //abortBroadcast();//拦截短信，成为系统默认短信应用才能屏蔽
                    //开启定位服务
                    Intent intent_gps = new Intent(context, GPSService.class);
                    context.startService(intent_gps);
                }else if ("#*alarm*#".equals(body)){
                    //播放音乐，创建媒体播放器
                    MediaPlayer mediaplayer = MediaPlayer.create(context, R.raw.alarm);
                    mediaplayer.start();
                    System.out.println("播放报警音乐");
                    //abortBroadcast();//拦截短信
                }else if ("#*wipedata*#".equals(body)){
                    System.out.println("远程删除数据");
                    if (devicePolicyManager.isAdminActive(componentName)){
                        //管理器直接删除数据
                        devicePolicyManager.wipeData(0);
                    }
                    //abortBroadcast();//拦截短信
                }else if ("#*lockscreen*#".equals(body)){
                    System.out.println("远程锁屏");
                    //判断超级管理员是否激活
                    if (devicePolicyManager.isAdminActive(componentName)){
                        //执行锁屏功能
                        devicePolicyManager.lockNow();
                    }
                    //abortBroadcast();//拦截短信
                }
//                Date date = new Date(msg.getTimestampMillis());//获取发件时间
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String receiveTime = format.format(date);


            }
        }
    //}
}