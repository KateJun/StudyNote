package com.bt.listview.waterlist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.bt.volley.util.VolleyTool;
import com.bt.R;

public class ImageLoaderAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> urls;
	public ImageLoaderAdapter(Context ctx, ArrayList<String> urls) {
		this.context = ctx;
		this.urls = urls;
	}

	@Override
	public int getCount() {
		return urls != null ? urls.size() : 0 ;
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
		ViewHolder holder;
		if(convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_sample,null);
			holder = new ViewHolder();
			holder.itemImage = (ImageView) convertView.findViewById(R.id.thumbnail);
			holder.itemText = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String url = getItem(position);
		//android.R.drawable.ic_menu_rotate代表默认图 
		//android.R.drawable.ic_delete代表加载失败的图
		ImageListener listener = ImageLoader.getImageListener(holder.itemImage, 
				android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
		VolleyTool.getInstance(context).getmImageLoader().get(url, listener);
		holder.itemText.setText(url.substring(url.length() - 8));
		return convertView;
	}
	
	static class ViewHolder {
		ImageView itemImage;
		TextView itemText;
	}

}
