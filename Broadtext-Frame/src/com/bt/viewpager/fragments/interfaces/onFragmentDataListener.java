package com.bt.viewpager.fragments.interfaces;

import android.os.Bundle;

/**
 * 数据回调
 * @author XJ
 *
 */
public interface onFragmentDataListener {

	/**
	 * 用于fragment间，fragment与Activity间通信
	 * @param obj 需要传递的数据
	 */
	public void onFragmentDataBack(Bundle obj);
}
