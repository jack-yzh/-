package com.itheima.mobliesafe75;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by jack_yzh on 2017/12/20.
 */

public class AToolsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }
    public void queryaddress(View view){
        Intent intent = new Intent(AToolsActivity.this, AddressActivity.class);
        startActivity(intent);
    }
}
