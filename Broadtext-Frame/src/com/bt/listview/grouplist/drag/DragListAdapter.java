package com.bt.listview.grouplist.drag;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bt.R;


public  class DragListAdapter extends ArrayAdapter<String>{
	private ArrayList<String> list;
	private ArrayList<String> listTag;
	private LayoutInflater inflater;
	
    public DragListAdapter(Context context, ArrayList<String> list,ArrayList<String> listTag) {
        super(context, 0, list);
        this.list=list;
        this.listTag=listTag;
        inflater=LayoutInflater.from(context);
    }
    
    public List<String> getList(){
        return list;
    }
    
    //如果是分组标签，返回false，不能选中，不能点击
    @Override
    public boolean isEnabled(int position) {
        if(listTag.contains(getItem(position))){
            return false;
        }
        return super.isEnabled(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(listTag.contains(getItem(position))){
            //如果是分组标签，就加载分组标签的布局文件，两个布局文件显示效果不同
            view = inflater.inflate(R.layout.drag_list_item_tag, null);
        }else{
            //如果是正常数据项标签，就加在正常数据项的布局文件
            view = inflater.inflate(R.layout.drag_list_item, null);
        }
        
        TextView textView = (TextView)view.findViewById(R.id.drag_list_item_text);
        textView.setText(getItem(position));
        
        return view;
    }
}