package com.bt.viewpager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bt.listview.MainActivity;
import com.bt.utils.LogUtil;
import com.bt.viewpager.fragments.ListFragmentA;
import com.bt.viewpager.fragments.interfaces.onFragmentDataListener;
import com.bt.R;

@SuppressLint("NewApi")
public class FragmentCommunicate extends FragmentActivity implements onFragmentDataListener{

	private ActionBar bar;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.fragment_container);
		bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayHomeAsUpEnabled(true);//类似返回按钮，出现在title栏左边
		bar.setDisplayShowTitleEnabled(false);//隐藏Activity的title
		bar.setDisplayUseLogoEnabled(false);//隐藏title栏的Logo
		bar.show();
		 
		initTabs(); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater  inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int itemID = item.getItemId();
		System.out.println(itemID);
		switch (itemID) {
		case R.id.action_settings:
			 showDialog();
			break;
		case android.R.id.home: 
	            Intent intent = new Intent(this, MainActivity.class); 
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	            startActivity(intent); 
	            return true; 
		default:
			if (itemID != R.id.action_settings2) {
				showDialog();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Tips");
		builder.setMessage("You have clicked  actionbar or menus." +
				"\n\rActionbar add tabs, tabs are fragments." +
				"The fragment extends android.app.Fragment");
		builder.setCancelable(true);
		builder.setPositiveButton("OK", null);
		builder.setNegativeButton("Cancel", null);
		builder.create().show();
	}

	/**
	 * 初始化Tabs
	 */
	private void initTabs(){
		Fragment fragmentA = new ListFragmentA();
		Bundle ba = fragmentA.getArguments();
		if (ba == null) {
			ba= new Bundle();
		}
		ba.putString("title", "Tab-A");
		fragmentA.setArguments(ba);
		
		Fragment fragmentB = new ListFragmentA();
		Bundle bb= fragmentB.getArguments();
		if (bb == null) {
			bb = new Bundle();
		}
		bb.putString("title", "Tab-B");
		fragmentB.setArguments(bb);
		 
		bar.addTab(bar.newTab().setText("Tab-A").setTabListener(new TabListener<Fragment>(this,"taba",fragmentA)));
		bar.addTab(bar.newTab().setText("TAB-B").setTabListener(new TabListener<Fragment>(this,"tabb",fragmentB)));
		 
	}
	
	
	@Override
	public void onFragmentDataBack(Bundle obj) {
		// TODO Auto-generated method stub
		LogUtil.toastShort(this,obj.getString("title")+obj.getString("pos"));
		System.out.println("click :"+obj.getString("title")+obj.getString("pos"));
	}  
	 
	
	private class TabListener<T extends Fragment> implements ActionBar.TabListener {
		private  Fragment mFragment;
		private String tag ;
		public TabListener(Context ct,String tag , Fragment fragment) {
			this.mFragment = fragment;
			this.tag = tag;
		}


		@Override
		public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
				ft.add(R.id.fragment_content, mFragment, tag);
		}

		@Override
		public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
			ft.remove(mFragment);
		}

		@Override
		public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
			LogUtil.toastShort(FragmentCommunicate.this, "Reselected");
		}

	}


}
