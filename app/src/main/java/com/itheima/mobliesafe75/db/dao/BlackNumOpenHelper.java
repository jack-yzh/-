package com.itheima.mobliesafe75.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jack_yzh on 2017/12/31.
 */

public class BlackNumOpenHelper extends SQLiteOpenHelper {

    /**
     *
     * @param context 上下文
     * @param name 数据库名
     * @param factory 一般是null
     * @param version 数据库版本号
     */
    public BlackNumOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,"balcknum.db", null, 1);
    }
    //第一次创建数据库调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表结构 字段  blccknum:黑名单号码  mode: 拦截类型

    }
    //数据库变化是调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
