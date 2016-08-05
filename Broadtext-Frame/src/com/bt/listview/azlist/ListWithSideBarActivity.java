package com.bt.listview.azlist;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.bt.listview.azlist.util.ContactBean;
import com.bt.listview.azlist.util.ContactInfoService;
import com.bt.listview.azlist.util.MySectionIndexer;
import com.bt.listview.azlist.util.PinnedHeaderListView;
import com.bt.listview.azlist.util.PinyinUtils;
import com.bt.listview.azlist.util.SideBar;
import com.bt.listview.azlist.util.SideBar.OnTouchingChangedListener;
import com.bt.R;

public class ListWithSideBarActivity extends Activity implements OnItemClickListener{

	private PinnedHeaderListView listView;
	private ContactListAdapter  adapter ;
//	private WindowManager wm;
	
	protected int[] counts;
	protected MySectionIndexer indexer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_with_sidebar);
 
		init();
		counts = new int[SideBar.sections.length];
		//不能监听到新增联系人，但能避免界面错乱
		runOnUiThread(run);
	}
	
	private void init (){
		listView = (PinnedHeaderListView)findViewById(R.id.lv_id);
		SideBar bar = (SideBar)findViewById(R.id.sideBar);
		final TextView tvChar = (TextView)findViewById(R.id.tvChar);
		bar.setTextView(tvChar);
		bar.setOnTouchingChangedListener(new OnTouchingChangedListener() {
			
			@Override
			public void onTouchingChanged(String s) {
				// TODO Auto-generated method stub
				//滑动，定位，显示列表
				int position = indexer.getPositionForSection(SideBar.az.indexOf(s));
				if (position != -1) {
					listView.setSelection(position);
				}
			}
		}) ;
		listView.setOnItemClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//能够动态的加载新增的联系人，存在bug：点击item跳转，返回，界面title消失
//		runOnUiThread(run);
		
	}
	

	//获取所有联系人
	private final Runnable run = new Runnable() {
		@Override
		public void run() {
			final ArrayList<ContactBean> lists = ContactInfoService
					.getInstance(ListWithSideBarActivity.this).getContact();
			 
			PinyinUtils.getOrderChar(lists);
			
			for(ContactBean bean : lists){	//计算全部联系人
				String firstCharacter = bean.firstCharName;
				if (TextUtils.isEmpty(firstCharacter)) {//未命名的
					counts[0]++;
				}else {
					int index = SideBar.az.indexOf(firstCharacter);
					counts[index]++;
				}
			}
			indexer = new MySectionIndexer(SideBar.sections , counts);
			if (adapter == null) {
				adapter = new ContactListAdapter(lists , indexer , ListWithSideBarActivity.this);
				listView.setAdapter(adapter);
				listView.setOnScrollListener(adapter);
				//设置顶部固定View
				listView.setPinnedHeaderView(LayoutInflater.from(getApplicationContext()).inflate(  
		                R.layout.contact_list_title, listView, false));  
			}else {
				adapter.upData(lists,indexer);
				
			}
		}

	};
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (position < 0 || position > parent.getCount()) {
			return ;
		}
		ContactBean  bean = (ContactBean) parent.getItemAtPosition(position);
		System.out.println(bean.getContactName()+"============="+bean.getContactPhone());
		 
//		callPhoneActivity(this, Uri.parse("tel:"+ bean.getContactPhone()));
		sendSMSActivity(this, Uri.parse("smsto:"+ bean.getContactPhone()));
//		send2SMSActivity(this, Uri.parse("smsto:"+ bean.getContactPhone()));
	}
	
	/**
	 * 拔打电话
	 * @param context
	 * @param data
	 */
	public void callPhoneActivity(Context context,Uri data){
		Intent intent = new Intent(Intent.ACTION_CALL,data);
		context.startActivity(intent);
	}
	
	/**
	 * 发短信
	 * @param context
	 * @param data
	 */
	public void sendSMSActivity(Context context,Uri data){
		Intent it = new Intent(Intent.ACTION_SENDTO,data); 
		it.putExtra("sms_body", "TheSMS text");
		context.startActivity(it); 
	}
	
	/**
	 * 已发送界面
	 * @param context
	 * @param data
	 */
	public void send2SMSActivity(Context context,Uri data){
		Intent it = new Intent(Intent.ACTION_MAIN,data); 
		it.putExtra("sms_body", "TheSMS text");
		it.setType("vnd.android-dir/mms-sms"); 
		context.startActivity(it); 
	}

}
