package com.bt.listview.grouplist.drag;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

import com.bt.R;

public class DragListActivity extends Activity {
    
    private static ArrayList<String> list = null;
    private DragListAdapter adapter = null;
    
    public static ArrayList<String> listTag= new ArrayList<String>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag_list_activity);
        
        initData();
        
        DragListView dragListView = (DragListView)findViewById(R.id.drag_list);
        adapter = new DragListAdapter(this, list,listTag);
        dragListView.setAdapter(adapter);
    }
    
    public void initData(){
        //数据结果
        list = new ArrayList<String>();
        //groupKey存放的是分组标签
        listTag.add("甲组");
        listTag.add("乙组");
        
        list.add("甲组");
        for(int i=0; i<6; i++){
            list.add(i+"--甲组");
        }
        list.add("乙组");
        for(int i=0; i<10; i++){
            list.add(i+"--乙组");
        }
    }
    
   
}