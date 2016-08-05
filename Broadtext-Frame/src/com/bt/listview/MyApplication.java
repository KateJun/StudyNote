package com.bt.listview;

import android.app.Application;

import com.bt.utils.Data;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//初始化数据源
		Data.getInstance().init();
	}
}
