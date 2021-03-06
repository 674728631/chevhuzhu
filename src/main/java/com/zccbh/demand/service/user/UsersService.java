package com.zccbh.demand.service.user;


import com.zccbh.demand.controller.quartz.ObservationJob;
import com.zccbh.demand.controller.quartz.QuartzUtils;
import com.zccbh.demand.mapper.activities.MiddleCouponCustomerMapper;
import com.zccbh.demand.mapper.activities.PackYearsCodeMapper;
import com.zccbh.demand.mapper.business.MiddleCustomerMaintenanceshopMapper;
import com.zccbh.demand.mapper.customer.*;
import com.zccbh.demand.mapper.foundation.FoundationMapper;
import com.zccbh.demand.service.basic.DictionaryService;
import com.zccbh.demand.service.customer.InvitationService;
import com.zccbh.demand.service.system.UserCustomerLogService;
import com.zccbh.demand.service.weChat.PromotionService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.*;
import com.zccbh.util.collect.Constant;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Project:
 * @Comments: 用户 service
 * @JDK version used:       1.8
 * @Author: luoyuangang
 * @Create Date:            2018年1月03日
 * @Modified By:            <修改人中文名或拼音缩写>
 * @Modified Date:          <修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: <修改原因描述>
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UsersService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WeiXinUtils weiXinUtils;

    @Autowired
    private UserCustomerMapper userCustomerMapper;

    @Autowired
    private FoundationMapper foundationMapper;

    @Autowired
    private WechatLoginMapper wechatLoginMapper;
    @Autowired
    private InvitationCustomerMapper invitationCustomerMapper;

    @Autowired
    private CarMapper carMapper;

    @Autowired
    MiddleCouponCustomerMapper middleCouponCustomerMapper;

    @Autowired
    MiddleCustomerMaintenanceshopMapper middleCustomerMaintenanceshopMapper;

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private InvitationTempMapper invitationTempMapper;

    @Autowired
    PromotionService promotionService;

    @Autowired
    private UserCustomerLogService userCustomerLogService;

    private Logger logger = LoggerFactory.getLogger(UsersService.class);


    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> appLogin(HttpServletRequest request) throws Exception {
        String openid = (String) request.getSession().getAttribute("fromUserName");
        logger.info("微信登录用户openid：{}", openid);
        String headimgurl = null;
        String nickname = null;
        if (Constant.toEmpty(openid)) {
            Map<String, String> nicknames = weiXinUtils.getNickname(openid);
            System.out.println(nicknames);
            logger.info("微信登录用户昵称：{}", nicknames);
            headimgurl = nicknames.get("headimgurl");
            nickname = nicknames.get("nickname");
        }
        // 手机号和验证码
        String phoneAndCode = request.getParameter("phoneAndCode");
        String iosDeviceId = null;
        String androidDeviceId = null;
        if (StringUtils.isBlank(phoneAndCode)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            phoneAndCode = Constant.toJsonValue(jsonObject, "phoneAndCode");
            iosDeviceId = Constant.toJsonValue(jsonObject, "iosDeviceId");
            androidDeviceId = Constant.toJsonValue(jsonObject, "androidDeviceId");
        } else {
            iosDeviceId = request.getParameter("iosDeviceId");
            androidDeviceId = request.getParameter("androidDeviceId");
        }
        if (Constant.toEmpty(phoneAndCode)) {
            String[] userIfno = phoneAndCode.split("_");
            String mobileNumber = userIfno[0];
            // 缓存中的验证码
            String redisStr = redisUtil.getStr(mobileNumber);
            logger.info("微信登录用户手机号：{}", mobileNumber);
            String verificationCode = userIfno[1];
            Map<String, Object> hashMap = new HashMap<>();
            String str = "8888";
            String lyg = "18284546959"; //TODO 测试号
            if (Constant.toEmpty(str) || Constant.toEmpty(redisStr)) {
                if (verificationCode.equals(redisStr)
                        || (lyg.equals(mobileNumber) && verificationCode.equals(str))) {

                    // 删除验证码
                    redisUtil.delect(mobileNumber);

                    Map<String, Object> resultMap = new HashMap<>();
                    // 查看是否注册
                    resultMap.put("customerPN", mobileNumber);
                    Map<String, Object> customer = userCustomerMapper.findUser(resultMap);
                    if (customer != null) {
                        // 判断用户状态 2-冻结
                        String status = String.valueOf(customer.get("status"));
                        if ("2".equals(status)) {
                            hashMap.put("flag", "2");
                            return hashMap;
                        }
                    }
                    SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = time.format(new Date());
                    if (customer != null) {

                        //用户登录日志
                        Map<String, Object> reqMap = new HashMap<>();
                        reqMap.put("customerId", customer.get("id"));
                        reqMap.put("customerPN", customer.get("customerPN"));
                        reqMap.put("source", customer.get("source"));
                        reqMap.put("createAt", customer.get("timeJoin"));
                        reqMap.put("currentStatus", customer.get("status"));
                        reqMap.put("optTime", DateUtils.formatDate(new Date()));
                        reqMap.put("optType", 2);
                        reqMap.put("optDesc", "用户登录");
                        reqMap.put("recordTime", DateUtils.formatDate(new Date()));
                        userCustomerLogService.saveUserCustomerLog(reqMap);

                        logger.info("{} 用户登录>>>>>>>>>>>>>>>>", mobileNumber);
                        Map<String, Object> pMap = new HashMap<String, Object>();
                        //车妈妈那边第一次登录发送微信消息
                        List<Map<String, Object>> carLists = carMapper.getCarList(Integer.valueOf(customer.get("id").toString()));
                        if (carLists != null && carLists.size() > 0 && carLists.get(0) != null) {
                            logger.info("循环遍历 carLists 开始>>>>>>>>>>>>>>>>>>>>>>");
                            carLists.forEach(a -> {
                                logger.info("车妈妈--车辆信息 ：{}", a);
                                Map<String, String> rmap = new HashMap<>();
                                rmap.put("openid", openid);
                                // 未发送观察期消息
                                if (a.get("messageFlag").toString().equals("5") && (Integer) a.get("status") == 13) {
                                    try {
                                        logger.info("车妈妈--车辆信息 ：发送观察期消息。");
                                        Map<String, Object> over = MapUtil.build().put("messageFlag", 7).put("id", (Integer) a.get("id")).over();
                                        carMapper.updateModel(over);
                                        int i = DateUtils.cutTwoDateToDay(new Date(), (Date) a.get("observationEndTime"));
                                        rmap.put("licensePlateNumber", String.valueOf(a.get("licensePlateNumber")));
                                        rmap.put("amtCompensation", String.valueOf(a.get("amtCompensation"))); //互助额度
                                        rmap.put("day", "剩余" + String.valueOf(i));
                                        rmap.put("money", "9");
                                        rmap.put("dayTime", DateUtils.getStringDateTime((Date) a.get("payTime")));
                                        weiXinUtils.sendTemplate(11, rmap);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        logger.error("异常>>>>>>>>>", e);
                                    }
                                }
                                // 未发送保障中消息
                                if (a.get("messageFlag").toString().equals("7") && (Integer) a.get("status") == 20) {
                                    try {
                                        logger.info("车妈妈--车辆信息 ：发送保障中消息。");
                                        Map<String, Object> over = MapUtil.build().put("messageFlag", 2).put("id", (Integer) a.get("id")).over();
                                        carMapper.updateModel(over);
                                        rmap.put("content", "爱车" + String.valueOf(a.get("licensePlateNumber")) + "加入车V互助成功，您爱车正享受互助保障中。");
                                        rmap.put("theme", "互助申请");
                                        rmap.put("keyword1", "审核通过");
                                        rmap.put("keyword2", new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format((Date) a.get("payTime")));
                                        rmap.put("url", Constant.toReadPro("realURL") + "hfive/view/rule_photo2.html");
                                        weiXinUtils.sendTemplate(1, rmap);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        logger.error("异常>>>>>>>>>", e);
                                    }
                                }

                            });
                        }
                        try {
                            // 处理车妈妈的邀请的数据
                            Map<String, Object> middleCustomerMaintenanceshop = middleCustomerMaintenanceshopMapper.selectByMobileNumberForChemama(mobileNumber);
                            if (middleCustomerMaintenanceshop != null && middleCustomerMaintenanceshop.get("id") != null) {
                                logger.info("更新车妈妈关注信息。");
                                Map<String, Object> middleCustomerMaintenanceshopParam = new HashMap<>();
                                middleCustomerMaintenanceshopParam.put("id", middleCustomerMaintenanceshop.get("id"));
                                middleCustomerMaintenanceshopParam.put("openId", openid);
                                middleCustomerMaintenanceshopParam.put("status", 1);
                                middleCustomerMaintenanceshopMapper.updateModelById(middleCustomerMaintenanceshopParam);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("异常>>>>>>>>>", e);
                        }
                        // 查询用户车辆列表
                        pMap.put("customerId", customer.get("id"));
                        List<Map<String, Object>> carList = carMapper.findCarList(pMap);
                        hashMap.put("carList", carList);

                        // 第一次登录处理
                        String timeLogin = String.valueOf(customer.get("timeLogin"));
                        if (timeLogin.equals("null")) {
                            logger.info("第一次登录>>>>");
                            Map<String, Object> rmap = new HashMap<>();
                            rmap.put("customerId", customer.get("id"));
                            List<Map<String, Object>> list = middleCouponCustomerMapper.findPayCoupon(rmap);
                            if (list != null && list.size() > 0) {
                                List<Map<String, Object>> coupon = new ArrayList<Map<String, Object>>();
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("amount", list.get(0).get("amount"));
                                map.put("shopName", list.get(0).get("name"));
                                coupon.add(map);
                                hashMap.put("coupon", coupon);
                                hashMap.put("firstLogin", "1");
                            }
                            customer.put("timeLogin", date);
                        }

                        //车妈妈登录提示
                        String chemamaHint = "chemama" + mobileNumber;
                        String s = redisUtil.get(chemamaHint);
                        if (Constant.toEmpty(s)) {
                            List<String> list = Arrays.asList(s.split("_"));
                            hashMap.put("chemamaHint", list);
                            redisUtil.delect(chemamaHint);
                        } else {
                            hashMap.put("chemamaHint", new ArrayList<String>());
                        }


                        // eBo包年新用户处理 middle表，车辆信息
                        eBoPackYearsUserFirstLogin(openid, customer.get("id").toString());

                        // 昵称头像获取
                        if (null != openid)
                            customer.put("openId", openid);
                        String associatorImg = String.valueOf(customer.get("portrait"));
                        String associatorNickname = String.valueOf(customer.get("nickname"));
                        //   String openid1 = String.valueOf(customer.get("openid"));
                        if (!Constant.toEmpty(associatorImg) || "null".equals(associatorImg)) {
                            logger.info("用户头像：{}", headimgurl);
                            customer.put("portrait", headimgurl);
                        }
                        if (!Constant.toEmpty(associatorNickname) || "null".equals(associatorNickname)) {
                            logger.info("用户昵称：{}", nickname);
                            customer.put("nickname", nickname);
                        }

                        // token处理
                        logger.info("用户{} token= {}失效时间：{}", customer.get("id"), customer.get("token"), customer.get("tokenaging"));
                        // 不管token是否失效，重新登录都更换token
                        String token = SecurityUtil.getToKen();
                        customer.put("token", token);
                        Date tokenTime = DateUtils.getTokenTime();
                        customer.put("tokenaging", time.format(tokenTime));
                        customer.put("androidDeviceId", androidDeviceId);
                        customer.put("iosDeviceId", iosDeviceId);
                        userCustomerMapper.updateModel(customer);
                        hashMap.put(CommonField.TOKEN, token);
                        hashMap.put("associatorId", customer.get("id"));
                        InsertLogin();
                        return hashMap;
                    }

                    // 注册
                    logger.info("{} 用户注册>>>>>>>>>>>>>>>>", mobileNumber);
                    Map<String, Object> paramModelMap = new HashMap<>();
                    Date tokenTime = DateUtils.getTokenTime();
                    String toKen = SecurityUtil.getToKen();
                    paramModelMap.put("timeLogin", date);
                    paramModelMap.put("customerPN", mobileNumber);
                    paramModelMap.put("toKen", toKen);
                    paramModelMap.put("tokenaging", tokenTime);
                    paramModelMap.put("openId", openid);
                    paramModelMap.put("portrait", headimgurl);
                    paramModelMap.put("nickname", nickname);
                    paramModelMap.put("androidDeviceId", androidDeviceId);
                    paramModelMap.put("iosDeviceId", iosDeviceId);
                    // 判断用户来源
                    Map<String, Object> source = getSource(openid);
                    int i = -1;
                    if (null != source) {
//                        source.put("customerPN",mobileNumber);
                        paramModelMap.putAll(source);
                        paramModelMap.put("status", 1);
                        int originType = (int) source.get("originType");
                        if (0 == originType) { // 个人
                            logger.info(">>>>>>>>>>>>>>>>>个人邀请用户注册>>>>>>>>>>>>>>>>>>>");
                            String rs = invitationService.saveInvition(paramModelMap);
                            logger.info("个人邀请用户注册完成>>>>>>>>>>>>>>>>>>>{}", rs);
                            if ("0".equals(rs)) { // 邀请成功
                                logger.info("{}邀请{}成功！", paramModelMap.get("customerId"), mobileNumber);
                                updateMiddleModel(openid, "-1", paramModelMap.get("newCustomerId").toString(), mobileNumber);
                            } else { // 邀请失败
                                logger.info("{}邀请{}失败！", paramModelMap.get("customerId"), mobileNumber);
                                throw new RuntimeException("对不起，该活动已结束！");
                            }
                        } else if (1 == originType) { // 渠道拉新
                            logger.info(">>>>>>>>>>>>>>>>>渠道用户注册>>>>>>>>>>>>>>>>>>>");
                            paramModelMap.put("couponNo", source.get("modelId"));
                            paramModelMap.put("mobileNumber", mobileNumber);
                            Map<String, Object> rs = promotionService.promotion(paramModelMap);
                            int returnCode = (int) rs.get("returnCode");
                            if (0 == returnCode)
                                logger.info("{}邀请{}成功！", source.get("shopId"), mobileNumber);
                            else
                                logger.info("渠道邀请活动失败！");
                        } else if (2 == originType) { // 自然用户
                            logger.info(">>>>>>>>>>>>>>>>>自然用户注册>>>>>>>>>>>>>>>>>>>");
                            paramModelMap.put("source", "自然用户");
                            i = userCustomerMapper.saveSingle(paramModelMap);
                            updateMiddleModel(openid, "-1", paramModelMap.get("id").toString(), mobileNumber);
                        }
                    } else {
                        logger.info(">>>>>>>>>>>>>>>>>自然用户注册>>>>>>>>>>>>>>>>>>>");
                        paramModelMap.put("source", "自然用户");
                        i = userCustomerMapper.saveSingle(paramModelMap);
                        updateMiddleModel(openid, "-1", paramModelMap.get("id").toString(), mobileNumber);
                    }
                    Map<String, Object> rs = userCustomerMapper.getCustomerinfo(mobileNumber);
//                    String invitedCustomerId = String.valueOf(paramModelMap.get("id"));
                    if (CollectionUtils.isEmpty(rs))
                        throw new RuntimeException("对不起，注册失败，请重新注册！");
                    String invitedCustomerId = rs.get("id").toString();

                    String fromId = String.valueOf(request.getParameter("fromId"));
                    if (!"null".equals(fromId) && !"".equals(fromId)) {
                        paramModelMap.clear();
                        paramModelMap.put("invitedCustomerId", invitedCustomerId);
                        List<Map<String, Object>> list = invitationCustomerMapper.findMore(paramModelMap);
                        if (list == null || (list != null && list.size() <= 0)) {
                            paramModelMap.put("customerId", fromId);

                            Map<String, Object> pm = new HashMap<String, Object>();
                            pm.put("customerId", fromId);
                            list = invitationCustomerMapper.findMore(pm);  //查这个人邀请了多少人

                            paramModelMap.put("status", 0);
                            invitationCustomerMapper.saveSingle(paramModelMap);
                        }
                    }
                    /*Foundation foundation = foundationMapper.findEntitySingle(new HashMap<String, Object>());
                    paramModelMap = new HashMap<String, Object>();
                    paramModelMap.put("showCustomer", foundation.getShowCustomer()+1);
                    foundationMapper.updateModel(paramModelMap);*/

                    redisUtil.delect(mobileNumber);
                    hashMap.put(CommonField.TOKEN, toKen);
                    hashMap.put("associatorId", i);

                    InsertLogin();
                    return hashMap;
                }
                return null;
            }
            return null;
        }
        return null;
    }

    /**
     * @param openid
     * @return
     */
    private Map<String, Object> getSource(String openid) throws Exception {
        // 先判断该openid是否注册过账号
        Map<String, Object> param = new HashMap<>();
        param.put("invitedCustomerOpenID", openid);
        param.put("openId", openid);
        List<Map<String, Object>> list = userCustomerMapper.getCustomerByOpenId(param);
        if (!CollectionUtils.isEmpty(list))
            return null;
        Map<String, Object> outmap = invitationTempMapper.findSingle(param);
        if (null != outmap) {
            return outmap;
        }
        return null;
    }

    public void InsertLogin() throws Exception {
        logger.info("添加登录/注册人数>>>>>>");
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
        String day = time.format(new Date());
        Map<String, Object> rmap = new HashMap<String, Object>();
        rmap.put("day", day);
        List<Map<String, Object>> list = wechatLoginMapper.findMore(rmap);
        if (list != null && list.size() > 0) {
            Map<String, Object> map = list.get(0);
            int count = Integer.valueOf(String.valueOf(map.get("number")).equals("null") ? "0" : String.valueOf(map.get("number")));
            rmap.put("number", ++count);
            rmap.put("id", map.get("id"));
            wechatLoginMapper.updateModel(rmap);
        } else {
            rmap.put("number", "1");
            wechatLoginMapper.saveSingle(rmap);
        }
    }

    public void addAmtCompensationByInvitation(String customerId) throws Exception {
        Map<String, Object> paramModelMap = new HashMap<String, Object>();
        paramModelMap.put("invitedCustomerId", customerId);
        paramModelMap.put("status", 0);
        List<Map<String, Object>> invitationList = invitationCustomerMapper.findMore(paramModelMap);
        if (invitationList != null && invitationList.size() > 0) {
            Map<String, Object> invitationMap = invitationList.get(0);
            String fromCustomerId = String.valueOf(invitationMap.get("customerId"));
            Map<String, Object> carMap = userCustomerMapper.findFirstCarByOpenId(fromCustomerId);
            paramModelMap.clear();
            paramModelMap.put("customerId", fromCustomerId);
            paramModelMap.put("status", 1);
            List<Map<String, Object>> iList = invitationCustomerMapper.findMore(paramModelMap);
            int count = 1;
            if (iList != null && iList.size() > 0) {
                count = iList.size() + 1;
            }
            int status = 1;
            if (carMap != null && carMap.size() > 0) {
                if (count <= 4) {  //最多只增加4个人的额度
                    paramModelMap.clear();
                    paramModelMap.put("amtCompensation", new BigDecimal(String.valueOf(carMap.get("amtCompensation")).equals("null") ? "0" : String.valueOf(carMap.get("amtCompensation"))).add(Constant.oneQuota));
                    paramModelMap.put("level", count >= 4 ? 2 : 1);
                    paramModelMap.put("id", carMap.get("id"));
                    carMapper.updateModel(paramModelMap);
                }
            } else {
                status = 2;
            }
            paramModelMap.clear();
            paramModelMap.put("id", invitationMap.get("id"));
            paramModelMap.put("status", status);
            invitationCustomerMapper.updateModel(paramModelMap);
        }
    }

    private void updateMiddleModel(String opneId, String maintenanceshopId, String customerId, String customerTel) {
        Map<String, Object> map = new HashMap<>();
        map.put("openId", opneId);
        try {
//            List<Map<String, Object>> shopInvitation = middleCustomerMaintenanceshopMapper.findMore(map);
//            if (CollectionUtils.isEmpty(shopInvitation)) {
            // save
            map.put("status", 1);
            map.put("responseNumber", 0);
            map.put("maintenanceshopId", maintenanceshopId);
            map.put("customerId", customerId);
            map.put("customerTel", customerTel);
            middleCustomerMaintenanceshopMapper.saveSingle(map);
//            } else {
//                // update
//                map.put("customerId", customerId);
//                middleCustomerMaintenanceshopMapper.updateModel(map);
//            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }


    @Autowired
    private SchedulerFactoryBean schedulerFactory;
    @Autowired
    PackYearsCodeMapper packYearsCodeMapper;
    @Autowired
    DictionaryService dictionaryService;

    /**
     * ebo包年用户首次登录处理
     *
     * @param openid
     * @param customerId
     */
    private void eBoPackYearsUserFirstLogin(String openid, String customerId) {
        List<Map<String, Object>> codes = packYearsCodeMapper.getCarsByUserId(customerId);
        if (!CollectionUtils.isEmpty(codes) && null != openid) {
            // 车辆发送信息
            codes.forEach(car -> {
                logger.info("eBo包年--车辆信息 ：{}", car);
                // 判断是否是eBo包年
                if (null != car.get("messageFlag") && "50".equals(car.get("messageFlag").toString())) {
                    String carId = car.get("id").toString();
                    try {
                        int status = (int) car.get("status");
                        Map<String, String> messageMap = new HashMap<>();
                        messageMap.put("openid", openid);
                        if (13 == status) {
                            logger.info("ebo用户首次登录发送观察期信息+++++++++++++++++++++++++++++++++++");
                            int i = DateUtils.cutTwoDateToDay(new Date(), (Date) car.get("observationEndTime"));
                            messageMap.put("licensePlateNumber", String.valueOf(car.get("licensePlateNumber")));
                            messageMap.put("amtCompensation", String.valueOf(car.get("amtCompensation"))); //互助额度
                            messageMap.put("day", "剩余" + String.valueOf(i));
                            messageMap.put("money", String.valueOf(car.get("amtCooperation")));
                            messageMap.put("dayTime", DateUtils.getStringDateTime((Date) car.get("payTime")));
                            weiXinUtils.sendTemplate(11, messageMap);
                            try {
                                String jobName = "observationJob_" + carId;
                                Scheduler sche = schedulerFactory.getScheduler();
                                JobKey jobKey = JobKey.jobKey(jobName, "JOB_GROUP_SYSTEM");
                                if (sche.checkExists(jobKey)) {
                                    logger.info("ebo用户首次登录发送观察期信息发送成功，修改保障中定时器openid={}", openid);
                                    JobDetail jobDetail = sche.getJobDetail(jobKey);
                                    JobDataMap jobDataMap = jobDetail.getJobDataMap();
                                    Map<String, Object> params = (Map<String, Object>) jobDataMap.get("params");
                                    params.put("openid", openid);
                                    logger.info("==================新jobDataMap={}", jobDataMap);
                                    QuartzUtils.removeJob(sche, jobName);
                                    String cron = params.get("cron").toString();
                                    QuartzUtils.addJob(sche, jobName, ObservationJob.class, params, cron);
                                }
                            } catch (Exception e) {
                                logger.error("修改定时器异常", e);
                            }
                        } else if (20 == status) {
                            Map dictionary = dictionaryService.findSingle(MapUtil.build().put("type","observationTime").over());
                            messageMap.put("licensePlateNumber", String.valueOf(car.get("licensePlateNumber")));
                            messageMap.put("observationTime",dictionary.get("value").toString());
                            weiXinUtils.sendTemplate(13, messageMap);
                        }
                        messageMap.clear();
                        logger.info("ebo车首次登录处理完成，修改车辆发送信息状态++++++++++++++++++++++++++++++++++++++++");
                        messageMap.put("messageFlag", "2");
                        messageMap.put("id", carId);
                        carMapper.updateModel(messageMap);
                    } catch (Exception e) {
                        logger.error("ebo用户微信推送异常", e);
                    }
                }
            });

            // middle表关系更新
            Map<String, Object> map = new HashMap<>();
            map.put("openId", openid);
            map.put("status", 1);
            map.put("maintenanceshopId", "163");
            map.put("customerId", customerId);
            try {
                middleCustomerMaintenanceshopMapper.updateSubscribeStatus4eBo(map);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }
}
