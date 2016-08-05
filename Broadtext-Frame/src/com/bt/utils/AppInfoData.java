package com.bt.utils;

import android.graphics.drawable.Drawable;

/**
 * APK 信息
 * 
 * @author XJ
 * 
 */
public class AppInfoData {

	private Drawable appIcon;
	private String appName;
	private String appPkgName;
	private String appVersionName;
	private String appVersionCode;

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppPkgName() {
		return appPkgName;
	}

	public void setAppPkgName(String appPkgName) {
		this.appPkgName = appPkgName;
	}

	public String getAppVersionName() {
		return appVersionName;
	}

	public void setAppVersionName(String appVersionName) {
		this.appVersionName = appVersionName;
	}

	public String getAppVersionCode() {
		return appVersionCode;
	}

	public void setAppVersionCode(String appVersionCode) {
		this.appVersionCode = appVersionCode;
	}

	 

}
