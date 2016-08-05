package com.bt.utils;

import java.io.ObjectInputStream.GetField;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class LogUtil {

	private static boolean showLog = true;
	private static String TAG = GetField.class.getName();

	/**
	 * Toast short
	 * 
	 * @param ct
	 * @param msg
	 */
	public static void toastShort(Context ct, String msg) {
		if (showLog)
			Toast.makeText(ct, TextUtils.isEmpty(msg) ? "no msg" : msg,
					Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast long
	 * 
	 * @param ct
	 * @param msg
	 */
	public static void toastLong(Context ct, String msg) {
		if (showLog)
			Toast.makeText(ct, TextUtils.isEmpty(msg) ? "no msg" : msg,
					Toast.LENGTH_LONG).show();
	}

	public static void syso(String log) {
		if (showLog)
			System.out.println(TextUtils.isEmpty(log) ? "system out print no msg"
					: log);
	}

	public static void logE(String msg) {
		if (showLog)
			Log.e(TAG, TextUtils.isEmpty(msg) ? "no log msg" : msg);
	}

	/**
	 * 是否显示日志
	 * 
	 * @param enable
	 */
	public static void enableLogging(boolean enable) {
		showLog = enable;
	}

	/**
	 * 一般信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, String msg) {
		if (showLog)
			Log.i(tag, msg);
	}

	/**
	 * 错误信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, String msg) {
		if (showLog)
			Log.e(tag, msg);
	}

	public static void e(String msg) {
		if (showLog)
			Log.e(TAG, msg);
	}

	/**
	 * 错误信息
	 * 
	 * @param tag
	 * @param msg
	 * @param tr
	 */
	public static void e(String tag, String msg, Throwable tr) {
		if (showLog)
			Log.e(tag, msg, tr);
	}

	/**
	 * 警告信息.
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void w(String tag, String msg) {
		if (showLog)
			Log.w(tag, msg);
	}

	/**
	 * 警告信息.
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void w(String tag, String msg, Throwable tr) {
		if (showLog)
			Log.w(tag, msg, tr);
	}

	/**
	 * debug信息.
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, String msg) {
		if (showLog)
			Log.d(tag, msg);
	}

	/**
	 * 详细信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void v(String tag, String msg) {
		if (showLog)
			Log.v(tag, msg);
	}

}
