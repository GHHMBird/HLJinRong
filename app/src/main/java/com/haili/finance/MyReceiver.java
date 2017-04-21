package com.haili.finance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.haili.finance.user.activity.UserMoreActivity;
import com.haili.finance.utils.StringUtils;
import com.haili.finance.utils.SystemUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/*
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			//send the Registration Id to your server...
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//        	processCustomMessage(context, bundle);
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			startApp(context, bundle);
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
			//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}
				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it = json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " + json.optString(myKey) + "]");
					}
				} catch (JSONException e) {

				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	//启动APP
	private void startApp(Context context, Bundle bundle) {
		//判断app进程是否存活
		if (SystemUtils.isAppAlive(context, "com.haili.finance")) {
			/*
			 * 如果存活的话，就直接启动DetailActivity，但要考虑一种情况，就是app的进程虽然仍然在但Task栈已经空了，
			 * 比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动DetailActivity,再按Back键就不会返回MainActivity了。
			 * 所以在启动DetailActivity前，要先启动MainActivity.
			 * 将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
			 * 如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
			 * 如果Task栈不存在MainActivity实例，则在栈顶创建
			 */

			String data = bundle.getString(JPushInterface.EXTRA_EXTRA);
			String uri = "";
			Intent detailIntent;
			try {
				JSONObject jsonObject = new JSONObject(data);
				uri = jsonObject.getString("url");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (uri.contains("http://")){
				detailIntent = new Intent(context,WebActivity.class);
				detailIntent.putExtra("params",uri);
			}else {
				detailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				detailIntent.putExtra("params", StringUtils.toStringHex(Uri.parse(uri).getQueryParameter("params")));
			}
			Intent mainIntent = new Intent(context,MainActivity.class);
			mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Intent[] intents = {mainIntent, detailIntent};
			context.startActivities(intents);

		} else {
			/*
			 * 如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，
			 * 参数经过SplashActivity传入MainActivity，此时app的初始化已经完成，
			 * 在MainActivity中就可以根据传入参数跳转到DetailActivity中去了
			 */
			Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.haili.finance");
			launchIntent.setFlags(
					Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			launchIntent.putExtra("launchBundle", bundle);
			context.startActivity(launchIntent);
		}

	}
}
