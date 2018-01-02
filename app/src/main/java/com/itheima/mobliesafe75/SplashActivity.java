package com.itheima.mobliesafe75;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobliesafe75.utils.StreamUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.IOUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {

	public static final String TAG = "SplashActivity";
    protected static final int MSG_UPDATE_DIALOG = 1;
	protected static final int MSG_ENTER_HOME = 2;
	protected static final int MSG_SERVER_ERROR = 3;
	protected static final int MSG_URL_ERROR = 4;
	protected static final int MSG_IO_ERROR = 5;
	protected static final int MSG_JSON_ERROR = 6;
	private TextView tv_splash_versionname;
    private String code;
	private String apkurl;
	private String des;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_UPDATE_DIALOG:
				//弹出对话框
				showdialog();
				break;
			case MSG_ENTER_HOME:
				enterHome();
				break;
			case MSG_SERVER_ERROR:
				//连接失败,服务器出现异常
				Toast.makeText(getApplicationContext(), "服务器异常", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case MSG_IO_ERROR:
				Toast.makeText(getApplicationContext(), "亲,网络没有连接..", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case MSG_URL_ERROR:
				//方便我们后期定位异常
				Toast.makeText(getApplicationContext(), "错误号:"+MSG_URL_ERROR, Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case MSG_JSON_ERROR:
				Toast.makeText(getApplicationContext(), "错误号:"+MSG_JSON_ERROR, Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			}
		};
	};
	private TextView tv_spalsh_plan;
	private SharedPreferences sp;


	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		x.Ext.init(getApplication());
		x.Ext.setDebug(BuildConfig.DEBUG);
		sp = getSharedPreferences("config",MODE_PRIVATE);
        setContentView(R.layout.activity_splash);
       tv_splash_versionname = (TextView) findViewById(R.id.tv_splash_versionname);
       tv_spalsh_plan = (TextView) findViewById(R.id.tv_spalsh_plan);
       tv_splash_versionname.setText("版本号:"+getVersionName());

		if(sp.getBoolean("update",true)){
			update();
			//用户选择更新提醒
		}else{
			//不能在主线程睡眠的，主线程有渲染界面的作用SystemClock.sleep(2000);
			new Thread(){
				public void run(){
					SystemClock.sleep(2000);
					enterHome();
				}
			}.start();
			//如果用户取消更新提醒，直接跳转到主界面
		}
		copyDb();
//		//开启监听电话服务
//		Intent intent = new Intent(this, AddressService.class);
//		startService(intent);
    }
//拷贝库文件
	private void copyDb() {
		//创建新文件
		File file = new File(getFilesDir(),"address.db");
		//判断文件是否存在
		if (!file.exists()){
			//将文件从资源文件中拷贝出来
			AssetManager am = getAssets();
			InputStream is = null;
			FileOutputStream out = null;
			try {
				is = am.open("address.db");
				out = new FileOutputStream(file);
				byte[] b = new byte[1024];
				int len = -1;
				while((len = is.read(b))!=-1){
					out.write(b,0,len);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
//			is.close();
//			out.close();
				IOUtil.closeQuietly(is);
				IOUtil.closeQuietly(out);

			}
		}

	}

	/**
	 * 弹出对话框
	 */
	protected void showdialog() {
		AlertDialog.Builder builder = new Builder(this);
		//设置对话框不能消失
		builder.setCancelable(false);
		//设置对话框的标题
		builder.setTitle("新版本:"+code);
		//设置对话框的图标
		builder.setIcon(R.drawable.ic_launcher);
		//设置对话框的描述信息
		builder.setMessage(des);
		//设置升级取消按钮
		builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//3.下载最新版本
				download();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//1.隐藏对话框
				dialog.dismiss();
				//2.跳转到主界面
				enterHome();
			}
		});
		//显示对话框
		//builder.create().show();//两种方式效果一样
		builder.show();
	}
	/**
	 * 3.在最新版本
	 */
	protected void download() {
		RequestParams params = new RequestParams(apkurl);
//自定义保存路径，Environment.getExternalStorageDirectory()：SD卡的根目录
// params.setSaveFilePath(Environment.getExternalStorageDirectory()+"/myapp/");
		params.setSaveFilePath("/mnt/sdcard/mobliesafe75_2.apk");
//自动为文件命名
		params.setAutoRename(true);
		x.http().post(params, new Callback.ProgressCallback<File>() {
			@Override
			public void onSuccess(File result) {
				//apk下载完成后，调用系统的安装方法
				Toast.makeText(getApplicationContext(),"下载成功",Toast.LENGTH_SHORT).show();
					installAPK();
//				Intent intent = new Intent(Intent.ACTION_VIEW);
//				intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
//				startActivity(intent);
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				Toast.makeText(getApplicationContext(),"下载失败",Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onCancelled(CancelledException cex) {
			}
			@Override
			public void onFinished() {
			}
			//网络请求之前回调
			@Override
			public void onWaiting() {
			}
			//网络请求开始的时候回调
			@Override
			public void onStarted() {
			}
			//下载的时候不断回调的方法
			@Override
			public void onLoading(long total, long current, boolean isDownloading) {
				//当前进度和文件总大小
//				super.onLoading(total, current, isUploading);
				//设置显示下载进度的textview可见,同时设置相应的下载进度
					tv_spalsh_plan.setVisibility(View.VISIBLE);//设置控件是否可见
					tv_spalsh_plan.setText(current+"/"+total);//110/200

			}
		});
//		HttpUtils httpUtils = new HttpUtils();
//		//判断SD卡是否挂载
//		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//			//url : 新版本下载的路径 apkurl
//			//target : 保存新版本的目录
//			//callback : RequestCallBack
//			httpUtils.download(apkurl,"/mnt/sdcard/mobliesafe75_2.apk", new RequestCallBack<File>() {
//				//下载成功调用的方法
//				@Override
//				public void onSuccess(ResponseInfo<File> arg0) {
//					//4.安装最新版本
//					Toast.makeText(getApplicationContext(),"下载成功",Toast.LENGTH_SHORT).show();
//					installAPK();
//				}
//				//下载失败调用的方法
//				@Override
//				public void onFailure(HttpException arg0, String arg1) {
//					Toast.makeText(getApplicationContext(),"下载失败",Toast.LENGTH_SHORT).show();
//					// TODO Auto-generated method stub
//
//				}
//				//显示当前下载进度操作
//				//total : 下载总进度
//				//current : 下载的当前进度
//				//isUploading : 是否支持断点续传
//				@Override
//				public void onLoading(long total, long current, boolean isUploading) {
//					super.onLoading(total, current, isUploading);
//					//设置显示下载进度的textview可见,同时设置相应的下载进度
//					tv_spalsh_plan.setVisibility(View.VISIBLE);//设置控件是否可见
//					tv_spalsh_plan.setText(current+"/"+total);//110/200
//				}
//
//			});
//		}else{
//			Toast.makeText(getApplicationContext(),"挂载失败",Toast.LENGTH_SHORT).show();
//		}

	}
	/**
	 * 4.安装最新的版本
	 */
	protected void installAPK() {
		/**
		 *  <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:scheme="content" /> //content : 从内容提供者中获取数据  content://
                <data android:scheme="file" /> // file : 从文件中获取数据
                <data android:mimeType="application/vnd.android.package-archive" />
            </intent-filter>
		 */
		Intent intent = new Intent(Intent.ACTION_VIEW);
		//intent.setAction("android.intent.action.VIEW");
		//intent.addCategory("android.intent.category.DEFAULT");
		//单独设置会相互覆盖
		/*intent.setType("application/vnd.android.package-archive");
		intent.setData(Uri.fromFile(new File("/mnt/sdcard/mobliesafe75_2.apk")));*/
		intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/mobliesafe75_2.apk")), "application/vnd.android.package-archive");
		//在当前activity退出的时候,会调用之前的activity的onActivityResult方法
		//requestCode : 请求码,用来标示是从哪个activity跳转过来
		//ABC  a -> c    b-> c  ,c区分intent是从哪个activity传递过来的,这时候就要用到请求码
		startActivityForResult(intent, 0);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		enterHome();
	}
	/**
	 * 跳转到主界面
	 */
	protected void enterHome() {
		Intent intent = new Intent(this,HomeActivity.class);
		startActivity(intent);
		//移出splash界面
		finish();
	}
	/**
	 * 提醒用户更新版本
	 */
    private void update() {
		//1.连接服务器,查看是否有最新版本,  联网操作,耗时操作,4.0以后不允许在主线程中执行的,放到子线程中执行
    	new Thread(){

			private int startTime;

			public void run() {
				Message message = Message.obtain();
				//在连接之前获取一个时间
				startTime = (int) System.currentTimeMillis();
    			try {
    				//1.1连接服务器
        			//1.1.1设置连接路径
        			//spec:连接路径
					URL url = new URL("http://10.0.2.2:8080/message.html");
					Log.d(TAG, "取得URL");
					//1.1.2获取连接操作
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();//http协议,httpClient
					//1.1.3设置超时时间
					conn.setConnectTimeout(5000);//设置连接超时时间
					//conn.setReadTimeout(5000);
					Log.d(TAG, "设置超时时间");
					//conn.setReadTimeout(5000);//设置读取超时时间
					//1.1.4设置请求方式
					conn.setRequestMethod("GET");//post
					//1.1.5获取服务器返回的状态码,200,404,500
					Log.d(TAG, "请求方式");
					int responseCode = conn.getResponseCode();

					Log.d(TAG, "回复码");
					if (responseCode == 200) {
						//连接成功,获取服务器返回的数据,code : 新版本的版本号     apkurl:新版本的下载路径      des:描述信息,告诉用户增加了哪些功能,修改那些bug
						//获取数据之前,服务器是如何封装数据xml  json
						System.out.println("连接成功.....");
						//获取服务器返回的流信息
						InputStream in = conn.getInputStream();
						//将获取到的流信息转化成字符串
						String json = StreamUtil.parserStreamUtil(in);
						//解析json数据
						JSONObject jsonObject = new JSONObject(json);
						//获取数据
						code = jsonObject.getString("code");
						apkurl = jsonObject.getString("apkurl");
						des = jsonObject.getString("des");
						System.out.println("code:"+code+"   apkurl:"+apkurl+"   des:"+des);
						//1.2查看是否有最新版本
						//判断服务器返回的新版本版本号和当前应用程序的版本号是否一致,一致表示没有最新版本,不一致表示有最新版本
						if (code.equals(getVersionName())) {
							//没有最新版本
							message.what = MSG_ENTER_HOME;
						}else{
							//有最新版本
							//2.弹出对话框,提醒用户更新版本
							message.what = MSG_UPDATE_DIALOG;
						}
					}else{
						//连接失败
						System.out.println("连接失败.....");
						message.what=MSG_SERVER_ERROR;
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					message.what = MSG_URL_ERROR;
				} catch (IOException e) {
					e.printStackTrace();
					message.what = MSG_IO_ERROR;
				} catch (JSONException e) {
					e.printStackTrace();
					message.what = MSG_JSON_ERROR;
				}finally{//不管有没有异常都会执行
					//处理连接外网连接时间的问题
					//在连接成功之后在去获取一个时间
					int endTime = (int) System.currentTimeMillis();
					//比较两个时间的时间差,如果小于两秒,睡两秒,大于两秒,不睡
					int dTime = endTime-startTime;
					if (dTime<2000) {
						//睡两秒钟
						SystemClock.sleep(2000-dTime);//始终都是睡两秒钟的时间
					}
					handler.sendMessage(message);
				}
    		};
    	}.start();
	}
	/**
     * 获取当前应用程序的版本号
     * @return
     */
    private String getVersionName(){
    	//包的管理者,获取清单文件中的所有信息
    	PackageManager pm = getPackageManager();
    	try {
    		//根据包名获取清单文件中的信息,其实就是返回一个保存有清单文件信息的javabean
    		//packageName :应用程序的包名
        	//flags : 指定信息的标签,0:获取基础的信息,比如包名,版本号,要想获取权限等等信息,必须通过标签来指定,才能去获取
        	//GET_PERMISSIONS : 标签的含义:处理获取基础信息之外,还会额外获取权限的信息
    		//getPackageName() : 获取当前应用程序的包名
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			//获取版本号名称
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			//包名找不到的异常
			e.printStackTrace();
		}
		return null;
    }
}
