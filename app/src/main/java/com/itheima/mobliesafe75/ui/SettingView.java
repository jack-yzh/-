package com.itheima.mobliesafe75.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobliesafe75.R;

/**
 * Created by jack_yzh on 2017/11/30.
 */

public class SettingView extends RelativeLayout {

    private TextView tv_setting_title;
    private TextView tv_setting_des;
    private CheckBox cb_setting_update;
    private String des_on;
    private String des_off;

    public SettingView(Context context) {
        super(context);
        init();
    }
    //AttributeSet:保存有所有文件的属性
    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        //通过AttributeSet 获取属性个数
        //i0nt count = attrs.getAttributeCount();
        String title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","title");
        des_on = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","des_on");
        des_off = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","des_off");
        tv_setting_title.setText(title);
        if (isChecked()){
            tv_setting_des.setText(des_on);
        }else{
            tv_setting_des.setText(des_off);
        }


    }

    public SettingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init(){
        //直接加载视图，指定当前内容
        View view = View.inflate(getContext(), R.layout.settingview,this);
        //给布局加载控件实例
        tv_setting_title = (TextView)view.findViewById(R.id.tv_setting_title);
        tv_setting_des = (TextView)view.findViewById(R.id.tv_setting_des);
        cb_setting_update = (CheckBox)view.findViewById(R.id.cb_setting_checkBox);

        //View view = View.inflate(getContext(), R.layout.settingview,null);
        //this.addView(view);先创建加载视图实例，再给当前内容添加视图
    }

    /**
     * 设置标题方法
     * @param title
     */
    public void setTitle(String title){
        tv_setting_title.setText(title);
    }

    /**
     * 设置描述信息
     * @param des
     */
    public void setDes(String des){
        tv_setting_des.setText(des);
    }


    public boolean isChecked(){
        return cb_setting_update.isChecked();
        //获取checkBox的状态
    }
    /**
     * 确认是否被选中
     * @param isChecked
     * @return
     */
    public void setChecked(boolean isChecked){
        cb_setting_update.setChecked(isChecked);
        if (isChecked()){
            tv_setting_des.setText(des_on);
        }else{
            tv_setting_des.setText(des_off);
        }
    }

}
