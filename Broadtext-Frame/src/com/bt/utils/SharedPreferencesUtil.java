package com.bt.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {
	
	private SharedPreferences sp; 
	private Editor editor;  
	private Context context;
	
	@SuppressWarnings("deprecation")
	@SuppressLint("WorldWriteableFiles")
	public SharedPreferencesUtil(Context c) {
		this.context = c;
		this.sp = context.getSharedPreferences(Constants.FILE_NAME, Context.MODE_WORLD_WRITEABLE);//创建文件
		this.editor = sp.edit();
	}

	public void putValue(String key, boolean checked) {  
		editor.putBoolean(key, checked);  
		editor.commit();
    } 
	
	public void putValue(String key, int update_interval) {  
		editor.putInt(key, update_interval);  
		editor.commit();
    } 
	
	public void putValue(String key, String value) {  
		editor.putString(key, value);  
		editor.commit();
    } 
	
	public boolean getBooleanValue(String key) {  
		return sp.getBoolean(key, false);
    } 
	
	public int getIntValue(String key) {  
		return sp.getInt(key, 0);
    } 
	
	public String getStringValue(String key) {  
		return sp.getString(key, "");
    } 
}
