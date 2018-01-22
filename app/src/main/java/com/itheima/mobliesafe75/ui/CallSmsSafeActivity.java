package com.itheima.mobliesafe75.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itheima.mobliesafe75.R;
import com.itheima.mobliesafe75.bean.BlackNumInfo;
import com.itheima.mobliesafe75.db.dao.BlackNumDao;
import com.itheima.mobliesafe75.utils.MyAsyncTask;

import java.util.List;

/**
 * Created by jack_yzh on 2018/1/20.
 */

public class CallSmsSafeActivity extends Activity {

    private ListView lv_callsmssafe_blacknums;
    private ProgressBar loading;
    private BlackNumDao blackNumDao;
    private List<BlackNumInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callsmssafe);
        blackNumDao = new BlackNumDao(getApplicationContext());
        lv_callsmssafe_blacknums = (ListView)findViewById(R.id.lv_callsmssafe_blacknums);
        loading = (ProgressBar)findViewById(R.id.loading);
        fillData();
    }

    /**
     * 异步加载数据
     */
    private void fillData() {
        new MyAsyncTask(){

            @Override
            public void preTask() {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void doinBack() {
                list = blackNumDao.queryAllBlackNum();
            }

            @Override
            public void postTask() {
                lv_callsmssafe_blacknums.setAdapter(new MyAdapter());
                loading.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }


    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BlackNumInfo blackNumInfo = list.get(position);
            View view = View.inflate(getApplicationContext(),R.layout.item_callsmssafe,null);
            TextView tv_itemcallsmssafe_blacknum = (TextView) view.findViewById(R.id.tv_itemcallsmssafe_blacknum);
            TextView tv_itemcallsmssafe_mode = (TextView) view.findViewById(R.id.tv_itemcallsmssafe_mode);
            ImageView iv_itemcallsmssafe_del = (ImageView) view.findViewById(R.id.iv_itemcallsmssafe_del);
            //设置显示数据
            tv_itemcallsmssafe_blacknum.setText(blackNumInfo.getBlacknum());
            int mode  = blackNumInfo.getMode();
            switch(mode){
                case 0:
                    tv_itemcallsmssafe_mode.setText("电话拦截");
                    break;
                case 1:
                    tv_itemcallsmssafe_mode.setText("短信拦截");
                    break;
                case 2:
                    tv_itemcallsmssafe_mode.setText("全部拦截");
                    break;
            }
            
            return null;
        }
    }

}
