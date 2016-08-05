package com.bt.listview.scroll;


import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bt.utils.Data;
import com.bt.R;
/**
 * scollview包含listview
 * @author XJ
 *
 */
public class ScollAndListview extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lv_and_scroll);
		
		ArrayList<String> arrayList= Data.strings;
		 
		ListView listView=(ListView)findViewById(R.id.lv_id);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,arrayList);
		listView.setAdapter(adapter);
	}
}
