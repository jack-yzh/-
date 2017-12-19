package com.itheima.mobliesafe75.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


/**
 * Created by jack_yzh on 2017/12/4.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //检查SIM卡是够发生变化
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        //判断是否开启防盗报警短信
        if (sp.getBoolean("protected",false)){
            String sp_sim = sp.getString("sim","");
            TelephonyManager tel = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            String sim = tel.getSimSerialNumber();
            if(!TextUtils.isEmpty(sp_sim)&&!TextUtils.isEmpty(sim)){
                if (!sp_sim.equals(sim)){
                    //两次SIM卡序列号不同，发送报警短信
                    //发送短信报警，创建短信管理器
                    SmsManager smsManager = SmsManager.getDefault();
                    //public void sendTextMessage(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent)
                    //destinationAddress：收件人
                    //scAddress短信中心地址，一般是空
                    //sentIntent：是否发送成功
                    //deliveryIntent：短信的协议
                    smsManager.sendTextMessage(sp.getString("safenum","5556"),null,"大哥我被人偷了",null,null);
                }
            }
        }


    }
}
