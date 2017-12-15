package com.itheima.mobliesafe75.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.itheima.mobliesafe75.R;

/**
 * Created by jack_yzh on 2017/12/3.
 */

public class Guide2View extends GuideBaseActivity {

    private SettingView sv_guideview2_sim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_view2);
        //获取settingView实例，获取点击事件，获取sim卡序列号
        sv_guideview2_sim = (SettingView) findViewById(R.id.sv_guideview2_sim);
        final SharedPreferences.Editor editor = sp.edit();
        sv_guideview2_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sv_guideview2_sim.isChecked()){
                    //解绑,把空字符串放入，所以就可以认为解绑
                    editor.putString("sim","");
                    sv_guideview2_sim.setChecked(false);
                }else{
                    //绑定SIM卡
                    TelephonyManager tel = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
                    String sim = tel.getSimSerialNumber();//获取SIM卡序列号
                    //保存序列号，然后提交
                    editor.putString("sim",sim);
                    //设置状态为已经保存
                    sv_guideview2_sim.setChecked(true);
                }
                editor.commit();
            }
        });
    }


    @Override
    public void next_activity() {
        if (sv_guideview2_sim.isChecked()){
            Intent intent = new Intent(this,Guide3View.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.guide_enter_next,R.anim.guide_exit_next);
        }else{
            Toast.makeText(this, "请绑定SIM卡", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void pre_activity() {
        Intent intent = new Intent(this, GuideView.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.guide_enter_pre,R.anim.guide_exit_pre);
    }
}
