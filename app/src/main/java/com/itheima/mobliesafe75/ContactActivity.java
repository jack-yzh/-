package com.itheima.mobliesafe75;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itheima.mobliesafe75.engine.ContactEngine;
import com.itheima.mobliesafe75.utils.MyAsyncTask;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jack_yzh on 2017/12/8.
 */

public class ContactActivity extends Activity {

    //ArrayAdapter是系统定义好的适配器，直接拿来用
    //ArrayAdapter<HashMap<String,String>> adapter;
    List<HashMap<String, String>> contactsList;
    //注解控件
    @ViewInject(R.id.loading)
    private ProgressBar loading;
    @ViewInject(R.id.lv_acContact_contacts)
    private ListView lv_acContact_contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        //新的注解方式来一键获取所有空间实例
        x.view().inject(this);

        new MyAsyncTask(){

            @Override
            public void preTask() {}

            @Override
            public void doinBack() {
                //模拟查询耗时操作
                //SystemClock.sleep(2000);
                //传递Context获取联系人列表
                contactsList = new ContactEngine().getAllContactInfo(getApplicationContext());
            }

            @Override
            public void postTask() {
                lv_acContact_contacts.setAdapter(new Myadapter());
                //隐藏进度条
                loading.setVisibility(View.INVISIBLE);
            }
        }.execute();

//        /**
//         * android自带异步加载框架
//         * 1.
//         */
//        new AsyncTask<Params, Progress, Result>(){
//            @Override
//            protected String doInBackground(String... strings) {
//                return null;
//            }
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//            }
//        }.execute();

        lv_acContact_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //移除选择号码界面
                Intent intent = new Intent();
                //intent.putExtra("name",contactsList.get(i).get("name"));
                intent.putExtra("no",contactsList.get(i).get("phone"));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return contactsList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View v, ViewGroup viewGroup) {
            View view = View.inflate(getApplicationContext(),R.layout.item_contact,null);
            //初始化控件
            TextView tv_itemcontact_name = (TextView) view.findViewById(R.id.tv_itemcontact_name);
            TextView tv_itemcontact_phone = (TextView) view.findViewById(R.id.tv_itemcontact_phone);
            //设置控件值
            tv_itemcontact_name.setText(contactsList.get(i).get("name"));
            tv_itemcontact_phone.setText(contactsList.get(i).get("phone"));
            return view;
        }
    }


}
