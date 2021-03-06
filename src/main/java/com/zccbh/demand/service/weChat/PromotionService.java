package com.zccbh.demand.service.weChat;

import com.zccbh.demand.mapper.activities.CouponMapper;
import com.zccbh.demand.mapper.activities.MiddleCouponCustomerMapper;
import com.zccbh.demand.mapper.business.MaintenanceshopMapper;
import com.zccbh.demand.mapper.business.MiddleCustomerMaintenanceshopMapper;
import com.zccbh.demand.mapper.customer.InvitationTempMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.mapper.merchants.CbhMaintenanceshopMapper;
import com.zccbh.demand.pojo.merchants.CbhMaintenanceshop;
import com.zccbh.demand.service.system.UserCustomerLogService;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.base.SecurityUtil;
import com.zccbh.util.base.SmsDemo;
import com.zccbh.util.collect.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PromotionService {

    private Logger logger = LoggerFactory.getLogger(PromotionService.class);


    @Autowired
    MiddleCustomerMaintenanceshopMapper middleCustomerMaintenanceshopMapper;
    @Autowired
    UserCustomerMapper userCustomerMapper;
    @Autowired
    WeiXinUtils weiXinUtils;
    @Autowired
    MiddleCouponCustomerMapper middleCouponCustomerMapper;
    @Autowired
    CouponMapper couponMapper;
    @Autowired
    MaintenanceshopMapper maintenanceshopMapper;
    @Autowired
    CbhMaintenanceshopMapper cbhMaintenanceshopMapper;

    @Autowired
    InvitationTempMapper invitationTempMapper;
    
    @Autowired
    private UserCustomerLogService userCustomerLogService;

    public Map<String, Object> promotion(Map<String, Object> param) throws Exception {
        Map<String, Object> rs = new HashMap<>();

        String shopId = param.get("shopId").toString();   //店ID
        String couponNo = param.get("couponNo").toString();  //优惠券编号
        String mobileNumber = param.get("mobileNumber").toString();  //手机号
        String openId = param.get("openId").toString();
        logger.info("店铺={}，优惠券={}，手机号={}", shopId, couponNo, mobileNumber);
        Map<String, Object> rMap = new HashMap<String, Object>();
        Map<String, String> nicknames = weiXinUtils.getNickname(openId);
        String headimgurl = nicknames.get("headimgurl");
        String nickname = nicknames.get("nickname");
        rMap.put("customerPN", mobileNumber);
        // 根据手机号查询用户
        List<Map<String, Object>> list = userCustomerMapper.findMore(rMap);
        String customerId = "";
        if (list != null && list.size() > 0) {
            logger.info("{} 该手机已经注册会员。", mobileNumber);
            rMap.clear();
            //优惠券数量减少1
            rMap.put("shopId", shopId);
            rMap.put("couponNo", couponNo);
            // 查找商家对应的优惠券
            List<Map<String, Object>> more = couponMapper.findMore(rMap);
            if (more != null && more.size() > 0) {
                Map<String, Object> coupon = more.get(0);
                Integer type = (Integer) coupon.get("type");
                if (type == 2) {
                    rs.put("returnCode", 500);
                    return rs;
                }
            }
            customerId = String.valueOf(list.get(0).get("id"));
            if (null != list.get(0).get("openId") && !"".equals(list.get(0).get("openId").toString())) {
                String oldOpenid = String.valueOf(list.get(0).get("openId"));
                logger.info("{} 手机拥有旧的微信号{}", mobileNumber, oldOpenid);
                //该手机号以前的openId 是否关注过（不管是否已取关）
                rMap.clear();
                rMap.put("openId", oldOpenid);
                list = middleCustomerMaintenanceshopMapper.findMore(rMap);
                if (list != null && list.size() > 0) {
                    Map<String, Object> map = list.get(0);
                    logger.info(" 旧微信{} 已关注过 。现在状态为（0-关注，1-取关）：{}", oldOpenid, map.get("status"));
                    String maintenanceshopId = String.valueOf(map.get("maintenanceshopId"));
                    if (!maintenanceshopId.equals(shopId)) {
                        rs.put("returnCode", 501);
                        return rs;
                    }
                } else { //未关注过
                    logger.info(" 旧微信{} 未关注过 。", oldOpenid);
                    rMap.clear();
                    rMap.put("openId", openId);
                    list = middleCustomerMaintenanceshopMapper.findMore(rMap);  //微信
                    if (list == null || (list != null && list.size() <= 0)) {
                        logger.info(" 新微信{} 未关注。保存关注关系>>>", openId);
                        rMap.put("maintenanceshopId", shopId);
                        rMap.put("status", 1);
                        rMap.put("customerId", customerId);
                        middleCustomerMaintenanceshopMapper.saveSingle(rMap);
                        
                        //获取用户信息
                        Map<String, Object> reqMap = new HashMap<>();
                        reqMap.put("id", param.get("newCustomerId"));
                        Map<String, Object> customer = userCustomerMapper.findUser(reqMap);
                        //用户领券
                    	reqMap.clear();
                    	reqMap.put("customerId", customer.get("id"));
                    	reqMap.put("customerPN", customer.get("customerPN"));
                    	reqMap.put("source", customer.get("source"));
                    	reqMap.put("createAt", customer.get("timeJoin"));
                    	reqMap.put("currentStatus", customer.get("status"));
                    	reqMap.put("optTime", DateUtils.formatDate(new Date()));
                    	reqMap.put("optType", 14);
                    	reqMap.put("optDesc", "用户领券");
                    	reqMap.put("recordTime", DateUtils.formatDate(new Date()));
                    	userCustomerLogService.saveUserCustomerLog(reqMap);
                    	
                    } else {
                        logger.info(" 新微信{} 已关注。更新关注关系>>>", openId);
                        rMap.put("maintenanceshopId", shopId);
                        rMap.put("customerId", customerId);
                        middleCustomerMaintenanceshopMapper.updateModel(rMap);
                    }
                    rMap.clear();
                    //更新用户表的openId
                    rMap.put("openId", openId);
                    rMap.put("id", customerId);
                    userCustomerMapper.updateModel(rMap);
                }
            }

        } else { //如果是新用户
            logger.info("{} 新用户>>>", mobileNumber);
            param.put("source", "自然用户");
            CbhMaintenanceshop maintenanceshop = cbhMaintenanceshopMapper.selectByPrimaryKey(Integer.valueOf(shopId));
            if (maintenanceshop != null) {
                param.put("source", maintenanceshop.getName());
            }
            logger.info("用户source={}", param.get("source"));
            userCustomerMapper.saveSingle(param);
            customerId = String.valueOf(param.get("id"));
            logger.info("{} 注册成功，id={}。", mobileNumber, customerId);


            // 取关注临时表数据
            rMap.clear();
            rMap.put("openId", openId);
//            list = middleCustomerMaintenanceshopMapper.findMore(rMap);  //微信
            rMap.put("customerId", customerId);
            rMap.put("maintenanceshopId", shopId);
            rMap.put("customerTel", mobileNumber);
            rMap.put("responseNumber", 0);
            rMap.put("status", 1);
//            if (list == null || (list != null && list.size() <= 0)) {
                logger.info("{} 新用户,未关注，保存关注关系。", mobileNumber);
                middleCustomerMaintenanceshopMapper.saveSingle(rMap);
//            } else {
//                logger.info("{} 新用户,已关注，更新关注关系，更新为最新注册渠道。", mobileNumber);
//                middleCustomerMaintenanceshopMapper.updateModel(rMap);
//            }
        }

        rMap.clear();
        rMap.put("customerId", customerId);
        rMap.put("couponNo", couponNo);
        list = middleCouponCustomerMapper.findMore(rMap);
        if (list != null && list.size() > 0) { //已经领取过
            logger.info("{} 已经领取过{}该优惠券。", customerId, couponNo);
            rs.put("returnCode", 502);
            return rs;
        }
        rMap.clear(); //优惠券数量减少1
        rMap.put("shopId", shopId);
        rMap.put("couponNo", couponNo);
        list = couponMapper.findMore(rMap);
        String flag = "";
        if (list != null && list.size() > 0) {
            Map<String, Object> map = list.get(0);
            String couponId = String.valueOf(map.get("id"));
            String status = String.valueOf(map.get("status"));
            switch (status) {
                case "0":
                    flag = "活动未开始";
                    break;
                case "2":
                    flag = "活动已结束";
                    break;
                case "100":
                    flag = "优惠券已领完";
                    break;
                default:
                    break;
            }
            logger.info(" >>>>>>>>>>>>>>>>>flag = {}", flag);
            if (flag.equals("")) {
                int surplusNum = Integer.valueOf(String.valueOf(map.get("surplusNum"))); // 优惠券剩余
                rMap.clear();
                rMap.put("surplusNum", --surplusNum); // TODO 有并发问题
                rMap.put("couponNo", couponNo);
                if (surplusNum == 0) {  //数量为0把状态改为100
                    rMap.put("status", 100);
                }
                couponMapper.updateModel(rMap);

                rMap.clear();   //插入优惠券和用户关联表
                rMap.put("customerId", customerId);
                rMap.put("couponNo", couponNo);
                rMap.put("status", 1);
                rMap.put("isDel", 0);
                middleCouponCustomerMapper.saveSingle(rMap);

                rMap.clear();
                rMap.put("id", shopId);
                Map<String, Object> m = maintenanceshopMapper.findSingle(rMap);
                // 发短信
//                if ("161".equals(shopId)) {
//                    // 太平洋保险拉新活动 特殊要求
//                    SmsDemo.sendSms(91, String.valueOf(mobileNumber), "车V互助");
//                } else {
//                    SmsDemo.sendSms(91, String.valueOf(mobileNumber), String.valueOf(m.get("name")));
//                }
            } else {
                logger.info("{}商家的{}活动异常", param.get("source"), couponNo);
                rs.put("returnCode", 503);
                rs.put("flag", flag);
                return rs;
            }
        } else {
            rs.put("returnCode", 504);
            return rs;
        }
        rs.put("returnCode", 0);
        return rs;
    }

}
