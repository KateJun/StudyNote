package com.bt.viewpager.fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bt.utils.LogUtil;
import com.bt.R;

public class CountingFragment extends Fragment{

	 int mNum;

     /**
      * Create a new instance of CountingFragment, providing "num"
      * as an argument.
      */
     static CountingFragment newInstance(int num) {
         CountingFragment f = new CountingFragment();

         // Supply num input as an argument.
         Bundle args = new Bundle();
         args.putInt("num", num);
         f.setArguments(args);

         return f;
     }

     /**
      * When creating, retrieve this instance's number from its arguments.
      */
     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         mNum = getArguments() != null ? getArguments().getInt("num") : 1;
     }

     /**
      * The Fragment's UI is just a simple text view showing its
      * instance number.
      */
     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.list_item, container, false);
         View tv = v.findViewById(R.id.tv);
         ((TextView)tv).setText("Fragment #" + mNum);
         tv.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.gallery_thumb));
         return v;
     }
     @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	 setHasOptionsMenu(true);
    	 
    	super.onActivityCreated(savedInstanceState);
    }
     @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	 // TODO Auto-generated method stub
    	 MenuItem item = menu.add("Button");
    	 MenuItemCompat.setShowAsAction(item,MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
    	 super.onCreateOptionsMenu(menu, inflater);
     }
     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	 LogUtil.toastShort(getActivity(), "button"); 
    	showDialog();
    	return super.onOptionsItemSelected(item);
    }
     
     private void showDialog() {
 		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
 		builder.setTitle("Tips");
 		builder.setMessage("You have clicked  actionbar." +
 				"\n\rUsed tabhost and container to show content, 4 fragments for tab change." +
 				"\n\rThe fragment extends android.support.v4.app.Fragment");
 		builder.setCancelable(true);
 		builder.setPositiveButton("OK", null);
 		builder.setNegativeButton("Cancel", null);
 		builder.create().show();
 	}
}
