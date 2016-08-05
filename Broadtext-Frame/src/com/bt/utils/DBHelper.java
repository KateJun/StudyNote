package com.bt.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.bt.photo.util.DownLoadUtil;
import com.bt.photo.util.PictureUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * 数据库操作类
 * @author XJ
 *
 */
public class DBHelper {
	
	private ReadWriteLock lock = new ReentrantReadWriteLock(true);
	private Lock readLock  = lock.readLock();
	private Lock writeLock   = lock.writeLock();
	
	/**
	 * 获取数据库读权限
	 * @param databaseDirPath .db文件存储目录
	 * @param databaseFileName 数据库名
	 * @return SQLiteDatabase
	 */
	public SQLiteDatabase getReadableDataBase(String databaseDirPath,String databaseFileName) {
		readLock.lock();
		try {
			String databasePath = databaseDirPath.concat(databaseFileName);
			return SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		}finally{
			readLock.unlock();
		}
	}
	
	/**
	 * 获取数据库写权限
	 * @param databaseDirPath  .db文件存储目录
	 * @param databaseFileName 数据库名
	 * @return SQLiteDatabase
	 */
	public SQLiteDatabase getWritableDataBase(String databaseDirPath,String databaseFileName) {
		writeLock.lock();
		try {
			String databasePath = databaseDirPath.concat(databaseFileName);
			
			return SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		} finally{
			writeLock.unlock();
		}
	}
	
	/**
	 * 拷贝资产目录下的文件到 手机
	 * @param fileName
	 */
	public void copyAssetsFile2SDCard(Context ct , String fileName) {
		final String APP_DIR = DownLoadUtil.getPath(ct);
		File desDir = new File(APP_DIR);
		if (!desDir.exists()) {
			desDir.mkdirs();
		}

		// 拷贝文件
		File file = new File(APP_DIR + fileName);
		if (file.exists()) {
			file.delete();
		}

		try {
			InputStream in = ct.getAssets().open(fileName);

			FileOutputStream fos = new FileOutputStream(file);

			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = in.read(buf)) > 0) {
				fos.write(buf, 0, len);
			}

			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
