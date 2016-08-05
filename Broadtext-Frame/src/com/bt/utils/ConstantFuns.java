package com.bt.utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public class ConstantFuns {

	/**
	 * 获取未安装的apk信息
	 * 
	 * @param ctx
	 * @param apkPath
	 * @return
	 */
	public static AppInfoData getApkFileInfo(Context ctx, String apkPath) {
		System.out.println(apkPath);
		File apkFile = new File(apkPath);
		if (!apkFile.exists()
				|| !apkPath.toLowerCase(Locale.CHINA).endsWith(".apk")) {
			System.out.println("文件路径不正确");
			return null;
		}
		AppInfoData appInfoData;
		String PATH_PackageParser = "android.content.pm.PackageParser";
		String PATH_AssetManager = "android.content.res.AssetManager";
		try {
			// 反射得到pkgParserCls对象并实例化,有参数
			Class<?> pkgParserCls = Class.forName(PATH_PackageParser);
			Class<?>[] typeArgs = { String.class };
			Constructor<?> pkgParserCt = pkgParserCls.getConstructor(typeArgs);
			Object[] valueArgs = { apkPath };
			Object pkgParser = pkgParserCt.newInstance(valueArgs);

			// 从pkgParserCls类得到parsePackage方法
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();// 这个是与显示有关的, 这边使用默认
			typeArgs = new Class<?>[] { File.class, String.class,
					DisplayMetrics.class, int.class };
			Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(
					"parsePackage", typeArgs);

			valueArgs = new Object[] { new File(apkPath), apkPath, metrics, 0 };

			// 执行pkgParser_parsePackageMtd方法并返回
			Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
					valueArgs);

			// 从返回的对象得到名为"applicationInfo"的字段对象
			if (pkgParserPkg == null) {
				return null;
			}
			Field appInfoFld = pkgParserPkg.getClass().getDeclaredField(
					"applicationInfo");

			// 从对象"pkgParserPkg"得到字段"appInfoFld"的值
			if (appInfoFld.get(pkgParserPkg) == null) {
				return null;
			}
			ApplicationInfo info = (ApplicationInfo) appInfoFld
					.get(pkgParserPkg);

			// 反射得到assetMagCls对象并实例化,无参
			Class<?> assetMagCls = Class.forName(PATH_AssetManager);
			Object assetMag = assetMagCls.newInstance();
			// 从assetMagCls类得到addAssetPath方法
			typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod(
					"addAssetPath", typeArgs);
			valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			// 执行assetMag_addAssetPathMtd方法
			assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);

			// 得到Resources对象并实例化,有参数
			Resources res = ctx.getResources();
			typeArgs = new Class[3];
			typeArgs[0] = assetMag.getClass();
			typeArgs[1] = res.getDisplayMetrics().getClass();
			typeArgs[2] = res.getConfiguration().getClass();
			Constructor<Resources> resCt = Resources.class
					.getConstructor(typeArgs);
			valueArgs = new Object[3];
			valueArgs[0] = assetMag;
			valueArgs[1] = res.getDisplayMetrics();
			valueArgs[2] = res.getConfiguration();
			res = (Resources) resCt.newInstance(valueArgs);

			// 读取apk文件的信息
			appInfoData = new AppInfoData();
			if (info != null) {
				if (info.icon != 0) {// 图片存在，则读取相关信息
					Drawable icon = res.getDrawable(info.icon);// 图标
					appInfoData.setAppIcon(icon);
				}
				if (info.labelRes != 0) {
					String neme = (String) res.getText(info.labelRes);// 名字
					appInfoData.setAppName(neme);
				} else {
					String apkName = apkFile.getName();
					appInfoData.setAppName(apkName.substring(0,
							apkName.lastIndexOf(".")));
				}
				String pkgName = info.packageName;// 包名
				appInfoData.setAppName(pkgName);
			} else {
				return null;
			}
			PackageManager pm = ctx.getPackageManager();
			PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath,
					PackageManager.GET_ACTIVITIES);
			if (packageInfo != null) {
				appInfoData.setAppVersionName(packageInfo.versionName);// 版本号
				appInfoData.setAppVersionCode(packageInfo.versionCode + "");// 版本码
			}
			return appInfoData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 是否已安装某个应用
	 * @param ct
	 * @param targetPakege
	 * @return
	 */
	public static boolean isInstalled(Context ct, String targetPakege) {
		if (targetPakege == null || "".equals(targetPakege))
			return false;
		PackageInfo packageInfo;
		try {
			packageInfo = ct.getPackageManager()
					.getPackageInfo(targetPakege, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			packageInfo = null ;
		}
		return packageInfo == null ? false : true ;
	}
	
	/**
	 * 是否已安装某个应用
	 * @param context
	 * @param intent
	 * @return
	 */
	public static boolean isInstalled(Context ct, Intent intent) {
		List<ResolveInfo> list =  ct.getPackageManager().queryIntentActivities(intent, 0);
		if(list != null && list.size() > 0){
			return true;
		}
		return false;
	}
}
