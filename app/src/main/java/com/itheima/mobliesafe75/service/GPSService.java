package com.itheima.mobliesafe75.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.List;

/**
 * Created by jack_yzh on 2017/12/18.
 */

public class GPSService extends Service {
    String locationProvider;
    private SharedPreferences sp;
    private LocationListener myLocationListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //定位操作一定要在onCreat方法里面执行
    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("config",MODE_PRIVATE);
        /**
         * 调用本地GPS来获取经纬度
         * @param context
         */
        int MIN_TIME = 1;
        int MIN_DISTANCE = 1;
        //1.获取位置管理器
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是网络定位
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS定位
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.PASSIVE_PROVIDER)) {
            //如果是PASSIVE定位
            locationProvider = LocationManager.PASSIVE_PROVIDER;
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }
        myLocationListener = new MyLocationListener();
        //3.请求位置更新 MIN_TIME 定位最小间隔时间 MIN_DISTANCE 定位最小间隔距离
        locationManager.requestLocationUpdates(locationProvider, MIN_TIME, MIN_DISTANCE, myLocationListener);

    }

    private class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();//获取维度
            double longitude = location.getLongitude();//获取经度
            SmsManager smsManger = SmsManager.getDefault();
            String num = sp.getString("safenum","15059358138");
            smsManger.sendTextMessage(num,null,"longitute:"+longitude+"latitude:"+latitude,null,null);
            //停止服务
            stopSelf();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }


}
