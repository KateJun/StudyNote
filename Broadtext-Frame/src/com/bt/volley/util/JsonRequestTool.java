package com.bt.volley.util;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bt.utils.LogUtil;

public class JsonRequestTool implements Listener<JSONObject>,ErrorListener {

	private Context ct ; 
	private String  url ;
	
	private onReponseLinsenter listener;
	
	public interface onReponseLinsenter{
		/**
		 *  服务器返回结果
		 *  @param jsonString 
		 */
		public void onReponse(String jsonString);
		 
	}
	
	 
	public JsonRequestTool(Context ct,String url ,onReponseLinsenter linsenter) {
		 this.url = url;
		 this.ct = ct;
		 this.listener = linsenter;
		 doJsonRequest();
	}
	private  void doJsonRequest() {
		//第三个参数
		//A JSONObject to post with the request. Null is allowed and indicates no parameters will be posted along with request.
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET,url,null, this,this);
		jsonObjectRequest.setTag(ct.getClass().getSimpleName());//设置tag callAll的时候使用
		VolleyTool.getInstance(ct).getmRequestQueue().add(jsonObjectRequest);
		 
	}
	
	
	@Override
	public void onResponse(JSONObject response) {
		if (listener != null) {
			listener.onReponse(response == null ? "" : response.toString());
		}
	}
	@Override
	public void onErrorResponse(VolleyError error) {
		if (listener != null) {
			listener.onReponse("");
			LogUtil.logE(error.toString());
//			listener.onReponse(error == null ? Type.error.toString() : Type.error.toString() + error.toString());
		}
	}
	
	public static void cancelAll(Context ct) {
		VolleyTool.getInstance(ct).getmRequestQueue().cancelAll(ct.getClass().getSimpleName());
		LogUtil.syso("cancell all  queue");
	}
	
//   public enum Type{
//		error,
//		
//	}
	
}
