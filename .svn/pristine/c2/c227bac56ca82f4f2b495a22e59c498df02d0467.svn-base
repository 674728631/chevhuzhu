package com.zccbh.demand.controller.weChat;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class HttpClientUtil {
	
	public static String httpGet(String url){
		String jsonStr = null;
		try {
			//获得httpclient
			HttpClient httpClient = new DefaultHttpClient();
			//获得httpget
			HttpGet httpGet = new HttpGet(url);
			//执行器中执行get请求 并拿到我们的响应对象
			HttpResponse response = httpClient.execute(httpGet);
			//获得响应状态
			int statusCode = response.getStatusLine().getStatusCode();
			//从响应对象里拿到内容
			HttpEntity entity = response.getEntity();
			jsonStr = EntityUtils.toString(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonStr;
		
	}
	//VRxAAWo9ZmjHTuQ5WnqFNz9Z9GWAxP5NoE86q0FKG8zelKqOHTRoSUMSRoxW9nELjyleVbOwoWwnrBkJ39AfEglj94C_WqXrCXWobO5tyOfsDyn5ojXIoS-Ei9JOXT8ZZREfAIATLY
	public static String httpPost(String url,String jsonParam){
		String jsonStr = null;
		try {
			//获得httpclient
			HttpClient httpClient = new DefaultHttpClient();
			//获得httpget
			HttpPost httpPost = new HttpPost(url);
			//设置httppost请求参数
			httpPost.setEntity(new StringEntity(jsonParam,"UTF-8"));
			//执行器中执行get请求 并拿到我们的响应对象
			HttpResponse response = httpClient.execute(httpPost);
			//获得响应状态
			int statusCode = response.getStatusLine().getStatusCode();
			//从响应对象里拿到内容
			HttpEntity entity = response.getEntity();
			jsonStr = EntityUtils.toString(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonStr;
		
	}
}
