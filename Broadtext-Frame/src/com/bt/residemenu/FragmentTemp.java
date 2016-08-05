package com.bt.residemenu;

import com.bt.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentTemp extends Fragment {

	
	public static Fragment getInstance(String contentString) {
		Fragment fragment = new FragmentTemp();
		Bundle args = new Bundle();
		args.putString("title", contentString);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = View.inflate(getActivity(), R.layout.fragment_temp, null);
		TextView tv = (TextView)v.findViewById(R.id.tv_fragment_temp);
		tv.setText(getArguments() != null ? getArguments().getString("title") : "null");
		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onStart() {
		super.onStart();
	}
	@Override
	public void onResume() {
		super.onResume();
	}
	
	
}
