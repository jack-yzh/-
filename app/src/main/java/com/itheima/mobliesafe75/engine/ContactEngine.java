package com.itheima.mobliesafe75.engine;

/**
 * Created by jack_yzh on 2017/12/5.
 */

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  获取ContentResorver需要有上下文，所以先设置ContactEngine构造方法，用于在引用类中传入上下文
 */
public class ContactEngine {

    public static List<HashMap<String,String>> getAllContactInfo(Context context){
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

            //联系人的Uri，也就是content://com.android.contacts/contacts
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            //指定获取_id和display_name两列数据，display_name即为姓名
            String[] projection = new String[] {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            //根据Uri查询相应的ContentProvider，cursor为获取到的数据集
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            //String[] arr = new String[cursor.getCount()];

            int i = 0;
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    //一定要在循环体里面创建一个map对象，否则会出现后面数据覆盖前面数据的问题!!!
                    HashMap<String,String> map = new HashMap<String,String>();
                    //获取姓名
                    String name = cursor.getString(1);
                    //指定获取NUMBER这一列数据
                    String[] phoneProjection = new String[] {
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    };
                            map.put("name",name);
                    //根据联系人的ID获取此人的电话号码
                    Long id = cursor.getLong(0);
                    Cursor phonesCusor = context.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            phoneProjection,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                            null,
                            null);

                    //因为每个联系人可能有多个电话号码，所以需要遍历
                    if (phonesCusor != null && phonesCusor.moveToFirst()) {
                        do {
                            String num = phonesCusor.getString(0);
                            map.put("phone",num);
                        }while (phonesCusor.moveToNext());
                    }
                    System.out.println(map.get("name"));
                    System.out.println(map.get("phone"));
                    list.add(map);
                    i++;
                }while (cursor.moveToNext());
                //cursor.close();
            }
            return list;
        }



//        //1.获取内容解析者
//        ContentResolver resolver = context.getContentResolver();
//        //2.raw_contacts表的地址：raw_contacts
//        //view_data表的地址：data
//        //3.生成查询地址
//        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
//        Uri data_uri = Uri.parse("content://com.android.contacts/data");
//        //4.查询操作，先查询raw_contacts，查询contacts_id
//        //projection:查询的字段
//        Cursor cursor = resolver.query(raw_uri,new String[]{"contact_id"},null,null,null);
//        //解析cursor
//        while(cursor.moveToNext()){
//            String contact_id = cursor.getString(0);
//            //
//            if(!TextUtils.isEmpty(contact_id)){
//            Cursor c = resolver.query(data_uri,new String[]{"data1","mimetype"},"raw_contact_id=?",new String[]{contact_id},null);
//            HashMap<String,String> map = new HashMap<String,String>();
//            //解析c
//            while(c.moveToNext()){
//                String data1 = c.getString(0);
//                String mimeType = c.getString(1);
//                if (mimeType.equals("vnd.android.cursor.item/phone_v2")){
//                    //电话
//                    map.put("phone",data1);
//                }else if(mimeType.equals("vnd.android.cursor.item/name")){
//                    //姓名
//                    map.put("name",data1);
//                }
//            }
//            //添加到集合中
//            list.add(map);
//            //关闭Cursor
//            c.close();
//            }
//        }
//        cursor.close();
//        return list;
    }
