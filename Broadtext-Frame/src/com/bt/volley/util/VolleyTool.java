package com.bt.volley.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyTool {
	private static VolleyTool mInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    
    private VolleyTool(Context context) {
    	//下面注释掉的代码是缓存到sdcard中的，需要打开mainfest中的权限文件写权限
//		File sdDir = Environment.getExternalStorageDirectory();//获取跟目录 
//		File file = new File(sdDir,"xx");
//		DiskBasedCache cache = new DiskBasedCache(file, 20*1024*1024);
//		Network network = new BasicNetwork(new HurlStack());// 使用HurlStack sdk>9  参照源码
//		mRequestQueue = new RequestQueue(cache, network);
//		mRequestQueue.add(request);
//		mRequestQueue.start();
    	mRequestQueue = Volley.newRequestQueue(context);
    	mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
    }
    
    public static VolleyTool getInstance(Context context){
        if(mInstance == null){
    		mInstance = new VolleyTool(context);
        }
        return mInstance;
    }
    
	public RequestQueue getmRequestQueue() {
		return mRequestQueue;
	}

	public ImageLoader getmImageLoader() {
		return mImageLoader;
	}

	public void cancel(Object tag) {
		this.mRequestQueue.cancelAll(tag);
		 
	}
	
	public void release() {
		this.mImageLoader = null;
		this.mRequestQueue = null;
		mInstance = null;
	}
}
