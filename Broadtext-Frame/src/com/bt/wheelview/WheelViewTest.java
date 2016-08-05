package com.bt.wheelview;


import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bt.utils.FuncUtils;
import com.bt.utils.LogUtil;
import com.bt.wheelview.util.NumericWheelAdapter;
import com.bt.wheelview.util.WheelView;
import com.bt.wheelview.util.WheelViewDateDialog;
import com.bt.wheelview.util.WheelViewDateDialog.IDatebackListener;
import com.bt.R;


public class WheelViewTest extends Activity implements IDatebackListener,OnClickListener{

	private NumericWheelAdapter<Integer> grade_adapter;
	private NumericWheelAdapter<Integer> num_adapter;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wheelviewtest);
		
		 WheelView wheel_grade = (WheelView) this.findViewById(R.id.wheel_grade);
		 WheelView	wheel_num = (WheelView) this.findViewById(R.id.wheel_num);
		 grade_adapter = new NumericWheelAdapter<Integer>(1, 30,"%1$02d");
		 num_adapter=new  NumericWheelAdapter<Integer>(0, 20,"%1$02d");
		 wheel_grade.setAdapter(grade_adapter);
		 wheel_num.setAdapter(num_adapter);
		 tv=(TextView)findViewById(R.id.tv_housecode);
		 
		 //获得wheelview数据
		 findViewById(R.id.bt_wheelview).setOnClickListener(this);
		 //分享图片或者文字
		 findViewById(R.id.bt_wheelview_share).setOnClickListener(this);
		 //日期对话框
		 findViewById(R.id.bt_wheelview_dialog).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_wheelview:
			String s= grade_adapter.getValues()+num_adapter.getValues();
			tv.setText(s);
			break;
		case R.id.bt_wheelview_dialog:
			new WheelViewDateDialog(this);
			break;
		case R.id.bt_wheelview_share:
			FuncUtils.shareInfo(this, "BroadtextFrame", 
					"BroadtextFrame", "来自Broadtext分享", 
					Environment.getExternalStorageDirectory()+"/share_image.png");
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onDateBack(String dateString, int id) {
		LogUtil.syso(dateString);
		LogUtil.toastShort(this, dateString);
	}
}
