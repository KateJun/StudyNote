package com.bt.listview.waterlist;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bt.listview.waterlist.internal.PLA_AdapterView;
import com.bt.listview.waterlist.internal.PLA_AdapterView.OnItemClickListener;
import com.bt.listview.waterlist.lib.MultiColumnListView;
import com.bt.listview.waterlist.lib.MultiColumnListView.OnLoadMoreListener;
import com.bt.listview.waterlist.lib.MultiColumnPullToRefreshListView;
import com.bt.listview.waterlist.lib.MultiColumnPullToRefreshListView.OnRefreshListener;
import com.bt.utils.Data;
import com.bt.utils.FuncUtils;
import com.bt.utils.LogUtil;
import com.bt.volley.util.VolleyTool;
import com.bt.R;


/**
 * 瀑布流
 * @author XJ
 * @date 2014-4-25
 *
 */
public class WaterfallListViewActivity extends Activity {

	private MultiColumnListView multiColumnListView;
	private MultiColumnPullToRefreshListView pullToRefreshListView;
	private ArrayList<String> urls;
	private ImageLoaderAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		multiListView();

//		multiPullRefeshListView();

	}
 
	private void multiListView(){
		setContentView(R.layout.act_sample);
		iniUrl();
		multiColumnListView = (MultiColumnListView)findViewById(R.id.multi_list);
		adapter = new ImageLoaderAdapter(this,urls);
		multiColumnListView.setAdapter(adapter);
		
		//定义最大图片宽度
//		int width = getWindowManager().getDefaultDisplay().getWidth();
//      int itemWidth = width / 2;  
////        int itemWidth = (width - 20)/2; //随便写了个20 限定大小，不限定也可以
//		ImageRequestAdapter requestAdapter = new ImageRequestAdapter(this, urls, itemWidth);
//		multiColumnListView.setAdapter(requestAdapter);
		
//		multiColumnListView.setOnLoadMoreListener(new OnLoadMoreListener() {
//	
//			@Override
//			public void onLoadMore() {
//				// TODO Auto-generated method stub
//				System.out.println("on load more");
//				if (urls != null && urls.size() != 0) {
//					urls.add(urls.size(),Data.addUrl);
//					adapter.notifyDataSetChanged();
//				}
//				
//			}
//		});
		multiColumnListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				LogUtil.toastShort(getBaseContext(), "click"+position);
			}
		});
	}
	
	/**
	 * 可下拉刷新的瀑布流
	 */       
	private void multiPullRefeshListView(){
		setContentView(R.layout.act_pull_to_refresh_sample);//可下拉刷新
		iniUrl();
		pullToRefreshListView = (MultiColumnPullToRefreshListView)findViewById(R.id.multi_list);
		adapter = new ImageLoaderAdapter(this,urls);
		pullToRefreshListView.setAdapter(adapter);
		
		pullToRefreshListView.setTextPullToRefresh("下拉刷新");
		pullToRefreshListView.setTextRefreshing("刷新中");
		pullToRefreshListView.setTextReleaseToRefresh("松开刷新");
		
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				System.out.println("on refresh");
				if (urls != null && urls.size() != 0) {
					urls.add(urls.size(),Data.addUrl);
					adapter = new ImageLoaderAdapter(WaterfallListViewActivity.this, urls);
					pullToRefreshListView.setAdapter(adapter);
//					adapter.notifyDataSetChanged();
					pullToRefreshListView.setSelection(adapter.getCount()-1);
					 
				}
			}
		});
		
		pullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				LogUtil.toastShort(getBaseContext(), "click"+position);
			}
		});
		 
	}
	
	
	private void iniUrl() {
		urls = new ArrayList<String>();
		for (int i = 0; i < Data.UrlBean.urls.length; i++) {
			urls.add(Data.UrlBean.urls[i]);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		VolleyTool.getInstance(this).getmRequestQueue().cancelAll(WaterfallListViewActivity.class.getSimpleName());
	}
}
