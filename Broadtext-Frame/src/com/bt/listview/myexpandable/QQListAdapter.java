package com.bt.listview.myexpandable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.bt.listview.myexpandable.QQListView.QQHeaderAdapter;
import com.bt.R;

public class QQListAdapter extends SimpleExpandableListAdapter implements QQHeaderAdapter {
	private QQListView listView; 
	private Context context;
	
	public QQListAdapter(Context context,QQListView listView,
			List<? extends Map<String, ?>> groupData, int expandedGroupLayout,
			String[] groupFrom, int[] groupTo,
			List<? extends List<? extends Map<String, ?>>> childData,
			int childLayout, String[] childFrom,int[] childTo) {
		super(context, groupData, expandedGroupLayout, groupFrom,groupTo, childData, childLayout, childFrom, childTo);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.listView = listView;
	}

	@Override
	public int getQQHeaderState(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		final int childCount = getChildrenCount(groupPosition);
		if(childPosition == childCount - 1){  
			return PINNED_HEADER_PUSHED_UP; 
		}
		else if(childPosition == -1 && !listView.isGroupExpanded(groupPosition)){ 
			return PINNED_HEADER_GONE; 
		}
		else{
			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configureQQHeader(View header, int groupPosition,
			int childPosition, int alpha) {
		// TODO Auto-generated method stub
		Map<String,String> groupData = (Map<String,String>)this.getGroup(groupPosition);
		((TextView)header.findViewById(R.id.groupto)).setText(groupData.get("g"));
	}

	private HashMap<Integer,Integer> groupStatusMap = new HashMap<Integer, Integer>();
	
	@Override
	public void setGroupClickStatus(int groupPosition, int status) {
		// TODO Auto-generated method stub
		groupStatusMap.put(groupPosition, status);
	}
	
	@Override
	public int getGroupClickStatus(int groupPosition) {
		// TODO Auto-generated method stub
		if(groupStatusMap.containsKey(groupPosition)){
			return groupStatusMap.get(groupPosition);
		}
		else{
			return 0;
		}
	}
	
	@Override  
	public View getGroupView(int groupPosition, boolean isExpanded,   
	                    View convertView, ViewGroup parent) {   
		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.group, null);
		}
		
		ImageView iv = (ImageView)convertView.findViewById(R.id.groupIcon);
		
		if (isExpanded) {
			iv.setImageResource(R.drawable.btn_browser2);
		}
		else{
			iv.setImageResource(R.drawable.btn_browser);
		}
		
		return super.getGroupView(groupPosition,isExpanded,convertView,parent);
	}
}