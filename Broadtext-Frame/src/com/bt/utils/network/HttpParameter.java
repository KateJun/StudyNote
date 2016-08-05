package com.bt.utils.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.bt.utils.Constants;

import android.util.Log;

public class HttpParameter {
	
	public static HttpClient getClient() {
		// 设置HttpClient连接超时
		HttpParams httpParameters = new BasicHttpParams();
		// Set the default socket timeout (SO_TIMEOUT)  
		HttpConnectionParams.setConnectionTimeout(httpParameters, 40000);
		// in milliseconds which is the timeout for waiting for data. 
		HttpConnectionParams.setSoTimeout(httpParameters, 40000);
		HttpClient httpClient = new DefaultHttpClient(httpParameters);
		Log.e("HttpParameter", "httpClient ");
		return httpClient;
	}
	
	public static HttpPost getHttpPost(String url){
		//对HttpPost设置jsessionid
		HttpPost httpPost= new HttpPost(url);
		httpPost.setHeader("Cookie", Constants.JSESSIONID);
		Log.e("HttpParameter", "httpPost ");
		return httpPost;
	}

	public static void setHttpEntity(HttpPost post, List<NameValuePair> params){
		//对HttpPost进行参数设置
		try {
			HttpEntity httpEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			post.setEntity(httpEntity);
			Log.e("HttpParameter", "httpEntity");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} 
	}

	public static String getResponceContent(HttpResponse responce) throws IOException{
		//从HttpResponse中获得数据
		String content = "";
		HttpEntity httpEntity = responce.getEntity();
		BufferedReader bufferedReader = null;
		InputStream inputStream = null;
		
		try {
			inputStream = httpEntity.getContent();
			bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line = null;
			Log.e("HttpParameter", "responce");
			//循环输出
			while ((line = bufferedReader.readLine()) != null) {
				content += line;
			}
			Log.i("HttpParameter", content);
		} 
		catch (IllegalStateException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(bufferedReader != null){
				bufferedReader.close();
				inputStream.close();
			}
		}
		return content;
	}
	
	public static String getRrovingContent(HttpResponse responce) throws IOException{
		//从HttpResponse中获得数据
		String content = "";
		HttpEntity entity = responce.getEntity();
		BufferedReader byffered = null;
		InputStream input = null;
		try {
			input = entity.getContent();
			byffered = new BufferedReader(
					new InputStreamReader(input));
			Log.e("HttpParameter", "responce");
			//循环输出
			content = byffered.readLine();
		} 
		catch (IllegalStateException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(byffered != null){
				byffered.close();
				input.close();
			}
		}
		return content;
	}
	
}

