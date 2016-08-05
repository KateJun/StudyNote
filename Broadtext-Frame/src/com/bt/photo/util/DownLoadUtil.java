package com.bt.photo.util;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

/**
 * 下载工具类
 * @author XJ
 *
 */
public class DownLoadUtil {
//	private static DownLoadUtil util;
//	private DownLoadUtil() {
//	}
//	public synchronized static DownLoadUtil getInstance() {
//		if (util == null) {
//			util = new DownLoadUtil();
//		}
//		return util;
//	}
	
	/**
	 *  将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	 * @param imgFile 图片路径
	 * @return
	 */
	public static  String GetImageStr(Context ct,String imgFile) {
		byte[] data = null;
		Bitmap bitmap = PictureUtil.getSmallBitmap(imgFile);
		// 读取图片字节数组
		ByteArrayOutputStream out = new ByteArrayOutputStream();  
		bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);  
		try {  
			out.flush();  
			out.close();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		data = out.toByteArray();
		// 对字节数组Base64编码
		
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}

	/**
	 * 图片进行解码
	 * 对字节数组字符串进行Base64解码并生成图片
	 * @param ct
	 * @param imgStr 图片编码字符串
	 * @return
	 */
	public static Bitmap generateImage(Context ct , String imgStr) {
		if (imgStr == null) // 图像数据为空
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = PictureUtil.calculateInSampleSize(options, 480, 800);
			return BitmapFactory.decodeByteArray(b, 0, b.length, options);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	 /** 
     * 获取本地图片 
     * @param mActivity  
     * @param imageName 图片名称
     * @param folder 文件夹名称
     * @return 
     */  
    public static Bitmap getBitmapFromFile(Context mActivity,String folder,String imageName){  
        Bitmap bitmap = null;  
        if(!TextUtils.isEmpty(imageName)){  
            File file = null;  
            String real_path = "";  
            try {  
                real_path = getFilePath(mActivity, folder);  
                file = new File(real_path, imageName);  
                System.out.println(real_path+"***获取sdcard图片成功？= ="+file.exists());
                if(file.exists()){
                	bitmap = BitmapFactory.decodeStream(new FileInputStream(file));  
                }
            } catch (Exception e) {  
                e.printStackTrace();  
                bitmap = null;  
            }  
        }  
        return bitmap;  
    }
    /** 
     * 图片保存到本地
     * @param mActivity 
     * @param folder 文件夹路径
     * @param imageName  
     * @param bitmap  
     * @return 
     */  
    @SuppressWarnings("resource")
	public static boolean setBitmapToFile(Context mActivity,String folder,String imageName,Bitmap bitmap){  
        File file = null;  
        String real_path = "";  
        try {  
            real_path = getFilePath(mActivity, folder);  
            File file2 = new File(real_path); 
            if(!file2.exists()){  
            	file2.mkdirs();
            }
            file=new File(file2, imageName);
            System.out.println(real_path+"==保存图片，是否存在？=="+file.exists());
            if (!file.exists()) {
            	file.createNewFile();  
			}
            FileOutputStream fos = null;  
            if(DownLoadUtil.hasSDCard()){  
                fos = new FileOutputStream(file);  
            }else{  
                fos = mActivity.openFileOutput(imageName, Context.MODE_PRIVATE);  
            }  
            if (imageName != null && (imageName.contains(".jpg") || imageName.contains(".JPG")
            		||imageName.contains(".gif")||imageName.contains(".GIF"))
            		||imageName.contains(".jpeg")||imageName.contains(".JPEG")){  
            	bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);  
            }else{  
            	bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);  
            }  
            fos.flush();  
            if(fos != null){  
                fos.close();  
            }  
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        } 
    }  

    /**
     * 获取存储文件目录
     * @param ct
     * @param folder 文件夹名称
     * @return
     */
    public static String getFilePath(Context ct, String folder) {
    	return getPath(ct) + ((!TextUtils.isEmpty(folder)) && folder.startsWith(File.separator) ? folder :File.separator+ folder);  

    }
	

	/**
	 * sdcard or app path
	 * @param ct 
	 * @return String /storage/sdcard0
	 */
	public static String getPath(Context ct) {
		String path = "";
		if (hasSDCard()) {
			path = Environment.getExternalStorageDirectory().getPath();
		}else {
			path = ct.getFilesDir().toString();
		}
		return path;
	}

	/**
	 * 判断sdcard
	 * 
	 * @return
	 */
	public static boolean hasSDCard() {
		boolean b = false;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			b = true;
		}
		return b;
	}
	/**
	 * 根据路径删除图片
	 * @param filePath 图片路径
	 */
	public static void deleteTempFile(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return;
		}
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}
	/**
	 * 删除缓存文件
	 * @param file 
	 */
	public static void deleteTempFile(File file) {
		if (file == null || !file.exists()) {
			return;
		}
		if (file.isFile()) {
			file.delete();
		}else if(file.isDirectory()){
			deleteDir(file.getPath());
		}
	}

	/**
	 * 递归删除文件
	 * @param path 文件夹路径
	 */
	public static void deleteDir(String path) {
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;
		
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(file.getPath()); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}
	
	/**
	 * 添加到图库
	 * @param context
	 * @param path 图片路径
	 */
	public static void galleryAddPic(Context context, String path) {
		if (TextUtils.isEmpty(path)) {
			return;
		}
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(path);
		if (f.exists() && f.isFile()) {
			Uri contentUri = Uri.fromFile(f);
			mediaScanIntent.setData(contentUri);
			context.sendBroadcast(mediaScanIntent);
		}
	}

	/**
	 * 根据url 获取图片名称
	 * 
	 * @param url
	 * @return
	 */
	public static String getImageName(String url) {
		String imageName = "";
		if (url != null) {
			imageName = url.substring(url.lastIndexOf(File.separator) + 1);
		}
		return imageName;
	}
}