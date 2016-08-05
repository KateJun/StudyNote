package com.bt.qqlogin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bt.R;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tencent.utils.HttpUtils.HttpStatusException;
import com.tencent.utils.HttpUtils.NetworkUnavailableException;

public class QQLoginActivity extends Activity {
	public static final String TAG = QQLoginActivity.class.getSimpleName();

	private Tencent mTencent;
	private UserInfo mUserInfo;//用户信息

	private Button btnLogin;
	private Button btnLogout;
	private TextView tvUsername;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.broadcast_activity);
		findWidget();
		initLogin();
		initListener();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		要成功接收到回调
		mTencent.onActivityResult(requestCode, resultCode, data);
		System.out.println(requestCode+"====="+resultCode);
	}

	private void findWidget() {
		btnLogin = (Button) this.findViewById(R.id.btn_send_broadcast);
		btnLogout = (Button) this.findViewById(R.id.btn_rigister);
		findViewById(R.id.btn_unrigister).setVisibility(View.INVISIBLE);
		tvUsername = (TextView) this.findViewById(R.id.tv_show);
		
		btnLogin.setText("登录");
		btnLogout.setText("注销");
	}

	private void initListener() {
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mTencent != null && mTencent.isSessionValid()){
					getUserInfo();
				}else{
					login();
				}
				
			}
		});
		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mTencent != null){
					mTencent.logout(QQLoginActivity.this);
					AccessTokenSaver.clear(QQLoginActivity.this);
				}
			}
		});
	}
	
	private void getUserInfo(){
		mUserInfo = new UserInfo(QQLoginActivity.this, mTencent.getQQToken());
		mUserInfo.getUserInfo(new BaseRequestListener(){

			@Override
			public void doComplete(Object response) {
				if (response != null && response instanceof JSONObject){
					JSONObject responseObj = (JSONObject) response;
					if (responseObj.has("nickname")) {
						try {
							tvUsername.setVisibility(android.view.View.VISIBLE);
							tvUsername.setText(responseObj.getString("nickname")+":"+responseObj.getString("gender"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}else{
					Toast.makeText(QQLoginActivity.this, getString(R.string.qq_getuser_failed) + "\n" + getString(R.string.qq_error_json_format), Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}

	private void initLogin() {
		mTencent = AccessTokenSaver.getAccessToken(QQLoginActivity.this);
		if (mTencent == null){
			mTencent = Tencent.createInstance(Constant.APP_ID,
					getApplicationContext());
		}
	}
	
	private void login(){
		mTencent.login(QQLoginActivity.this, Constant.SCOPE, new BaseRequestListener(){

			@Override
			public void doComplete(Object response) {
				if (response != null && response instanceof JSONObject){
					Log.e(TAG, "登录返回："+response.toString());
					AccessTokenSaver.saveAccessToken(QQLoginActivity.this, mTencent);
					getUserInfo();
				}else{
					Toast.makeText(QQLoginActivity.this, getString(R.string.qq_login_failed) + "\n" + getString(R.string.qq_error_json_format), Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}

	private class BaseRequestListener implements IUiListener {

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			showResult("onCancel:", "取消授权");
		}

		@Override
		public void onComplete(Object response) {
			doComplete(response);
		}
		
		public void doComplete(Object response){
			
			
		}

		@Override
		public void onError(UiError arg0) {
			 
			showResult("onError:", arg0.errorCode+"-"+arg0.errorDetail+"-"+arg0.errorMessage);
		}

	}

//	使用requestAsync、request等通用方法调用sdk未封装的接口时，例如上传图片、查看相册等。
	private class BaseApiListener implements IRequestListener {
		
		public BaseApiListener(String requestKey,boolean isLog){
			
		}

		@Override
		public void onComplete(final JSONObject response) {
			showResult("IRequestListener.onComplete:", response.toString());
			doComplete(response);
		}

		protected void doComplete(JSONObject response) {

		}

		@Override
		public void onIOException(final IOException e) {
			showResult("IRequestListener.onIOException:", e.getMessage());
		}

		@Override
		public void onMalformedURLException(final MalformedURLException e) {
			showResult("IRequestListener.onMalformedURLException", e.toString());
		}

		@Override
		public void onJSONException(final JSONException e) {
			showResult("IRequestListener.onJSONException:", e.getMessage());
		}

		@Override
		public void onNetworkUnavailableException(
				NetworkUnavailableException arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSocketTimeoutException(SocketTimeoutException arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onUnknowException(Exception arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onConnectTimeoutException(ConnectTimeoutException arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onHttpStatusException(HttpStatusException arg0) {
			// TODO Auto-generated method stub

		}

	}
	
	private void showResult(String tag,String content){
		Log.e(TAG, tag+content);
		Toast.makeText(this, tag + content, Toast.LENGTH_SHORT).show();;
	}

}
