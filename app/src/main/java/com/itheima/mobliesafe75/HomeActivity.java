package com.itheima.mobliesafe75;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {

	private GridView gv_home_gridview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//加载布局文件
		setContentView(R.layout.activity_home);
		gv_home_gridview =(GridView) findViewById(R.id.gv_home_girdview);
		gv_home_gridview.setAdapter(new Myadapter());
        gv_home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 8:
                        Intent intent = new Intent(HomeActivity.this,SettingActivity.class);
                        startActivity(intent);
                }
            }
        });

	}

	public class Myadapter extends BaseAdapter {

        int[] imageId = {R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,R.drawable.taskmanager,R.drawable.netmanager,
                R.drawable.trojan,R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings};
        String[] names = {"手机防盗","通讯卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"};

		@Override
		public int getCount() {
			return 9;
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
		//设置条目样式
		public View getView(int i, View view, ViewGroup viewGroup) {
			View item_view = View.inflate(getApplicationContext(),R.layout.item_home,null);
			ImageView iv_itemhome_icon = (ImageView) item_view.findViewById(R.id.iv_itemhom_icon);
            TextView tv_itemhome_text = (TextView) item_view.findViewById(R.id.tv_itemhome_text);
            iv_itemhome_icon.setImageResource(imageId[i]);
            tv_itemhome_text.setText(names[i]);
            return item_view;
		}
	}
	
}
