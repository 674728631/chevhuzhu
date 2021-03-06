package com.zccbh.test;


import com.zccbh.demand.controller.weChat.HttpClientUtil;
import com.zccbh.demand.controller.weChat.WeixinConstants;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/zccbh/config/spring.xml")
public class WeiXinTest {
	
	@Test
	public void testName() throws Exception {
		System.out.println("appid+++" + WeixinConstants.APPID);
		String url = WeixinConstants.ACCESS_TOKEN_URL.
				replace("APPID", "wx225b0d1543cb1cbb"). //正式appid
				replace("APPSECRET", "93b1266533d4a6451f5602369877aba6");//正式秘钥
//				replace("APPID", "wx6d24b8007f94f503").//测试appid
//				replace("APPSECRET", "c2dfad34f931c59ff1baa83ab000bfb7");//测试密码
		//创建httclient实列
		HttpClient hClient = new DefaultHttpClient();
		//创建httpget
		HttpGet httpGet = new  HttpGet(url);
		//执行get请求.嘻嘻对象传入执行请求,发送请求或者响应对象
		HttpResponse response = hClient.execute(httpGet);
		//获得响应的实体
		HttpEntity entity = response.getEntity();
		//获得响应状态400 200 404 500
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("statusCode:"+statusCode);
		//转换成字符串
		String string = EntityUtils.toString(entity);
		System.out.println("string = " + string);
		String accessToken = JSONObject.fromObject(string).getString("access_token");
		System.out.println(accessToken);
	}

	@Test
	public void testName1() throws Exception {
		String url = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=-SEn3AnbshbVuG583Pc08C0WV38sh0oOFKW9akQ6gep7fc3MoXmKTrikxsGtCcV__00rLoOeWjU3SBQXRRGBCC34-HeNul0T-fDsX7EHkWW5PVmzf6_sWphtW9aeleLyYPPfADARII";
		//创建httclient实列
		HttpClient hClient = new DefaultHttpClient();
		//创建httpget
		HttpGet httpGet = new  HttpGet(url);
		//执行get请求.嘻嘻对象传入执行请求,发送请求或者响应对象
		HttpResponse response = hClient.execute(httpGet);
		//获得响应的实体
		HttpEntity entity = response.getEntity();
		//获得响应状态400 200 404 500
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("statusCode:"+statusCode);
		//转换成字符串
		String string = EntityUtils.toString(entity);
		System.out.println("string = " + string);
		//String accessToken = JSONObject.fromObject(string).getString("access_token");
		//System.out.println(accessToken);
	}

	@Test
	public void testName2() throws Exception {
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+ WeixinConstants.ACCESS_TOKEN+"&openid=o7BMHxB6dCD3OJMJK8B9TTw-3qjs";
		//创建httclient实列
		HttpClient hClient = new DefaultHttpClient();
		//创建httpget
		HttpGet httpGet = new  HttpGet(url);
		//执行get请求.嘻嘻对象传入执行请求,发送请求或者响应对象
		HttpResponse response = hClient.execute(httpGet);
		//获得响应的实体
		HttpEntity entity = response.getEntity();
		//获得响应状态400 200 404 500
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("statusCode:"+statusCode);
		//转换成字符串
		String string = EntityUtils.toString(entity);
		System.out.println("string = " + string);
		// 编码后的json
		String json = new String(string.getBytes("ISO-8859-1"), "UTF-8");
		System.out.println(json);
		String nickname = JSONObject.fromObject(json).getString("nickname");
		String headimgurl = JSONObject.fromObject(json).getString("headimgurl");
		System.out.println(nickname);
		System.out.println(headimgurl);
	}

	@Test
	public void testPost() throws Exception {
//		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+ WeixinConstants.ACCESS_TOKEN;
//		//发送get请求步骤:
//		//1 创建httpClient执行对象
//		HttpClient execution = new DefaultHttpClient();
//		//2 创建httpGet请求
//		HttpPost httpPost = new HttpPost(url);
//		JSONObject pay_success_json = CreateJson.create_pay_success_Json("1", "http://www.chevdian.com/hfive/view/chexian_order_detail.html?id=2017120714142800016");
//		//设置参数
//		String menuStr = pay_success_json.toString();
//		httpPost.setEntity(new StringEntity(menuStr, "utf-8"));
//		//3 通过执行对象传入执行请求,发送请求,获取响应对象
//		HttpResponse response = execution.execute(httpPost);
//		//400 200 404 500
//		int statusCode = response.getStatusLine().getStatusCode();
//		System.out.println("statusCode:"+statusCode);
//		//4 通过响应对象获取响应实体,把响应实体转换json字符串
//		HttpEntity responseEntity = response.getEntity();
//		String jsonStr = EntityUtils.toString(responseEntity);
//		System.out.println(jsonStr);
	}
	//获取素材ID
	@Test
	public void testPost2() throws Exception {
		String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token="+ WeixinConstants.ACCESS_TOKEN;
		//发送get请求步骤:
		//1 创建httpClient执行对象
		HttpClient execution = new DefaultHttpClient();
		//2 创建httpGet请求
		HttpPost httpPost = new HttpPost(url);
		//设置参数
		String menuStr ="{ \"type\":\"image\",\"offset\":\"0\",\"count\":\"20\"}";
		httpPost.setEntity(new StringEntity(menuStr, "utf-8"));
		//3 通过执行对象传入执行请求,发送请求,获取响应对象
		HttpResponse response = execution.execute(httpPost);
		//400 200 404 500
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("statusCode:"+statusCode);
		//4 通过响应对象获取响应实体,把响应实体转换json字符串
		HttpEntity responseEntity = response.getEntity();
		String jsonStr = EntityUtils.toString(responseEntity);
		System.out.println(jsonStr);
	}
	@Test
	public void testPost1() throws Exception {
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+ WeixinConstants.ACCESS_TOKEN;
		//发送get请求步骤:
		//1 创建httpClient执行对象
		HttpClient execution = new DefaultHttpClient();
		//2 创建httpGet请求
		HttpPost httpPost = new HttpPost(url);
		Map<String,String>result =new HashMap<>();
		result.put("openid","oWKBW1b4EQubobKFW1zNmcVvxbns");
		result.put("keyword1","川A66666");
		result.put("keyword2","太平洋给出报价");
		result.put("keyword3","5000");
		result.put("keyword4","2015年12月1日 12:12");
		result.put("orderNo","2017120714142800016");
		//JSONObject pay_success_json = PushTemplate.quoteSucceed(result);
		//String menuStr = pay_success_json.toString();
		//httpPost.setEntity(new StringEntity(menuStr, "utf-8"));
		//3 通过执行对象传入执行请求,发送请求,获取响应对象
		HttpResponse response = execution.execute(httpPost);
		//400 200 404 500
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("statusCode:"+statusCode);
		//4 通过响应对象获取响应实体,把响应实体转换json字符串
		HttpEntity responseEntity = response.getEntity();
		String jsonStr = EntityUtils.toString(responseEntity);
		System.out.println(jsonStr);
	}
		//创建自定义菜单
		@Test
		public void testAddMenu() throws Exception {
			String url = WeixinConstants.ADD_MENU_URL.replace("ACCESS_TOKEN",WeixinConstants.ACCESS_TOKEN);
			String string = HttpClientUtil.httpPost(url, getJsonMenu());
//			String string = HttpClientUtil.httpPost(url, getTestJsonMenu());
			System.out.println(string);
			System.out.println(getJsonMenu());
//			System.out.println(getTestJsonMenu());
		}
		//查询菜单
		@Test
		public void testGetMenu() throws Exception {
			String url = WeixinConstants.GET_MENU_URL.replace("ACCESS_TOKEN", WeixinConstants.ACCESS_TOKEN);
			String string = HttpClientUtil.httpGet(url);
			System.out.println(string);
		}
		
		//删除菜单
		@Test
		public void testRemoveMenu() throws Exception {
			String url = WeixinConstants.REMOVE_MENU_URL.replace("ACCESS_TOKEN", WeixinConstants.ACCESS_TOKEN);
			String string = HttpClientUtil.httpGet(url);
			System.out.println(string);
		}

	public static  String getJsonMenu(){
		//JSONObject json对象
		JSONObject root = new JSONObject();
		//json数组  一级菜单数组
		JSONArray oneleveMenu = new JSONArray();

		//第一个一级菜单
		JSONObject functions1 = new JSONObject();
		functions1.put("type", "view");
		functions1.put("name", "加入互助");
		functions1.put("url", "http://www.chevhuzhu.com");
		
		//第二个一级菜单
		JSONObject functions2 = new JSONObject();
		functions2.put("type", "view");
		functions2.put("name", "发起理赔");
		functions2.put("url", "http://www.chevhuzhu.com/hfive/view/order.html");

		//第二个一级菜单
//		JSONObject functions2 = new JSONObject();
//		functions2.put("name", "发起理赔");

		//第二个一级菜单 对应的 二级菜单数组
//		JSONArray twoleveMenu2 = new JSONArray();
//
//		JSONObject customer1 = new JSONObject();
//		customer1.put("type", "view");
//		customer1.put("name", "擦刮救助");
//		customer1.put("url", "http://www.chevhuzhu.com/hfive/view/order.html");
//
//		JSONObject customer2 = new JSONObject();
//		customer2.put("type", "view");
//		customer2.put("name", "保险理赔");
//		customer2.put("url", "http://www.chevhuzhu.com/hfive/view/rule_photo_baoxian.html?type=1");
//
//		twoleveMenu2.add(customer1);
//		twoleveMenu2.add(customer2);
//		functions2.put("sub_button", twoleveMenu2);

		////第三个一级菜单
		JSONObject functions3 = new JSONObject();
		functions3.put("type", "view");
		functions3.put("name", "会员卡");
		functions3.put("url", "http://www.chevhuzhu.com/hfive/view/profile.html");

		//把一级菜单放入一级菜单数组
		oneleveMenu.add(functions1);
		oneleveMenu.add(functions2);
		oneleveMenu.add(functions3);

		root.put("button", oneleveMenu);
		return root.toString();
	}
	
	/**
	 * 测试公众号菜单
	 * @author xiaowuge  
	 * @date 2018年11月23日  
	 * @version 1.0
	 */
	public static  String getTestJsonMenu(){
		//JSONObject json对象
		JSONObject root = new JSONObject();
		//json数组  一级菜单数组
		JSONArray oneleveMenu = new JSONArray();

		//第一个一级菜单
		JSONObject functions1 = new JSONObject();
		functions1.put("type", "view");
		functions1.put("name", "加入互助");
		functions1.put("url", "http://test.chevhuzhu.com");
		
		//第二个一级菜单
		JSONObject functions2 = new JSONObject();
		functions2.put("type", "view");
		functions2.put("name", "发起理赔");
		functions2.put("url", "http://test.chevhuzhu.com/hfive/view/order.html");

		//第二个一级菜单
//		JSONObject functions2 = new JSONObject();
//		functions2.put("name", "发起理赔");

		//第二个一级菜单 对应的 二级菜单数组
//		JSONArray twoleveMenu2 = new JSONArray();
//
//		JSONObject customer1 = new JSONObject();
//		customer1.put("type", "view");
//		customer1.put("name", "擦刮救助");
//		customer1.put("url", "http://www.chevhuzhu.com/hfive/view/order.html");
//
//		JSONObject customer2 = new JSONObject();
//		customer2.put("type", "view");
//		customer2.put("name", "保险理赔");
//		customer2.put("url", "http://www.chevhuzhu.com/hfive/view/rule_photo_baoxian.html?type=1");
//
//		twoleveMenu2.add(customer1);
//		twoleveMenu2.add(customer2);
//		functions2.put("sub_button", twoleveMenu2);

		////第三个一级菜单
		JSONObject functions3 = new JSONObject();
		functions3.put("type", "view");
		functions3.put("name", "会员卡");
		functions3.put("url", "http://test.chevhuzhu.com/hfive/view/profile.html");

		//把一级菜单放入一级菜单数组
		oneleveMenu.add(functions1);
		oneleveMenu.add(functions2);
		oneleveMenu.add(functions3);

		root.put("button", oneleveMenu);
		return root.toString();
	}
}
