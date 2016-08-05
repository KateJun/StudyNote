package com.bt.progressbar.vertical;

import com.bt.R;

import android.widget.ProgressBar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

public class BatteryView extends ProgressBar {

	private int viewWidth;
	private int viewHeight;

	public BatteryView(Context context) {
		super(context);
		initView(context);
	}

	public BatteryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public BatteryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		this.setMeasuredDimension(viewWidth, viewHeight);

	}

	private void initView(Context context) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) context.getResources()
				.getDrawable(R.drawable.ic_launcher);
		Bitmap bitmap = bitmapDrawable.getBitmap();
		viewWidth = bitmap.getWidth();
		viewHeight = bitmap.getHeight();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldw, oldh);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		// 旋转
		canvas.rotate(-90);
		canvas.translate(-this.getHeight(), 0);
		super.onDraw(canvas);
	}
}
