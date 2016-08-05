package com.bt.listview;


import uk.co.senab.photoview.sample.LauncherActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bt.album.AlbumBuketActivity;
import com.bt.code.util.QRActivity;
import com.bt.listview.azlist.ListWithSideBarActivity;
import com.bt.listview.clickaddmore.ListViewDownloadActivity;
import com.bt.listview.grouplist.drag.DragListActivity;
import com.bt.listview.multipleitem.MultipleItemsList;
import com.bt.listview.myexpandable.QQExpandableListMain;
import com.bt.listview.pullrefresh.XListViewActivity;
import com.bt.listview.scroll.ScollAndListview;
import com.bt.listview.tv_edt_bt.ItemVarietyList;
import com.bt.listview.waterlist.WaterfallListViewActivity;
import com.bt.photo.PicChooseActivity;
import com.bt.progressbar.VerticalViewActivity;
import com.bt.qqlogin.QQLoginActivity;
import com.bt.residemenu.MenuActivity;
import com.bt.service.BroadcastActivity;
import com.bt.slidingmenu.SlidingMenuActivity;
import com.bt.viewflipper.ViewFlipperActivity;
import com.bt.viewpager.FragmentCommunicate;
import com.bt.viewpager.FragmentTabs;
import com.bt.viewpager.FragmentTabsPager;
import com.bt.viewpager.LoaderThrottleSupport;
import com.bt.volley.JsonRequestAcitivity;
import com.bt.volley.VolleyTest;
import com.bt.wheelview.WheelViewTest;
import com.bt.zipfile.util.FileZipActivity;
import com.bt.zoomimage.ZoomAndCircleImageTest;
import com.bt.R;

public class MainActivity extends Activity {

	private ListView listView;
	private Class<?>[] activityClass  
		= {
			ListViewDownloadActivity.class,
			ItemVarietyList.class, MultipleItemsList.class,
			ScollAndListview.class ,DragListActivity.class,XListViewActivity.class,
			QQExpandableListMain.class,
			LoaderThrottleSupport.class,FragmentTabs.class,FragmentTabsPager.class,
			ViewFlipperActivity.class , FragmentCommunicate.class,
			JsonRequestAcitivity.class,VolleyTest.class,WheelViewTest.class,
			ZoomAndCircleImageTest.class,PicChooseActivity.class,AlbumBuketActivity.class,
			QRActivity.class,ListWithSideBarActivity.class,WaterfallListViewActivity.class,
			MenuActivity.class,SlidingMenuActivity.class,BroadcastActivity.class,
			FileZipActivity.class,VerticalViewActivity.class,QQLoginActivity.class,
			LauncherActivity.class,com.zbar.lib.CaptureActivity.class
			
		};

	private String[] titleStrings
		= {
			"1.Click add more","2.Item contain tv.edt.bt","3.Item contain two differrent layout","4.Listview in scrollview" 
			,"5.Drag groupList","6.Pullrefresh" ,"7.Like QQ group","8.FragmentActivity","9.FragmentTabs","10.FragmentTabs and ViewPager",
			"11.ViewFlipper",
			"12.ActionBar,Fragment communicate ","13.Volley-JsonRequest","14.Volley-ImageLoader","15.WheelView-DateDialog&ShareInfo",
			"16.ZoomImage and CirclePhoto","17.Camera Photo","18.User define Album","19.QR Code Create&Scan","20.SideBar for Contact",
			"20.Waterfall ListView","21.ReSideMenu","22.SlidingMenu","23.Broadcast&Notification","24.File to Zip",
			"26.ProgressBars","27.QQ Login","28.PhotoView(ZoomImage)","29.ZBar Scan"
		};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTitle("效果集合");
		//讲菜单栏变成3个小点，版本4.0以上
//		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		setContentView(R.layout.list);
		
		initDate();
		
	}

   private void initDate() {
		listView = (ListView) findViewById(R.id.lv_id);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(),  R.layout.list_item,
				 R.id.tv , titleStrings);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position < 0 || position > activityClass.length - 1) {
					return;
				}
				Intent intent=new Intent(getApplicationContext(), activityClass[position]);
				startActivity(intent);
			}
		});
		listView.setSelection(adapter.getCount()-1);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.actionbar_menu, menu);
//		return true;
//	}

}
