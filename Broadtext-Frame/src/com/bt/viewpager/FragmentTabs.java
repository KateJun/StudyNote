/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bt.viewpager;



import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.bt.viewpager.fragments.AppListFragment;
import com.bt.viewpager.fragments.CountingFragment;
import com.bt.viewpager.fragments.CursorLoaderListFragment;
import com.bt.viewpager.fragments.ThrottledLoaderListFragment;
import com.bt.R;

/**
 * This demonstrates how you can implement switching between the tabs of a
 * TabHost through fragments, using FragmentTabHost.
 */
public class FragmentTabs extends FragmentActivity implements OnTabChangeListener{
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_tabs);
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		mTabHost.addTab(getTabSpec("Simple"), CountingFragment.class, null);
		mTabHost.addTab(getTabSpec("Contacts"), CursorLoaderListFragment.class,null);
		mTabHost.addTab(getTabSpec("Custom"), AppListFragment.class, null);
		mTabHost.addTab(getTabSpec("Throttle"),ThrottledLoaderListFragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator("Simple"),
//        		CountingFragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("contacts").setIndicator("Contacts"),
//        		 CursorLoaderListFragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("custom").setIndicator("Custom"),
//        		AppListFragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("throttle").setIndicator("Throttle"),
//                ThrottledLoaderListFragment.class, null);
		mTabHost.setOnTabChangedListener(this);
		mTabHost.setCurrentTab(0);
    }
    
    private TabSpec getTabSpec(String tag){
    	TabSpec tab = mTabHost.newTabSpec(tag);
    	View v = View.inflate(this, R.layout.list_item_icon_text_v, null);
    	tab.setIndicator(v);
    	v.setBackground(getResources().getDrawable(R.drawable.tabone));
    	return tab;
    }

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		System.out.println("tabid="+tabId);
	}
}

