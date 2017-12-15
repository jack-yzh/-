package com.itheima.mobliesafe75.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.itheima.mobliesafe75.LostfindActivity;
import com.itheima.mobliesafe75.R;

/**
 * Created by jack_yzh on 2017/12/3.
 */

public class Guide4View extends GuideBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_view4);
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
