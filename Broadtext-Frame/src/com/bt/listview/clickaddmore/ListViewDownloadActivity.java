package com.bt.listview.clickaddmore;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bt.R;

 
public class ListViewDownloadActivity extends Activity {
	private ListView listView;
	private List<Map<String, Object>> data;
	private listViewAdapter adapter;
	private int pageSize = 10;
	private final int pageType = 1;
	private TextView moreTextView;
	private LinearLayout loadProgressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		listView = (ListView) findViewById(R.id.lv_id);
		data = InitValue.initValue(1, 15);

		addPageMore();
		adapter = new listViewAdapter();
		listView.setAdapter(adapter);
	}

	public class listViewAdapter extends BaseAdapter {
		
		public  listViewAdapter() {
			
		}
		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(ListViewDownloadActivity.this)
					.inflate(R.layout.list_item2, null);
			TextView title = (TextView) view.findViewById(R.id.tv_id);
			TextView text = (TextView) view.findViewById(R.id.title_id);
			title.setText(data.get(position).get("title").toString());
			text.setText(data.get(position).get("text").toString());
			return view;
		}

	}

	 
	private void chageListView(int pageStart, int pageSize) {
		List<Map<String, Object>> data = InitValue.initValue(pageStart,
				pageSize);
		this.data.addAll(data);
//		for (Map<String, Object> map : data) {
//			this.data.add(map);
//		}
		data = null;
	}

	 
	private void addPageMore() {
		View view = LayoutInflater.from(this).inflate(R.layout.list_footer,
				null);
		moreTextView = (TextView) view.findViewById(R.id.more_id);
		loadProgressBar = (LinearLayout) view.findViewById(R.id.load_id);
		moreTextView.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				moreTextView.setVisibility(View.GONE);
				loadProgressBar.setVisibility(View.VISIBLE);

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Message mes = handler.obtainMessage(pageType);
						handler.sendMessage(mes);
					}
				}).start();
			}
		});
		listView.addFooterView(view);
	}

	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case pageType:
				int size = data.size() + pageSize;
				chageListView(data.size(), size);
				
				adapter.notifyDataSetChanged();
				listView.setSelection(size);
				moreTextView.setVisibility(View.VISIBLE);
			    loadProgressBar.setVisibility(View.GONE);
				break;

			default:
				break;
			}

			super.handleMessage(msg);
		}
	};
	
    
}