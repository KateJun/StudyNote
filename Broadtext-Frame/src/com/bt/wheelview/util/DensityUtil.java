package com.bt.wheelview.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class DensityUtil {

	// 当前屏幕的densityDpi
	private static float dmDensityDpi = 0.0f;
	private static DisplayMetrics dm;
	private  float scale = 0.0f;
	private int width = 0;
	private int height = 0;

	/**
	 * 
	 * 根据构造函数获得当前手机的屏幕系数
	 * 
	 * */
	public DensityUtil(Context context) {
		// 获取当前屏幕
		dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		// 设置DensityDpi
		setDmDensityDpi(dm.densityDpi);
		// 密度因子
		scale = dm.density;
//		scale = getDmDensityDpi() / 160;//与上一行结果相同
		width = dm.widthPixels;
		height = dm.heightPixels;//除掉了状态栏高度48dp
//		System.out.println(toString());
	}

	/**
	 * 当前屏幕的density因子
	 * 
	 * @param DmDensity
	 * @retrun DmDensity Getter
	 * */
	public static float getDmDensityDpi() {
		return dmDensityDpi;
	}

	/**
	 * 当前屏幕的density因子
	 * 
	 * @param DmDensity
	 * @retrun DmDensity Setter
	 * */
	public static void setDmDensityDpi(float dmDensityDpi) {
		DensityUtil.dmDensityDpi = dmDensityDpi;
	}

	/**
	 * 密度转换像素
	 * */
	public  int dip2px(float dipValue) {
		return (int) (dipValue * scale + 0.5f);

	}

	/**
	 * 像素转换密度
	 * */
	public int px2dip(float pxValue) {
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 密度因子
	 * @return
	 */
	public int  getScale(){
		return (int)scale;
	}
	/**
	 * 获取屏幕宽度 （像素）
	 * @return
	 */
	public int getScreenWidth() {
		return this.width;
	}
	/**
	 * 获取屏幕高度 （像素）
	 * @return
	 */
	public int getScreenHeight() {
		return this.height;
	}
	
	@Override
	public String toString() {
		return " scale:"+ scale + " ,dmDensityDpi:" + dmDensityDpi;
	}
}
