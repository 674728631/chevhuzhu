package com.zccbh.demand.controller.weChat;


import com.zccbh.demand.controller.weChat.util.CoreService;

import com.zccbh.demand.mapper.business.MiddleCustomerMaintenanceshopMapper;
import com.zccbh.demand.service.weChat.WeiXinUtils;

import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/weChATIndex")
//@RequestMapping("/index")
public class WeChATIndexController {
	@Autowired
	private CoreService coreService;
	/**
	 * 处理微信认证，get提交
	 */
	@RequestMapping(method = RequestMethod.GET)
	public void signature(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("weChATIndex进来了*******************************************/r/nweChATIndex进来了*******************************************/r/nweChATIndex进来了*******************************************");
		response.setCharacterEncoding("UTF-8");
		// response.getWriter().println("weixin");
		/*
		 * signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		 * timestamp 时间戳 nonce 随机数 echostr 随机字符串
		 * 1）将token、timestamp、nonce三个参数进行字典序排序 2）将三个参数字符串拼接成一个字符串进行sha1加密
		 * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		 */
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		System.out.println("signature:" + signature);
		System.out.println("timestamp:" + timestamp);
		System.out.println("nonce:" + nonce);
		System.out.println("echostr:" + echostr);
		// 进行排序
		String[] all = { "chevhuzhu", timestamp, nonce };
//		String[] all = { "luoyuangang", timestamp, nonce };
		Arrays.sort(all);
		// 2）将三个参数字符串拼接成一个字符串进行sha1加密
		StringBuilder sBuilder = new StringBuilder();
		for (String string : all) {
			sBuilder.append(string);
		}
		// 转换成字符串
		String content = sBuilder.toString();
		// 进行编码
		String sha1Msg = SecurityUtil.sha1(content);
		// 编码后的字符串和微信加密签名就返回微信加密签名
		if (signature.equals(sha1Msg)) {
			System.out.println("微信接入认证成功....");
			response.getWriter().println(echostr);
		} else {
			System.out.println("微信接入认证失败....");
			// 直接返回
		}
		
	}
	@RequestMapping(method = RequestMethod.POST)
	public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 调用核心业务类接收消息、处理消息
		String respMessage = coreService.processRequest(request);
		if (!Constant.toEmpty(respMessage)) {
			return;
		}
		// 响应消息
		PrintWriter out = response.getWriter();
		out.print(respMessage);
		out.close();
	}
}

