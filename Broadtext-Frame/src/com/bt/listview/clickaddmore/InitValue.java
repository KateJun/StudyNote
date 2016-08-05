package com.bt.listview.clickaddmore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitValue {
	public static int page = 1;

	public static List<Map<String, Object>> initValue(int pageStart,
			int pageSize) {
		Map<String, Object> map;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = pageStart; i < pageSize; i++) {
			map = new HashMap<String, Object>();
			map.put("title", page + "_ListView分页显示");
			map.put("text", "发表");
			++page;
			list.add(map);
		}
		return list;
	}
}