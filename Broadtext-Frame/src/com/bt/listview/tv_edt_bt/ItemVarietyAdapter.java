package com.bt.listview.tv_edt_bt;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.bt.utils.Constants;
import com.bt.utils.LogUtil;
import com.bt.R;

public class ItemVarietyAdapter extends BaseAdapter{

	ArrayList<HashMap<String, String>> arrayList;
	Context context;
	LayoutInflater layoutInflater;
	
	public ItemVarietyAdapter(Context convertView,ArrayList<HashMap<String, String>> list) {
		super();
		// TODO Auto-generated constructor stub
		this.arrayList=list;
		this.context=convertView;
		layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public HashMap<String, String> getItem(int position) {
		return arrayList.get(position);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if (convertView==null) {
			convertView=(View)layoutInflater.inflate(R.layout.list_item_tv_edt_bt, null);
			viewHolder=new ViewHolder();
			viewHolder.textView=(TextView)convertView.findViewById(R.id.tv_listitem);
			viewHolder.editText=(EditText)convertView.findViewById(R.id.edittext);
			viewHolder.button=(Button)convertView.findViewById(R.id.bt_listitem);
			convertView.setTag(viewHolder);
			
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(getItem(position).get(Constants.KEY));
		viewHolder.editText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						viewHolder.editText.setText(new StringBuilder().append(year)
						.append("-").append(monthOfYear + 1).append("-").append(dayOfMonth)
						.append(""));
					}
				}, 2014, 3, 8).show();
			}
		});
		
		viewHolder.button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogUtil.toastShort(context, "点击了按钮"+position);
			}
		});
		return convertView;
	}
	
	private static class ViewHolder{
		TextView textView;
		EditText editText;
		Button button;
	}

}
