package com.itheima.mobliesafe75;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobliesafe75.utils.MD5Util;

public class HomeActivity extends Activity {

	private GridView gv_home_gridview;
	private Dialog dialog;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//加载布局文件
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("config",MODE_PRIVATE);

		gv_home_gridview =(GridView) findViewById(R.id.gv_home_girdview);
		gv_home_gridview.setAdapter(new Myadapter());
        gv_home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
					case 0:
						//判断用户是否设置过密码
						if(TextUtils.isEmpty(sp.getString("password",""))){
							ShowSetPasswordDialog();
						}else{
							showEnterPasswordDialog();
						}

						break;
                    case 8:
                        Intent intent = new Intent(HomeActivity.this,SettingActivity.class);
                        startActivity(intent);
						break;
                }
            }
        });

	}
	//输入密码对话框
	private void showEnterPasswordDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		View view = View.inflate(getApplicationContext(),R.layout.dialog_enterpassword,null);
		//从布局文件里面找控件
		final EditText et_setpassword = (EditText) view.findViewById(R.id.et_setpassword);
		Button btn_ok = (Button)view.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
		final ImageView iv_enterPs_hide = (ImageView)view.findViewById(R.id.iv_enterPs_hide);
		final ImageView iv_enterPs_display = (ImageView)view.findViewById(R.id.iv_enterPs_display);
		final Boolean[] dis_hide = {false};
		//设置 隐藏 显示 输入密码
		iv_enterPs_hide.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//隐藏显示密码
				if (dis_hide[0]){
					//隐藏密码
					et_setpassword.setInputType(129);
					iv_enterPs_hide.setVisibility(View.VISIBLE);
					iv_enterPs_display.setVisibility(View.GONE);
				}else{
					//显示密码 0,129表示输入类型的系统id
					et_setpassword.setInputType(0);
					iv_enterPs_hide.setVisibility(View.GONE);
					iv_enterPs_display.setVisibility(View.VISIBLE);
				}
				dis_hide[0] = true;
			}
		});
		iv_enterPs_display.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//隐藏显示密码
				if (dis_hide[0]){
					//隐藏密码
					et_setpassword.setInputType(129);
					iv_enterPs_hide.setVisibility(View.VISIBLE);
					iv_enterPs_display.setVisibility(View.GONE);
				}else{
					//显示密码 0,129表示输入类型的系统id
					et_setpassword.setInputType(0);
					iv_enterPs_hide.setVisibility(View.GONE);
					iv_enterPs_display.setVisibility(View.VISIBLE);
				}
				dis_hide[0] = false;
			}
		});

		//确定密码操作
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String password = et_setpassword.getText().toString().trim();
				if (TextUtils.isEmpty(password)){
					Toast.makeText(getApplicationContext(),"请输入密码",Toast.LENGTH_SHORT).show();
					return;
				}
				//sp.getString("password","");
				if(MD5Util.passwordMD5(password).equals(sp.getString("password",""))){
					//进入手机防盗界面
					dialog.dismiss();
					Toast.makeText(getApplicationContext(),"密码正确",Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(HomeActivity.this,LostfindActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
				}
			}
		});
		//取消密码操作
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});

		builder.setView(view);
		dialog = builder.create();
		dialog.show();

	}


	//设置密码对话框
	private void ShowSetPasswordDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//builder.setCancelable(false);
		//点击空白不能取消对话框
		//将布局文件转化成View对象
		View view = View.inflate(getApplicationContext(),R.layout.dialog_setpassword,null);
		final EditText et_setpassword = (EditText) view.findViewById(R.id.et_setpassword);
		final EditText et_setpassword_confirm = (EditText) view.findViewById(R.id.et_setpassword_confirm);
		Button btn_ok = (Button)view.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//获取输入框内容,trim()去掉首位空格
				String password = et_setpassword.getText().toString().trim();
				if (TextUtils.isEmpty(password)){
					Toast.makeText(getApplicationContext(),"请输入密码",Toast.LENGTH_SHORT).show();
					return;
				}
				String confirm_password = et_setpassword_confirm.getText().toString().trim();
				if (password.equals(confirm_password)){
					//保存密码 隐藏对话框
					SharedPreferences.Editor edit = sp.edit();
					//直接把密码转换成密文存入内存
					edit.putString("password", MD5Util.passwordMD5(password));
					edit.commit();
					dialog.dismiss();
					Toast.makeText(getApplicationContext(),"密码设置成功",Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(getApplicationContext(),"密码设置不一致",Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.setView(view);
		//builder.show();
		//builder.create();会产生一个dialog对象 为了dismiss()对话框，所以定义dialog
		dialog = builder.create();
		dialog.show();
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
