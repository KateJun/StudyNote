package com.bt.viewflipper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.bt.R;
public class VflipperLayout extends RelativeLayout{
	private static final int ID_RADIOGROUP_LOC = 11;
	private static final int ID_RADIOBTN_GPRS = 12;
	private static final int ID_RADIOBTN_WIFI = 13;
	private static final int ID_RADIOBTN_GPS = 14;
	private RadioGroup  mRadioGroupLoc;
	private RadioButton mRadioBtnGprs;
	private RadioButton mRadioBtnWifi;
	private RadioButton mRadioBtnGps;
	private ViewFlipper mViewFlipperLoc;
	private ViewLayoutA mViewLayoutA;
	private ViewLayoutB mViewLayoutB;
	private ViewLayoutC mViewLayoutC;
	private Context ct ; 
	public VflipperLayout(Context context) {
		super(context);
		this.ct = context;
		initLayout(context);
		// TODO Auto-generated constructor stub
	}

	private void initLayout(Context context) {
		// TODO Auto-generated method stub
		Bitmap bmNull=null;
		BitmapDrawable bDrawable=new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.browse_bg_tile));
		bDrawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		this.setBackgroundDrawable(bDrawable);
		
		LayoutParams relativeParams=new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mRadioGroupLoc=new RadioGroup(context);
		mRadioGroupLoc.setOrientation(RadioGroup.HORIZONTAL);
		mRadioGroupLoc.setId(ID_RADIOGROUP_LOC);
		mRadioGroupLoc.setGravity(Gravity.CENTER_HORIZONTAL);
		this.addView(mRadioGroupLoc, relativeParams);
		
		RadioGroup.LayoutParams redioGroupParams=new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		redioGroupParams.weight=1;
		mRadioBtnGprs=new RadioButton(context);
		mRadioBtnGprs.setBackgroundResource(R.drawable.nav_inactive_tile);
		mRadioBtnGprs.setText("GPRS");
		mRadioBtnGprs.getPaint().setFakeBoldText(true);
		mRadioBtnGprs.setButtonDrawable(new BitmapDrawable(bmNull));
		mRadioBtnGprs.setId(ID_RADIOBTN_GPRS);
		mRadioBtnGprs.setTextColor(0xff839359);
		mRadioBtnGprs.setOnClickListener(mOnClickListener);
		mRadioBtnGprs.setGravity(Gravity.CENTER);
		mRadioGroupLoc.addView(mRadioBtnGprs,redioGroupParams);
		
		redioGroupParams=new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ImageView mImgSplitlineLeft=new ImageView(context);
		mImgSplitlineLeft.setImageResource(R.drawable.nav_divider);
		mRadioGroupLoc.addView(mImgSplitlineLeft, redioGroupParams);
		
		redioGroupParams=new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		redioGroupParams.weight=1;
		mRadioBtnWifi=new RadioButton(context);
		mRadioBtnWifi.setBackgroundResource(R.drawable.nav_inactive_tile);
		mRadioBtnWifi.setText("WIFI");
		mRadioBtnWifi.setButtonDrawable(new BitmapDrawable(bmNull));
		mRadioBtnWifi.getPaint().setFakeBoldText(true);
		mRadioBtnWifi.setId(ID_RADIOBTN_WIFI);
		mRadioBtnWifi.setTextColor(0xff839359);
		mRadioBtnWifi.setOnClickListener(mOnClickListener);
		mRadioBtnWifi.setGravity(Gravity.CENTER);
		mRadioGroupLoc.addView(mRadioBtnWifi,redioGroupParams);
		mRadioGroupLoc.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				changeCheckStatus(checkedId);
			}
		});
		
		redioGroupParams=new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ImageView mImgSplitlineRight=new ImageView(context);
		mImgSplitlineRight.setImageResource(R.drawable.nav_divider);
		mRadioGroupLoc.addView(mImgSplitlineRight, redioGroupParams);
		
		redioGroupParams=new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		redioGroupParams.weight=1;
		mRadioBtnGps=new RadioButton(context);
		mRadioBtnGps.setButtonDrawable(new BitmapDrawable(bmNull));
		mRadioBtnGps.setBackgroundResource(R.drawable.nav_inactive_tile);
		mRadioBtnGps.setText("GPS");
		mRadioBtnGps.setTextColor(0xff839359);
		mRadioBtnGps.getPaint().setFakeBoldText(true);
		mRadioBtnGps.setId(ID_RADIOBTN_GPS);
		mRadioBtnGps.setGravity(Gravity.CENTER);
		mRadioGroupLoc.addView(mRadioBtnGps,redioGroupParams);
		mRadioBtnGps.setOnClickListener(mOnClickListener);
		
		relativeParams=new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		relativeParams.addRule(RelativeLayout.BELOW,ID_RADIOGROUP_LOC);
		mViewFlipperLoc=new ViewFlipper(context);
		mViewFlipperLoc.setInAnimation(getContext(),R.anim.push_left_in);
		mViewFlipperLoc.setOutAnimation(getContext(),R.anim.push_left_out);
		mViewFlipperLoc.setPersistentDrawingCache(ViewGroup.PERSISTENT_ALL_CACHES);
		mViewFlipperLoc.setFlipInterval(1000);
		mViewFlipperLoc.setClickable(true);
		mViewFlipperLoc.setOnTouchListener(mOnTouchListener);
		this.addView(mViewFlipperLoc,relativeParams);

		relativeParams=new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mViewLayoutA=new ViewLayoutA(context);
		mViewLayoutB=new ViewLayoutB(context);
		mViewLayoutC=new ViewLayoutC(context);
		
		mViewFlipperLoc.addView(mViewLayoutA,relativeParams);
		mViewFlipperLoc.addView(mViewLayoutB,relativeParams);
		mViewFlipperLoc.addView(mViewLayoutC,relativeParams);
		
		changeCheckStatus(0);
	}

	private OnClickListener mOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case ID_RADIOBTN_GPRS:
				mViewFlipperLoc.setDisplayedChild(0);
				break;
			case ID_RADIOBTN_WIFI:
				mViewFlipperLoc.setDisplayedChild(1);
				break;
			case ID_RADIOBTN_GPS:
				mViewFlipperLoc.setDisplayedChild(2);
				break;
			default:
				break;
			}
		}
	};
	
	private void changeCheckStatus(int checkedId)
	{

		switch (checkedId) {
		case 0:
		case ID_RADIOBTN_GPRS:
			mRadioBtnGprs.setBackgroundResource(R.drawable.nav_active_2options);
			mRadioBtnWifi.setBackgroundResource(R.drawable.nav_inactive_tile);
			mRadioBtnGps.setBackgroundResource(R.drawable.nav_inactive_tile);
			break;
		case 1:
		case ID_RADIOBTN_WIFI:
			mRadioBtnGprs.setBackgroundResource(R.drawable.nav_inactive_tile);
			mRadioBtnWifi.setBackgroundResource(R.drawable.nav_active_2options);
			mRadioBtnGps.setBackgroundResource(R.drawable.nav_inactive_tile);
			break;
		case 2:
		case ID_RADIOBTN_GPS:
			mRadioBtnGprs.setBackgroundResource(R.drawable.nav_inactive_tile);
			mRadioBtnWifi.setBackgroundResource(R.drawable.nav_inactive_tile);
			mRadioBtnGps.setBackgroundResource(R.drawable.nav_active_2options);
			break;
		default:
			break;
		}
	}
    private void showNext()
    {
    	mViewFlipperLoc.setInAnimation(AnimationHelper.inFromLeftAnimation());
        mViewFlipperLoc.setOutAnimation(AnimationHelper.outToRightAnimation());
        mViewFlipperLoc.showNext();
        Log.e("debug", "showNext():"+mViewFlipperLoc.getDisplayedChild());
        changeCheckStatus(mViewFlipperLoc.getDisplayedChild());
    }
    private void showPrevious()
    {
    	  mViewFlipperLoc.setInAnimation(AnimationHelper.inFromRightAnimation());
    	  mViewFlipperLoc.setOutAnimation(AnimationHelper.outToLeftAnimation());
    	  mViewFlipperLoc.showPrevious();
    	  Log.e("debug", "showPrevious():"+mViewFlipperLoc.getDisplayedChild());
    	  changeCheckStatus(mViewFlipperLoc.getDisplayedChild());
    }
    
	private OnTouchListener mOnTouchListener=new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent ev) {
			// TODO Auto-generated method stub
			 return mGestureDetector.onTouchEvent(ev);  
		}
	};
    
      GestureDetector mGestureDetector =new GestureDetector(ct ,new OnGestureListener() {
		
		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {  
			if(velocityX<0)
			{
				showPrevious();
			}
			else
			{
	            showNext();
			}
			return false;
		}
		
		@Override
		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	}); 

	public static class AnimationHelper {
      public static Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, +1.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(350);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
      }

      public static Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, -1.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(350);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
      }

      // for the next movement
      public static Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, -1.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(350);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
      }

      public static Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, +1.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(350);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
      }
    }
}
