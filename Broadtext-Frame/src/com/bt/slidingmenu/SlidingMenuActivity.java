package com.bt.slidingmenu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.bt.R;
import com.slidingmenu.lib.SlidingMenu;

public class SlidingMenuActivity extends Activity {

	private SlidingMenu menu ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		Button button = new Button(this);
		button.setText("Left");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menu.showMenu();
			}
		});

		Button button2 = new Button(this);
		button2.setText("Right");
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menu.showSecondaryMenu();
			}
		});
		RelativeLayout view = new RelativeLayout(this);
		LayoutParams llp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		llp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		view.addView(button,llp);
		LayoutParams rlp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		view.addView(button2,rlp);
		view.setBackgroundColor(Color.YELLOW);
		
		setContentView(view);
		
		DisplayMetrics metrics=new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setAboveOffsetRes(R.dimen.slidingmenu_offset);
		menu.setBehindWidth(metrics.widthPixels*5/6);
		menu.setFadeDegree(0.55f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.fragment_temp);


		menu.setMode(SlidingMenu.LEFT_RIGHT);
		menu.setSecondaryMenu(R.layout.fragment_temp);
		menu.setSecondaryShadowDrawable(R.drawable.shadow);
		menu.setShadowDrawable(R.drawable.shadow);
	}
}
