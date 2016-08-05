package com.bt.volley;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.NetworkImageView;
import com.bt.utils.Data;
import com.bt.volley.util.VolleyTool;
import com.bt.R;

public class VolleyAdapter extends BaseAdapter {

	private Context ct ;
	private String[] urls;
	private boolean isNetWorkImageView = false;
	
	public VolleyAdapter(Context ct,boolean isNetWorkImageView) {
		this.ct = ct ; 
		this.isNetWorkImageView = isNetWorkImageView;
		urls = Data.UrlBean.urls;
	}
	@Override
	public int getCount() {
		return urls.length;
	}

	@Override
	public String getItem(int position) {
		return urls[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		// TODO Auto-generated method stub
		String url = getItem(position);
		Holder mHolder;
		if (!isNetWorkImageView) {
			if (v == null) {
				v = View.inflate(ct, R.layout.list_item_icon_text, null);
				mHolder = new Holder();
				mHolder.itemImage = (ImageView)v.findViewById(R.id.icon);
				mHolder.itemText = (TextView)v.findViewById(R.id.text);
				v.setTag(mHolder);
			}else {
				mHolder = (Holder) v.getTag();
			}
			//android.R.drawable.ic_menu_rotate代表默认图 
			//android.R.drawable.ic_delete代表加载失败的图
			ImageListener listener = ImageLoader.getImageListener(mHolder.itemImage, 
					android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
			VolleyTool.getInstance(ct).getmImageLoader().get(url, listener,480,800);
		}else {
			if (v == null) {
				v = View.inflate(ct, R.layout.list_item_networkicon_text, null);
				mHolder = new Holder();
				mHolder.itemNetworkImage = (NetworkImageView)v.findViewById(R.id.icon);
				mHolder.itemText = (TextView)v.findViewById(R.id.text);
				v.setTag(mHolder);
			}else {
				mHolder = (Holder) v.getTag();
			}
			mHolder.itemNetworkImage.setImageUrl(url, VolleyTool.getInstance(ct).getmImageLoader());
		}
		
		mHolder.itemText.setText(url.substring(url.length() - 8));
		return v;
	}
	
	private static class  Holder {
		ImageView itemImage;
		NetworkImageView itemNetworkImage;
		TextView itemText;
	}

}
