package com.itheima.mobliesafe75;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobliesafe75.db.dao.AddressDao;

/**
 * Created by jack_yzh on 2017/12/20.
 * 查找归属地
 */

public class AddressActivity extends Activity {
    private EditText et_address_queryphone;
    private TextView tv_address_queryaddress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        et_address_queryphone = (EditText)findViewById(R.id.et_address_queryphone);
        tv_address_queryaddress = (TextView)findViewById(R.id.tv_address_queryaddress);
        //自动监听输入框文本变化
        et_address_queryphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String phone = charSequence.toString();
                String queryAddress = AddressDao.queryAdress(phone,getApplicationContext());
                if (!queryAddress.isEmpty()){
                    tv_address_queryaddress.setText(queryAddress);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    //查询号码归属地
    public void query(View v){
        //1.获取输入的号码
        String phone = et_address_queryphone.getText().toString().trim();
        //2.判断号码是否为空
        if(TextUtils.isEmpty(phone)){
            //showToast("请输入要查询的号码");
            Toast.makeText(getApplicationContext(),"请输入要查询的号码",Toast.LENGTH_SHORT).show();
            //实现抖动的效果
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
//            shake.setInterpolator(new Interpolator() {
//                //用算法搞一个轨迹
//                @Override
//                public float getInterpolation(float v) {
//                    return 0;
//                }
//            });
            et_address_queryphone.startAnimation(shake);
            //实现震动
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(100);

            return;
        }else{
            //3.根据号码查询归属地
            String queryAddress = AddressDao.queryAdress(phone,getApplicationContext());
            //4.判断归属地是否为空
            if (!queryAddress.isEmpty()){
                tv_address_queryaddress.setText(queryAddress);
            }
        }
    }


}
