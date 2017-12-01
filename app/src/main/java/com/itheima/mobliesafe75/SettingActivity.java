package com.itheima.mobliesafe75;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobliesafe75.ui.SettingView;

/**
 * Created by jack_yzh on 2017/11/30.
 */

public class SettingActivity extends Activity {

    private SettingView sv_setting_update;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //存储用户信息
        sp = getSharedPreferences("config",MODE_PRIVATE);
        //获取对象实例
        sv_setting_update = (SettingView)findViewById(R.id.sv_setting_update);
        sv_setting_update.setTitle("提示更新");
        //根据用户历史操作初始化控件的值
        if (sp.getBoolean("update",true)){
            sv_setting_update.setDes("打开提示更新");
            sv_setting_update.setChecked(true);
        }else{
            sv_setting_update.setDes("关闭提示更新");
            sv_setting_update.setChecked(false);
        }

        sv_setting_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = sp.edit();
                if(sv_setting_update.isChecked()){
                    sv_setting_update.setDes("关闭提示更新");
                    sv_setting_update.setChecked(false);

                    //保存状态
                    edit.putBoolean("update",false);

                }else{
                    sv_setting_update.setDes("打开提示更新");
                    sv_setting_update.setChecked(true);

                    edit.putBoolean("update",true);
                }
                edit.commit();
            }
        });

    }
}
