package com.bt.qqlogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.tencent.tauth.Tencent;

public class AccessTokenSaver {
	private static Tencent mTencent;
	
	private static final String SP_ACCESS_TOKEN = "sp_access_token";
	private static final String KEY_OPEN_ID = "key_open_id";
	private static final String KEY_ACCESS_TOKEN = "key_access_token";
	private static final String KEY_EXPIRES_IN = "key_expires_in";
	
	public static Tencent getAccessToken(Context context){
		if (context == null || mTencent != null){
			return mTencent;
		}
		SharedPreferences sp = context.getSharedPreferences(SP_ACCESS_TOKEN, Context.MODE_APPEND);
		String openId = sp.getString(KEY_OPEN_ID, null);
		String accessToken = sp.getString(KEY_ACCESS_TOKEN, null);
		long expiresIn = sp.getLong(KEY_EXPIRES_IN, 0);
		mTencent = Tencent.createInstance(Constant.APP_ID, context.getApplicationContext());
		mTencent.setOpenId(openId);
		mTencent.setAccessToken(accessToken, ""+expiresIn);
		
		return mTencent;
	}
	
	public static void saveAccessToken(Context context,Tencent tencent){
		if (context == null || tencent == null){
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(SP_ACCESS_TOKEN, Context.MODE_APPEND);
		Editor editor = sp.edit();
		editor.putString(KEY_OPEN_ID, tencent.getOpenId());
		editor.putString(KEY_ACCESS_TOKEN, tencent.getAccessToken());
		editor.putLong(KEY_EXPIRES_IN, tencent.getExpiresIn());
		editor.commit();
	}
	
	public static void clear(Context context){
		if (context == null){
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(SP_ACCESS_TOKEN, Context.MODE_APPEND);
		Editor editor = sp.edit();
		editor.remove(KEY_OPEN_ID);
		editor.remove(KEY_ACCESS_TOKEN);
		editor.remove(KEY_EXPIRES_IN);
		editor.commit();
	}

}
