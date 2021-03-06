package com.zccbh.demand.service.weChat;


import com.zccbh.demand.controller.weChat.WeixinConstants;
import com.zccbh.util.base.Base64;
import com.zccbh.util.base.RedisUtil;
import com.zccbh.util.collect.Constant;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeiXinUtils {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WeiXinUtils weiXinUtils;

    @Autowired
    private PushTemplate pushTemplate;

    private Logger logger = LoggerFactory.getLogger(WeiXinUtils.class);
    /**
     * 获取token
     *
     * @return
     * @throws Exception
     */
    public String getAccessToken() throws Exception {

        String accessToken = redisUtil.get("accessToken2");
        if (Constant.toEmpty(accessToken)) {
            return accessToken.substring(1, accessToken.length() - 1);
        }
        String url = WeixinConstants.ACCESS_TOKEN_URL.
                replace("APPID", WeixinConstants.APPID).
                replace("APPSECRET", WeixinConstants.APPSECRET);
        //创建httclient实列
        HttpClient hClient = new DefaultHttpClient();
        //创建httpget
        HttpGet httpGet = new HttpGet(url);
        //执行get请求.嘻嘻对象传入执行请求,发送请求或者响应对象
        HttpResponse response = hClient.execute(httpGet);
        //获得响应的实体
        HttpEntity entity = response.getEntity();
        //获得响应状态400 200 404 500
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("statusCode:" + statusCode);
        //转换成字符串
        String string = EntityUtils.toString(entity);
        System.out.println("string = " + string);
        String access_token = JSONObject.fromObject(string).getString("access_token");
        System.out.println("access_token = " + access_token);
        //保存在redis里 设置时间为115分钟
        redisUtil.putAndTime("accessToken2", access_token, 1 * 60 * 115);
        return access_token;

    }
//	/**
//	 *  获取泛华的accessToken
//	 * @return
//	 * @throws Exception
//	 */
//	public  String getFHAccessToken() throws Exception {
//		String accessToken = redisUtil.get("FHAccessToken");
//		System.out.println("accessToken = " + accessToken);
//		if (Constant.toEmpty(accessToken)) {
//			return accessToken.substring(1,accessToken.length()-1);
//		}
//
//        String url = Constant.toReadPro("FHURL")+ Constant.toReadPro("getToken");
//		Map map = Maps.newHashMapWithExpectedSize(2);
//		Map headers = Maps.newHashMapWithExpectedSize(1);
//		map.put("channelId", Constant.toReadPro("channelId"));
//		map.put("channelSecret", Constant.toReadPro("channelSecret"));
//		headers.put("Content-Type","application/json;charset=utf-8");
//		String string = HttpSender.doPost(url, JSONObject.fromObject(map).toString(), headers);
//		System.out.println("string = " + string);
//		JSONObject jsonObject = JSONObject.fromObject(string);
//		String respCode = (String) jsonObject.get("respCode");
//        if (respCode.equals("00")) {
//			accessToken = (String) jsonObject.get("accessToken");
//            System.out.println("accessToken = " + accessToken);
//			//保存在redis里 设置时间为115分钟
//			redisUtil.putAndTime("FHAccessToken",accessToken,1*60*115);
//		}
//		return accessToken;
//
//	}

    /**
     * 通过微信用户id获取用户名
     *
     * @param openid 微信用户id
     * @return
     * @throws Exception
     */
    public Map<String, String> getNickname(String openid) throws Exception {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="
                + weiXinUtils.getAccessToken() + "&openid=" + openid;
        //创建httclient实列
        HttpClient hClient = new DefaultHttpClient();
        //创建httpget
        HttpGet httpGet = new HttpGet(url);
        //执行get请求.嘻嘻对象传入执行请求,发送请求或者响应对象
        HttpResponse response = hClient.execute(httpGet);
        //获得响应的实体
        HttpEntity entity = response.getEntity();
//		System.out.println("+++++获取响应数据++++" + entity );
        //获得响应状态400 200 404 500
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("statusCode:" + statusCode);
        //转换成字符串
        String string = EntityUtils.toString(entity);
//		System.out.println("string = " + string);
        // 编码后的json
        Map<String, String> result = new HashMap<>();
        String json = new String(string.toString().getBytes("UTF-8"), "UTF-8");
        if (json.contains("subscribe")) {
//			System.out.println("json = " + json);
            String subscribe = JSONObject.fromObject(json).getString("subscribe");
            if (subscribe.equals("0")) {
                result.put("subscribe", subscribe);
                result.put("nickname", null);
                result.put("headimgurl", null);
                return result;
            }
            String nickname = JSONObject.fromObject(json).getString("nickname");
//			System.out.println("+++++++工具类++++++微信获取原始nickname++++" + nickname);
            String headimgurl = JSONObject.fromObject(json).getString("headimgurl");
            if (Constant.toEmpty(nickname)) {
                String base64 = Base64.getBase64(nickname);
                result.put("nickname", base64);
            } else {
                result.put("nickname", null);
            }
            result.put("subscribe", subscribe);
            result.put("headimgurl", headimgurl);
//			System.out.println("+++++++工具类++++++返回nickname++++" + result.get("nickname"));
            return result;
        }
        redisUtil.delect("accessToken2");
        result.put("subscribe", null);
        result.put("nickname", null);
        result.put("headimgurl", null);
        System.out.println("result = " + result);
        return result;
    }

    public void sendTemplate(int type, Map<String, String> result) throws Exception {
        logger.info("微信模板消息推送=============================================>");
        logger.info("模板id={}，内容={}",type,result);
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + getAccessToken();
        //发送get请求步骤:
        //1 创建httpClient执行对象
        HttpClient execution = new DefaultHttpClient();
        //2 创建httpGet请求
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonString = null;
        // 1 互助和理赔审核通过通知 【审核通过提醒】
        // 2 定损通过支付通知 【定损通过提醒】
        // 3 维修完成取车通知 【维修已完成提醒】
        // 4 余额不足通知 【余额不足提醒】
        // 5 分摊推送通知
        // 6 活动推送通知
        // 7 加入车V互助支付推送通知【未支付通知提醒】
        // 8 加入车V互助添加完好车辆照片推送通知
        // 9 审核失败通知 【审核未通过提醒】
        // 10 订单状态更新通知 【订单状态更新】
        // 11 车辆进入观察期通知 【车辆进入观察期提醒】
        // 12 账单生成通知
        // 13 车辆保障中通知
        // 14【退出互助计划通知】
        // 15【报告生成通知】
        // 16【互助额度不足500】
        // 17【充值成功提醒】
        // 18【月账单提醒】
        // 19【邀请成功提醒】
        switch (type) {
            case 1:
                try {
                    jsonString = pushTemplate.auditNotification(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    jsonString = pushTemplate.paymentNotice(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    jsonString = pushTemplate.maintenanceNotification(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    jsonString = pushTemplate.lackBalance(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    jsonString = pushTemplate.shareMoney(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    jsonString = pushTemplate.activitiesPush(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                try {
                    jsonString = pushTemplate.paymentHelp(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                try {
                    jsonString = pushTemplate.addCarImg(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 9:
                try {
                    jsonString = pushTemplate.examineFail(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 10:
                try {
                    jsonString = pushTemplate.orderUpdate(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 11:
                try {
                    jsonString = pushTemplate.carObservationInform(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 12:
                try {
                    jsonString = pushTemplate.billNotifications(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 13:
                try {
                    jsonString = pushTemplate.carGuaranteeNotice(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 14:
                try {
                    jsonString = pushTemplate.quitRescue(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 15:
                try {
                    jsonString = pushTemplate.createNotice(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 16:
                try {
                    jsonString = pushTemplate.lackAmount(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 17:
                try {
                    jsonString = pushTemplate.rechargeSuccess(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 18:
                try {
                    jsonString = pushTemplate.monthBill(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 19:
                try {
                    jsonString = pushTemplate.invitationSuccess(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
        }
        String menuStr = jsonString.toString();
        httpPost.setEntity(new StringEntity(menuStr, "utf-8"));
        //3 通过执行对象传入执行请求,发送请求,获取响应对象
        HttpResponse response = execution.execute(httpPost);
        //400 200 404 500
        int statusCode = response.getStatusLine().getStatusCode();
        logger.info("statusCode={}" ,statusCode);
        //4 通过响应对象获取响应实体,把响应实体转换json字符串
        HttpEntity responseEntity = response.getEntity();
        String jsonStr = EntityUtils.toString(responseEntity);
        logger.debug("返回结果=",jsonStr);
    }
}
