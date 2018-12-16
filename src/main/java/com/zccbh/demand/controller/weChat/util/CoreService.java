package com.zccbh.demand.controller.weChat.util;


import com.zccbh.demand.controller.weChat.response.Image;
import com.zccbh.demand.controller.weChat.response.ImageMessage;
import com.zccbh.demand.controller.weChat.response.TextMessage;
import com.zccbh.demand.mapper.business.MaintenanceshopMapper;
import com.zccbh.demand.mapper.business.MiddleCustomerMaintenanceshopMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.mapper.merchants.CbhMaintenanceshopMapper;
import com.zccbh.demand.service.activities.CouponService;
import com.zccbh.demand.service.customer.InvitationTempService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.Base64;
import com.zccbh.util.collect.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 核心服务类
 *
 * @author luoyuangang
 * @date 2017/12/13
 */
@Controller
public class CoreService {
    @Autowired
    private WeiXinUtils weiXinUtils;
    @Autowired
    MiddleCustomerMaintenanceshopMapper middleCustomerMaintenanceshopMapper;
    @Autowired
    MaintenanceshopMapper maintenanceshopMapper;

    @Autowired
    private CbhMaintenanceshopMapper cbhMaintenanceshopMapper;

    private Logger logger = LoggerFactory.getLogger(CoreService.class);
//    @Autowired
//    private WechatAttentionMapper wechatAttentionMapper;

    @Autowired
    private UserCustomerMapper userCustomerMapper;

    @Autowired
    private InvitationTempService invitationTempService;

    @Autowired
    CouponService couponService;

    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String processRequest(HttpServletRequest request) {
        String respMessage = null;
        try {
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            logger.info("微信接收消息：{}", requestMap);
            // 发送方帐号（open_id）-----用户openid
            String fromUserName = requestMap.get("FromUserName");
            request.getSession().setAttribute("fromUserName", fromUserName);
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");
            // 事件类型
            String Event = requestMap.get("Event");

            // 菜单事件不做处理
            if (MessageUtil.EVENT_TYPE_VIEW.equalsIgnoreCase(Event))
                return respMessage;
            // 推送事件不做处理
            if (MessageUtil.EVENT_TYPE_TEMP.equals(Event))
                return respMessage;

            // 默认回复此文本消息
            // 获取微信昵称
            Map<String, String> nicknameMap = weiXinUtils.getNickname(fromUserName);
            String nickname = nicknameMap.get("nickname");
            if (Constant.toEmpty(nickname)) {
                nickname = Base64.getFromBase64(nickname);
                logger.info("微信用户：{}", nickname);
            }
            if (!Constant.toEmpty(nickname)) // 没有昵称
                nickname = "您好";

//            WechatAttention wechatAttention = wechatAttentionMapper.selectAll().get(0);

            // 二维码参数
            String EventKey = requestMap.get("EventKey");

            // 将文本消息对象转换成xml字符串
            respMessage = createTextMessage(fromUserName, toUserName, pushWelComeMessage(nickname, Event, EventKey));

//          // 文本消息
            if (MessageUtil.REQ_MESSAGE_TYPE_TEXT.equalsIgnoreCase(msgType)) {
                respMessage = textEvent(requestMap, Event, fromUserName, toUserName);
//                *********************************************************
//                // 创建图文消息
//                NewsMessage newsMessage = new NewsMessage();
//                newsMessage.setToUserName(fromUserName);
//                newsMessage.setFromUserName(toUserName);
//                newsMessage.setCreateTime(new Date().getTime());
//                newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
//                newsMessage.setFuncFlag(0);
//
//                List<Article> articleList = new ArrayList<Article>();
//                // 单图文消息
////                if ("1".equals(content)) {
//                    Article article = new Article();
//                    article.setTitle("客服热线 400-6858-158");
//                    article.setDescription("客服佳佳妹");
//                    article.setPicUrl("http://www.chevdian.com/img/associator/774455536016245505.png");
//                    article.setUrl("");
//                    articleList.add(article);
//                    // 设置图文消息个数
//                    newsMessage.setArticleCount(articleList.size());
//                    // 设置图文消息包含的图文集合
//                    newsMessage.setArticles(articleList);
//                    // 将图文消息对象转换成xml字符串
//                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
//                }
//            }
//                // 单图文消息---不含图片
//                else if ("2".equals(content)) {
//                    Article article = new Article();
//                    article.setTitle("微信公众帐号开发教程Java版");
//                    // 图文消息中可以使用QQ表情、符号表情
//                    article.setDescription("柳峰，80后，" + emoji(0x1F6B9)
//                            + "，微信公众帐号开发经验4个月。为帮助初学者入门，特推出此系列连载教程，也希望借此机会认识更多同行！\n\n目前已推出教程共12篇，包括接口配置、消息封装、框架搭建、QQ表情发送、符号表情发送等。\n\n后期还计划推出一些实用功能的开发讲解，例如：天气预报、周边搜索、聊天功能等。");
//                    // 将图片置为空
//                    article.setPicUrl("");
//                    article.setUrl("http://blog.csdn.net/lyq8479");
//                    articleList.add(article);
//                    newsMessage.setArticleCount(articleList.size());
//                    newsMessage.setArticles(articleList);
//                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
//                }
//                // 多图文消息
//                else if ("3".equals(content)) {
//                    Article article1 = new Article();
//                    article1.setTitle("微信公众帐号开发教程\n引言");
//                    article1.setDescription("");
//                    article1.setPicUrl("http://0.xiaoqrobot.duapp.com/images/avatar_liufeng.jpg");
//                    article1.setUrl("http://blog.csdn.net/lyq8479/article/details/8937622");
//
//                    Article article2 = new Article();
//                    article2.setTitle("第2篇\n微信公众帐号的类型");
//                    article2.setDescription("");
//                    article2.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");
//                    article2.setUrl("http://blog.csdn.net/lyq8479/article/details/8941577");
//
//                    Article article3 = new Article();
//                    article3.setTitle("第3篇\n开发模式启用及接口配置");
//                    article3.setDescription("");
//                    article3.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");
//                    article3.setUrl("http://blog.csdn.net/lyq8479/article/details/8944988");
//
//                    articleList.add(article1);
//                    articleList.add(article2);
//                    articleList.add(article3);
//                    newsMessage.setArticleCount(articleList.size());
//                    newsMessage.setArticles(articleList);
//                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
//                }
//                // 多图文消息---首条消息不含图片
//                else if ("4".equals(content)) {
//                    Article article1 = new Article();
//                    article1.setTitle("微信公众帐号开发教程Java版");
//                    article1.setDescription("");
//                    // 将图片置为空
//                    article1.setPicUrl("");
//                    article1.setUrl("http://blog.csdn.net/lyq8479");
//
//                    Article article2 = new Article();
//                    article2.setTitle("第4篇\n消息及消息处理工具的封装");
//                    article2.setDescription("");
//                    article2.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");
//                    article2.setUrl("http://blog.csdn.net/lyq8479/article/details/8949088");
//
//                    Article article3 = new Article();
//                    article3.setTitle("第5篇\n各种消息的接收与响应");
//                    article3.setDescription("");
//                    article3.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");
//                    article3.setUrl("http://blog.csdn.net/lyq8479/article/details/8952173");
//
//                    Article article4 = new Article();
//                    article4.setTitle("第6篇\n文本消息的内容长度限制揭秘");
//                    article4.setDescription("");
//                    article4.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");
//                    article4.setUrl("http://blog.csdn.net/lyq8479/article/details/8967824");
//
//                    articleList.add(article1);
//                    articleList.add(article2);
//                    articleList.add(article3);
//                    articleList.add(article4);
//                    newsMessage.setArticleCount(articleList.size());
//                    newsMessage.setArticles(articleList);
//                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
//                }
//                // 多图文消息---最后一条消息不含图片
//                else if ("5".equals(content)) {
//                    Article article1 = new Article();
//                    article1.setTitle("第7篇\n文本消息中换行符的使用");
//                    article1.setDescription("");
//                    article1.setPicUrl("http://0.xiaoqrobot.duapp.com/images/avatar_liufeng.jpg");
//                    article1.setUrl("http://blog.csdn.net/lyq8479/article/details/9141467");
//
//                    Article article2 = new Article();
//                    article2.setTitle("第8篇\n文本消息中使用网页超链接");
//                    article2.setDescription("");
//                    article2.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");
//                    article2.setUrl("http://blog.csdn.net/lyq8479/article/details/9157455");
//
//                    Article article3 = new Article();
//                    article3.setTitle("如果觉得文章对你有所帮助，请通过博客留言或关注微信公众帐号xiaoqrobot来支持柳峰！");
//                    article3.setDescription("");
//                    // 将图片置为空
//                    article3.setPicUrl("");
//                    article3.setUrl("http://blog.csdn.net/lyq8479");
//
//                    articleList.add(article1);
//                    articleList.add(article2);
//                    articleList.add(article3);
//                    newsMessage.setArticleCount(articleList.size());
//                    newsMessage.setArticles(articleList);
//                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
//                }
            }

            List<Map<String, Object>> userList = null;
            Map<String, Object> userInvitaion = null;
            List<Map<String, Object>> shopInvitation = null;
            if (MessageUtil.EVENT_TYPE_SUBSCRIBE.equalsIgnoreCase(Event)
                    || MessageUtil.EVENT_TYPE_SCAN.equalsIgnoreCase(Event)) {
                Map<String, Object> map = new HashMap<>();
                map.put("openId", fromUserName);
                map.put("invitedCustomerOpenID", fromUserName);
                // 判断是否注册
                userList = userCustomerMapper.getCustomerByOpenId(map);
                // 查找邀请
                userInvitaion = invitationTempService.findSingle(map);
                // 是否关注过
                shopInvitation = middleCustomerMaintenanceshopMapper.findMore(map);
            }

            // e泊包年活动处理
            String msg = eBoPackYearsActivity(Event, EventKey, fromUserName, nickname);
            if (null != msg) {
                logger.info("ebo用户================{}", msg);
                respMessage = createTextMessage(fromUserName, toUserName, msg);
                return respMessage;
            }

            // 关注事件数据记录（拉新渠道记录）
            if (MessageUtil.EVENT_TYPE_SUBSCRIBE.equalsIgnoreCase(Event)) {
                logger.info(">>>>>>>>> 未关注扫描带参二维码：{}", EventKey);
                if (CollectionUtils.isEmpty(userList)) { // 未注册用户
                    logger.info("{}微信关注进来了。", fromUserName);
                    // EventKey	事件KEY值，qrscene_为前缀，后面为二维码的参数值 qrscene_111/qrscene_111_u_5
                    if (EventKey != null && !"".equals(EventKey.trim())) {
                        saveUserRefrence(fromUserName, EventKey, null, userInvitaion);
                    } else { // 自然用户
                        if (!CollectionUtils.isEmpty(userInvitaion)) {
                            subUpdateTempModel(fromUserName, null, "-1", 2, true);
                        } else {
                            subUpdateTempModel(fromUserName, null, "-1", 2, false);
                        }
//                        subUpdateMiddleModel(null, shopInvitation, fromUserName, "-1");
                    }
                } else {
                    String message = nickname + "，终于等到您~~1000元擦刮救助额度已到账！小擦刮不用走保险，不增加保险理赔次数。现在" +
                            "<a href=\"" + Constant.toReadPro("realURL") + "hfive/view/index.html\">去领取 >></a>";
                    respMessage = createTextMessage(fromUserName, toUserName, message);
                    subUpdateMiddleModel(userList, shopInvitation, fromUserName, "-1");
                }
            }
            // 取关
            else if (MessageUtil.EVENT_TYPE_UNSUBSCRIBE.equalsIgnoreCase(Event)) {
                unsubscribe(fromUserName, 0);
            }
            // 已关注，扫描带参二维码
            else if (MessageUtil.EVENT_TYPE_SCAN.equalsIgnoreCase(Event)) {
                // EventKey	事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id : 111_u_5/111
                logger.info(">>>>>>>>> 已关注微信用户扫描带参二维码：{}", EventKey);
                String subUrl;
                if (CollectionUtils.isEmpty(userList)) { // 未注册用户
                    String message = null;
                    if (EventKey != null && !"".equals(EventKey.trim())) {
                        String[] arr = EventKey.split("_");
                        String customerId = arr[0];
                        if (arr.length > 1 && "u".equalsIgnoreCase(arr[1])) { // 个人
                            Map<String, Object> shopMap = userCustomerMapper.getUserInfoById(Integer.valueOf(customerId));
                            if (null != shopMap) {
                                subUrl = MessageFormat.format("<a href=\"{0}hfive/view/index.html?{1}\">", Constant.toReadPro("realURL"), "uid=" + customerId);
                                message = String.format("%s，终于等到您~~ %s邀请您免费领取1000元擦刮维修补贴！完成最后一步即刻到账。%s 猛戳  >></a>", nickname, Base64.getFromBase64(shopMap.get("nickname").toString()), subUrl);
                            }
                            logger.info("个人邀请二维码：{}", customerId);
//                            if (!CollectionUtils.isEmpty(shopInvitation)) {
//                                scanUpdateMiddleModel(fromUserName, "-1");
//                            }
//                            else {
//                                subUpdateMiddleModel(null, null, fromUserName, "-1");
//                            }
                            if (CollectionUtils.isEmpty(userInvitaion)) {
                                // 保存关注渠道到临时表
                                subUpdateTempModel(fromUserName, customerId, null, 0, false);
                            } else {
                                // 更新临时表
                                subUpdateTempModel(fromUserName, customerId, null, 0, true);
                            }
                        } else if (arr.length == 1) { // 渠道
                            Map<String, Object> pm = new HashMap<>();
                            pm.put("id", customerId);
                            Map<String, Object> shopMap = maintenanceshopMapper.findSingle(pm);
                            String type = String.valueOf(shopMap == null ? "-1" : shopMap.get("type"));
                            customerId = type.equals("10") ? "-1" : customerId;
                            if (null != shopMap) {
                                subUrl = MessageFormat.format("<a href=\"{0}hfive/view/index.html?{1}\">", Constant.toReadPro("realURL"), "sid=" + customerId);
                                message = String.format("%s，终于等到您~~ 恭喜您获得由 %s 送出的1000元擦刮维修补贴！完成最后一步即刻到账。%s 猛戳  >></a>", nickname, shopMap.get("name"), subUrl);
                            }
                            logger.info("渠道邀请二维码：{}", customerId);
//                            if (!CollectionUtils.isEmpty(shopInvitation)) {
//                                scanUpdateMiddleModel(fromUserName, customerId);
//                            }
//                            else {
//                                subUpdateMiddleModel(null, null, fromUserName, customerId);
//                            }
                            if (CollectionUtils.isEmpty(userInvitaion)) {
                                // 保存关注渠道到临时表
                                subUpdateTempModel(fromUserName, null, customerId, 1, false);
                            } else {
                                // 更新临时表
                                subUpdateTempModel(fromUserName, null, customerId, 1, true);
                            }
                        }
                    }
                    respMessage = createTextMessage(fromUserName, toUserName, message);
                } else {
                    subUpdateMiddleModel(userList, shopInvitation, fromUserName, "-1");
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return respMessage;
    }

    /***
     *
     * @param fromUserName 被邀请者openid
     * @param EventKey 二维码参数
     * @param shopInvitation 渠道记录
     * @param userInvitaion 个人邀请记录
     * @throws Exception
     */
    private void saveUserRefrence(String fromUserName, String EventKey,
                                  List<Map<String, Object>> shopInvitation, Map<String, Object> userInvitaion) throws Exception {
        String[] arr = EventKey.split("_");//qrscene_111/qrscene_111_u_5
        if (arr.length > 1) {
            String originType = null;
            if (arr.length > 2)
                originType = arr[2];
            String maintenanceshopId = new String(arr[1]);
            if ("u".equalsIgnoreCase(originType)) { // 个人
                logger.info("个人邀请>>>>>>");
                if (!CollectionUtils.isEmpty(userInvitaion)) {
                    // 不操作
                    subUpdateTempModel(fromUserName, maintenanceshopId, null, 0, true);
                } else {
                    // 保存新用户openid和老用户id之间的关系
                    subUpdateTempModel(fromUserName, maintenanceshopId, null, 0, false);
                }
                maintenanceshopId = "-1";
            } else if (null == originType) { // 渠道
                logger.info("渠道邀请>>>>>>");
                Map<String, Object> pm = new HashMap<>();
                pm.put("id", maintenanceshopId);
                Map<String, Object> rm = maintenanceshopMapper.findSingle(pm);
                String type = String.valueOf(rm == null ? "-1" : rm.get("type"));
                maintenanceshopId = type.equals("10") ? "-1" : maintenanceshopId;
                // 判断用户是否有其他渠道
                if (!CollectionUtils.isEmpty(userInvitaion)) {
                    // 更新临时表
                    subUpdateTempModel(fromUserName, null, maintenanceshopId, 1, true);
                } else {
                    // 保存关注渠道到临时表
                    subUpdateTempModel(fromUserName, null, maintenanceshopId, 1, false);
                }
            }
//            subUpdateMiddleModel(null, shopInvitation, fromUserName, maintenanceshopId);
        }
    }

    /**
     * emoji表情转换(hex -> utf-16)
     *
     * @param hexEmoji
     * @return
     */
    public static String emoji(int hexEmoji) {
        return String.valueOf(Character.toChars(hexEmoji));
    }


    /**
     * 创建文本消息
     *
     * @param fromUserName 发消息者openid
     * @param toUserName   公众号
     * @param msg          返回消息
     * @return
     */
    private String createTextMessage(String fromUserName, String toUserName, String msg) {
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setFuncFlag(0);
        textMessage.setContent(msg);
        return MessageUtil.textMessageToXml(textMessage);
    }

    /**
     * 创建图片消息
     *
     * @param fromUserName 发消息者openid
     * @param toUserName   公众号
     * @param mediaId      返回图片
     * @return
     */
    private String createImgMessage(String fromUserName, String toUserName, String mediaId) {
        ImageMessage imageMessage = new ImageMessage();
        imageMessage.setToUserName(fromUserName);
        imageMessage.setFromUserName(toUserName);
        imageMessage.setCreateTime(new Date().getTime());
        imageMessage.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_IMAGE);
        Image image = new Image();
        image.setMediaId(mediaId);
        imageMessage.setImage(image);
        return MessageUtil.imageMessageToXml(imageMessage);
    }

    private void unsubscribe(String fromUserName, int status) throws Exception {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("openId", fromUserName);
        map1.put("status", status);
        map1.put("responseNumber", 0);
        // 更新关注
        int i = middleCustomerMaintenanceshopMapper.updateModel(map1);
        logger.info("{}用户取关,更新关注表{}。", fromUserName, i);
        // 更新邀请
        map1.put("unsubscribe", status);
        map1.put("invitedCustomerOpenID", fromUserName);
        int j = invitationTempService.updateModel(map1);
        logger.info("{}用户取关,更新邀请表{}。", fromUserName, j);
    }

    private void subUpdateMiddleModel(List<Map<String, Object>> userList, List<Map<String, Object>> shopInvitation,
                                      String fromUserName, String maintenanceshopId) throws Exception {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("openId", fromUserName);
        map1.put("status", 1);
        map1.put("responseNumber", 0);
        if (!CollectionUtils.isEmpty(shopInvitation)) {
            // 关注过--更新
            middleCustomerMaintenanceshopMapper.updateSubscribeStatus(map1);
            logger.info("{}用户关注。", fromUserName);
            return;
        }
        if (!CollectionUtils.isEmpty(userList)) {
            for (Map<String, Object> user : userList) {
                map1.put("customerId", user.get("id"));
                map1.put("customerTel", user.get("customerPN"));
                map1.put("maintenanceshopId", maintenanceshopId);
                middleCustomerMaintenanceshopMapper.saveSingle(map1);
                logger.info("{}用户关注。", fromUserName);
            }
            return;
        }
    }

    private void subUpdateTempModel(String fromUserName, String customerId, String shopId,
                                    Integer originType, boolean update) throws Exception {

        Map<String, Object> saveMap = new HashMap<>();
        saveMap.put("shopId", shopId);
        if (0 == originType) { // 个人
            logger.info("获取个人邀请活动编号>>>");
            saveMap.put("modelId", couponService.getUserInvitationModelId());
        } else if (1 == originType) { // 渠道
            logger.info("获取渠道邀请活动编号>>>");
            saveMap.put("modelId", couponService.getShopCouponNo(saveMap));
        } else { // 自然用户
            logger.info("自然用户活动编号>>>");
            saveMap.put("modelId", "-1");
        }
        saveMap.put("originType", originType);
        saveMap.put("customerId", customerId);
        saveMap.put("invitedCustomerOpenID", fromUserName);
        saveMap.put("unsubscribe", 1);
        logger.info("invitationTempMapper.update>>>>>>{}", saveMap);
        if (update) {
            invitationTempService.updateModel(saveMap);
            logger.info("{} 更新临时邀请关系", fromUserName);
            return;
        }
        invitationTempService.saveSingle(saveMap);
        logger.info("{}保存临时邀请关系成功", fromUserName);
    }

    private void scanUpdateMiddleModel(String fromUserName, String maintenanceshopId) throws Exception {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("openId", fromUserName);
        map1.put("status", 1);
        map1.put("responseNumber", 0);
        // 关注过--更新
        map1.put("maintenanceshopId", maintenanceshopId);
        middleCustomerMaintenanceshopMapper.updateModel(map1);
        logger.info("{}更新用户关注。", fromUserName);
    }

    /**
     * 欢迎消息创建
     *
     * @param nickname
     * @param Event
     * @param EventKey
     * @return
     */
    private String pushWelComeMessage(String nickname, String Event, String EventKey) {
        logger.info(">>>>>nickname：{}", nickname);
        String message = null;
        String subUrl;
        if ("subscribe".equals(Event) && EventKey != null && !"".equals(EventKey.trim())) {
            String[] arr = EventKey.split("_");//qrscene_694_u_5  qrscene_694
            // 增加个人送出消息
            String maintenanceshopId = arr[1];
            Map<String, Object> shopMap;
            if (arr.length > 2 && "u".equalsIgnoreCase(arr[2])) { // 个人
                shopMap = userCustomerMapper.getUserInfoById(Integer.valueOf(maintenanceshopId));
                if (null != shopMap) {
                    subUrl = MessageFormat.format("<a href=\"{0}hfive/view/index.html?{1}\">", Constant.toReadPro("realURL"), "uid=" + maintenanceshopId);
                    message = String.format("%s，终于等到您~~ %s邀请您免费领取1000元擦刮维修补贴！完成最后一步即刻到账。%s 猛戳  >></a>", nickname, Base64.getFromBase64(shopMap.get("nickname").toString()), subUrl);
                }
            } else { // 商家
                Map<String, Object> pm = new HashMap<>();
                pm.put("id", maintenanceshopId);
                shopMap = cbhMaintenanceshopMapper.getMaintenanceShopDetail(pm);
                if (null != shopMap) {
                    subUrl = MessageFormat.format("<a href=\"{0}hfive/view/index.html?{1}\">", Constant.toReadPro("realURL"), "sid=" + maintenanceshopId);
                    message = String.format("%s，终于等到您~~ 恭喜您获得由 %s 送出的1000元擦刮维修补贴！完成最后一步即刻到账。%s 猛戳  >></a>", nickname, shopMap.get("name"), subUrl);
                }
            }
            if (null == message) {
                subUrl = MessageFormat.format("<a href=\"{0}hfive/view/index.html\">", Constant.toReadPro("realURL"));
                message = String.format("%s，终于等到您~~ 恭喜您获得由 车V互助 送出的1000元擦刮维修补贴！完成最后一步即刻到账。%s 猛戳  >></a>", nickname, subUrl);
            }
            logger.info("根据渠道id获取渠道信息：{}", shopMap);
            if (maintenanceshopId.equals("66666")) {
                message = nickname + ",终于等到您~~恭喜您获得 i车i生活“1元洗车”活动参与资格！" +
                        "<a href=\"http://mp.weixinbridge.com/mp/wapredirect?url=http%3A%2F%2Fweixinweb.auto11.com%2FDouble11%2Fwashcarcoupon%2Findex.aspx&action=appmsg_redirect&uin=ODg4MDc4MzU%3D&biz=MzI3MTA3MzYzOQ==&mid=2650615643&idx=1&type=0&scene=0\">猛戳 >></a>";
            }
            logger.info("关注事件推送消息>>>>>>>>>>>>");
        } else {
            // 非关注事件或参数为空
            if (String.valueOf(EventKey).equals("66666")) {
                message = nickname + "，终于等到你~~恭喜您获得 i车i生活“1元洗车”活动参与资格！" +
                        "<a href=\"http://mp.weixinbridge.com/mp/wapredirect?url=http%3A%2F%2Fweixinweb.auto11.com%2FDouble11%2Fwashcarcoupon%2Findex.aspx&action=appmsg_redirect&uin=ODg4MDc4MzU%3D&biz=MzI3MTA3MzYzOQ==&mid=2650615643&idx=1&type=0&scene=0\">猛戳 >></a>";
            } else {
                // 由于href属性值必须用双引号引起，这与字符串本身的双引号冲突，所以要转义
                message = nickname + "，终于等到您~~1000元擦刮救助额度已到账！小擦刮不用走保险，不增加保险理赔次数。现在" +
                        "<a href=\"" + Constant.toReadPro("realURL") + "hfive/view/index.html\">去领取 >></a>";
                //             		textMessage.setContent(wechatAttention.getCreateUser());
            }
            logger.info("{}事件推送消息>>>>>>>>>>>>", Event);
        }
        return message;
    }

    /**
     * 接收用户发送的文本消息内容处理
     *
     * @param requestMap
     * @param Event
     * @param fromUserName
     * @param toUserName
     * @return
     * @throws Exception
     */
    private String textEvent(Map<String, String> requestMap, String Event, String fromUserName, String toUserName) throws Exception {
        // 接收用户发送的文本消息内容
        String content = String.valueOf(requestMap.get("Content"));
        logger.info("{}事件用户发送消息：{}", Event, content);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("openId", fromUserName);
        List<Map<String, Object>> list = middleCustomerMaintenanceshopMapper.findMore(map1);
        String respMessage = null;
        if (list != null && list.size() > 0) {
            Map<String, Object> objectMap = list.get(0);
            Integer responseNumber = (Integer) objectMap.get("responseNumber");
            if (responseNumber == 1) {
                if (content.equals("92.5")) {
//                            创建图片消息
                    respMessage = createImgMessage(fromUserName, toUserName, "sGBCKR4aYbvHOgmIjf34c6g-ACLU4kJaskGXdnc197E");
                } else if (content.equals("我不是车神")) {
                    String msg = "邀请好友升级成1500元互助特权!\n"
                            + "<a href=\"" + Constant.toReadPro("realURL") + "hfive/view/share.html\">猛戳 >></a>";
                    respMessage = createTextMessage(fromUserName, toUserName, msg);
                } else {
//                             默认回复此文本消息
                    respMessage = createTextMessage(fromUserName, toUserName, "transfer_customer_service");
                }
            } else {
//                          创建图片消息
                if (content.equals("我不是车神")) {
                    String msg = "邀请好友升级成1500元互助特权!\n"
                            + "<a href=\"" + Constant.toReadPro("realURL") + "hfive/view/share.html\">猛戳 >></a>";
                    respMessage = createTextMessage(fromUserName, toUserName, msg);
                } else {
                    respMessage = createImgMessage(fromUserName, toUserName, "sGBCKR4aYbvHOgmIjf34c2BMbgpDsOmUkwHb44ajRHk");
                }
                map1.put("responseNumber", 1);
                middleCustomerMaintenanceshopMapper.updateModel(map1);
            }
        }
        // 回复管理端和商家端的链接
        if (content.equals("管理端")) {
            String msg = "<a href=\"" + Constant.toReadPro("realURL") + "app/android/chevhuzhugld.apk\">点击此条消息，立即下载安卓版管理端 </a>";
            respMessage = createTextMessage(fromUserName, toUserName, msg);
        }
        if (content.equals("商家端")) {
            String msg = "<a href=\"" + Constant.toReadPro("realURL") + "app/android/chevhuzhu.apk\">点击此条消息，立即下载安卓版商家端 </a>";
            respMessage = createTextMessage(fromUserName, toUserName, msg);
        }
        return respMessage;
    }

    /**
     * @param EventKey
     * @param fromUserName
     * @param nickname
     * @return
     */
    private String eBoPackYearsActivity(String Event, String EventKey, String fromUserName, String nickname) {
        // e泊车包年特殊处理
        logger.info("e泊包年用户关注========================={}===========================>{}", fromUserName, EventKey);
        if (EventKey != null && !"".equals(EventKey.trim())) {
            String[] arr = EventKey.split("_");
            boolean b = false;
            if (MessageUtil.EVENT_TYPE_SCAN.equalsIgnoreCase(Event)) {//111/111_u_5
                if (arr.length == 1 && "163".equals(arr[0]))
                    b = true;
            } else if (MessageUtil.EVENT_TYPE_SUBSCRIBE.equalsIgnoreCase(Event)) {//qrscene_111/qrscene_111_u_5
                if (arr.length == 2 && "163".equals(arr[1]))
                    b = true;
            }
            if (b) {
                String subUrl = MessageFormat.format("<a href=\"{0}hfive/view/index.html\">", Constant.toReadPro("realURL"));
                String message = String.format("尊敬的e泊包年用户%s,欢迎加入车v互助大家庭，点击查看车辆详情.%s 猛戳  >></a>",
                        nickname, subUrl);
                return message;
            }
        }

        return null;
    }
}
