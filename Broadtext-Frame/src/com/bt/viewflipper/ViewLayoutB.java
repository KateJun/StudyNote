package com.bt.viewflipper;

import android.content.Context;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewLayoutB extends RelativeLayout{

	public ViewLayoutB(Context context) {
		super(context);
		initLayout(context);
		// TODO Auto-generated constructor stub
	}

	private void initLayout(Context context) {
		// TODO Auto-generated method stub
		LayoutParams relativeParams=new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		TextView mTxt=new TextView(context);
		mTxt.setText("ViewLayoutWIFI");
		mTxt.setGravity(Gravity.CENTER);
		mTxt.setTextSize(28.0f);
		this.addView(mTxt,relativeParams);
	}
}