package com.itheima.mobliesafe75.engine;

/**
 * Created by jack_yzh on 2017/12/5.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  获取ContentResorver需要有上下文，所以先设置ContactEngine构造方法，用于在引用类中传入上下文
 */
public class ContactEngine {




    public static List<HashMap<String,String>> getAllContactInfo(Context context){
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        //1.获取内容解析者
        ContentResolver resolver = context.getContentResolver();
        //2.raw_contacts表的地址：raw_contacts
        //view_data表的地址：data
        //3.生成查询地址
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri data_uri = Uri.parse("content://com.android.contacts/data");
        //4.查询操作，先查询raw_contacts，查询contacts_id
        //projection:查询的字段
        Cursor cursor = resolver.query(raw_uri,new String[]{"contact_id"},null,null,null);
        //解析cursor
        while(cursor.moveToNext()){
            String contact_id = cursor.getString(0);
            Cursor c = resolver.query(data_uri,new String[]{"data1","mimetype"},"raw_contact_id=?",new String[]{contact_id},null);
            HashMap<String,String> map = new HashMap<String,String>();
            //解析c
            while(c.moveToNext()){
                String data1 = c.getString(0);
                String mimeType = c.getString(1);
                if (mimeType.equals("vnd.android.cursor.item/phone_v2")){
                    //电话
                    map.put("phone",data1);
                }else if(mimeType.equals("vnd.android.cursor.item/name")){
                    //姓名
                    map.put("name",data1);
                }
            }
            //添加到集合中
            list.add(map);
            //关闭Cursor
            c.close();
        }
        cursor.close();
        return list;
    }
}
