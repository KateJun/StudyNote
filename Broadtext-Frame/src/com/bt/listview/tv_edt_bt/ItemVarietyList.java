package com.bt.listview.tv_edt_bt;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bt.utils.Data;
import com.bt.utils.LogUtil;
import com.bt.R;

/**
 * Item含有textview,edittext,button
 * @author XJ
 *
 */
public class ItemVarietyList extends Activity implements OnItemClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		ListView listView = (ListView) findViewById(R.id.lv_id);

		ArrayList<HashMap<String, String>> arrayList = Data.arrayList;
		listView.setAdapter(new ItemVarietyAdapter(this, arrayList));
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		LogUtil.toastShort(this, "点击了item"+position);
		
	}
	
}
