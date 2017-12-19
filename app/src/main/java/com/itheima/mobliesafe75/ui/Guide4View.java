package com.itheima.mobliesafe75.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.itheima.mobliesafe75.LostfindActivity;
import com.itheima.mobliesafe75.R;

/**
 * Created by jack_yzh on 2017/12/3.
 */

public class Guide4View extends GuideBaseActivity {

    private CheckBox cb_guideview4_protected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_view4);
        cb_guideview4_protected = (CheckBox)findViewById(R.id.cb_guideview4_protected);
        //根据保存的状态进行回显操作
        if (sp.getBoolean("protected",false)){
            //开启了防盗保护
            cb_guideview4_protected.setText("您已经开启了防盗保护");
            cb_guideview4_protected.setChecked(true);
        }else{
            cb_guideview4_protected.setText("您还没有开启防盗保护");
            cb_guideview4_protected.setChecked(false);
        }
        //checkbox状态改变时调用
        cb_guideview4_protected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                SharedPreferences.Editor edit = sp.edit();
                if (ischecked){
                    cb_guideview4_protected.setText("您已经开启了防盗保护");
                    cb_guideview4_protected.setChecked(true);
                    //保存选择状态
                    edit.putBoolean("protected",true);
                }else {
                    cb_guideview4_protected.setText("您还没有开启防盗保护");
                    cb_guideview4_protected.setChecked(false);
                    edit.putBoolean("protected",false);
                }
                edit.commit();
            }
        });
    }

    @Override
    public void next_activity() {
        //保存用户第一次进入向导界面的状态
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("first",false);
        edit.commit();
        //跳转界面
        Intent intent = new Intent(this, LostfindActivity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.guide_enter_next,R.anim.guide_exit_next);
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(this,Guide3View.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.guide_enter_pre,R.anim.guide_exit_pre);
    }
}
