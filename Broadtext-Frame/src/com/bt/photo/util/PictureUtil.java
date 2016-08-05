package com.bt.photo.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.text.TextUtils;

/**
 *  图片处理工具类
 * @author XJ
 *
 */
public class PictureUtil {

	
	/**
	 * 根据路径获得图片并压缩返回bitmap用于显示
	 * 默认根据尺寸800*480缩放显示
	 * @param filePath 图片路径 
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		return getSmallBitmap(filePath, 480, 800);
	}

	/**
	 * 根据路径获得图片返回缩略图
	 * @param filePath 图片路径
	 * @param reqWidth 宽度
	 * @param reqHeight 高度
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath, int reqWidth,int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}
	
	/**
	 * 计算图片的缩放值
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	
	/*********************** 图片旋转 **********************/
	/**
	 * 压缩图片，处理某些手机拍照角度旋转的问题
	 * @param context
	 * @param filePath 路径
	 * @param fileName 文件名称
	 * @param q 压缩质量 0-100
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String compressImage(Context context, String filePath,
			String fileName, int q) throws FileNotFoundException {

		Bitmap bm = getSmallBitmap(filePath);
		int degree = readPictureDegree(filePath);
		if (degree != 0) {// 旋转照片角度
			bm = rotateBitmap(bm, degree);
		}
		File outputFile = new File(DownLoadUtil.getPath(context), fileName);
		FileOutputStream out = new FileOutputStream(outputFile);
		bm.compress(Bitmap.CompressFormat.JPEG, q, out);
		return outputFile.getPath();
	}
	
	/** 
	 * 判断照片角度 
	 * @param path 图片路径
	 * */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			default:
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 *  旋转照片 
	 *  @param bitmap 图片
	 *  @param degress 旋转角度
	 * 
	 * */
	public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
		if (bitmap != null) {
			Matrix m = new Matrix();
			m.postRotate(degress);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, true);
			return bitmap;
		}
		return bitmap;
	}
	
	/**
	 * 圆形头像
	 * @param bitmap
	 * @return
	 */
	public static Bitmap toOvalBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		//消除锯齿
		paint.setAntiAlias(true);
		// 和画圆形图片就这块不同
		canvas.drawOval(rectF, paint);
		// 这句一定要加 设置两图交映的效果
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// paint.setXfermode(new PorterDuffXfermode(Mode.SCREEN));
		// paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
		// paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
		// paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
		canvas.drawBitmap(bitmap, rect, rectF, paint);
		return output;
	}

	/**
	 * 圆角图片
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		//消除锯齿
		paint.setAntiAlias(true);
		// 和画圆形图片就这块不同
		canvas.drawRoundRect(rectF, pixels, pixels, paint);
		// 这句一定要加 设置两图交映的效果
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rectF, paint);
		return output;
	}
	
	/**
	 * 获得带倒影的图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);
		return bitmapWithReflection;
	}
	
	
	/**
	 * 1)Drawable → Bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap convertDrawable2BitmapByCanvas(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 2)Drawable → Bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap convertDrawable2BitmapSimple(Drawable drawable){
		BitmapDrawable bd = (BitmapDrawable) drawable;
		return bd.getBitmap();
	}

	/**
	 *  Bitmap → Drawable
	 * @param bitmap
	 * @return
	 */
	public static Drawable convertBitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		// 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
		return bd;
	}
	
}
