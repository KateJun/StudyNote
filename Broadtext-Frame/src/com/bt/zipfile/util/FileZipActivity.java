package com.bt.zipfile.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.ZipException;

import com.bt.photo.util.DownLoadUtil;
import com.bt.R;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FileZipActivity extends Activity implements OnClickListener{

	private Button btnFileToZip,btnZipToFile,btn;
	private TextView tvShow;
	private ArrayList<File> files;
	
	private final String folder = "Zip";
	private final String zipFileName = folder + "/pics.zip";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("文件压缩");
		setContentView(R.layout.broadcast_activity);
		
		btnFileToZip =  (Button)findViewById(R.id.btn_send_broadcast);
		btnZipToFile =  (Button)findViewById(R.id.btn_rigister);
		btn =  (Button)findViewById(R.id.btn_unrigister);
		
		btnFileToZip.setOnClickListener(this);
		btnZipToFile.setOnClickListener(this);
		btn.setOnClickListener(this);
		btn.setVisibility(View.INVISIBLE);
		
		btnFileToZip.setText("压缩");
		btnZipToFile.setText("解压");
		
		tvShow = (TextView)findViewById(R.id.tv_show);
		files = new ArrayList<File>();
	}
	
	@Override
	public void onClick(View v) {
		if (btnFileToZip.equals(v)) {
			filesToZip();
		} else if (v.equals(btnZipToFile)) {
			zipToFile();
		} else if (v.equals(btn)) {

		}
	}
	
	/**
	 * 将asset文件 压缩到SDCard
	 */
	private void filesToZip(){
		AssetManager assetManager = this.getResources().getAssets();
		try {
			String[] ss = assetManager.list("");
			if (ss != null) {
				InputStream in = null;
				byte[] buffer = new byte[2014];
				for (int i = 0; i < ss.length; i++) {
					String  name = ss[i];
					System.out.println("asset file:"+name);
					if (name.endsWith("jpg") || name.endsWith("png")
							|| name.endsWith("JPEG") || name.endsWith("gif")
							|| name.endsWith("bmp")) {
						
						in = assetManager.open(name);
						File tempFile = new File(DownLoadUtil.getFilePath(this,name));
						FileOutputStream stream = new FileOutputStream(tempFile);
						int count = 0;
						while ((count = in.read(buffer)) > 0) {
							stream.write(buffer, 0, count);
						}
						files.add(tempFile);
						
						stream.close();
						in.close();
					}
				}
				System.out.println(files.size());
				if (files.size() != 0) {
					ZipUtils.zipFiles(files, new File(DownLoadUtil.getFilePath(this, zipFileName)));
					tvShow.setText("压缩文件存储位置：" + DownLoadUtil.getFilePath(this, zipFileName));
					//打包完毕删除缓存文件
					for (Iterator<File> iterator = files.iterator(); iterator
							.hasNext();) {
						File type = (File) iterator.next();
						DownLoadUtil.deleteTempFile(type);
					}
					files.clear();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  解压缩SDCard  zip文件
	 */
	private void zipToFile(){
		File zipFile = new File(DownLoadUtil.getFilePath(this, zipFileName));
		try {
			ZipUtils.upZipFile(zipFile, DownLoadUtil.getFilePath(this,folder));
			tvShow.setText("解压缩后文件存储目录：" + DownLoadUtil.getFilePath(this,folder));
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
