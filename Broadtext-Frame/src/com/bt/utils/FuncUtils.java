package com.bt.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class FuncUtils {

	/**
	 * 收起软键盘
	 * 
	 */
	public static void hideSoftInputMethod(Activity act) {
		if (act.getCurrentFocus() != null) {//点击隐藏软键盘
			if (act.getCurrentFocus().getWindowToken() != null) {
				InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(act.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
	
	/**
	 *  收起软键盘
	 * @param context
	 * @param v
	 */
      
    public static void collapseSoftInputMethod(Context context, View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     * @param context
     * @param v
     */
      
    public static void showSoftInputMethod(Context context, View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, 0);
        }
    }
	
	/**
	 * 重启应用
	 * 
	 * @param ct
	 */
	public static void restartApplication(Context ct) {
		final Intent intent = ct.getPackageManager().getLaunchIntentForPackage(
				ct.getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ct.startActivity(intent);
	}

	/**
	 * 分享功能
	 * 
	 * @param context
	 *            上下文
	 * @param activityTitle
	 *            Activity的名字
	 * @param msgTitle
	 *            消息标题
	 * @param msgText
	 *            消息内容
	 * @param imgPath
	 *            图片路径，不分享图片则传null
	 */
	public static void shareMsg(Context context, String activityTitle,
			String msgTitle, String msgText, String imgPath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (TextUtils.isEmpty(imgPath)) {
			intent.setType("text/plain"); // 纯文本
		} else {
			File f = new File(imgPath);
			if (f != null && f.exists() && f.isFile()) {
				intent.setType("image/*");
				Uri u = Uri.fromFile(f);
				intent.putExtra(Intent.EXTRA_STREAM, u);
			}
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, activityTitle));
	}

	/**
	 * 分享到指定应用
	 * 
	 * @param context
	 *            上下文
	 * @param activityTitle
	 *            Activity的名字
	 * @param msgTitle
	 *            消息标题
	 * @param msgText
	 *            消息内容
	 * @param imgPath
	 *            图片路径，不分享图片则传null
	 * 
	 */
	public static void shareInfo(Context ct, String activityTitle,
			String msgTitle, String msgText, String imgPath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("*/*");
		PackageManager pm = ct.getPackageManager();
		List<ResolveInfo> infoList = pm.queryIntentActivities(intent,
				PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		if (infoList != null) {
			List<Intent> targetedShareIntents = new ArrayList<Intent>();
			for (ResolveInfo info : infoList) {
				Intent targeted = new Intent(Intent.ACTION_SEND);
				ActivityInfo activityInfo = info.activityInfo;
				System.out.println(activityInfo.packageName + "===="
						+ activityInfo.name);
				if (activityInfo.packageName.contains("mobileqq")
						|| activityInfo.name.contains("mobileqq")) {
					if (TextUtils.isEmpty(imgPath)) {
						targeted.setType("text/plain"); // 纯文本
					} else {
						File f = new File(imgPath);
						if (f != null && f.exists() && f.isFile()) {
							// *图片分享
							targeted.setType("image/*");
							Uri u = Uri.fromFile(f);
							targeted.putExtra(Intent.EXTRA_STREAM, u);
						}
					}
					targeted.setPackage(activityInfo.packageName);
					targeted.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
					targeted.putExtra(Intent.EXTRA_TEXT, msgText);// Html.fromHtml(suggestStr)
					targeted.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					targetedShareIntents.add(targeted);
					break;
				}

			}
			Intent share = Intent.createChooser(targetedShareIntents.remove(0),
					activityTitle);
			if (share != null && FuncUtils.isNetworkConect(ct)) {
				share.putExtra(Intent.EXTRA_INITIAL_INTENTS,
						targetedShareIntents.toArray(new Parcelable[] {}));
				ct.startActivity(share);
			}
		}
	}

	/**
	 * 判断二维码格式
	 * 
	 * @param format
	 *            正则式
	 * @param content
	 *            内容
	 * @return
	 */
	public static boolean match(String format, String content) {
		/**
		 * 手机号：^(1)[3578]\\d{9}$
		 * 身份证：
		 * 15位- ^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$
		 * 18位- ^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$
		 * 车牌：^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$
		 * email：^\\w+@\\w+\\.(\\w{2,3}|\\w{2,3}\\.\\w{2,3})$
		 */
		if (TextUtils.isEmpty(format)) {
			format = "^\\d{3}-\\d{4}-\\d{3}$";// 123-1234-123
		}
		try {
			Pattern pattern = Pattern.compile(format);
			Matcher match = pattern.matcher(content);
			return match.matches();
		} catch (Exception e) {
			return true;
		}
	}

	private static long lastClickTime;

	/**
	 * 判断按钮两次点击时间间隔是否大于2s
	 */
	public boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime > 2000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 检查网路是否连接
	 * 
	 * @param ct
	 * @return
	 */
	public static boolean isNetworkConect(Context ct) {
		if (ct == null) {
			return false;
		}
		ConnectivityManager cwjManager = (ConnectivityManager) ct
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		if (info != null) {
			return info.isAvailable();
		} else {
			Toast.makeText(ct, "检查网络状态", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	/**
	 * 判断网络连接状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getConnectedType(Context context) {
		if (context == null) {
			return false;
		}
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] mNetworkInfo = mConnectivityManager.getAllNetworkInfo();
		if (mNetworkInfo != null) {
			for (int i = 0; i < mNetworkInfo.length; i++) {
				NetworkInfo info = mNetworkInfo[i];
				State state = info.getState();
				if (info.isAvailable()) {
					if (info.getType() == ConnectivityManager.TYPE_WIFI
							|| info.getType() == ConnectivityManager.TYPE_MOBILE) {

						switch (state) {
						case SUSPENDED:
							break;
						case CONNECTING:
							break;
						case CONNECTED:
							break;
						case DISCONNECTED:
							break;
						case DISCONNECTING:
							break;
						default:
							break;
						}
						return true;
					}
				}
			}
		}
		return false;

	}

	/**
	 * 网络连接类型
	 */
	public void IntenertType(Context ct) {
		ConnectivityManager cManager = (ConnectivityManager) ct
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cManager.getActiveNetworkInfo();
		if (null == networkInfo) {
			// netTypeText.setText("无网络");
		} else {
			switch (networkInfo.getType()) {
			case ConnectivityManager.TYPE_WIFI: // wifi
				// netTypeText.setText("WIFI网络");
				break;
			case ConnectivityManager.TYPE_MOBILE:// 手机网络
				// * NETWORK_TYPE_CDMA 网络类型为CDMA
				// * NETWORK_TYPE_EDGE 网络类型为EDGE
				// * NETWORK_TYPE_EVDO_0 网络类型为EVDO0
				// * NETWORK_TYPE_EVDO_A 网络类型为EVDOA
				// * NETWORK_TYPE_GPRS 网络类型为GPRS
				// * NETWORK_TYPE_HSDPA 网络类型为HSDPA
				// * NETWORK_TYPE_HSPA 网络类型为HSPA
				// * NETWORK_TYPE_HSUPA 网络类型为HSUPA
				// * NETWORK_TYPE_UMTS 网络类型为UMTS
				// 联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EDGE，电信的2G为CDMA，电信的3G为EVDO
				switch (networkInfo.getSubtype()) {
				case TelephonyManager.NETWORK_TYPE_CDMA:
					// netTypeText.setText("电信2G网络");
					break;
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					// netTypeText.setText("联通3G网络");
					break;
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
					// netTypeText.setText("移动或联通2G网络");
					break;
				default:
					// netTypeText.setText("其他网络");
					break;
				}
				break;
			default:
				break;
			}
		}
	}
	
	

	/** 画一个2d爱心（半圆+sin曲线） */
	public static void drawHeart2D(Canvas mCanvas, Paint mPaint, int centerX,
			int centerY, float height) {

		float r = height / 4;
		/* 心两半圆结点处 */
		float topX = centerX;
		float topY = centerY - r;

		/* 左上半圆 */
		RectF leftOval = new RectF(topX - 2 * r, topY - r, topX, topY + r);
		mCanvas.drawArc(leftOval, 180f, 180f, false, mPaint);
		/* 右上半圆 */
		RectF rightOval = new RectF(topX, topY - r, topX + 2 * r, topY + r);
		mCanvas.drawArc(rightOval, 180f, 180f, false, mPaint);

		/* 下半两sin曲线 */
		float base = 3 * r;
		double argu = Math.PI / 2 / base;
		float y = base, value;
		while (y >= 0) {
			value = (float) (2 * r * Math.sin(argu * (base - y)));
			mCanvas.drawPoint(topX - value, topY + y, mPaint);
			mCanvas.drawPoint(topX + value, topY + y, mPaint);
			y -= 1;
		}

		// 1）心形函数：x²+(y-³√x²)²=1
		// >> x^2+(y-(x^2)^(1/3))^2=1
		//
		// 2）心形的各种画法：
		// >> http://woshao.com/article/1a855728bda511e0b40e000c29fa3b3a/
		//
		// 3）笛卡尔情书的秘密——心形函数的绘制
		// >> http://www.cssass.com/blog/index.php/2010/808.html
	}

	/** 画一个3d爱心 */
	public static void drawHeart3D(Canvas mCanvas, Paint mPaint) {
		int w = mCanvas.getWidth();
		int h = mCanvas.getHeight();
		/* 画一个3d爱心 */
		int i, j;
		double x, y, r;
		for (i = 0; i <= 90; i++) {
			for (j = 0; j <= 90; j++) {
				r = Math.PI / 45 * i * (1 - Math.sin(Math.PI / 45 * j)) * 20;
				x = r * Math.cos(Math.PI / 45 * j) * Math.sin(Math.PI / 45 * i)
						+ w / 2;
				y = -r * Math.sin(Math.PI / 45 * j) + h / 4;
				mCanvas.drawPoint((float) x, (float) y, mPaint);
			}
		}
	}

	/**
	 * 半角转全角
	 * 
	 * @param input
	 *            String.
	 * @return 全角字符串.
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * 
	 * @param input
	 *            String.
	 * @return 半角字符串
	 */
	public static String ToDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);
		return returnString;
	}
}
