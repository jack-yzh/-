package com.itheima.mobliesafe75.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by jack_yzh on 2017/11/30.
 */

public class HomeTextView extends TextView {
    //在代码中调用
    public HomeTextView(Context context) {
        super(context);
    }
    //在布局文件中调用，布局文件经过系统编译成代码才会执行
    //AttributeSet 是布局的属性
    public HomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //多了个样式，很少使用
    public HomeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
        //return super.isFocused();
    }
}
