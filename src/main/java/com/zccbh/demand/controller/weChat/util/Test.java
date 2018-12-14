package com.zccbh.demand.controller.weChat.util;

import com.zccbh.demand.controller.weChat.response.Image;
import com.zccbh.demand.controller.weChat.response.ImageMessage;
import com.zccbh.demand.controller.weChat.response.TextMessage;
import com.zccbh.demand.mapper.business.MaintenanceshopMapper;
import com.zccbh.demand.mapper.business.MiddleCustomerMaintenanceshopMapper;
import com.zccbh.demand.mapper.merchants.CbhMaintenanceshopMapper;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.Base64;
import com.zccbh.util.collect.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    @Autowired
    private WeiXinUtils weiXinUtils;
    @Autowired
    MiddleCustomerMaintenanceshopMapper middleCustomerMaintenanceshopMapper;
    @Autowired
    MaintenanceshopMapper maintenanceshopMapper;

    @Autowired
    private CbhMaintenanceshopMapper cbhMaintenanceshopMapper;

    private Logger logger = LoggerFactory.getLogger(CoreService.class);
    public String processRequest(HttpServletRequest request) throws Exception {

        String respMessage;
        // xml请求解析
        Map<String, String> requestMap = MessageUtil.parseXml(request);
        logger.info("微信接收消息：{}" ,requestMap);
        // 发送方帐号（open_id）-----用户openid
        String fromUserName = requestMap.get("FromUserName");
        request.getSession().setAttribute("fromUserName", fromUserName);
        // 公众帐号
        String toUserName = requestMap.get("ToUserName");
        // 消息类型
        String msgType = requestMap.get("MsgType");
        // 事件类型
        String Event = requestMap.get("Event");
        // 二维码参数
        String EventKey = requestMap.get("EventKey");
        // 关注事件
        if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT) ) {
            if("subscribe".equals(Event)){
                respMessage = pushMessageToNewUser(requestMap,fromUserName,toUserName);

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("openId", fromUserName);
                List<Map<String, Object>> list = middleCustomerMaintenanceshopMapper.findMore(map);
                System.out.println("关注进来了---------------，list为:" + list);
                if (EventKey != null && !"".equals(EventKey.trim())) { //有内容表示通过渠道或者维修厂
                    String[] arr = EventKey.split("_");
                    if (arr.length > 1) {
                        String maintenanceshopId = arr[1];
//                        System.out.println("++++++++++++++++++" + maintenanceshopId);
                        Map<String, Object> pm = new HashMap<String, Object>();
                        pm.put("id", maintenanceshopId);
                        Map<String, Object> rm = maintenanceshopMapper.findSingle(pm);
                        String type = String.valueOf(rm == null ? "-1" : rm.get("type"));
                        maintenanceshopId = type.equals("10") ? "-1" : maintenanceshopId;
                        map.put("maintenanceshopId", maintenanceshopId);
                        if (list != null && list.size() > 0) {
                            Map<String, Object> map1 = new HashMap<String, Object>();
                            map1.put("openId", fromUserName);
                            map1.put("status", 1);
                            middleCustomerMaintenanceshopMapper.updateModel(map1);
//                            System.out.println("更新状态成功---------");

                        } else {
                            map.put("status", 1);
                            middleCustomerMaintenanceshopMapper.saveSingle(map);
                            System.out.println("保存维修厂id成功---------");
                        }
                    }
                } else {
                    if (list != null && list.size() > 0) {
                        Map<String, Object> map1 = new HashMap<String, Object>();
                        map1.put("openId", fromUserName);
                        map1.put("status", 1);
                        middleCustomerMaintenanceshopMapper.updateModel(map1);
//                        System.out.println("更新状态成功---------");
                    } else {
                        map.put("maintenanceshopId", -1);
                        map.put("status", 1);
                        middleCustomerMaintenanceshopMapper.saveSingle(map);
                        System.out.println("保存openid成功---------");
                    }
                }
            }else if("unsubscribe".equals(Event)){
                // 更新微信统计
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("openId", fromUserName);
                map.put("status", 0);
                map.put("responseNumber", 0);
                middleCustomerMaintenanceshopMapper.updateModel(map);
//                System.out.println("更新状态成功---------");
            }
        }
        // 接收到消息
        else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)){
            respMessage =  returnMessageToUser(requestMap,fromUserName,toUserName);
        }

        return null;
    }

    private String returnMessageToUser(Map<String, String> requestMap,String fromUserName,String toUserName) throws Exception {
       /* // 接收用户发送的文本消息内容
        String content = String.valueOf(requestMap.get("Content"));
        logger.info("用户发送微信消息：{}",content);
        // 查询用户
        Map<String, Object> map = new HashMap<>();
        map.put("openId", fromUserName);
        List<Map<String, Object>> list = middleCustomerMaintenanceshopMapper.findMore(map);
        String respMessage = null;
        if (list != null && list.size() > 0) {
            Map<String, Object> objectMap = list.get(0);
            // 自动回复次数
            Integer responseNumber = (Integer) objectMap.get("responseNumber");
            if (responseNumber == 1) {
                if (content.equals("92.5")) {
                    // 创建图片消息
                    respMessage = createImgMessage(fromUserName,toUserName,"sGBCKR4aYbvHOgmIjf34c6g-ACLU4kJaskGXdnc197E");
                } else if (content.equals("我不是车神")) {
                    String msg = "邀请好友升级成1500元互助特权!\n"
                            + "<a href=\"" + Constant.toReadPro("realURL") + "hfive/view/share.html\">猛戳 >></a>";
                    respMessage = createTextMessage(fromUserName,toUserName,msg);
                } else {
                    // 默认回复此文本消息
                    respMessage = createTextMessage(fromUserName,toUserName,"transfer_customer_service");
                }
            } else {
                // 创建图片消息
                if (content.equals("我不是车神")) {
                    String msg= "邀请好友升级成1500元互助特权!\n"
                            + "<a href=\"" + Constant.toReadPro("realURL") + "hfive/view/share.html\">猛戳 >></a>";
                    respMessage = createTextMessage(fromUserName,toUserName,msg);
                } else {
                    respMessage = createImgMessage(fromUserName,toUserName,"sGBCKR4aYbvHOgmIjf34c2BMbgpDsOmUkwHb44ajRHk");
                }
                map.put("responseNumber", 1);
                middleCustomerMaintenanceshopMapper.updateModel(map);
            }
        }
        // 回复管理端和商家端的链接
        if (content.equals("管理端")) {
            String msg = "<a href=\"" + Constant.toReadPro("realURL") + "app/android/chevhuzhugld.apk\">点击此条消息，立即下载安卓版管理端 </a>";
            respMessage = createTextMessage(fromUserName,toUserName,msg);
        }
        if (content.equals("商家端")) {
            String msg = "<a href=\"" + Constant.toReadPro("realURL") + "app/android/chevhuzhu.apk\">点击此条消息，立即下载安卓版商家端 </a>";
            respMessage = createTextMessage(fromUserName,toUserName,msg);
        }
        return respMessage;*/
       return null;
    }

    private String pushMessageToNewUser(Map<String, String> requestMap,String fromUserName,String toUserName) throws Exception {
        // 事件类型
        String Event = requestMap.get("Event");
        // 获取微信昵称
        Map<String, String> nicknameMap = weiXinUtils.getNickname(fromUserName);
        String nickname = nicknameMap.get("nickname");
        if (Constant.toEmpty(nickname)) {
            nickname = Base64.getFromBase64(nickname);
            logger.info("微信用户：{}" ,nickname);
        }
        // 二维码参数
        String EventKey = requestMap.get("EventKey");
        String msg = null;
        if ("subscribe".equals(Event) && EventKey != null && !"".equals(EventKey.trim())) {
            // 关注事件且带参数
            String[] arr = EventKey.split("_");
            // 商家id
            String maintenanceshopId = arr[1];
            Map<String, Object> pm = new HashMap<String, Object>();
            pm.put("id", maintenanceshopId);
            Map<String, Object> shopMap = cbhMaintenanceshopMapper.getMaintenanceShopDetail(pm);
            if(!Constant.toEmpty(nickname)){
                if (maintenanceshopId.equals("66666")) {
                    msg  = "您好，终于等到你~~恭喜您获得 i车i生活“1元洗车”活动参与资格！" +
                            "<a href=\"http://mp.weixinbridge.com/mp/wapredirect?url=http%3A%2F%2Fweixinweb.auto11.com%2FDouble11%2Fwashcarcoupon%2Findex.aspx&action=appmsg_redirect&uin=ODg4MDc4MzU%3D&biz=MzI3MTA3MzYzOQ==&mid=2650615643&idx=1&type=0&scene=0\">猛戳 >></a>";
                } else {
                    msg  = "您好，终于等到您~~恭喜您获得由" + shopMap.get("name") + "送出的1000元擦刮维修补贴！完成最后一步即刻到账。" +
                            "<a href=\"" + Constant.toReadPro("realURL") + "hfive/view/index.html?sid=" + shopMap.get("id") + "\">猛戳  >></a>";
                }
            }else{
                if (maintenanceshopId.equals("66666")) {
                    msg  = nickname + ",终于等到你~~恭喜您获得 i车i生活“1元洗车”活动参与资格！" +
                            "<a href=\"http://mp.weixinbridge.com/mp/wapredirect?url=http%3A%2F%2Fweixinweb.auto11.com%2FDouble11%2Fwashcarcoupon%2Findex.aspx&action=appmsg_redirect&uin=ODg4MDc4MzU%3D&biz=MzI3MTA3MzYzOQ==&mid=2650615643&idx=1&type=0&scene=0\">猛戳 >></a>";
                } else {
                    msg  = nickname + "，终于等到您~~恭喜您获得由" + shopMap.get("name") + "送出的1000元擦刮维修补贴！完成最后一步即刻到账。" +
                            "<a href=\"" + Constant.toReadPro("realURL") + "hfive/view/index.html?sid=" + shopMap.get("id") + "\">猛戳  >></a>";
                }
            }
        } else {
            // 取关，或则参数为空
            if(!Constant.toEmpty(nickname)){
                if (String.valueOf(EventKey).equals("66666")) {
                    msg  = "您好，终于等到你~~恭喜您获得 i车i生活“1元洗车”活动参与资格！" +
                            "<a href=\"http://mp.weixinbridge.com/mp/wapredirect?url=http%3A%2F%2Fweixinweb.auto11.com%2FDouble11%2Fwashcarcoupon%2Findex.aspx&action=appmsg_redirect&uin=ODg4MDc4MzU%3D&biz=MzI3MTA3MzYzOQ==&mid=2650615643&idx=1&type=0&scene=0\">猛戳 >></a>";
                } else {
                    // 由于href属性值必须用双引号引起，这与字符串本身的双引号冲突，所以要转义
                    msg  = "您好，终于等到您~~1000元擦刮救助额度已到账！小擦刮不用走保险，不增加保险理赔次数。现在" +
                            "<a href=\"" + Constant.toReadPro("realURL") + "hfive/view/index.html\">去领取 >></a>";
                }

            }else{
                if (String.valueOf(EventKey).equals("66666")) {
                    msg  = nickname + "，终于等到你~~恭喜您获得 i车i生活“1元洗车”活动参与资格！" +
                            "<a href=\"http://mp.weixinbridge.com/mp/wapredirect?url=http%3A%2F%2Fweixinweb.auto11.com%2FDouble11%2Fwashcarcoupon%2Findex.aspx&action=appmsg_redirect&uin=ODg4MDc4MzU%3D&biz=MzI3MTA3MzYzOQ==&mid=2650615643&idx=1&type=0&scene=0\">猛戳 >></a>";
                } else {
                    // 由于href属性值必须用双引号引起，这与字符串本身的双引号冲突，所以要转义
                    msg  = nickname + "，终于等到您~~1000元擦刮救助额度已到账！小擦刮不用走保险，不增加保险理赔次数。现在" +
                            "<a href=\"" + Constant.toReadPro("realURL") + "hfive/view/index.html\">去领取 >></a>";
                }
            }
        }
        return createTextMessage(fromUserName,toUserName,msg);
    }

    /**
     * 创建文本消息
     * @param fromUserName
     * @param toUserName
     * @param msg
     * @return
     */
    public String createTextMessage(String fromUserName,String toUserName,String msg){
        /*TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setFuncFlag(0);
        textMessage.setContent(msg);
        return MessageUtil.textMessageToXml(textMessage);*/
        return null;
    }

    /**
     * 创建图片消息
     * @param fromUserName
     * @param toUserName
     * @param mediaId
     * @return
     */
    public String createImgMessage(String fromUserName,String toUserName,String mediaId){
        /*ImageMessage imageMessage = new ImageMessage();
        imageMessage.setToUserName(fromUserName);
        imageMessage.setFromUserName(toUserName);
        imageMessage.setCreateTime(new Date().getTime());
        imageMessage.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_IMAGE);
        Image image = new Image();
        image.setMediaId(mediaId);
        imageMessage.setImage(image);
        return MessageUtil.imageMessageToXml(imageMessage);*/
        return null;
    }

}
