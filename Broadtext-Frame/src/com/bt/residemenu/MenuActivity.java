package com.bt.residemenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.bt.residemenu.view.ResideMenu;
import com.bt.residemenu.view.ResideMenuItem;
import com.bt.R;

public class MenuActivity extends FragmentActivity implements OnClickListener {

	private ResideMenu resideMenu;
	private ResideMenuItem itemHome;
	private ResideMenuItem itemProfile;
	private ResideMenuItem itemCalendar;
	private ResideMenuItem itemSettings;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.menu_activity);
		setUpMenu();
		changeFragment(new FragmentHome());
	}

	private void setUpMenu() {
		// attach to current activity;
		resideMenu = new ResideMenu(this);
		resideMenu.setBackground(R.drawable.share_image);
		resideMenu.attachToActivity(this);
		resideMenu.setMenuListener(menuListener);

		// create menu items;
		itemHome = new ResideMenuItem(this, R.drawable.ic_launcher, "Home");
		itemProfile = new ResideMenuItem(this, R.drawable.ic_launcher, "Profile");
		itemCalendar = new ResideMenuItem(this, R.drawable.ic_launcher, "Calendar");
		itemSettings = new ResideMenuItem(this, R.drawable.ic_launcher, "Settings");

		itemHome.setOnClickListener(this);
		itemProfile.setOnClickListener(this);
		itemCalendar.setOnClickListener(this);
		itemSettings.setOnClickListener(this);

		resideMenu.addMenuItem(itemHome);
		resideMenu.addMenuItem(itemProfile);
		resideMenu.addMenuItem(itemCalendar);
		resideMenu.addMenuItem(itemSettings);

		findViewById(R.id.title_bar_menu).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						resideMenu.openMenu();
					}
				});
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return resideMenu.onInterceptTouchEvent(ev)
				|| super.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == itemHome) {
			changeFragment(new FragmentHome());
		} else if (view == itemProfile) {
			changeFragment(FragmentTemp.getInstance("Profile"));
		} else if (view == itemCalendar) {
			changeFragment(FragmentTemp.getInstance("Calendar"));
		} else if (view == itemSettings) {
			changeFragment(FragmentTemp.getInstance("Settings"));
		}
		resideMenu.closeMenu();
	}

	private void changeFragment(Fragment targetFragment) {
		resideMenu.clearIgnoredViewList();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
		@Override
		public void openMenu() {
			Toast.makeText(getBaseContext(), "Menu is opened!",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void closeMenu() {
//			Toast.makeText(getBaseContext(), "Menu is closed!",
//					Toast.LENGTH_SHORT).show();
		}
	};

	public ResideMenu getResideMenu() {
		return resideMenu;
	}
}
