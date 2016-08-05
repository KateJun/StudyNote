package com.bt.zoomimage;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bt.R;
import com.bt.photo.util.PictureUtil;

public class ZoomAndCircleImageTest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		init();
	}
	
	private void init(){
		LinearLayout layout =  new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);
		
		TextView tv = new TextView(this);
		tv.setPadding(10, 10, 10, 10);
		tv.setText("Multiple touch can zoom the image. ");
		
		ZoomImageView img = new ZoomImageView(this);
		Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
		img.setImageBitmap(PictureUtil.toOvalBitmap(bitmap));
        LayoutParams child = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        img.setLayoutParams(child);
        tv.setLayoutParams(child);
        layout.addView(img, child);
        layout.addView(tv, 0);
		
		setContentView(layout);
		
	}
}
