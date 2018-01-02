package com.itheima.mobliesafe75.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobliesafe75.R;

/**
 * Created by jack_yzh on 2017/11/30.
 * 自定义设置选择框界面2
 */

public class SettingClickView extends RelativeLayout {

    private TextView tv_setting_title;
    private TextView tv_setting_des;
    private ImageView iv_setting_entrance;

    public SettingClickView(Context context) {
        super(context);
        init();
    }
    //AttributeSet:保存有所有文件的属性
    public SettingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        //通过AttributeSet 获取属性个数
        //i0nt count = attrs.getAttributeCount();
        String title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","title");
        tv_setting_title.setText(title);

    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init(){
        //直接加载视图，指定当前内容
        View view = View.inflate(getContext(), R.layout.settingclickview,this);
        //给布局加载控件实例
        tv_setting_title = (TextView)view.findViewById(R.id.tv_setting_title);
        tv_setting_des = (TextView)view.findViewById(R.id.tv_setting_des);
        iv_setting_entrance = (ImageView) view.findViewById(R.id.iv_setting_entrance);


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





}
