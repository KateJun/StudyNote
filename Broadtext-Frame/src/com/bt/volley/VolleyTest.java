package com.bt.volley;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.bt.utils.LogUtil;
import com.bt.volley.util.VolleyTool;
import com.bt.R;

public class VolleyTest extends Activity {

	private ListView lv_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		lv_id = (ListView)findViewById(R.id.lv_id);
		loadImg();
	}
	
	private void loadImg(){
		int radom = (int) (Math.random()*10);
		boolean isNetWorkImage = false;
		if (radom > 5) {
			isNetWorkImage = true;
			LogUtil.toastShort(this, "NetWorkImageView显示图片");
		}
		VolleyAdapter adapter = new VolleyAdapter(this,isNetWorkImage);
		lv_id.setAdapter(adapter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		VolleyTool.getInstance(this).getmRequestQueue().cancelAll(getClass().getSimpleName());
	}
}
