package com.itheima.mobliesafe75.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itheima.mobliesafe75.bean.BlackNumInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack_yzh on 2018/1/20.
 */

public class BlackNumDao {
    public static final int CALL = 0;
    public static final int SMS = 1;
    public static final int ALL = 2;
    private final BlackNumOpenHelper blackNumOpenHelper;

    //增删改查
    public BlackNumDao(Context context){
        blackNumOpenHelper = new BlackNumOpenHelper(context);
    }
    //添加操作
    public void  addBlackNum(String blacknum,int mode){
        //获取数据库
        SQLiteDatabase database = blackNumOpenHelper.getWritableDatabase();
        //添加操作
        ContentValues values = new ContentValues();
        values.put("blacknum",blacknum);
        values.put("mode",mode);
        database.insert(BlackNumOpenHelper.DB_NAME,null,values);
        //关闭数据库
        database.close();
    }
    //更新操作 更新黑名单拦截模式
    public void undateBlackNum(String blacknum,int mode){
        //获取数据库
        SQLiteDatabase database = blackNumOpenHelper.getWritableDatabase();
        //更新操作
        ContentValues values = new ContentValues();
        values.put("mode",mode);
        database.update(BlackNumOpenHelper.DB_NAME,values,"blacknum=?",new String[]{blacknum});
        database.close();
    }
    //通过黑名单号码查询拦截模式
    public int queryBlackNumMode(String blacknum){
        int mode = -1;
        //获取数据库
        SQLiteDatabase database = blackNumOpenHelper.getWritableDatabase();
        Cursor cursor = database.query(BlackNumOpenHelper.DB_NAME,new String[]{"mode"},"blacknum=?",new String[]{blacknum},null,null,null);
        if(cursor.moveToNext()){
            mode = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return mode;
    }
    //删除操作
    public void deleteBlackNum(String blacknum){
        SQLiteDatabase database = blackNumOpenHelper.getWritableDatabase();
        database.delete(BlackNumOpenHelper.DB_NAME,"blacknum=?",new String[]{blacknum});
        database.close();
    }
    /**
     * 查询全部黑名单
     */
    public List<BlackNumInfo> queryAllBlackNum(){
        List<BlackNumInfo> list = new ArrayList<BlackNumInfo>();
        //获取数据库
        SQLiteDatabase database = blackNumOpenHelper.getWritableDatabase();
        //查询
        Cursor cursor = database.query(BlackNumOpenHelper.DB_NAME,new String[]{"blacknum,mode"},null,null,null,null,null);
        while(cursor.moveToNext()){
            String blacknum = cursor.getString(0);
            int mode = cursor.getInt(1);
            BlackNumInfo blackNumInfo = new BlackNumInfo(blacknum,mode);
            list.add(blackNumInfo);
        }
        cursor.close();
        database.close();
        return list;
    }

}
