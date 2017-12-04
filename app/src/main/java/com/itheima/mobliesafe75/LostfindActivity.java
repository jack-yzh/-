package com.itheima.mobliesafe75;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.itheima.mobliesafe75.ui.GuideView;

/**
 * 手机防盗界面
 * Created by jack_yzh on 2017/12/3.
 */

public class LostfindActivity extends Activity {
    private SharedPreferences sp;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lostfind);
        //两部分显示1.第一次进入，设置防盗功能 2.第n次进入设置过的界面
        //判断是否第一次进入防盗界面
        sp = getSharedPreferences("config",MODE_PRIVATE);
        if (sp.getBoolean("first",true)){
            Intent intent = new Intent(LostfindActivity.this, GuideView.class);
            startActivity(intent);
            finish();
        }else{

        }
    }
}
