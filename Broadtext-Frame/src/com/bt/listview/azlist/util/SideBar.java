package com.bt.listview.azlist.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * A-Z列表
 * 
 * @author XJ
 * 
 */
public class SideBar extends View {

	 
	public static String az = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static String[] sections = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
			"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
			"Y", "Z" };
	// 画笔
	private Paint p;
	// 显示当前选中位置
	private TextView mTextDialog;
	// bar字体大小
	private int mTextSize = 20;
	// bar字体颜色
	private int mTextColor = Color.BLACK;
	// bar选中字体颜色
	private int mTextColorChoose = Color.RED;

	// 选中位置
	private int choose = -1;
	
	private Handler handler = new Handler();
	//接口显示选中位置
	private OnTouchingChangedListener listener;

	public SideBar(Context context) {
		this(context, null);
	}

	public SideBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	// 初始化
	private void init() {
		p = new Paint();
		p.setAntiAlias(true);// 消除锯齿
		p.setFakeBoldText(true);
		p.setTextSize(mTextSize);
		p.setColor(mTextColor);

	}

	/******************************************/
	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}

	public int getmTextSize() {
		return mTextSize;
	}

	public void setmTextSize(int mTextSize) {
		this.mTextSize = mTextSize;
	}

	public int getmTextColor() {
		return mTextColor;
	}

	public void setmTextColor(int mTextColor) {
		this.mTextColor = mTextColor;
	}

	/*******************************/
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		// 获取焦点改变背景颜色.
		
		int height = getHeight();// 获取对应高度
		int width = getWidth(); // 获取对应宽度
		int singleHeight = height / sections.length;// 获取每一个字母的高度
		for (int i = 0; i < sections.length; i++) {
			init();
			if (i == choose) {
				p.setColor(mTextColorChoose);
				p.setFakeBoldText(true);
			}
			float x = width / 2 - p.measureText(sections[i]) / 2;
			float y = singleHeight * i + singleHeight;
			canvas.drawText(sections[i], x, y, p);
			p.reset();
		}
		super.onDraw(canvas);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		final int action = event.getAction();
		final float posY = event.getY();
		final int oldChoose = choose;
		// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.  
		final int charPos = (int) (posY / getHeight() * sections.length);
		
		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundColor(Color.WHITE);  
			choose = -1;//  
			invalidate();  
//			if (mTextDialog != null) {  
//				mTextDialog.setVisibility(View.INVISIBLE);  
//			} 
			dismissTextView();
			break;

		default:
			setBackgroundColor(Color.CYAN);
			if (oldChoose != charPos) {
				if (charPos > -1 && charPos < sections.length) {
					if (listener != null) {  
						listener.onTouchingChanged(sections[charPos]);  
					} 
//					if (mTextDialog != null) {  
//						mTextDialog.setText(String.valueOf(chars[charPos]));  
//						mTextDialog.setVisibility(View.VISIBLE);  
//					}  
					showTextView(sections[charPos]);
					choose = charPos;  
					invalidate();  
				}
			}
			break;
		}
		return true;
	}

	//显示
	private void showTextView(String charString){
		if (mTextDialog != null) {
			if (mTextDialog.getVisibility() != View.VISIBLE) {
				mTextDialog.setVisibility(View.VISIBLE);  
			} 
			mTextDialog.setText(charString);
		}
	}
	
	//隐藏
	private void dismissTextView() {  
		handler.postDelayed(dismissRunnable, 800);  
	}  

	private Runnable dismissRunnable = new Runnable() {  

		@Override  
		public void run() {  
			if (mTextDialog != null) {  
				mTextDialog.setVisibility(View.INVISIBLE);  
			}  
		}  
	};  
	
	 /** 
     * 向外公开的方法 
     *  
     * @param onTouchingChangedListener 
     */  
    public void setOnTouchingChangedListener(OnTouchingChangedListener onTouchingChangedListener) {  
        this.listener = onTouchingChangedListener;  
    }  
  
    /** 
     * 接口 
     *  
     */  
    public interface OnTouchingChangedListener {  
        public void onTouchingChanged(String s);  
    }  
}
