package com.bt.wheelview.util;

import java.util.Calendar;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.bt.wheelview.util.WheelView.OnWheelScrollListener;
import com.bt.R;

public class WheelViewDateDialog extends Dialog implements
		android.view.View.OnClickListener, OnWheelScrollListener {

	private Button btn_cancel;
	private Button btn_sure;
	private NumericWheelAdapter<Integer> year_adapter;
	private NumericWheelAdapter<Integer> month_adapter;
	private NumericWheelAdapter<Integer> day_adapger;

	private IDatebackListener listener;
	private WheelView day;
	private WheelView year;
	private WheelView month;
	private int sumDay=0;
	private  int id;
	private Window window;
	private String  charset ;
	
	/**
	 * 日期回调接口，返回选择的日期
	 * @author XJ
	 *
	 */
	public interface IDatebackListener {
		/**
		 * 日期回调接口
		 * @param dateString 日期:yyyy-MM-dd
		 * @param id 用于区分多个日期选择
		 */
		public void onDateBack(String dateString ,int id);
	}
	
	/**
	 * 构造日期对话框
	 * @param context 上下文引用，Activity必须实现接口IDatebackListener
	 * @param charset 显示的格式  如"%1$02d"则显示为01
	 * @param id 用于区分多个日期选择
	 */
	public WheelViewDateDialog(Context context ,String charset, int id) {
		super(context);
		this.id = id;
		this.charset =charset;
		init(context);
		
	}
	
	/**
	 * 构造日期对话框
	 * @param context 上下文引用，Activity必须实现接口IDatebackListener
	 * @param charset 显示的格式  如"%1$02d"则显示为01
	 */
	public WheelViewDateDialog(Context context,String charset) {
		this(context,charset,0);
	}
	/**
	 * 构造日期对话框
	 * @param context 上下文引用，Activity必须实现接口IDatebackListener
	 */
	public WheelViewDateDialog(Context context) {
		this(context,"%1$02d",0);
	}

	
	private void init(Context ct){
		if (ct instanceof IDatebackListener) {
			listener = (IDatebackListener)ct;
		}
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.time_layout);
		setCanceledOnTouchOutside(true);
		btn_sure = (Button) findViewById(R.id.lay_left);
		btn_cancel = (Button) findViewById(R.id.lay_right);
		btn_sure.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);

		year = (WheelView) this.findViewById(R.id.year);
//	   year.setVisibleItems(7);
		year.setLabel("年");
		 
		month = (WheelView) this.findViewById(R.id.month);
		month.setLabel("月");
		day = (WheelView) this.findViewById(R.id.day);
		day.setLabel("日");

		year.setCyclic(true);
		month.setCyclic(true);
		day.setCyclic(true);
		
		Calendar c = Calendar.getInstance();
		int curMonth = c.get(Calendar.MONTH);
		int curYear = c.get(Calendar.YEAR);
		int curDay = c.get(Calendar.DAY_OF_MONTH);

//		System.out.println(curYear+"-"+curMonth+"-"+curDay);
		year_adapter = new NumericWheelAdapter<Integer>(curYear - 99 , curYear);
		year.setAdapter(year_adapter);
		year.setCurrentItem(curYear+85);
		
		month_adapter = new NumericWheelAdapter<Integer>(01, 12,charset);
		month.setAdapter(month_adapter);
		month.setCurrentItem(curMonth);

		year_adapter.setValue(curYear+"");
		month_adapter.setValue(curMonth+"");
		sumDay = setwheelDay(Integer.parseInt(year_adapter.getValues()),
				Integer.parseInt(month_adapter.getValues()));
		day_adapger = new NumericWheelAdapter<Integer>(01, sumDay,charset);
		day.setAdapter(day_adapger);
		day.setCurrentItem(curDay-1);

		year.addScrollingListener(this);
		month.addScrollingListener(this);
		
        windowDeploy(0, 0);
		show();
		
	}

	 //设置窗口显示
    public void windowDeploy(int x, int y){
        window = getWindow(); //得到对话框
//        window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        window.setBackgroundDrawableResource(android.R.color.transparent); //设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        Display d = getWindow().getWindowManager().getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);
        wl.width = (int) (p.x*1);
//        wl.height = (int) (p.y * 0.55);
        //根据x，y坐标设置窗口需要显示的位置
        wl.x = x; //x小于0左移，大于0右移
        wl.y = y; //y小于0上移，大于0下移  
        wl.alpha = 1.0f; //设置透明度
        wl.gravity = Gravity.BOTTOM; //设置重力
        window.setAttributes(wl);
    }
	@Override
	public void onClick(View v) {
		if (listener != null) {
			if (v.equals(btn_sure)) {
				listener.onDateBack(year_adapter.getValues() + "-"
						+ month_adapter.getValues() + "-"
						+ day_adapger.getValues(),id);
			} else {
				listener.onDateBack("",id);
			}
			dismiss();
		}else {
			new Exception("Your activitiy must implements interface IDatebackListener.").printStackTrace();
		}
	}

	private int setwheelDay(int year, int month) {
		int day = 31;
		if (month == 2) {
			if ((year % 4 == 0) && ((year % 100 != 0) | (year % 400 == 0))) {
				day = 29;
			} else {
				day = 28;
			}
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			day = 30;
		}
		return day;
	}

	@Override
	public void onScrollingStarted(WheelView wheel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollingFinished(WheelView wheel) {
		if (wheel.equals(year) || wheel.equals(month)) {
			
			System.out.println(year.getCurrentItem()+"----位置-----"+month.getCurrentItem());
			System.out.println(year_adapter.getItem(year.getCurrentItem())+"==scroll值=="+month_adapter.getItem(month.getCurrentItem()));
			sumDay = setwheelDay(
					Integer.parseInt(year_adapter.getItem(year.getCurrentItem())),
					Integer.parseInt(month_adapter.getItem(month.getCurrentItem())));
			day_adapger = new NumericWheelAdapter<Integer>(01, sumDay,charset);
			day.setAdapter(day_adapger);
			
		}
	}


}
