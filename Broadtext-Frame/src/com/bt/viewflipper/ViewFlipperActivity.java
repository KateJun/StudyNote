package com.bt.viewflipper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ViewFlipperActivity extends Activity {
    /** Called when the activity is first created. */
	private VflipperLayout viewfilpper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewfilpper=new VflipperLayout(this);
        setContentView(viewfilpper);
    }
}