package com.bt.volley;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bt.utils.Data;
import com.bt.utils.FuncUtils;
import com.bt.volley.util.JSONHelper;
import com.bt.volley.util.JsonRequestOperation;
import com.bt.volley.util.JsonRequestTool;
import com.bt.volley.util.VolleyTool;
import com.bt.volley.util.JsonRequestTool.onReponseLinsenter;
import com.bt.R;

/**
 * jsonRequest
 * @author XJ
 *
 */
public class JsonRequestAcitivity extends Activity implements OnClickListener {
	private Button requestBtn;
	private TextView dispText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.json_request);
		requestBtn = (Button) findViewById(R.id.requestBtn);
		dispText = (TextView) findViewById(R.id.tv_response);
		requestBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		dispText.setText("请求中...");
		try {
			doRequest();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void doRequest() throws JSONException{
		HashMap<String, String> params = new HashMap<String, String>();
		// TODO: params为post请求的参数，请根据实际情况，自行完善
//		?username=jianghao&password=jianghe
		params.put("username", "smc\rxeve");
		params.put("password", "Password3#");
//		JsonRequestOperation<JsonEntity> entityRequest = new JsonRequestOperation<JsonEntity>(Data.jsonUrl, JsonEntity.class, params,
//				new Listener<JsonEntity>() {
//
//			@Override
//			public void onResponse(JsonEntity response) {
//				// TODO Auto-generated method stub
//				dispText.setText(response.getName()+response.getDepartment()+response.getValue());
//				System.out.println("json entity =============="+response);
//				 
//			}
//
//		}, null);
		
		JsonObjectRequest entityRequest = new JsonObjectRequest(Method.POST, Data.jsonUrl, new JSONObject(JSONHelper.toJSON(params)), new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				System.out.println("json entity =============="+response);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				System.out.println("json error =============="+error);
			}
		});

		entityRequest.setTag(getClass().getSimpleName());
		VolleyTool.getInstance(this).getmRequestQueue().add(entityRequest);
		
		
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		JsonRequestTool.cancelAll(this);
		VolleyTool.getInstance(this).getmRequestQueue().cancelAll(getClass().getSimpleName());
	}
	 
}
