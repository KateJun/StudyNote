package com.bt.listview.waterlist;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageRequest;
import com.bt.volley.util.VolleyTool;
import com.bt.R;

public class ImageRequestAdapter extends BaseAdapter { //implements Listener<Bitmap>,ErrorListener{
	private Context context;
	private  ArrayList<String> urls;	
	private int itemWidth;
	public ImageRequestAdapter(Context context,  ArrayList<String> urls, int itemWidth) {
		this.context = context;
		this.urls = urls;
		this.itemWidth = itemWidth;
	}

	@Override
	public int getCount() {
		return  urls != null ? urls.size() : 0 ;
	}

	@Override
	public String getItem(int position) {
		return urls.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_sample,null);
		}
		final ImageView itemImage = (ImageView) convertView.findViewById(R.id.thumbnail);
		TextView itemText = (TextView) convertView.findViewById(R.id.text);
		String url = getItem(position);
		RequestQueue requestQueue = VolleyTool.getInstance(context).getmRequestQueue();
		ImageRequest request = (ImageRequest) requestQueue.add(
				new ImageRequest(url, new Listener<Bitmap>() {

					@Override
					public void onResponse(Bitmap response) {
						itemImage.setImageBitmap(response);
					}
				}, itemWidth, 0, Config.ARGB_8888, null));
		request.setTag(WaterfallListViewActivity.class.getSimpleName());
		request.setShouldCache(true);
		//默认缓存位置data/data/包名/cache/volley,默认大小5MB
		//修改缓存默认存储位置和存储区大小帖子上面有介绍
		itemText.setText(url.substring(url.length() - 8));
		return convertView;
	}
	
	static class MyAdapterHolder {
		ImageView itemImage;
		TextView itemText;
	}
}
