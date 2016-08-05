package com.bt.volley.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Context;

import com.android.volley.Cache.Entry;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class JsonRequestOperation<T> extends Request<T>{

	private Class<T> clazz;
//	private Type typeOfT;
	private Map<String, String> params;
//	private Map<String, String> headers;
	private final Listener<T> listener;

	public JsonRequestOperation(String url, Class<T> clazz, Map<String, String> params, Listener<T> listener, ErrorListener errorListener) {
		super(Method.POST, url, errorListener);
		this.clazz = clazz;
		this.params = params; 
		this.listener = listener;
	}
	public JsonRequestOperation(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
		super(Method.POST, url, errorListener);
		this.clazz = clazz;
		this.listener = listener;
	}

//	public JsonRequestOperation(String url, Type typeOfT, Map<String, String> params, Listener<T> listener, ErrorListener errorListener) {
//		super(Method.POST, url, errorListener);
//		this.typeOfT = typeOfT;
//		this.params = params;
//		this.listener = listener;
//	}
	
	@Override
	public byte[] getBody() throws AuthFailureError {
		// TODO Auto-generated method stub
		return super.getBody();
	}
	
	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(super.getUrl());
		if (params != null && params.size() != 0) {
			url.append("?");
			Set<String> keySet =  params.keySet() ;
			Iterator<String> iterator = keySet.iterator(); 
			while(iterator.hasNext()){
				String string = (String) iterator.next();
				url.append(string);
				url.append("=");
				url.append(params.get(string));
				url.append("&");
			}
			 
		}
		 System.out.println("]]]]]]]]]]]]]url:"+url.toString());
		return url.toString();
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			System.out.println("-------------json:"+json);
			if (clazz != null) {
				Entry entry = HttpHeaderParser.parseCacheHeaders(response);
				 
				T t = JSONHelper.parseObject(new JSONObject(json).getString("result"), clazz);
				return Response.success(t, entry);
			}else {
				 
				return Response.success((T)json, HttpHeaderParser.parseCacheHeaders(response));
			}
//			throw new RuntimeException("clazz and typeOfT are null");
		} catch (Exception e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(T response) {
		// TODO Auto-generated method stub
		listener.onResponse(response);
	}
	
	private static <T> T newInstance(Class<T> clazz) throws JSONException {
		if (clazz == null)
			return null;
		T obj = null;
		if (clazz.isInterface()) {
			if (clazz.equals(Map.class)) {
				obj = (T) new HashMap();
			} else if (clazz.equals(List.class)) {
				obj = (T) new ArrayList();
			} else if (clazz.equals(Set.class)) {
				obj = (T) new HashSet();
			} else {
				throw new JSONException("unknown interface: " + clazz);
			}
		} else {
			try {
				obj = clazz.newInstance();
			} catch (Exception e) {
				throw new JSONException("unknown class type: " + clazz);
			}
		}
		return obj;
	}
	
	
}
