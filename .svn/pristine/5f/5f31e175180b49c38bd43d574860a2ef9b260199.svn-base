package com.zccbh.demand.controller.weChat;

import com.zccbh.util.collect.Constant;


public class WeixinConstants {

	/*获取access_token填写client_credential
	appid	是	第三方用户唯一凭证
	secret	是	第三方用户唯一凭证密钥，即appsecret*/
	public static  final String ACCESS_TOKEN ="15_j61krHU_JBw2NH-uRsPAbNhwB7sPjyh4mX_ab2YicauDnP-e7Mi73ZRIX9uV6jqW1wm7n9FlUIAXJSi8vUKBDCxPff-msBqyX5AjbIoPjoZsCani9Ry9x-B_cl1U_DAYD5r_RArWIvWUdq_1JBQeAFAPHW";
//	public static  final String ACCESS_TOKEN ="";
	//第三方用户唯一凭证
//	public static  final String APPID ="wx1becbde3eb564694";
	public static  final String APPID = Constant.toReadPro("appId");
//	第三方用户唯一凭证密钥
//	public static  final String APPSECRET="64697c3bad79ef95d03d88429566ce4b";
	public static  final String APPSECRET=Constant.toReadPro("appSecret");
	public static  final String ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	//创建菜单
	public static  final String ADD_MENU_URL="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	//获取菜单
	public static  final String GET_MENU_URL="https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	//删除菜单
	public static  final String REMOVE_MENU_URL="https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	//通过code获取openid
	public static final String AUTH_GET_OID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	//授权登录的路径
	public static final String AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	//创建二维码路径
	public static final String QRCODE_URL ="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
	//获取二维码
	public static final String QRCODE_URL_GET ="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
	
	//授权回调地址
	public static final String BASE_PATH = "/authorize/baseRequest";

	public static final String GET_USERS = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
}