package com.bt.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootBroadcastReciver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("接受到广播-----------------------"+intent.getAction());
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) || "com.bt.broadcast".equals(intent.getAction())) {
			Toast.makeText(context, "BroadcastRecive！5s onece", Toast.LENGTH_SHORT).show();
		
			//重新开启下载服务
			 Intent intent2 = new Intent(context, UpdateService.class);
			 context.startService(intent2);
		}
	}


}
