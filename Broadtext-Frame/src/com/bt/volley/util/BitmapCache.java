package com.bt.volley.util;

import java.lang.ref.SoftReference;
import java.util.HashSet;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

@SuppressLint("NewApi") 
public class BitmapCache implements ImageCache {
	private LruCache<String, Bitmap> mCache;
	//存放图片的软引用
    public static HashSet<SoftReference<Bitmap>> mReusableBitmaps = new HashSet<SoftReference<Bitmap>>();
	
	public BitmapCache() {
		int maxSize = 10 * 1024 * 1024;
		mCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				// TODO Auto-generated method stub
				 //将超出设置的最大内存限制的图片放入软引用系统会自动回收
                mReusableBitmaps.add(new SoftReference<Bitmap>(oldValue));
//				super.entryRemoved(evicted, key, oldValue, newValue);
			}
			
		};
	}

	@Override
	public Bitmap getBitmap(String url) {
		return mCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mCache.put(url, bitmap);
	}

}
