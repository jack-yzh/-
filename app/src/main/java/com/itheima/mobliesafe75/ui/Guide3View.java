package com.itheima.mobliesafe75.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima.mobliesafe75.ContactActivity;
import com.itheima.mobliesafe75.R;

/**
 * Created by jack_yzh on 2017/12/3.
 */

public class Guide3View extends GuideBaseActivity {

    private EditText et_guideview3_safenum;
    private Button selectContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_view3);
        et_guideview3_safenum = (EditText)findViewById(R.id.et_guideview3_safenum);
        selectContacts = (Button) findViewById(R.id.button);
    }

    @Override
    public void next_activity() {
        //保存输入的安全号码
        //获取输入的号码
        String safenum = et_guideview3_safenum.getText().toString().trim();
        //判断号码是否为空
        if (TextUtils.isEmpty(safenum)){
            Toast.makeText(this, "请输入安全号码", Toast.LENGTH_SHORT).show();
            return;
        }
        //保存安全号码
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("safenum",safenum);
        edit.commit();
        //跳转到第四个页面
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

    //选择联系人
    public void selectContacts(View v){
        Intent intent = new Intent(this, ContactActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    //接收传递过来的数据
                    String num = data.getStringExtra("no");
                    et_guideview3_safenum.setText(num);
                }
                break;
            default:
        }
    }
}
