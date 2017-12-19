package com.itheima.mobliesafe75;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobliesafe75.ui.GuideView;

/**
 * 手机防盗界面
 * Created by jack_yzh on 2017/12/3.
 */

public class LostfindActivity extends Activity {
    private SharedPreferences sp;
    private TextView tv_lostfind_safenum;
    private ImageView iv_lostfind_protected;

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
            //setContentView(R.layout.activity_lostfind);
            tv_lostfind_safenum = (TextView)findViewById(R.id.tv_lostfind_safenum);
            iv_lostfind_protected = (ImageView)findViewById(R.id.iv_lostfind_protected);
            //根据保存的安全号码显示出来
            tv_lostfind_safenum.setText(sp.getString("safenum",""));
            //根据保存的状态显示图片
            boolean b = sp.getBoolean("protected",false);
            if (b){
                iv_lostfind_protected.setImageResource(R.drawable.lock);
            }else{
                iv_lostfind_protected.setImageResource(R.drawable.unlock);
            }
        }
    }

    public void turnGuideView(View v){
        Intent intent = new Intent(this,GuideView.class);
        startActivity(intent);
        finish();
    }
}
