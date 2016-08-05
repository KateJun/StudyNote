package com.bt.viewpager.fragments;

import com.bt.utils.Data;
import com.bt.viewpager.fragments.interfaces.onFragmentDataListener;
import com.bt.R;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListFragmentA extends  Fragment {

    private ListView mListView;	
    private String title ;
	private onFragmentDataListener listener;
	
	public String getText(){
		return title;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		if (activity != null) {
			listener = (onFragmentDataListener)activity;
		}
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (savedInstanceState != null) {
			title = savedInstanceState.getString("title");
			System.out.println("bundle:"+title);
		}else {
			Bundle b = getArguments();
			if (b != null) {
				title = b.getString("title");
			}
		}
		super.onCreate(savedInstanceState);
	}
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view =  inflater.inflate(R.layout.list, null, true);
		mListView = (ListView)view.findViewById(R.id.lv_id);
		 
		if (savedInstanceState != null) {
			title = savedInstanceState.getString("title");
			System.out.println("bundle act created:"+title);
		}
		return view;
	}
	 
	public void onActivityCreated(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			title = savedInstanceState.getString("title");
			System.out.println("bundle act created:"+title);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.tv,Data.strings);
		TextView titleView  = new TextView(getActivity());
		titleView.setText(title);
		titleView.setTextSize(18.0f);
		titleView.setTextColor(Color.RED);
		mListView.addHeaderView(titleView);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (listener != null) {
					Bundle b = getArguments();
					if (b == null) {
						b = new Bundle();
					} 
					b.putString("title", title);
					System.out.println("bundle click:"+title);
					b.putString("pos", position+"");
					listener.onFragmentDataBack(b);
				}
			}
		});
		super.onActivityCreated(savedInstanceState);
		
	};
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putString("title", title);
		System.out.println("bundle  save:"+title);
		super.onSaveInstanceState(outState);
	}
	@Override
	public void onStop() {
		Bundle b = getArguments();
		if (b == null) {
			b = new Bundle();
		} 
		b.putString("title", title);
		System.out.println("bundle stop:"+title);
		 
		super.onStop();
	}
}
