package com.bt.listview.myexpandable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;

import com.bt.utils.LogUtil;
import com.bt.R;

public class QQExpandableListMain extends Activity {   
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {   
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.myexpandablelist);   
  
        List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();   
        for (int i = 0; i <= 10; i++) {
            Map<String, String> group = new HashMap<String, String>();   
            group.put("g", "分组" + i);  
            groups.add(group);  			
            List<Map<String, String>> child = new ArrayList<Map<String, String>>();  
            for (int j = 0; j < 10; j++) { 
                Map<String, String> childdata = new HashMap<String, String>();
                childdata.put("c", "用户" + i + "-" + j);
                child.add(childdata);
			}
            childs.add(child);
        }
  
        QQListView exList = (QQListView)findViewById(R.id.home_expandableListView);
        View headerView = getLayoutInflater().inflate(R.layout.group,exList,false);
        ((ImageView)(headerView.findViewById(R.id.groupIcon))).setImageResource(R.drawable.btn_browser2);
        exList.setHeaderView(headerView);
        
        QQListAdapter adapter = new QQListAdapter(   
                this,exList, groups, R.layout.group, new String[] { "g" },   
                new int[] { R.id.groupto }, childs, R.layout.child,   
                new String[] { "c" }, new int[] { R.id.childto });   
        exList.setAdapter(adapter);
        exList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				LogUtil.toastShort(getBaseContext(), "点击了:"+groupPosition +"--"+childPosition);
				return false;
			}
		});
    }
}