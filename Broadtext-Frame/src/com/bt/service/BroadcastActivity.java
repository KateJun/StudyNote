package com.bt.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.bt.wheelview.WheelViewTest;
import com.bt.R;

public class BroadcastActivity extends Activity implements OnClickListener {

	public final static String MYACTION = "com.bt.broadcast";

	private TextView tv_show;
	private UpdateDataService service;
	private boolean isBind = false;

	private AlarmManager am;

	private PendingIntent pendingIntent;

	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.broadcast_activity);
		findViewById(R.id.btn_send_broadcast).setOnClickListener(this);
		findViewById(R.id.btn_rigister).setOnClickListener(this);
		findViewById(R.id.btn_unrigister).setOnClickListener(this);

		tv_show = (TextView)findViewById(R.id.tv_show);
		tv_show.setText("");
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send_broadcast:
			sendBroadcast();
			break;

		case R.id.btn_rigister:
			bindService();
			break;

		case R.id.btn_unrigister:
			unBindService();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (am != null) {
			am.cancel(pendingIntent);
		}
		stopService(new Intent(MYACTION));

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
			Toast.makeText(this,"Cancel timer" , Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 发送广播,以及通知
	 */
	private void sendBroadcast(){
		final Intent  in = new Intent(this, BootBroadcastReciver.class);
		in.setAction(MYACTION);
		pendingIntent = PendingIntent.getBroadcast(this, 0, in, 0);
		// 发送即时广播
//		sendBroadcast(in);
		// 定时器1，每5s发送广播
		timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() { 
				sendBroadcast(in);
				
			}
		};
		timer.schedule(task, 5*1000,5*1000);
		//闹钟定时器2， 每5s发送一个广播
//		am = (AlarmManager)getSystemService(ALARM_SERVICE);   
//		am.setRepeating(AlarmManager.ELAPSED_REALTIME, 5*1000, 5*1000, pendingIntent);  
		 
		//发送通知
		sendNotification();
	}

	/**
	 * 发送通知
	 */
	private void sendNotification() {
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notifi = new Notification();
		// 接收时状态栏图标
		notifi.icon = R.drawable.ic_launcher;
		// 事件
		notifi.contentIntent = PendingIntent.getActivity(this, 0, new Intent(
				this, WheelViewTest.class), 0);
		// 接收时状态栏显示的title
		notifi.tickerText = "test";
		// 添加声音效果
		notifi.defaults |= Notification.DEFAULT_SOUND;
		// 添加震动,后来得知需要添加震动权限 : Virbate Permission
		// notifi.defaults |= Notification.DEFAULT_VIBRATE ;
		// 添加状态标志
		// FLAG_AUTO_CANCEL 该通知能被状态栏的清除按钮给清除掉
		// FLAG_NO_CLEAR 该通知能被状态栏的清除按钮给清除掉
		// FLAG_ONGOING_EVENT 通知放置在正在运行
		// FLAG_INSISTENT 通知的音乐效果一直播放
		notifi.flags = Notification.FLAG_AUTO_CANCEL;
		notifi.when = System.currentTimeMillis();
		// 1、创建一个自定义的消息布局 notification.xml
		// 2、在程序代码中使用RemoteViews的方法来定义image和text。然后把RemoteViews对象传到contentView字段
		RemoteViews remoteView = new RemoteViews(this.getPackageName(),
				R.layout.list_item_icon_text);
		remoteView.setImageViewResource(R.id.icon, R.drawable.btn_browser);
		remoteView.setTextViewText(R.id.text,
				"Hello,this message is in a custom expanded view");
		notifi.contentView = remoteView;

		manager.notify(0, notifi);
	}

	/**
	 * 启动服务
	 */
	private void bindService() {
		Intent serviceIntent = new Intent(this, UpdateService.class);
//		Intent serviceIntent = new Intent(this, UpdateDataService.class);
		// bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
		// isBind = true;
		serviceIntent.setAction(MYACTION);
		startService(serviceIntent);
	}

	/**
	 * 注销启动服务
	 */
	private void unBindService() {
		if (isBind) {
			unbindService(connection);
			isBind = false;
		}
		stopService(new Intent(MYACTION));

	}

	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			service = null;
			System.out.println("---onServiceDisConnect------");
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			service = new UpdateDataService();
			System.out.println("onServiceConnect------");
		}
	};

}
