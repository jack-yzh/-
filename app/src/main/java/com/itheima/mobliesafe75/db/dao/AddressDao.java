package com.itheima.mobliesafe75.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by jack_yzh on 2017/12/20.
 */

public class AddressDao {

    private static Cursor cursor;

    /**
     * 打开数据库，查询号码
     * @param num
     * @return
     */
    public static String queryAdress(String num,Context context){
        String location = "";
        //打开数据库的路径
        File file = new File(context.getFilesDir(),"address.db");
        //打开数据库
        SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getAbsolutePath(),null,SQLiteDatabase.OPEN_READONLY);

        //正则表达式
        if (num.matches("^1[34578]\\d{9}$")){
            //substring:包含头不包含尾部
            cursor = database.rawQuery("select location from data2 where id = (select outkey from data1 where id = ?)",new String[]{num.substring(0,7)});
            if (cursor.moveToNext()){
                location = cursor.getString(0);
            }
            //关闭数据库,光标
            cursor.close();
        }else{
            //把特殊的电话列出来
            switch (num.length()){
                case 3://110 120 119 911
                    location = "特殊电话";
                    break;
                case 4://5554 5556
                    location = "虚拟电话";
                    break;
                case 5://10086 10010 10000
                    location = "客服电话";
                    break;
                case 7://座机
                case 8:
                    location = "本地电话";
                    break;
                default:
                    //长途电话
                    if (num.length()>=10 && num.startsWith("0")){
                        //获取三位的区号
                        String result = num.substring(1,3);
                        Cursor cursor = database.rawQuery("select location from data2 where area = ?",new String[]{result});
                        if (cursor.moveToNext()){
                            location = cursor.getString(0);
                            cursor.close();
                        }else{
                            //获取四位的区号
                            result = num.substring(1,4);
                            cursor = database.rawQuery("select location from data2 where area = ?",new String[]{result});
                            if (cursor.moveToNext()){
                                location = cursor.getString(0);
                                cursor.close();
                            }
                        }
                    }

            }
        }
        //解析cursor

        database.close();
        return location;
    }
}
