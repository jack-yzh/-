package com.itheima.mobliesafe75.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jack_yzh on 2017/12/31.
 */

public class BlackNumOpenHelper extends SQLiteOpenHelper {
    //为了以后实现数据库操作能方便修改表名
    public static final String DB_NAME = "info";
    /**
     *
     * @param context 上下文
     *  name 数据库名
     *  factory 一般是null
     *  version 数据库版本号
     */
    public BlackNumOpenHelper(Context context) {
        super(context,"blacknum.db", null, 1);
    }
    //第一次创建数据库调用,创建表结构
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表结构 字段  blccknum:黑名单号码  mode: 拦截类型
        db.execSQL("create table "+DB_NAME+"(_id integer primary key autoincrement,blacknum varchar(20),mode varchar(2))");

    }
    //数据库变化时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
