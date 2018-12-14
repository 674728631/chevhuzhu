package com.zccbh.demand.controller.weChat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.net.URLCodec;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zccbh.util.base.CommonField;
import com.zccbh.util.collect.Constant;

import net.sf.json.JSONObject;

/** 
 * @ClassName: WeixinAuthController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年10月26日 下午2:25:43 
 *  
 */
@Controller
@RequestMapping("/authorize")
public class WeixinAuthController {
	
	@RequestMapping(value="/getOpenid",method=RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getOpenid(HttpServletRequest request){
		String openid = String.valueOf(request.getSession().getAttribute("fromUserName"));
		if (openid.equals("null") || openid.equals("")) {
			return Constant.toReModel(CommonField.FAIL_OPENID, "没有获取到OPENID,请刷新后再试", null);
		}else{
			return Constant.toReModel(CommonField.SUCCESS, "openid获取成功", null);
		}
	}
	
	@RequestMapping(value = "/weixinAuth", method=RequestMethod.POST,produces = "application/json;charset=utf-8")
	@ResponseBody
	public String weixinAuth(HttpServletRequest request){
		try {
//			Map<String, Object> requestMap = JSONObject.fromObject(strJson);
			System.out.println("++++++++"+request.getParameter("strJson"));
//			String path = requestMap.get("path").toString();
//			String query = requestMap.get("query").toString();
//			if (query != null) {
//				path = path + "?" + query;
//			}
			StringBuffer baseUrl = request.getRequestURL();  
			String tempContextUrl = baseUrl.delete(baseUrl.length() - request.getRequestURI().length(), baseUrl.length()).toString(); 
			
			System.out.println(tempContextUrl + WeixinConstants.BASE_PATH);
			HttpServletRequest hRequest = (HttpServletRequest)request;
			hRequest.getSession().setAttribute("path", request.getParameter("strJson"));
			String uri = WeixinConstants.AUTH_URL.replace("APPID", WeixinConstants.APPID)
					.replace("REDIRECT_URI", URLEncoder.encode(tempContextUrl + WeixinConstants.BASE_PATH,"UTF-8"))
					.replace("SCOPE", "snsapi_userinfo").replace("STATE", "1");
			return Constant.toReModel(CommonField.SUCCESS, "授权成功", uri);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return Constant.toReModel(CommonField.FAIL_AUTH, "授权失败", null);
		}
	}
	
	/**
	 * 微信回调
	 * @author xiaowuge  
	 * @date 2018年10月26日  
	 * @version 1.0
	 */
	@RequestMapping(value = "/baseRequest",method=RequestMethod.GET)
	@ResponseBody
	public void baseRequest(HttpServletRequest request, HttpServletResponse response){
		try {
			StringBuffer baseUrl = request.getRequestURL();  
			String tempContextUrl = baseUrl.delete(baseUrl.length() - request.getRequestURI().length(), baseUrl.length()).toString();  
			
			HttpServletRequest hRequest = (HttpServletRequest)request;
			HttpServletResponse hResponse = (HttpServletResponse) response;
			String code = request.getParameter("code");
			String url =  WeixinConstants.AUTH_GET_OID.replace("APPID", WeixinConstants.APPID)
			        .replace("SECRET", WeixinConstants.APPSECRET)
			        .replace("CODE", code);
			String json = HttpClientUtil.httpGet(url);
//			String path = hRequest.getSession().getAttribute("path").toString() != null? URLEncoder.encode(tempContextUrl + hRequest.getSession().getAttribute("path").toString(),"UTF-8"):URLEncoder.encode(tempContextUrl + "/hfive/view/index.html","UTF-8");
//			String path1 = URLEncoder.encode(tempContextUrl + hRequest.getSession().getAttribute("path").toString(),"UTF-8");
			String path2 = URLEncoder.encode(tempContextUrl + "/hfive/view/index.html","UTF-8");
			if(Constant.toEmpty(json) && hRequest.getSession().getAttribute("path").toString() != null){
				JSONObject jsonObject = JSONObject.fromObject(json);
			    if (jsonObject.containsKey("openid")) {
			        hRequest.getSession().setAttribute("fromUserName", jsonObject.getString("openid"));
			    }
				hResponse.sendRedirect(hRequest.getSession().getAttribute("path").toString());
			    return;
			}
			hResponse.sendRedirect(path2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
