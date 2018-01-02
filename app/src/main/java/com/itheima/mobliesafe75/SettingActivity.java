package com.itheima.mobliesafe75;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobliesafe75.service.AddressService;
import com.itheima.mobliesafe75.ui.SettingClickView;
import com.itheima.mobliesafe75.ui.SettingView;
import com.itheima.mobliesafe75.utils.AddressUtils;

/**
 * Created by jack_yzh on 2017/11/30.
 */

public class SettingActivity extends Activity {

    private SettingView sv_setting_update;
    private SharedPreferences sp;
    private SettingView sv_setting_address;
    private SettingClickView scv_setting_changebg;
    private String[] items;
    private SettingClickView scv_setting_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //存储用户信息
        sp = getSharedPreferences("config",MODE_PRIVATE);
        //获取对象实例
        sv_setting_update = (SettingView)findViewById(R.id.sv_setting_update);
        sv_setting_address = (SettingView)findViewById(R.id.sv_setting_address);
        scv_setting_changebg = (SettingClickView)findViewById(R.id.scv_setting_changebg);
        scv_setting_location = (SettingClickView)findViewById(R.id.scv_setting_location);
        items = new String[]{"卫士蓝","苹果绿","火山红","活力橙","金属灰"};
        update();
        changebg();
        location();
    }

    private void location() {
        scv_setting_location.setTitle("提示框位置");
        scv_setting_location.setDes("设置归属地提示框的显示位置");
        scv_setting_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,DragActivity.class);
                startActivity(intent);
            }
        });
    }

    private void changebg() {
        scv_setting_changebg.setTitle("归属地提示框风格");
        scv_setting_changebg.setDes(items[sp.getInt("which",0)]);

        scv_setting_changebg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("归属地提示风格");
                //设置单选框,1.选项文本数组2.选中的选项3.点击事件
                //设置单选框回选操作
                builder.setSingleChoiceItems(items,sp.getInt("which",0), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("which",which);
                        editor.commit();
                        //设置选项描述信息
                        scv_setting_changebg.setDes(items[which]);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("取消",null);//写null表示隐藏对话框
                builder.show();

            }
        });
    }

    //当界面可见的时候调用
    @Override
    protected void onStart() {
        super.onStart();
        address();
    }
    //当界面不可见得时候调用
    @Override
    protected void onStop() {
        super.onStop();
    }
    //当活动获取焦点的时候调用
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void address() {
        //回显操作，动态获取服务是否开启
        if (AddressUtils.isRunningService("com.itheima.mobliesafe75.service.AddressService",getApplicationContext())){
            //开启服务
            sv_setting_address.setChecked(true);
        }else{
            //关闭服务
            sv_setting_address.setChecked(false);
        }

        sv_setting_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, AddressService.class);

                if (sv_setting_address.isChecked()){
                    //去关闭服务
                    stopService(intent);
                    sv_setting_address.setChecked(false);
                }else{
                    //打开更新
                    startService(intent);
                    sv_setting_address.setChecked(true);
                }
            }
        });
    }

    private void update() {
        //sv_setting_update.setTitle("提示更新");
        //根据用户历史操作初始化控件的值
        if (sp.getBoolean("update",true)){
           // sv_setting_update.setDes("打开提示更新");
            sv_setting_update.setChecked(true);
        }else{
            //sv_setting_update.setDes("关闭提示更新");
            sv_setting_update.setChecked(false);
        }

        sv_setting_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = sp.edit();
                if(sv_setting_update.isChecked()){
                    //sv_setting_update.setDes("关闭提示更新");
                    sv_setting_update.setChecked(false);

                    //保存状态
                    edit.putBoolean("update",false);

                }else{
                   // sv_setting_update.setDes("打开提示更新");
                    sv_setting_update.setChecked(true);

                    edit.putBoolean("update",true);
                }
                edit.commit();
            }
        });
    }
}
