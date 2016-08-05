package com.bt.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;


/**
 * 定时更新数据
 * @author XJ
 *
 */
public class UpdateDataService extends Service {

	private Timer timer;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new Binder();
	}

	//第一次使用startService时调用，只执行一次
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		System.out.println("create  service/////////////////");
		timerTask();
		super.onCreate();
	}
	//多次调用startService时 ，startId不同
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		System.out.println("start  service------------"+intent.getAction()+flags+startId);
//		timerTask();
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * 定时发送广播
	 * @param in
	 */
	private void timerTask(){
		final Intent  in = new Intent(this, BootBroadcastReciver.class);
		in.setAction(BroadcastActivity.MYACTION);

        timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				sendBroadcast(in);
			}
		};
		//定时更新数据
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date time = calendar.getTime();
		timer.schedule(task, time, 10 * 1000);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
			Toast.makeText(this, "Service cancel timer", Toast.LENGTH_SHORT).show();
		}
	}
	
}
