package com.bt.listview.azlist;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bt.listview.azlist.util.ContactBean;
import com.bt.listview.azlist.util.MySectionIndexer;
import com.bt.listview.azlist.util.PinnedHeaderListView;
import com.bt.listview.azlist.util.PinnedHeaderListView.PinnedHeaderAdapter;
import com.bt.listview.azlist.util.PinyinUtils;
import com.bt.R;


public class ContactListAdapter extends BaseAdapter implements
		PinnedHeaderAdapter, OnScrollListener {

	private static final String TAG = "ContactListAdapter";
	private ArrayList<ContactBean> peoples ;
	private MySectionIndexer indexer ;
	private int mLocationPosition = -1;
	private Context context;
	
	public ContactListAdapter(ArrayList<ContactBean> peoples,MySectionIndexer mIndexer,
			Context context) {
		this.peoples = peoples;
		this.context = context;
		this.indexer = mIndexer;
	}

	/**
	 * 更新数据
	 * @param peoples
	 */
	public void upData(ArrayList<ContactBean> peoples,MySectionIndexer mIndexer) {
		this.peoples = peoples ;
		this.indexer = mIndexer ;
		notifyDataSetChanged();
	}
	/**
	 * 更新数据
	 * @param peoples
	 */
	public void upData(ArrayList<ContactBean> peoples) {
		this.peoples = peoples ;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return peoples != null ? peoples.size() : 0;
	}

	@Override
	public ContactBean getItem(int position) {
		return peoples.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ContactBean bean = getItem(position);
		ViewHolder holder = null ;
		if(null == convertView){
			convertView = View.inflate(context,R.layout.contact_list_item, null);
			holder = new ViewHolder();
			holder.tv_title = (TextView) convertView.findViewById(R.id.title);
			holder.tv_name = (TextView) convertView.findViewById(R.id.phonename);
			holder.tv_number = (TextView) convertView.findViewById(R.id.select_list_number);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

		int section = indexer.getSectionForPosition(position);
		if (indexer.getPositionForSection(section) == position) {
			holder.tv_title.setVisibility(View.VISIBLE);
			holder.tv_title.setText(bean.firstCharName);
		} else {
			holder.tv_title.setVisibility(View.GONE);
		}
		
		holder.tv_name.setText(bean.getContactName());
		String number = bean.getContactPhone(); // null
		if(null == number || "".equals(number)){
			number = bean.getContactHomePhone();	
		}
		holder.tv_number.setText(number);
		return convertView;
	}
	
	static class ViewHolder{
		 TextView tv_title;
		 TextView tv_name;
    	 TextView tv_number;
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
		}
	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (realPosition < 0
				|| (mLocationPosition != -1 && mLocationPosition == realPosition)) {
			return PINNED_HEADER_GONE;
		}
		mLocationPosition = -1;
		int section = indexer.getSectionForPosition(realPosition);
		int nextSectionPosition = indexer.getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		int realPosition = position;
		int section = indexer.getSectionForPosition(realPosition);
		String title = (String) indexer.getSections()[section];
		((TextView) header.findViewById(R.id.title)).setText(title);
		
	}

}
