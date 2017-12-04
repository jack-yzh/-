package com.itheima.mobliesafe75.ui;

import android.content.Intent;
import android.os.Bundle;

import com.itheima.mobliesafe75.R;

/**
 * Created by jack_yzh on 2017/12/3.
 */

public class Guide3View extends GuideBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_view3);
    }

    @Override
    public void next_activity() {
        Intent intent = new Intent(this,Guide4View.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.guide_enter_next,R.anim.guide_exit_next);
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(this,Guide2View.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.guide_enter_pre,R.anim.guide_exit_pre);
    }
}
