package com.bt.zoomimage;

import android.widget.TextView;

public class ZoomTextView extends ZoomView<TextView> {
	/** 最小字体 */
	public   float MIN_TEXT_SIZE = 12.0f;

	/** 最大子图 */
	public static final float MAX_TEXT_SIZE = 24.0f;

	/** 缩放比例 */
	float scale;

	/** 设置字体大小 */
	float textSize;

	public ZoomTextView(TextView view, float scale) {
		super(view);
		this.scale = scale;
		textSize = view.getTextSize();
		MIN_TEXT_SIZE = view.getTextSize();
	}

	/**
	 * 放大
	 */
	protected void zoomOut() {
		textSize += scale;
		if (textSize > MAX_TEXT_SIZE) {
			textSize = MAX_TEXT_SIZE;
		}
		view.setTextSize(textSize);
	}

	/**
	 * 缩小
	 */
	protected void zoomIn() {
		textSize -= scale;
		if (textSize < MIN_TEXT_SIZE) {
			textSize = MIN_TEXT_SIZE;
		}
		view.setTextSize(textSize);
	}

}