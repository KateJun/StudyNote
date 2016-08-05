package com.bt.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.bt.listview.MainActivity;
import com.bt.photo.util.DownLoadUtil;
import com.bt.R;


import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.RemoteViews;

public class UpdateService extends Service {

	private final int DOWNLOAD = 0x001;
	private final int DOWNLOAD_FINISH = 0x002;
	private final int DOWNLOAD_FAILURE = 0x003;
	private Context context;
	private String url = null;
	private String urlAPK = null;
	private String mVersionName;
	private int progress;
	private String mSavePath;
	private NotificationManager manager;
	private Notification notifi;
	private PendingIntent updatePendingIntent;
	private RemoteViews remoteView;
	private downloadApkThread thread;

	private final String PROPERTY = "PROPERTY";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// 第一次使用startService时调用，只执行一次
	@Override
	public void onCreate() {
		System.out.println("create  service/////////////////");
		init();
		super.onCreate();
	}

	// 多次调用startService时 ，startId不同
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		System.out.println("start  service------------" + intent.getAction()
				+ flags + startId);
		if (thread == null || !thread.isAlive() || thread.isInterrupted()) {
			setVisibility(true);
			sendNotification();
			thread = new downloadApkThread();
			thread.start();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	 @Override
	public void onDestroy() {
		System.out.println("stop service=================");
		if (thread != null && thread.isAlive()) {
			thread.interrupt();
			
		}
		super.onDestroy();
	}
	 
	private void init() {
		context = getApplicationContext();
		mSavePath = DownLoadUtil.getFilePath(getApplicationContext(),
				PROPERTY);
		initNotification();
	}

	private void initNotification(){
		manager = (NotificationManager) (context.getSystemService("notification"));
		//高级写法
//		Notification.Builder  builder = new Builder(context);
//		builder.setContent(remoteView);
//		notifi = builder.build();
		notifi = new Notification();
		notifi.icon = R.drawable.icon_lighting_on;
		Intent updateIntent = new Intent(context, MainActivity.class);
		updatePendingIntent = PendingIntent.getActivity(context, 0,updateIntent, 0);
		notifi.contentIntent = updatePendingIntent;
		notifi.tickerText = "开始下载";
		notifi.flags = Notification.FLAG_NO_CLEAR;
		remoteView = new RemoteViews(context.getPackageName(),
				R.layout.download_notification);
		remoteView.setTextViewText(R.id.tv_progress, "0%");
		remoteView.setProgressBar(R.id.progress, 100, 0, false);

		// 设置按钮点击事件，这里要放一个PendingIntent ，
		// 注意只有在在sdk3.0以上的系统中，通知栏中的按钮点击事件才能响应，
		// 这里最好加个条件，sdk3.0以下，不显示按钮
		// if (android.os.Build.VERSION.SDK_INT >= 11) {
		// remoteView.setOnClickPendingIntent(R.id.icon_notifi, null);
		// remoteView.setViewVisibility(R.id.icon_notifi, View.VISIBLE);
		// }

		notifi.contentView = remoteView;
	}
	/**
	 * 发送通知
	 */
	private void sendNotification() {
		manager.notify(0, notifi);

	}

	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			System.out.println("启动线程下载===================");
			loadAPK();
		}
	};

	/**
	 * 下载文件线程
	 */
	private void loadAPK() {
		try {
			// URL url = new URL(urlAPK+updateEntity); // 服务器上返回的url
			URL url = new URL("http://bcs.91rb.com/rbreszy/android/soft/2014/5/4/88ec1545f56249dc965bc196c6f17602/com.ting.mp3.android_59_4.7.0_635348005572777418.apk");
			// 创建连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			// 创建输入流
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			// 获取文件大小
			int length = conn.getContentLength();
			System.out.println("length :" + length);

			File file = new File(mSavePath);
			// 判断文件目录是否存在
			if (!file.exists()) {
				file.mkdir();
			}
			 
			File apkFile = new File(mSavePath, PROPERTY+mVersionName+".apk");
			if (!apkFile.exists()) {
				apkFile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(apkFile);
			// 缓存
			byte buf[] = new byte[1024];
			// 写入到文件中
			int count = 0;
			int numread;
			int downloadCount = 0 ;
			while ((numread = bis.read(buf)) != -1) {
				fos.write(buf, 0, numread);
				count += numread;
				// 计算进度条位置
				progress =  count * 100 / length;
				// 更新进度
				if (progress - 1 >= downloadCount) {
					downloadCount += 1;
				    mHandler.sendEmptyMessage(DOWNLOAD);
				}
			}

			fos.close();
			is.close();
//			if (progress == 100) {
				// 下载完成
				mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
//			} else {
//				// 下载失败
//				mHandler.sendEmptyMessage(DOWNLOAD_FAILURE);
//			}

		} catch (MalformedURLException e) {
			// 下载失败
			mHandler.sendEmptyMessage(DOWNLOAD_FAILURE);
			e.printStackTrace();
		} catch (IOException e) {
			// 下载失败
			mHandler.sendEmptyMessage(DOWNLOAD_FAILURE);
			e.printStackTrace();
		}
	}

	/**
	 * 进度条更新
	 */
	private  Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				remoteView.setTextViewText(R.id.tv_progress, progress + "%");
				remoteView.setProgressBar(R.id.progress, 100, progress, false);
				sendNotification();
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				stopSelf();
				break;
			case DOWNLOAD_FAILURE:
				setVisibility(false);
				//下载失败
				downLoadFailure();
				break;
			default:
				stopSelf();
				break;
			}
		};
	};

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, PROPERTY + mVersionName
				+ ".apk");
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 解决4.0以上安装闪退问题
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		updatePendingIntent = PendingIntent.getActivity(context, 0, i, 0);
		notifi.defaults |= Notification.DEFAULT_SOUND;
		notifi.flags = Notification.FLAG_AUTO_CANCEL;
		notifi.contentView = null;
		notifi.setLatestEventInfo(context, "测试更新", "下载完成,点击安装。",updatePendingIntent);
		sendNotification();
		// context.startActivity(i);
	}
	
	/**
	 * 下载失败 ，
	 * 发送广播，重新开下载线程。
	 */
	private void downLoadFailure(){
		Intent i = new Intent(context,BootBroadcastReciver.class);
		i.setAction("com.bt.broadcast");
//		notifi.flags = Notification.FLAG_AUTO_CANCEL;
		updatePendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
//		notifi.setLatestEventInfo(context, "上汽新闻", "下载失败。",updatePendingIntent);
		if (Build.VERSION.SDK_INT >= 11) {
			remoteView.setOnClickPendingIntent(R.id.btn_reload, updatePendingIntent);
		}
		sendNotification();
		File apkfile = new File(mSavePath, PROPERTY + mVersionName + ".apk");
		if (apkfile.exists()) {
			apkfile.delete();
		}
	}
	
	/**
	 * 下载成功或失败控制View显隐
	 * @param isVisible,true is downloading show
	 */
	private void setVisibility(boolean  isVisible){
		if (isVisible) {
			remoteView.setViewVisibility(R.id.tv_title, View.VISIBLE);
			remoteView.setViewVisibility(R.id.tv_progress, View.VISIBLE);
			remoteView.setViewVisibility(R.id.progress, View.VISIBLE);
			remoteView.setViewVisibility(R.id.btn_reload, View.GONE);
			
			remoteView.setTextViewText(R.id.tv_progress, "0%");
			remoteView.setProgressBar(R.id.progress, 100, 0, false);
		}else {
			remoteView.setViewVisibility(R.id.tv_title, View.GONE);
			remoteView.setViewVisibility(R.id.tv_progress, View.GONE);
			remoteView.setViewVisibility(R.id.progress, View.GONE);
			remoteView.setViewVisibility(R.id.btn_reload, View.VISIBLE);
			
			remoteView.setTextViewText(R.id.btn_reload,"下载失败，点击重新下载");
		}
	}
	
}
