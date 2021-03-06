package com.zccbh.demand.controller.weChat;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.activities.CouponModelMapper;
import com.zccbh.demand.mapper.activities.MiddleCouponCustomerMapper;
import com.zccbh.demand.mapper.business.AccountMapper;
import com.zccbh.demand.mapper.business.MiddleCustomerMaintenanceshopMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.customer.InvitationCustomerMapper;
import com.zccbh.demand.mapper.customer.RecordRechargeMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.mapper.event.EventApplyMapper;
import com.zccbh.demand.mapper.event.EventCommentMapper;
import com.zccbh.demand.mapper.event.EventComplaintMapper;
import com.zccbh.demand.mapper.event.EventReceivecarMapper;
import com.zccbh.demand.mapper.foundation.FoundationMapper;
import com.zccbh.demand.mapper.user.MessageBackstageMapper;
import com.zccbh.demand.pojo.common.Foundation;
import com.zccbh.demand.service.activities.CouponService;
import com.zccbh.demand.service.business.MaintenanceshopService;
import com.zccbh.demand.service.customer.*;
import com.zccbh.demand.service.event.DistributionOrder;
import com.zccbh.demand.service.event.EventCommentService;
import com.zccbh.demand.service.event.EventService;
import com.zccbh.demand.service.order.OrderService;
import com.zccbh.demand.service.system.CarLogService;
import com.zccbh.demand.service.system.UserCustomerLogService;
import com.zccbh.demand.service.user.UserAdminService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.Base64;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.base.HttpUtils;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.uploadImg.UploadFileUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class IndexController {
    @Autowired
    EventService eventService;
    @Autowired
    CarService carService;
    @Autowired
    CarMapper carMapper;
    @Autowired
    UserCustomerService userCustomerService;
    @Autowired
    UserCustomerMapper userCustomerMapper;
    @Autowired
    EventComplaintMapper eventComplaintMapper;
    @Autowired
    EventCommentMapper eventCommentMapper;
    @Autowired
    EventReceivecarMapper eventReceivecarMapper;
    @Autowired
    RecordRechargeMapper recordRechargeMapper;
    @Autowired
    MessageService messageService;
    @Autowired
    MaintenanceshopService maintenanceshopService;
    @Autowired
    EventCommentService eventCommentService;
    @Autowired
    FoundationMapper foundationMapper;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    EventApplyMapper eventApplyMapper;
    @Autowired
    MessageBackstageMapper messageBackstageMapper;
    @Autowired
    MiddleCustomerMaintenanceshopMapper middleCustomerMaintenanceshopMapper;
    @Autowired
    DistributionOrder distributionOrder;
    @Autowired
    WeiXinUtils weiXinUtils;
    @Autowired
    MiddleCouponCustomerMapper middleCouponCustomerMapper;
    @Autowired
    private InvitationCustomerMapper invitationCustomerMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MessageBackstageService messageBackstageService;

    @Autowired
    private InvitationService invitationService;
    @Autowired
    private UserCustomerMapper customerMapper;
    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponModelMapper couponModelMapper;

    @Autowired
    private UserAdminService userAdminService;

    @Autowired
    private InvitationTempService invitationTempService;

    @Autowired
    private CarLogService carLogService;

    @Autowired
    private UserCustomerLogService userCustomerLogService;

    private Logger logger = LoggerFactory.getLogger(IndexController.class);


    @RequestMapping(value = "/index", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String index(HttpServletRequest request) throws Exception {
        try {
            String openid = String.valueOf(request.getSession().getAttribute("fromUserName"));
            String agent = request.getHeader("User-Agent");
            if (StringUtils.isBlank(openid) && agent.toLowerCase().indexOf("micromessenger") >= 0) {
                return Constant.toReModel(CommonField.FAIL_OPENID, "没有获取到OPENID,请刷新后再试", null);
            }
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            Map<String, Object> map = eventService.getWeChatIndexInfo();
            try {
                if (!openid.equals("null") && !openid.equals("")) {
                    Map<String, String> oMap = weiXinUtils.getNickname(openid);
                    map.put("wxNickname", oMap.get("nickname") == null ? null : Base64.getFromBase64(oMap.get("nickname")));
                    map.put("wxHead", oMap.get("headimgurl"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (tokenMap != null) {
                List<Map<String, Object>> carList = eventService.getWeChatIndexInfo(tokenMap.get("id"));
                map.put("carList", carList);
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("id", tokenMap.get("id"));
                Map<String, Object> userMap = userCustomerService.findUser(param);
                String portrait = String.valueOf(userMap == null ? null : userMap.get("portrait"));
                String nickname = String.valueOf(userMap.get("nickname"));
                nickname = nickname.equals("null") ? "" : Base64.getFromBase64((String) nickname);
                map.put("nickname", nickname);
                map.put("head", portrait.indexOf("thirdwx.qlogo.cn") == -1 ? UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL, portrait) : portrait);
            }
            Map<String, Object> randomCar = eventService.findRandomCar();
            if (randomCar != null) {
                String portrait = String.valueOf(randomCar.get("portrait"));
                if (portrait.equals("null")) {
                    portrait = "";
                } else {
                    portrait = portrait.indexOf("thirdwx.qlogo.cn") == -1 ? UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL, portrait) : portrait;
                }
                String model = String.valueOf(randomCar.get("model"));
                try {
                    model = model.substring(0, model.length() - 3) + "***";
                } catch (Exception e) {
                    e.printStackTrace();
                }
                model = "刚刚" + model + "加入车V互助";
                model = portrait == "" ? model : model + "_" + portrait;
                map.put("randomCar", model);
            }
            return map == null ?
                    Constant.toReModel(CommonField.PARAMETER_ERROR, "查询失败", null)
                    : Constant.toReModel(CommonField.SUCCESS, "SUCCESS", map);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
        }

    }

    @RequestMapping(value = "/checkSubscribe", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String checkSubscribe(HttpServletRequest request) {
        try {
            String flag = "0";
            String openId = (String) request.getSession().getAttribute("fromUserName");
            logger.info("检查用户{}是否关注>>>>>", openId);
            String agent = request.getHeader("User-Agent");
            if (StringUtils.isBlank(openId) && agent.toLowerCase().indexOf("micromessenger") >= 0) {
                return Constant.toReModel(CommonField.FAIL_OPENID, "没有获取到OPENID,请刷新后再试", null);
            }
            Map<String, Object> pmap = new HashMap<String, Object>();
            pmap.put("openId", openId);
            pmap.put("status", "1");
            List<Map<String, Object>> list = middleCustomerMaintenanceshopMapper.findMore(pmap);
            if (list != null && list.size() > 0) {
                if ("1".equals(String.valueOf(list.get(0).get("status")))) {
                    logger.info("用户已关注>>>>>>middle_customer_maintenanceshop");
                    flag = "1";
                }
            }
            if ("0".equals(flag)) {
                pmap.put("invitedCustomerOpenID", openId);
                Map<String, Object> userInvitaion = invitationTempService.findSingle(pmap);
                if (!CollectionUtils.isEmpty(userInvitaion)) {
                    if ("1".equals(String.valueOf(userInvitaion.get("unsubscribe")))) {
                        logger.info("用户已关注>>>>>>cbh_invitation_temp");
                        flag = "1";
                    }
                }
            }
            return Constant.toReModel(CommonField.SUCCESS, "SUCCESS", flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
    }

    @RequestMapping(value = "/distinguish", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String distinguish(HttpServletRequest request) throws Exception {
        String host = "https://dm-53.data.aliyun.com";
        String path = "/rest/160601/ocr/ocr_vehicle.json";
        String method = "POST";
        String appcode = "b038b159767745958ecfada0a3b5bfd0";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        String image = request.getParameter("image");
        String side = request.getParameter("side");
        try {
            Map<String, Object> map = carService.saveImg(image, null);  //根据微信图片ID获取图片BASE
            image = String.valueOf(map.get("base"));
            String bodys = "{\"image\":\"" + image + "\",\"configure\":\"{\\\"side\\\":\\\"" + side + "\\\"}\"}";
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            String status = String.valueOf(response.getStatusLine());
            return status.indexOf("OK") != -1 ? Constant.toReModel(CommonField.SUCCESS, "SUCCESS", EntityUtils.toString(response.getEntity())) :
                    Constant.toReModel(CommonField.FAIL, "请上传有效的行驶证", EntityUtils.toString(response.getEntity()));
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
        }
    }

    /**
     * @param request 专门查询token是否在有效期内
     * @return
     */
    @RequestMapping(value = "/validationToken", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String validationToken(HttpServletRequest request) {
        try {
            Map<String, String> customer = userCustomerService.validationToken(request);
            if (customer != null) {
                String flag = customer.get("flag");
                if ("1".equals(flag)) {
                    return Constant.toReModel(CommonField.SUCCESS, "是", customer);
                }
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
            return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
    }

    /**
     * @param request 保存车辆第一步
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/register1", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String register1(HttpServletRequest request) throws Exception {
        try {
            //验证token，获取车主信息
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                //新用户id
                int customerId = Integer.valueOf(tokenMap.get("id"));
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("customerId", customerId);

                //获取用户信息
                Map<String, Object> reqMap = new HashMap<>();
                reqMap.put("id", customerId);
                Map<String, Object> customer = userCustomerMapper.findUser(reqMap);
                //用户添加车辆日志
                reqMap.clear();
                reqMap.put("customerId", customer.get("id"));
                reqMap.put("customerPN", customer.get("customerPN"));
                reqMap.put("source", customer.get("source"));
                reqMap.put("createAt", customer.get("timeJoin"));
                reqMap.put("currentStatus", customer.get("status"));
                reqMap.put("optTime", DateUtils.formatDate(new Date()));
                reqMap.put("optType", 3);
                reqMap.put("optDesc", "用户添加车辆");
                reqMap.put("recordTime", DateUtils.formatDate(new Date()));
                userCustomerLogService.saveUserCustomerLog(reqMap);

                BigDecimal amtCompensation = new BigDecimal("1000");
                int level = 1;
                Map<String, Object> rm = new HashMap<String, Object>();
                rm.put("customerId", customerId);
                //查询会员所属车辆
                List<Map<String, Object>> carList = carMapper.findCarList(rm);
                if (carList == null || (carList != null && carList.size() <= 0)) {
                    rm.put("status", 2);
                    rm.put("customerId", customerId);
                    //查询该会员推荐记录
                    List<Map<String, Object>> l = invitationCustomerMapper.findMore(rm);
                    if (l != null && l.size() > 0) {
                        amtCompensation = amtCompensation.add(Constant.oneQuota.multiply(new BigDecimal(String.valueOf(l.size() > 4 ? 4 : l.size()))));
                        level = l.size() >= 4 ? 2 : level;
                        //修改记录--推荐人获取奖励
                        for (Map<String, Object> m : l) {
                            Map<String, Object> paramModelMap = new HashMap<String, Object>();
                            paramModelMap.put("id", m.get("id"));
                            paramModelMap.put("status", 1);
                            paramModelMap.put("oldStatus", 0);
                            invitationCustomerMapper.updateModel(paramModelMap);
                        }
                    }
                }
                map.put("amtCompensation", amtCompensation);
                map.put("level", level);
                map.put("createAt", DateUtils.formatDate(new Date()));
                map.put("drvingCity", request.getParameter("drvingCity")); //行驶城市
                map.put("licensePlateNumber", request.getParameter("licensePlateNumber")); //车牌
                String id = request.getParameter("id");
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("licensePlateNumber", request.getParameter("licensePlateNumber"));
                Map<String, Object> carMap = carService.findOne(paramMap);
                if ((id == null || "".equals(id)) && carMap != null && !"30".equals(String.valueOf(carMap.get("status")))) {
                    if (customerId == Integer.valueOf(carMap.get("customerId").toString()))
                        return Constant.toReModel(CommonField.REPEATCAR, "该车辆已添加过且没有退出", CommonField.PARAMETER_ERROR_PROMPT);
                    return Constant.toReModel(CommonField.FAIL, "该车辆已添加过且没有退出", CommonField.PARAMETER_ERROR_PROMPT);
                }


                if (id != null && (!"".equals(id))) { // 编辑车辆会有id参数
                    Map<String, Object> newCarMap = new HashMap<>();
                    newCarMap.put("id", id);
                    newCarMap.put("customerId", customerId);
//					newCarMap.put("level", level);
                    newCarMap.put("createAt", DateUtils.formatDate(new Date()));
                    newCarMap.put("drvingCity", request.getParameter("drvingCity")); //行驶城市
                    newCarMap.put("licensePlateNumber", request.getParameter("licensePlateNumber")); //车牌
//					newCarMap.put("status", "13");
//					newCarMap.put("messageFlag", null);
//					newCarMap.put("reasonSignout", null);
                    newCarMap.put("amtCompensation", carMap.get("amtCompensation"));
                    newCarMap.put("typeGuarantee", carMap.get("typeGuarantee"));
                    newCarMap.put("signoutMessageFlag", 0);
                    carService.updateCar(newCarMap);
//					carService.oldCarObservation(newCarMap);
                    Map<String, Object> resultMap = new HashMap<String, Object>();
                    resultMap.put("id", newCarMap.get("id"));
                    return Constant.toReModel(CommonField.SUCCESS, "SUCCESS", resultMap);
                } else {
                    map.put("status", 1);
                    //保存车辆信息--免支付
                    String code = carService.saveCar(map);
                    if (!"0".equals(code))
                        return Constant.toReModel("500", "该车辆已存在,请勿重复添加", "");

                    Map<String, Object> resultMap = new HashMap<String, Object>();
                    String carId = String.valueOf(map.get("id"));
                    logger.info("检查用户添加车辆++{}>>>>>", carId);
                    resultMap.put("id", carId);

                    //保存车辆后------>保存操作记录
                    Map<String, Object> carLogMap2 = new HashMap<>();
                    carLogMap2.put("customerId", customerId);
                    carLogMap2.put("carId", map.get("id"));
                    carLogMap2.put("optTime", DateUtils.formatDate(new Date()));
                    carLogMap2.put("optType", 1);
                    carLogMap2.put("optDesc", "用户添加车辆");
                    carLogMap2.put("recordeTime", DateUtils.formatDate(new Date()));
                    carLogService.saveCarLog(carLogMap2);

                    //获取名下优惠券信息
                    map.clear();
                    map.put("customerId", Integer.valueOf(tokenMap.get("id")));
                    List<Map<String, Object>> list = middleCouponCustomerMapper.findPayCoupon(map);
                    String invitationUserId = null;
                    if (list != null && list.size() > 0) {
                        Map<String, Object> m = list.get(0);
                        //获取优惠券面额
                        BigDecimal a = new BigDecimal(String.valueOf(m.get("amount")).equals("null") ? "0" : String.valueOf(m.get("amount")));
                        //累加虚拟金额，
                        carService.addFoundationAllowance(a.compareTo(new BigDecimal("9")) >= 0 ? a : new BigDecimal("9"), a);
                        //修改车辆信息
                        Map<String, Object> rmap = new HashMap<String, Object>();
                        rmap.put("amtCompensation", 1000);
                        rmap.put("amtCooperation", a);
                        rmap.put("status", 13);
                        rmap.put("typeGuarantee", 1);
                        rmap.put("timeSignout", "1111-11-11 11:11:11");
                        rmap.put("reasonSignout", "");
                        rmap.put("payTime", DateUtils.formatDate(new Date()));
                        rmap.put("reJoinNum", "reJoinNum");

                        rmap.put("id", carId);
                        carService.updateCar(rmap);

                        //车辆充值后---->查询车辆信息------>保存操作记录
                        Map<String, Object> carLogMap = new HashMap<>();
                        carLogMap.put("customerId", customerId);
                        carLogMap.put("carId", rmap.get("id"));
                        carLogMap.put("optTime", rmap.get("payTime"));
                        carLogMap.put("optType", 2);
                        carLogMap.put("optDesc", "用户车辆支付");
                        carLogMap.put("recordeTime", DateUtils.formatDate(new Date()));
                        carLogService.saveCarLog(carLogMap);

                        //车辆进入观察期------>保存操作记录
                        Map<String, Object> carLogMap1 = new HashMap<>();
                        carLogMap1.put("customerId", customerId);
                        carLogMap1.put("carId", rmap.get("id"));
                        carLogMap1.put("optTime", rmap.get("payTime"));
                        carLogMap1.put("optType", 3);
                        carLogMap1.put("optDesc", "用户车辆进入观察期");
                        carLogMap1.put("recordeTime", DateUtils.formatDate(new Date()));
                        carLogService.saveCarLog(carLogMap1);

                        carService.addFoundationShowCustomer();
                        //插入充值记录
                        Map<String, Object> pMap = new HashMap<String, Object>();
                        pMap.put("id", Integer.valueOf(tokenMap.get("id")));
                        Map<String, Object> userInfo = userCustomerService.findUser(pMap);
                        pMap.clear();
                        pMap.put("customerId", Integer.valueOf(tokenMap.get("id")));
                        pMap.put("carId", carId);
                        pMap.put("amt", a);
                        pMap.put("type", 3);
                        pMap.put("status", 1);
                        pMap.put("eventType", "1");
                        pMap.put("timeRecharge", DateUtils.formatDate(new Date()));
                        pMap.put("description", "手机号为" + userInfo.get("customerPN") + "的会员为" + request.getParameter("licensePlateNumber") + "充值了" + a + "元");
                        recordRechargeMapper.saveSingle(pMap);

                        Map<String, Object> carMap1 = carMapper.findCarByRecordRechargeId(String.valueOf(pMap.get("id")));
                        carMap1.put("typeGuarantee", 1);

                        //根据被邀请人id,获取邀请记录详情
                        Map<String, Object> mp = new HashMap<>();
                        mp.put("invitedCustomerId", customerId);
                        List<Map<String, Object>> invitionCustomerList = invitationService.selectByInvitedCustomerId(mp);
                        if (invitionCustomerList.size() > 0) {
                            //获取邀请人详情
                            mp.clear();
                            mp.put("customerId", invitionCustomerList.get(0).get("customerId"));
                            invitationUserId = mp.get("customerId").toString();
                            Map<String, Object> user = customerMapper.findSingle(mp);
                            //获取推荐人邀请成功记录
                            mp.clear();
                            mp.put("customerId", user.get("id"));
                            List<Map<String, Object>> customerList = invitationCustomerMapper.getInvitedList(mp);
                            //获取推荐人车辆
                            Map<String, Object> carDetail = carService.getCarByCustomerId(mp);

                            if (carDetail != null) {

                                // 根据推荐记录查询活动模板
                                mp.clear();
                                mp.put("modelId", invitionCustomerList.get(0).get("modelId"));
                                Map<String, Object> couponModel = couponModelMapper.selectByModelId(mp);
                                // 给邀请人充值互助金
                                mp.clear();
                                mp.put("customerId", invitionCustomerList.get(0).get("customerId"));
                                mp.put("carId", carDetail.get("id"));
                                mp.put("amt", couponModel.get("inviterAmount"));
                                mp.put("type", 3);
                                mp.put("status", 1);
                                mp.put("eventType", 1);
                                mp.put("timeRecharge", DateUtils.formatDate(new Date()));
                                mp.put("description", "邀请活动为" + carDetail.get("licensePlateNumber") + "充值了" + couponModel.get("inviterAmount") + "元");
                                recordRechargeMapper.saveSingle(mp);
                                // 累加虚拟金额，
                                carService.addFoundationAllowance(new BigDecimal(couponModel.get("inviterAmount").toString()), new BigDecimal(couponModel.get("inviterAmount").toString()));

                                // 增加邀请人互助金
                                mp.clear();
                                String amtCooperation;
                                if(null == carDetail.get("amtCooperation") || "".equals(carDetail.get("amtCooperation")))
                                    amtCooperation="0";
                                else
                                    amtCooperation = carDetail.get("amtCooperation").toString();
                                mp.put("amtCooperation", (new BigDecimal(amtCooperation).add(new BigDecimal((couponModel.get("inviterAmount").toString())))));
                                mp.put("id", carDetail.get("id"));
                                // 如果车辆的状态是待支付或者已退出,改为观察期
                                String status = String.valueOf(carDetail.get("status"));
                                boolean flag = false;
                                if ("1".equals(status) || "30".equals(status)) {
                                    mp.put("status", 13);
                                    flag = true;
                                    if ("30".equals(status)) {
                                        mp.put("timeSignout", "1111-11-11 11:11:11");
                                        mp.put("reasonSignout", "");
                                        mp.put("signoutMessageFlag", 0);
                                        mp.put("reJoinNum", "reJoinNum");
                                        mp.put("createAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                        mp.put("payTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                    }
                                }
                                carService.updateCar(mp);
                                if (flag) {
                                    Map<String, Object> carParamMap = new HashMap<>();
                                    carParamMap.put("id", carDetail.get("id"));
                                    carParamMap.put("typeGuarantee", carDetail.get("typeGuarantee"));
                                    carParamMap.put(CommonField.OPEN_ID, user.get("openId"));
                                    carParamMap.put("licensePlateNumber", carDetail.get("licensePlateNumber"));
                                    carParamMap.put("customerId", user.get("id"));
                                    carParamMap.put("inviterAmount", couponModel.get("inviterAmount").toString());
                                    carParamMap.put("amtCompensation", String.valueOf(carDetail.get("amtCompensation"))); //互助额度
                                    carService.observation(carParamMap);
                                }
//
//									mp.clear();
//									mp.put("status", "1");
//									mp.put("id", invitionCustomerList.get(0).get("id"));
//									invitationService.updateInvitation(mp);
                            } else {
                                mp.clear();
                                mp.put("status", "2");
                                mp.put("id", invitionCustomerList.get(0).get("id"));
                                invitationService.updateInvitation(mp);
                            }
                        }
                        carService.observation(carMap1); //自动
                        //修改优惠券记录
                        rmap.clear();
                        rmap.put("status", 10);
                        rmap.put("id", m.get("id"));
                        middleCouponCustomerMapper.updateModel(rmap);
                        resultMap.put("noNeedPay", "1");
                    } else {
                        resultMap.put("noNeedPay", "0");
                    }

                    eventService.updateDayNumber("initNum", 1);
                    if ("1".equals(resultMap.get("noNeedPay"))) // 领取优惠成功，发送推荐人消息
                        messageService.pushInvitationSuccessTemplate(invitationUserId);

                    return Constant.toReModel(CommonField.SUCCESS, "SUCCESS", resultMap);
                }

            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            logger.error("",e);
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
        }
    }

    /**
     * 添加行驶证信息
     *
     * @author xiaowuge
     * @date 2018年10月16日
     * @version 1.0
     */
    @RequestMapping(value = "/modifyDrivingLicense", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String register11(HttpServletRequest request) throws Exception {
        Map<String, Object> msgMap = new HashMap<>();
        String eventNum = request.getParameter("eventNo");
        String orderNum = request.getParameter("orderNo");
        boolean b = eventNum != null && !"".equals(eventNum);
        String orderNu = b ? eventNum : orderNum;
        msgMap.put("orderNo", orderNu);
        msgMap.put("orderStatus", 1);
        if (messageBackstageService.countBackMsg(msgMap) > 0) {
            return Constant.toReModel(CommonField.FAIL, "请不要重复提交", null);
        }

        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("customerId", Integer.valueOf(tokenMap.get("id")));
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = time.format(new Date());
                map.put("brand", request.getParameter("brand"));  //品牌
                map.put("carType", request.getParameter("carType")); //车型
                map.put("model", request.getParameter("model")); //车型
                map.put("drvingCity", request.getParameter("drvingCity")); //行驶城市
                map.put("nameCarOwner", request.getParameter("nameCarOwner"));  //车主姓名
                map.put("VIN", request.getParameter("VIN")); //车辆识别号
                map.put("engineNum", request.getParameter("engineNum"));  //发动机号码
                map.put("registerDate", request.getParameter("registerDate")); //注册日期
                map.put("issueDate", request.getParameter("issueDate")); //发证日期
                map.put("id", request.getParameter("carId"));
                String base = request.getParameter("base");  //行驶证微信ID
                if (base != null && !base.equals("")) {
                    Map<String, Object> bMap = carService.saveImg(base, null); //根据图片ID获取图片BASE
                    base = String.valueOf(bMap.get("base"));
                    Map<String, Object> rmap = UploadFileUtil.WeChatBase64ToImg(org.apache.commons.lang3.ArrayUtils.toArray(base), CommonField.DRIVING_IMG_URL);
                    if (rmap != null && rmap.get("associatorimg") != null) {
                        map.put("drivingLicense", String.valueOf(rmap.get("associatorimg")));
                    } else {
                        return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
                    }
                }
                carService.updateCar(map);
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("id", map.get("id"));
                String eventNo = request.getParameter("eventNo");
                String orderNo = request.getParameter("orderNo");
                map.clear();
                System.out.println("eventNo:" + eventNo + ";---------------orderNo:" + orderNo);
                Map<String, Object> backMsgParam = new HashMap<>();
                Map<String, Object> carInfo = carService.findCarById(Integer.parseInt(request.getParameter("carId")));
                if (eventNo != null && !"".equals(eventNo)) {
                    map.put("eventNo", eventNo);
                    map.put("timeApply", date);
                    eventApplyMapper.updateModel(map);

                    map.put("statusEvent", 1);
                    eventService.updateEvent(map);
                    backMsgParam.put("orderNo", eventNo);
                    backMsgParam.put("type", 1);
                    backMsgParam.put("content", carInfo.get("licensePlateNumber") + "的救助订单等待您审核！");
                    backMsgParam.put("orderStatus", 1);
                    // 发送推送消息
                    Map<String, Object> pushMsgMap = new HashMap<>();
                    pushMsgMap.put("orderNo", eventNo);
                    pushMsgMap.put("type", 1);
                    pushMsgMap.put("orderStatus", 1);
                    userAdminService.pushMessageToManager("event", "待审核", carInfo.get("licensePlateNumber") + "的救助订单等待您审核！", pushMsgMap);
                } else if (orderNo != null && !"".equals(orderNo)) {
                    map.put("orderNo", orderNo);
                    map.put("applyTime", date);
                    map.put("status", 1);
                    orderService.updateOrder(map);
                    backMsgParam.put("orderNo", orderNo);
                    backMsgParam.put("type", 2);
                    backMsgParam.put("content", carInfo.get("licensePlateNumber") + "的保险理赔订单等待您审核！");
                    backMsgParam.put("orderStatus", 1);
                    // 发送推送消息
                    Map<String, Object> pushMsgMap = new HashMap<>();
                    pushMsgMap.put("orderNo", orderNo);
                    pushMsgMap.put("type", 2);
                    pushMsgMap.put("orderStatus", 1);
                    userAdminService.pushMessageToManager("order", "待审核", carInfo.get("licensePlateNumber") + "的保险理赔订单等待您审核！", pushMsgMap);
                }
                // 保存后台消息
                backMsgParam.put("title", "待审核");
                backMsgParam.put("isSolve", 1);
                backMsgParam.put("createTime", DateUtils.formatDate(new Date()));
                messageBackstageMapper.saveSingle(backMsgParam);
                carService.sendSMS(request.getParameter("carId"), 1);
                return Constant.toReModel(CommonField.SUCCESS, "SUCCESS", resultMap);
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
        }
    }

    /**
     * @param request 保存车辆第二步
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/register2", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String register2(HttpServletRequest request) throws Exception {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                String[] base = request.getParameterValues("base");  //行驶证base64编码
                JSONObject json = new JSONObject();
                String flag = request.getParameter("flag");
                JSONObject jsonObject = new JSONObject();
                if ("2".equals(flag)) {
                    Map<String, Object> resultMap = new HashMap<String, Object>();
                    resultMap.put("id", request.getParameter("id"));
                    Map<String, Object> m = carService.findOne(resultMap);
                    String carPhotos = String.valueOf(m.get("carPhotos"));
                    jsonObject = JSONObject.fromObject(carPhotos);
                }
                int i = 1;
                for (String b : base) {
                    if (b.equals("null")) {
                        switch (i) {
                            case 1:
                                json.put("zh", jsonObject.get("zh"));
                                break;
                            case 2:
                                json.put("yh", jsonObject.get("yh"));
                                break;
                            case 3:
                                json.put("zq", jsonObject.get("zq"));
                                break;
                            case 4:
                                json.put("yq", jsonObject.get("yq"));
                                break;
                            case 5:
                                json.put("qd", jsonObject.get("qd"));
                                break;
                            case 6:
                                json.put("zc", jsonObject.get("zc"));
                                break;
                            case 7:
                                json.put("yc", jsonObject.get("yc"));
                                break;
						/*case 5:
							json.put("cjh", jsonObject.get("cjh"));
							break;*/
                            default:
                                break;
                        }
                    } else {
                        Map<String, Object> rmap = carService.saveImg(b, CommonField.CAR_IMG_URL);
                        if (rmap != null && rmap.get("flag") != null) {
                            switch (i) {
                                case 1:
                                    json.put("zh", rmap.get("associatorimg"));
                                    break;
                                case 2:
                                    json.put("yh", rmap.get("associatorimg"));
                                    break;
                                case 3:
                                    json.put("zq", rmap.get("associatorimg"));
                                    break;
                                case 4:
                                    json.put("yq", rmap.get("associatorimg"));
                                    break;
                                case 5:
                                    json.put("qd", rmap.get("associatorimg"));
                                    break;
                                case 6:
                                    json.put("zc", rmap.get("associatorimg"));
                                    break;
                                case 7:
                                    json.put("yc", rmap.get("associatorimg"));
                                    break;
							/*case 5:
								json.put("cjh", rmap.get("associatorimg"));
								break;*/
                                default:
                                    break;
                            }
                        }
                    }

                    i++;
                }
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = time.format(new Date());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("carPhotos", JSON.toJSONString(json));
                map.put("id", request.getParameter("id"));
                map.put("timeExamine", date);
                map.put("status", 10);
                carService.updateCar(map);

                carService.sendSMS(request.getParameter("id"), 1);

                eventService.updateDayNumber("examineNum", 1);
                return Constant.toReModel(CommonField.SUCCESS, "SUCCESS", "SUCCESS");
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
        }
    }

    /**
     * 查询可支付车辆接口
     *
     * @author xiaowuge
     * @date 2018年10月11日
     * @version 1.0
     */
    @RequestMapping(value = "/weChatGoPay", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String weChatGoPay(HttpServletRequest request) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                Map<String, Object> rmap = new HashMap<String, Object>();
                rmap.put("customerId", Integer.valueOf(tokenMap.get("id")));
                List<Map<String, Object>> list = carService.findPayCar(rmap);
                if (list != null && list.size() > 0) {
                    for (Map<String, Object> m : list) {
                        String carId = String.valueOf(m.get("id"));
                        Map<String, Object> rMap = carService.getPayAmount(carId);
                        m.put("yearPayAmount", new BigDecimal("99").subtract(new BigDecimal(String.valueOf(rMap.get("amount")))));
                    }
                    map.put("carList", list);
                } else {
                    map.put("carList", null);
                }
                List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
                Map<String, Object> mMap = new HashMap<String, Object>();
                mMap.put("id", "1");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
//	        		   if("2018-05-31".equals(df.format(date))||"2018-06-01".equals(df.format(date))||"2018-06-02".equals(df.format(date))||"2018-06-03".equals(df.format(date))||"2018-06-04".equals(df.format(date))){
//	        			   mMap.put("amount", 6.1);
//	        			   mMap.put("remark", "儿童节体验价，预计分摊1-2个月");
//	        		   }else{
                mMap.put("amount", 9);
                mMap.put("remark", "体验价，预计分摊1-2个月");
//	        		   }
                mList.add(mMap);
/*			           mMap = new HashMap<String, Object>();
			           mMap.put("id", "2");
			           mMap.put("amount", 18);
			           mMap.put("remark", "预计分摊二个月");
			           mList.add(mMap);


			           */
                mMap = new HashMap<String, Object>();
                mMap.put("id", "3");
                mMap.put("amount", 99);
                mMap.put("remark", "分摊全年12个月");

                mList.add(mMap);
                map.put("amountList", mList);
                return Constant.toReModel(CommonField.SUCCESS, "是", map);
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
    }

    /**
     * 微信支付车辆
     *
     * @author xiaowuge
     * @date 2018年9月17日
     * @version 1.0
     */
    @RequestMapping(value = "/weChatPay1", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String weChatPay(HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                System.out.println("支付进来了--------------------------------:");
                String json = "";
                String customerId = tokenMap.get("id");
                String[] carIds = request.getParameterValues("CarId");  //选择的车辆id
                String amountId = request.getParameter("amountId");  //金额的Id
                BigDecimal a = new BigDecimal("0");
                String orderNo = "";
                String outTradeNo = "";

                String str = "";
                for (String carId : carIds) {
                    if (amountId.equals("1")) {
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date();
// 							a = "2018-05-31".equals(df.format(date))||"2018-06-01".equals(df.format(date))?new BigDecimal("6.1"):new BigDecimal("9");
                        a = new BigDecimal("9");
                    } else {
                        Map<String, Object> rMap = carService.getPayAmount(carId);
                        a = new BigDecimal("99").subtract(new BigDecimal(String.valueOf(rMap.get("amount"))));
                    }
                    Map<String, Object> cmap = new HashMap<String, Object>();
                    cmap.put("id", carId);
                    Map<String, Object> carMap = carService.findCarDetail(cmap);
                    String timeEnd = String.valueOf(carMap.get("timeEnd"));
                    String typeGuarantee = String.valueOf(carMap.get("typeGuarantee"));
                    String licensePlateNumber = String.valueOf(carMap.get("licensePlateNumber"));

                    SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date now = new Date();
                    if (typeGuarantee.equals("2")) {
                        if (now.getTime() < time.parse(timeEnd).getTime()) {
                            str += str == "" ? licensePlateNumber + "正在包年中，无法充值" : "_" + licensePlateNumber + "正在包年中，无法充值";
                            continue;
                        }
                    }
                    Map<String, Object> pMap = new HashMap<String, Object>();
                    pMap.put("customerId", customerId);
                    pMap.put("carId", carId);
                    pMap.put("amt", a);
                    pMap.put("type", 1);
                    pMap.put("status", 2);
                    pMap.put("eventType", carMap.get("timeBegin") == null ? "1" : "2");
                    recordRechargeMapper.saveSingle(pMap);

                    orderNo += orderNo == "" ? String.valueOf(pMap.get("id")) : "|" + String.valueOf(pMap.get("id"));
                }
                int isZ = carService.PayCarResult(orderNo);
                if (isZ == 1) {
                    return Constant.toReModel(CommonField.SUCCESS, "是", "success");
                } else {
                    return Constant.toReModel(CommonField.FAIL, "失败", "");
                }

            }
            return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
    }


    @RequestMapping(value = "/compensation1", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String compensation(HttpServletRequest request) throws Exception {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                //String base1 = request.getParameter("base1"); //车头照片
                //String base2 = request.getParameter("base2");//车尾
                String carId = request.getParameter("carId"); //车辆ID
                String eventNo1 = request.getParameter("eventNo");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("carId", carId);
                List<Map<String, Object>> count = orderService.weChatFindUnfinishedOrder(map);
                if (eventNo1 == null || "".equals(eventNo1)) {
                    if (count != null && count.size() > 0) {
                        return Constant.toReModel(CommonField.REPETITION, "该车辆在救助或保险理赔中", count.get(0).get("eventNo"));
                    }
                }
                map.put("customerId", tokenMap.get("id"));
                Map<String, Object> card = carService.findCarById(Integer.valueOf(carId));
                if (!String.valueOf(card.get("timeSignout")).equals("1111-11-11 11:11:11.0") && card.get("timeSignout") != null) {
                    return Constant.toReModel(CommonField.REPETITION, "该车已退出保障，无法申请救助", "");
                }
                String[] base = request.getParameterValues("base");  //车损照片base64编码
                String description = request.getParameter("description"); //描述

                String eventNo = Constant.createEventNo();
                String cs = "";
/*				Map<String,Object> rmap1 = UploadFileUtil.WeChatBase64ToImg(org.apache.commons.lang3.ArrayUtils.toArray(base1),CommonField.COMPENSATE_IMG_URL);
				if(rmap1!=null&&rmap1.get("associatorimg")!=null){
					json.put("ct",rmap1.get("associatorimg"));
				}
				Map<String,Object> rmap2 = UploadFileUtil.WeChatBase64ToImg(org.apache.commons.lang3.ArrayUtils.toArray(base2),CommonField.COMPENSATE_IMG_URL);
				if(rmap2!=null&&rmap2.get("associatorimg")!=null){
					json.put("cw",rmap2.get("associatorimg"));
				}*/
                Map<String, Object> pMap = new HashMap<String, Object>();
                pMap.put("eventNo", eventNo1);
                Map<String, Object> detail = eventService.findOrderDetail(pMap);
                String accidentImg = String.valueOf(detail.get("accidentImg"));
                for (String b : base) {
                    if (b.contains("jpg") && accidentImg.contains(b)) {
                        cs = cs == "" ? b : cs + "_" + b;
                    } else {
                        Map<String, Object> rmap3 = carService.saveImg(b, CommonField.COMPENSATE_IMG_URL);
                        if (rmap3 != null && rmap3.get("flag") != null) {
                            cs = cs == "" ? String.valueOf(rmap3.get("associatorimg")) : cs + "_" + rmap3.get("associatorimg");
                        }
                    }

                }
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = time.format(new Date());
                map.put("statusEvent", 1);
                if (eventNo1 != null && !"".equals(eventNo1)) {
                    eventNo = eventNo1;
                    map.put("eventNo", eventNo);
                    eventService.updateEvent(map);

                    map = new HashMap<String, Object>();
                    map.put("eventNo", eventNo);
                    map.put("accidentDescription", description);
                    map.put("accidentImg", cs);
                    map.put("timeApply", date);
                    map.put("reasonFailure", "");
                    eventApplyMapper.updateModel(map);
                } else {
                    map.put("eventNo", eventNo);
                    map.put("createAt", date);
                    eventService.saveSingle(map);

                    map = new HashMap<String, Object>();
                    map.put("eventNo", eventNo);
                    map.put("accidentDescription", description);
                    map.put("accidentImg", cs);
                    map.put("timeApply", date);
                    map.put("createAt", date);
                    eventService.saveApply(map);
                }


                carService.sendSMS(carId, 2);
                return Constant.toReModel(CommonField.SUCCESS, "SUCCESS", "SUCCESS");
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
        }
    }

    /**
     * 发起互助理赔
     *
     * @author xiaowuge
     * @date 2018年9月17日
     * @version 1.0
     */
    @RequestMapping(value = "/compensation", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String compensation1(HttpServletRequest request) throws Exception {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                //String base1 = request.getParameter("base1"); //车头照片
                //String base2 = request.getParameter("base2");//车尾
                String carId = request.getParameter("carId"); //车辆ID
                String eventNo1 = request.getParameter("eventNo");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("carId", carId);
                List<Map<String, Object>> count = orderService.weChatFindUnfinishedOrder(map);
                if (eventNo1 == null || "".equals(eventNo1)) {
                    if (count != null && count.size() > 0) {
                        return Constant.toReModel(CommonField.REPETITION, "该车辆在救助或保险理赔中", count.get(0).get("eventNo"));
                    }
                }
                map.put("customerId", tokenMap.get("id"));
                Map<String, Object> card = carService.findCarById(Integer.valueOf(carId));
                System.out.println("++++++++" + card + "++++++++++");
                if (Double.valueOf(card.get("amtCompensation").toString()) <= 0) {
                    return Constant.toReModel("2000", "本年度互助额度已用完，建议发起保险理赔", "");
                }

                if (!String.valueOf(card.get("timeSignout")).equals("1111-11-11 11:11:11.0") && card.get("timeSignout") != null) {
                    return Constant.toReModel(CommonField.REPETITION, "该车已退出保障，无法申请救助", "");
                }
                String[] base = request.getParameterValues("base");  //车损照片base64编码
                String description = request.getParameter("description"); //描述

                String eventNo = Constant.createEventNo();
                String cs = "";
/*				Map<String,Object> rmap1 = UploadFileUtil.WeChatBase64ToImg(org.apache.commons.lang3.ArrayUtils.toArray(base1),CommonField.COMPENSATE_IMG_URL);
				if(rmap1!=null&&rmap1.get("associatorimg")!=null){
					json.put("ct",rmap1.get("associatorimg"));
				}
				Map<String,Object> rmap2 = UploadFileUtil.WeChatBase64ToImg(org.apache.commons.lang3.ArrayUtils.toArray(base2),CommonField.COMPENSATE_IMG_URL);
				if(rmap2!=null&&rmap2.get("associatorimg")!=null){
					json.put("cw",rmap2.get("associatorimg"));
				}*/
                Map<String, Object> pMap = new HashMap<String, Object>();
                pMap.put("eventNo", eventNo1);
                Map<String, Object> detail = eventService.findOrderDetail(pMap);
                String accidentImg = String.valueOf(detail.get("accidentImg"));
                for (String b : base) {
                    if (b.contains("jpg") && accidentImg.contains(b)) {
                        cs = cs == "" ? b : cs + "_" + b;
                    } else {
                        Map<String, Object> rmap3 = carService.saveImg(b, CommonField.COMPENSATE_IMG_URL);
                        if (rmap3 != null && rmap3.get("flag") != null) {
                            cs = cs == "" ? String.valueOf(rmap3.get("associatorimg")) : cs + "_" + rmap3.get("associatorimg");
                        }
                    }

                }
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = time.format(new Date());
                map.put("statusEvent", 4);
                if (eventNo1 != null && !"".equals(eventNo1)) {
                    eventNo = eventNo1;
                    map.put("eventNo", eventNo);
                    eventService.updateEvent(map);

                    map = new HashMap<String, Object>();
                    map.put("eventNo", eventNo);
                    map.put("accidentDescription", description);
                    map.put("accidentImg", cs);
                    //map.put("timeApply", date);
                    map.put("reasonFailure", "");
                    map.put("createAt", date);
                    eventApplyMapper.updateModel(map);
                } else {
                    map.put("eventNo", eventNo);
                    map.put("createAt", date);
                    eventService.saveSingle(map);

                    map = new HashMap<String, Object>();
                    map.put("eventNo", eventNo);
                    map.put("accidentDescription", description);
                    map.put("accidentImg", cs);
                    //map.put("timeApply", date);
                    map.put("createAt", date);
                    eventService.saveApply(map);

                    //获取用户信息
                    Map<String, Object> reqMap = new HashMap<>();
                    reqMap.put("id", card.get("customerId"));
                    Map<String, Object> customer = userCustomerMapper.findUser(reqMap);
                    //用户发起理赔
                    reqMap.clear();
                    reqMap.put("customerId", customer.get("id"));
                    reqMap.put("customerPN", customer.get("customerPN"));
                    reqMap.put("source", customer.get("source"));
                    reqMap.put("createAt", customer.get("timeJoin"));
                    reqMap.put("currentStatus", customer.get("status"));
                    reqMap.put("optTime", DateUtils.formatDate(new Date()));
                    reqMap.put("optType", 7);
                    reqMap.put("optDesc", "用户发起理赔");
                    reqMap.put("recordTime", DateUtils.formatDate(new Date()));
                    userCustomerLogService.saveUserCustomerLog(reqMap);
                }
                return Constant.toReModel(CommonField.SUCCESS, "SUCCESS", eventNo);
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
        }
    }

    @RequestMapping(value = "/carList", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String carList(HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                Map<String, Object> paramModelMap = new HashMap<String, Object>();
                paramModelMap.put("pageNo", request.getParameter("pageNo"));
                paramModelMap.put("pageSize", request.getParameter("pageSize"));
                String status = request.getParameter("status");
                paramModelMap.put("status", status == null ? null : "(" + status + ")");
                paramModelMap.put("flag", request.getParameter("flag"));
                paramModelMap.put("customerId", tokenMap.get("id"));
                PageInfo<Map<String, Object>> carList = carService.findCarList(paramModelMap);

                String messageId = request.getParameter("messageId");
                if (messageId != null && !"".equals(messageId)) {
                    Map<String, Object> pmap = new HashMap<String, Object>();
                    pmap.put("isRead", 3);
                    pmap.put("id", messageId);
                    messageService.updateModel(pmap);
                }
                return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", carList);
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    @RequestMapping(value = "/carDetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String carDetail(HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                Map<String, Object> paramModelMap = new HashMap<String, Object>();
                paramModelMap.put("id", request.getParameter("id"));
                paramModelMap.put("customerId", tokenMap.get("id"));
                Map<String, Object> detail = carService.findOne(paramModelMap);
                if (Constant.toEmpty(detail.get("drivingLicense"))) {
                    detail.put("drivingLicense", CommonField.getCarDrivingUrl((String) detail.get("drivingLicense")));
                }
                if (Constant.toEmpty(detail.get("carPhotos"))) {
                    detail.put("carPhotos", CommonField.getCarUrl((String) detail.get("carPhotos")));
                }
                return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", detail);
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    @RequestMapping(value = "/compensateList", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String compensateList(HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                Map<String, Object> paramModelMap = new HashMap<String, Object>();
                paramModelMap.put("pageNo", request.getParameter("pageNo"));
                paramModelMap.put("pageSize", request.getParameter("pageSize"));
                paramModelMap.put("statusEvent", request.getParameter("status"));
                paramModelMap.put("customerId", tokenMap.get("id"));
                PageInfo<Map<String, Object>> carList = eventService.findCompensateList(paramModelMap, 0);
                return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", carList);
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/goUserDetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String goUserDetail(HttpServletRequest request) {
        try {
            logger.info("获取用户信息开始>>>>>>");
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                logger.info("token验证通过>>>>");
                Map<String, Object> paramModelMap = new HashMap<>();
                paramModelMap.put("id", tokenMap.get("id"));
                Map<String, Object> detail = userCustomerService.findUserInfo(paramModelMap);
                detail.put("isRead", detail.get("mId") == null ? "无" : "有");
                detail.remove("mId");
                detail.put("portrait", String.valueOf(detail.get("portrait")).indexOf("thirdwx.qlogo.cn") == -1 ? UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL, String.valueOf(detail.get("portrait"))) : String.valueOf(detail.get("portrait")));
                Map<String, Object> rm = new HashMap<>();
                rm.put("customerId", tokenMap.get("id"));
                List<Map<String, Object>> carList = carMapper.findCarList(rm);
                detail.put("carList", carList);
                List<Map<String, Object>> iList = invitationCustomerMapper.selectInvitationList(Integer.valueOf(String.valueOf(tokenMap.get("id"))));
                detail.put("invitationCount", iList != null ? iList.size() : 0);
                return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", detail);
            } else {
                logger.info("token验证失败！");
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String updateUser(HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                Map<String, Object> paramModelMap = new HashMap<String, Object>();
                paramModelMap.put("id", tokenMap.get("id"));
                paramModelMap.put("sex", request.getParameter("sex")); //0女1男
                paramModelMap.put("nickname", request.getParameter("userName") == null ? null : Base64.getBase64(request.getParameter("userName")));

                String base = request.getParameter("base");  //头像编码
                if (base != null) {
                    Map<String, Object> rmap = UploadFileUtil.WeChatBase64ToImg(org.apache.commons.lang3.ArrayUtils.toArray(base), CommonField.POSTER_IMG_URL);
                    if (rmap != null && rmap.get("associatorimg") != null) {
                        paramModelMap.put("portrait", String.valueOf(rmap.get("associatorimg")));
                    } else {
                        return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
                    }
                }
                userCustomerService.updateModel(paramModelMap);
                return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", "SUCCESS");
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    /**
     * 订单详情
     *
     * @author xiaowuge
     * @date 2018年9月17日
     * @version 1.0
     */
    @RequestMapping(value = "/goOrderDetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String goOrderDetail(HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                Map<String, Object> pMap = new HashMap<String, Object>();
                pMap.put("eventNo", request.getParameter("eventNo"));
                Map<String, Object> detail = eventService.findOrderDetail(pMap);

                String messageId = request.getParameter("messageId");
                if (messageId != null && !"".equals(messageId)) {
                    Map<String, Object> pmap = new HashMap<String, Object>();
                    pmap.put("isRead", 3);
                    pmap.put("id", messageId);
                    messageService.updateModel(pmap);
                }
                return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", detail);
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    @RequestMapping(value = "/publicityDetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String publicityDetail(HttpServletRequest request) {
        try {
            Map<String, Object> pMap = new HashMap<String, Object>();
            pMap.put("eventNo", request.getParameter("eventNo"));
            pMap.put("publicity", "1");
            Map<String, Object> detail = eventService.findOrderDetail(pMap);
            String model = String.valueOf(detail.get("model"));
//			detail.put("model", model = model.substring(0,model.length()-3)+"***");
            detail.put("portrait", String.valueOf(detail.get("portrait")).indexOf("thirdwx.qlogo.cn") == -1 ? UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL, String.valueOf(detail.get("portrait"))) : String.valueOf(detail.get("portrait")));
//			String licensePlateNumber = String.valueOf(detail.get("licensePlateNumber"));
//			detail.put("licensePlateNumber", carService.hideStr(licensePlateNumber));
            detail.put("nameCarOwner", eventService.hideName(String.valueOf(detail.get("nameCarOwner"))));
            return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", detail);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    /**
     * 添加接车信息
     *
     * @author xiaowuge
     * @date 2018年9月17日
     * @version 1.0
     */
    @RequestMapping(value = "/insertReceivecar", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String insertReceivecar(HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("eventNo", request.getParameter("eventNo")); //订单号
                map.put("nameCarOwner", request.getParameter("nameCarOwner"));  //联系人
                map.put("telCarOwner", request.getParameter("telCarOwner")); //电话
                map.put("place", request.getParameter("place"));  //地址
                Map<String, Object> detail = eventService.findOrderDetail(map);
                map.put("licensePlateNumber", String.valueOf(detail.get("licensePlateNumber")));
                map.put("longitude", request.getParameter("longitude")); //经度
                map.put("latitude", request.getParameter("latitude"));  //维度
                map.put("reciveCarTime", request.getParameter("reciveCarTime"));  //用户填写接车时间
                map.put("status", 1);  //地址
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = time.format(new Date());
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, 10);
                String date1 = time.format(calendar.getTime());
                map.put("createAt", date);  //维度
                map.put("timeReceiveCar", date1);
                eventService.saveReceivecar(map);
                map = new HashMap<String, Object>();
                map.put("eventNo", request.getParameter("eventNo"));
                //String flag = distributionOrder.autoDistribution(map);
                //if(flag.equals("4000")){
                map.put("statusEvent", 10);
                eventService.updateEvent(map);
                //}
                // 保存后台消息
                Map<String, Object> backMsgParam = new HashMap<>();
                backMsgParam.put("type", 1);
                backMsgParam.put("orderNo", request.getParameter("eventNo"));
                backMsgParam.put("title", "待分单");
                backMsgParam.put("content", String.valueOf(detail.get("licensePlateNumber")) + "的救助订单等待您分单！");
                backMsgParam.put("isSolve", 1);
                backMsgParam.put("orderStatus", 10);
                backMsgParam.put("createTime", DateUtils.formatDate(new Date()));
                messageBackstageService.save(backMsgParam);
                // 发送推送消息
                Map<String, Object> pushMsgMap = new HashMap<>();
                pushMsgMap.put("orderNo", request.getParameter("eventNo"));
                pushMsgMap.put("type", 1);
                pushMsgMap.put("orderStatus", 10);
                userAdminService.pushMessageToManager("event", "待分单", String.valueOf(detail.get("licensePlateNumber")) + "的救助订单等待您分单！", pushMsgMap);
                return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", "success");
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    /**
     * 车主取车
     *
     * @author xiaowuge
     * @date 2018年9月17日
     * @version 1.0
     */
    @RequestMapping(value = "/updateReceivecar", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String updateReceivecar(HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                //分摊金额计算（车主接车定分摊金额）
                Map<String, Object> paramModelMap = new HashMap<>();
                paramModelMap.put("eventNo", request.getParameter("eventNo"));
                eventService.updateAmtShare(paramModelMap);

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("eventNo", request.getParameter("eventNo")); //订单号

                Map<String, Object> detail = eventService.findOrderDetail(map);
                if (String.valueOf(detail.get("statusEvent")).equals("71") || String.valueOf(detail.get("statusEvent")).equals("100")) {
                    return Constant.toReModel("1212", "状态错误，请勿重复提交", CommonField.PARAMETER_ERROR_PROMPT);
                }
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = time.format(new Date());
                map.put("timeReceiveEnd", date);
                map.put("createAt", date);
                map.put("timeReceiveCar", date);
                map.put("status", 3);
                map.put("nameCarOwner", detail.get("fromName"));
                map.put("telCarOwner", detail.get("customerPN"));
                eventReceivecarMapper.saveSingle(map);

                //获取用户信息
                Map<String, Object> reqMap = new HashMap<>();
                reqMap.put("id", detail.get("customerId"));
                Map<String, Object> customer = userCustomerMapper.findUser(reqMap);
                //用户取车
                reqMap.clear();
                reqMap.put("customerId", customer.get("id"));
                reqMap.put("customerPN", customer.get("customerPN"));
                reqMap.put("source", customer.get("source"));
                reqMap.put("createAt", customer.get("timeJoin"));
                reqMap.put("currentStatus", customer.get("status"));
                reqMap.put("optTime", DateUtils.formatDate(new Date()));
                reqMap.put("optType", 9);
                reqMap.put("optDesc", "用户取车");
                reqMap.put("recordTime", DateUtils.formatDate(new Date()));
                userCustomerLogService.saveUserCustomerLog(reqMap);

                Map<String, Object> pMap = new HashMap<String, Object>();
                pMap.put("status", 20);

                map = new HashMap<String, Object>();
                map.put("statusEvent", 71);
                map.put("timeComplete", date);
                map.put("eventNo", request.getParameter("eventNo"));
                eventService.updateEvent(map);
                detail.put("eventNo", request.getParameter("eventNo"));
                eventService.reveiveCar(detail);
                map = new HashMap<>();
                map.put("carId", detail.get("carId"));
                carService.updateCompensateNum(map);
                return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", "success");
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    /**
     * 投诉 / 取消投诉并接车
     *
     * @author xiaowuge
     * @date 2018年11月13日
     * @version 1.0
     */
    @RequestMapping(value = "/updateComplaint", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String updateComplaint(HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                String content = request.getParameter("content");
                map.put("eventNo", request.getParameter("eventNo")); //订单号

                Map<String, Object> detail = eventService.findOrderDetail(map);

                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = time.format(new Date());
                if (content != null) {
                    map.put("timeComplaint", date);
                    map.put("createAt", date);
                    map.put("timeReceiveEnd", date);
                    map.put("content", content);
                    eventComplaintMapper.saveSingle(map);

                    map = new HashMap<String, Object>();
                    map.put("statusEvent", 81);
                    map.put("eventNo", request.getParameter("eventNo"));
                    eventService.updateEvent(map);

                    eventService.saveMessageScore(request.getParameter("eventNo"), -5.0, "");
                    // 保存后台消息
                    Map<String, Object> backMsgParam = new HashMap<>();
                    backMsgParam.put("type", 1);
                    backMsgParam.put("orderNo", request.getParameter("eventNo"));
                    backMsgParam.put("title", "投诉中");
                    backMsgParam.put("content", detail.get("licensePlateNumber") + "的救助订单在投诉中，等待您处理！");
                    backMsgParam.put("isSolve", 1);
                    backMsgParam.put("orderStatus", 81);
                    backMsgParam.put("createTime", DateUtils.formatDate(new Date()));
                    messageBackstageMapper.saveSingle(backMsgParam);
                    // 发送推送消息
                    Map<String, Object> pushMsgMap = new HashMap<>();
                    pushMsgMap.put("orderNo", backMsgParam.get("orderNo"));
                    pushMsgMap.put("type", backMsgParam.get("type"));
                    pushMsgMap.put("orderStatus", backMsgParam.get("orderStatus"));
                    userAdminService.pushMessageToManager("event", (String) backMsgParam.get("title"), (String) backMsgParam.get("content"), pushMsgMap);
                } else {   //撤销投诉
                    if (String.valueOf(detail.get("statusEvent")).equals("71") || String.valueOf(detail.get("statusEvent")).equals("100")) {
                        return Constant.toReModel(CommonField.TOKEN_FAILURE, "状态错误，请勿重复提交", CommonField.PARAMETER_ERROR_PROMPT);
                    }
                    map.put("timeUnComplaint", date);

                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("eventNo", request.getParameter("eventNo")); //订单号
                    m.put("status", 3);
                    m.put("createAt", date);
                    m.put("timeReceiveCar", date);
                    m.put("timeReceiveEnd", date);
                    m.put("nameCarOwner", detail.get("fromName"));
                    m.put("telCarOwner", detail.get("customerPN"));
                    eventReceivecarMapper.saveSingle(m);
                    eventComplaintMapper.updateModel(map);

                    Map<String, Object> pMap = new HashMap<String, Object>();
                    pMap.put("status", 20);

                    map = new HashMap<String, Object>();
                    map.put("statusEvent", 71);
                    map.put("timeComplete", date);
                    map.put("eventNo", request.getParameter("eventNo"));
                    eventService.updateEvent(map);

                    eventService.reveiveCar(detail);
                }
                return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", "success");
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    @RequestMapping(value = "/insertComment", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String insertComment(HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("eventNo", request.getParameter("eventNo")); //订单号

                Map<String, Object> detail = eventService.findOrderDetail(map);
                map.put("maintenanceshopId", detail.get("maintenanceshopId")); //
                map.put("labelContent", request.getParameter("labelContent")); //
                map.put("content", request.getParameter("content")); //
                map.put("score", request.getParameter("score")); //
                map.put("customerId", tokenMap.get("id")); //
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = time.format(new Date());
                map.put("createAt", date);
                eventCommentMapper.saveSingle(map);

                map = new HashMap<String, Object>();
                map.put("statusEvent", 100);
                map.put("eventNo", request.getParameter("eventNo"));
                eventService.updateEvent(map);

                Double s = 0.0;
                if (request.getParameter("score").equals("5")) {
                    s = 0.5;
                } else if (request.getParameter("score").equals("4")) {
                    s = -1.0;
                } else {
                    s = -2.0;
                }
                eventService.saveMessageScore(request.getParameter("eventNo"), s, request.getParameter("score"));
                return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", "success");
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    /**
     * 互助理赔支付
     *
     * @author xiaowuge
     * @date 2018年11月30日
     * @version 1.0
     */
    @RequestMapping(value = "/paymentRepair1", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String paymentRepair1(HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                Map<String, Object> peMap = new HashMap<String, Object>();
                String eventNo = request.getParameter("eventNo");
                String customerId = tokenMap.get("id");
                peMap.put("eventNo", eventNo);
                Map<String, Object> eventMap = eventService.findOrderDetail(peMap);
                BigDecimal amt = new BigDecimal(String.valueOf(eventMap.get("amtPay")).equals("null") ? "0" : String.valueOf(eventMap.get("amtPay")));
                Map<String, Object> pMap = new HashMap<String, Object>();
                pMap.put("customerId", customerId);
                pMap.put("eventNo", eventNo);
                pMap.put("carId", eventMap.get("carId"));
                pMap.put("amt", amt);
                pMap.put("type", 1);
                pMap.put("status", 2);
                pMap.put("eventType", 3);
                recordRechargeMapper.saveSingle(pMap);

                String orderNo = String.valueOf(pMap.get("id"));
                System.out.println("救助支付进来了-----------------------------" + orderNo);
                int isZ = carService.paymentRepairResult(orderNo);
                if (isZ == 1) {
                    return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", "success");
                } else {
                    return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, "");
                }

            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    @RequestMapping(value = "/publicity", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String publicity(HttpServletRequest request) {
        try {
            Map<String, Object> map = eventService.findPublicity();
            return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", map);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    @RequestMapping(value = "/publicityList", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String publicityList(HttpServletRequest request) {
        try {
            Map<String, Object> paramModelMap = new HashMap<String, Object>();
            paramModelMap.put("pageNo", request.getParameter("pageNo"));
            paramModelMap.put("pageSize", request.getParameter("pageSize"));
            String flag = request.getParameter("flag");
            if (flag == null) {
                return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, "参数不能为空");
            }
            paramModelMap.put("publicity", flag.equals("1") ? "1" : null);
            paramModelMap.put("finish", flag.equals("2") ? "1" : null);
            paramModelMap.put("isInvalid", 1);
            PageInfo<Map<String, Object>> CompensateList = eventService.findCompensateList(paramModelMap, 1);
            return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", CompensateList);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    @RequestMapping(value = "/myAccount", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String myAccount(HttpServletRequest request) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                Map<String, Object> rmap = new HashMap<String, Object>();
                rmap.put("customerId", Integer.valueOf(tokenMap.get("id")));
                List<Map<String, Object>> list = carService.findPayCar(rmap);
                if (list != null && list.size() > 0) {
                    map.put("carList", list);
                } else {
                    map.put("carList", null);
                }
                Map<String, Object> m = carService.findTotalCooperationByCustomerId(rmap);
                if (m != null) {
                    map.putAll(m);
                }
                return Constant.toReModel(CommonField.SUCCESS, "是", map);
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
    }

    @RequestMapping(value = "/getRechargeList", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getRechargeList(HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                Map<String, Object> rmap = new HashMap<String, Object>();
                rmap.put("customerId", Integer.valueOf(tokenMap.get("id")));
                List<Map<String, Object>> list = userCustomerService.selectRechargeList(rmap);
                return Constant.toReModel(CommonField.SUCCESS, "是", list);
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
    }

    @RequestMapping(value = "/getMessageList", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getMessageList(HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                Map<String, Object> rmap = new HashMap<String, Object>();
                rmap.put("customerId", Integer.valueOf(tokenMap.get("id")));
                rmap.put("type", request.getParameter("type"));
                rmap.put("isRead", 3);
                messageService.updateRead(rmap);
                PageInfo<Map<String, Object>> list = messageService.findMessageList(rmap);
                return Constant.toReModel(CommonField.SUCCESS, "是", list);
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
    }

    @RequestMapping(value = "/getMaintenanceShopList", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getMaintenanceShopList(HttpServletRequest request) {
        try {
            Map<String, Object> rmap = new HashMap<String, Object>();
            rmap.put("latitude", request.getParameter("latitude")); //纬度
            rmap.put("longitude", request.getParameter("longitude")); //经度
            rmap.put("sortFlag", request.getParameter("sortFlag")); //距离传1，评分2，销量3
            rmap.put("distanceSort", "1".equals(request.getParameter("sortFlag")) ? 1 : null);
            rmap.put("scoreSort", "2".equals(request.getParameter("sortFlag")) ? 1 : null);
            rmap.put("repairNumSort", "3".equals(request.getParameter("sortFlag")) ? 1 : null);
            rmap.put("pageNo", request.getParameter("pageNo"));
            rmap.put("pageSize", request.getParameter("pageSize"));
            PageInfo<Map<String, Object>> list = maintenanceshopService.findShopSortList(rmap);
            return Constant.toReModel(CommonField.SUCCESS, "是", list);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
    }

    @RequestMapping(value = "/getMaintenanceShopDetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getMaintenanceShopDetail(HttpServletRequest request) {
        try {
            Map<String, Object> rmap = new HashMap<String, Object>();
            rmap.put("id", request.getParameter("id"));
            Map<String, Object> map = maintenanceshopService.findSingle(rmap);
            String imgA = String.valueOf(map.get("img"));
            if (!map.equals("null")) {
                String u = "";
                String[] arr = imgA.split("_");
                for (String a : arr) {
                    String url = UploadFileUtil.getImgURL("maintenanceshop/maintenanceshopImg/", a);
                    u += u.equals("") ? url : "_" + url;
                }
                map.put("img", u);
            }
            imgA = String.valueOf(map.get("businessLicenseImg"));
            if (!map.equals("null")) {
                String u = "";
                String[] arr = imgA.split("_");
                for (String a : arr) {
                    String url = UploadFileUtil.getImgURL("maintenanceshop/businessLicense/", a);
                    u += u.equals("") ? url : "_" + url;
                }
                map.put("businessLicenseImg", u);
            }
            return Constant.toReModel(CommonField.SUCCESS, "是", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
    }

    @RequestMapping(value = "/getCommentListByMaintenanceShopId", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getCommentListByMaintenanceShopId(HttpServletRequest request) {
        try {
            Map<String, Object> rmap = new HashMap<String, Object>();
            rmap.put("maintenanceshopId", request.getParameter("id"));
            PageInfo<Map<String, Object>> list = eventCommentService.findCommentList(rmap);
            return Constant.toReModel(CommonField.SUCCESS, "是", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, CommonField.PARAMETER_ERROR_PROMPT);
    }

    @RequestMapping(value = "/findJionNew")
    public void serverSend(HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            List<Map<String, Object>> list = eventService.findJionNew();
            String model = "";
            String o = "";
            if (list != null && list.size() > 0) {
                for (Map<String, Object> m : list) {
                    String portrait = String.valueOf(m.get("portrait"));
                    if (portrait.equals("null")) {
                        portrait = "";
                    } else {
                        portrait = portrait.indexOf("thirdwx.qlogo.cn") == -1 ? UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL, portrait) : portrait;
                    }
                    model = String.valueOf(m.get("model"));
                    try {
                        model = model.substring(0, model.length() - 3) + "***";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    model = "刚刚" + model + "加入车V互助";
                    model = portrait == "" ? model : model + "_" + portrait;
                    o += o == "" ? model : "|" + model;
                }
            }
            writer.write("data: " + o + " \n\n");//这里需要\n\n，必须要，不然前台接收不到值,键必须为data
            writer.flush();

            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/findMyMessage")
    public void findMessageNew(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, String> tokenMap = userCustomerService.validationToken(request.getParameter("token"));
            if (tokenMap != null) {
                map.put("customerId", Integer.valueOf(tokenMap.get("id")));
                List<Map<String, Object>> list = eventService.findMessageNew(map);
                String o = "";
                if (list != null && list.size() > 0) {
                    for (Map<String, Object> m : list) {
                        String content = String.valueOf(m.get("content"));
                        String type = String.valueOf(m.get("type"));
                        String id = String.valueOf(m.get("id"));
                        String messageId = String.valueOf(m.get("messageId"));
                        o += o == "" ? content + "_" + type + "_" + id + "_" + messageId : "|" + content + "_" + type + "_" + id + "_" + messageId;
                    }
                }
                writer.write("data: " + o + " \n\n");//这里需要\n\n，必须要，不然前台接收不到值,键必须为data
                writer.flush();
                Thread.sleep(10000);
            } else {
                writer.write("data: " + "null" + " \n\n");//这里需要\n\n，必须要，不然前台接收不到值,键必须为data
                writer.flush();
                Thread.sleep(10000);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/test1", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public void test1(HttpServletRequest request) throws Exception {
        try {
            List<Map<String, Object>> userList = userCustomerMapper.findUserList();
            if (userList != null && userList.size() > 0) {
                int count = userCustomerMapper.findEventCount();
                Foundation foundation = foundationMapper.findEntitySingle(new HashMap<String, Object>());
                String showTotal = String.valueOf(foundation.getShowTotal());
                for (Map<String, Object> m : userList) {
                    String nickname = String.valueOf(m.get("nickname")).equals("null") ? String.valueOf(m.get("nickname")) : Base64.getFromBase64(String.valueOf(m.get("nickname")));
                    String openId = String.valueOf(m.get("openId"));
                    String[] carIds = String.valueOf(m.get("carId")).split(",");
                    String[] timeBegins = String.valueOf(m.get("timeBegin")).split(",");
                    String[] timeSignouts = String.valueOf(m.get("timeSignout")).split(",");

                    String names = "";
                    String ids = "";
                    BigDecimal totalAmounts = new BigDecimal("0");
                    BigDecimal moneys = new BigDecimal("0");
                    for (int i = 0; i < carIds.length; i++) {
                        if (!carIds[i].equals("-1")) {
                            Map<String, Object> rmap = new HashMap<String, Object>();
                            rmap.put("timeBegin", timeBegins[i]);
                            rmap.put("timeSignout", timeSignouts[i].equals("-1") || timeSignouts[i].equals("1111-11-11 11:11:11.0") || timeSignouts[i].equals("1111-11-11 11:11:11") ? "" : timeSignouts[i]);
                            Map<String, Object> map = userCustomerMapper.findAmtBycar(rmap);
                            if (map != null && map.get("number") != null && !"0".equals(String.valueOf(map.get("number")))) {
                                int number = Integer.valueOf(String.valueOf(map.get("number")));
                                String name = String.valueOf(map.get("names"));
                                String id = String.valueOf(map.get("ids"));
                                //BigDecimal totalAmount = new BigDecimal("0");
                                BigDecimal money = new BigDecimal("0");
                                try {
                                    //totalAmount = new BigDecimal(String.valueOf(map.get("totalAmount")).equals("null")?"0":String.valueOf(map.get("totalAmount")));
                                    money = new BigDecimal(String.valueOf(map.get("money")).equals("null") ? "0" : String.valueOf(map.get("money")));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (!name.equals("")) {
                                    String[] nn = name.split(",");
                                    String aa = "";
                                    for (String s : nn) {
                                        s = Base64.getFromBase64(s);
                                        aa += aa.equals("") ? s : "," + s;
                                    }
                                    names += names.equals("") ? aa : "," + aa;
                                }
                                if (!id.equals("")) {
                                    ids += ids.equals("") ? id : "," + id;
                                }
                                //totalAmounts = totalAmounts.add(totalAmount);
                                moneys = moneys.add(money);
                            }
                        }
                    }
                    String[] na = names.split(",");
                    List<String> l = new ArrayList<String>();
                    if (na.length > 1) {
                        for (String s : na) {
                            if (!l.contains(s)) {
                                l.add(s);
                            }
                        }
                    }
                    names = "";
                    for (String s : l) {
                        names += names.equals("") ? s : "," + s;
                    }
                    String[] ia = ids.split(",");
                    List<String> il = new ArrayList<String>();
                    if (ia.length > 1) {
                        for (String s : ia) {
                            if (!il.contains(s)) {
                                il.add(s);
                            }
                        }
                    }
                    Map<String, String> mm = new HashMap<String, String>();
                    mm.put("number", String.valueOf(count));
                    mm.put("number1", String.valueOf(l.size()));
                    mm.put("names", names);
                    mm.put("totalAmount", showTotal);
                    mm.put("money", String.valueOf(moneys));
                    mm.put("nickname", nickname);
                    mm.put("openid", openId);
                    //weiXinUtils.sendTemplate(5, mm);

                    mm.clear();
                    mm.put("number", String.valueOf(il.size()));
                    mm.put("openid", openId);
                    //weiXinUtils.sendTemplate(12, mm);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/invitationList", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String invitationList(HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = userCustomerService.validationToken(request);
            if (tokenMap != null) {
                int id = Integer.valueOf(String.valueOf(tokenMap.get("id")));
                List<Map<String, Object>> iList = invitationCustomerMapper.selectInvitationList(id);
                Map<String, Object> map = invitationCustomerMapper.selectFirstCar(id);
                BigDecimal edu = new BigDecimal("1000");
                for (Map<String, Object> m : iList) {
                    String status = String.valueOf(m.get("status"));
                    if ("1".equals(status)) {
                        edu = edu.add(Constant.oneQuota);
                    }
                    String portrait = String.valueOf(m.get("portrait"));
                    m.put("head", portrait.indexOf("thirdwx.qlogo.cn") == -1 ? UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL, portrait) : portrait);
                    m.put("nickname", m.get("nickname") == null ? null : Base64.getFromBase64(String.valueOf(m.get("nickname"))));
                }
                if (map == null) {
                    map = new HashMap<String, Object>();
                }
                map.put("MaxEdu", edu);
                map.put("list", iList);

                return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", map);
            } else {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "否", CommonField.PARAMETER_ERROR_PROMPT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    /**
     * 根据用户的id，获取头像和昵称
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getUserInfoById", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getUserInfo(HttpServletRequest request, Integer customerId) {
        try {
            Map<String, Object> userInfo = userCustomerService.getUserInfoById(customerId);
            return Constant.toReModel(CommonField.SUCCESS, "SUCCESSFUL", userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
        }
    }

    /**
     * 邀请用户，传入邀请人customerId、被邀请人电话newCustomerPN，活动modelId
     *
     * @author xiaowuge
     * @date 2018年9月18日
     * @version 1.0
     */
    @RequestMapping(value = "/inviteUser", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String inviteUser(HttpServletRequest request, Integer customerId, String newCustomerPN, Integer modelId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customerId", customerId);
        map.put("customerPN", newCustomerPN);
        map.put("modelId", modelId);
        String openid = (String) request.getSession().getAttribute("fromUserName");
        logger.info("邀请用户的openid={}", openid);
        if (Constant.toEmpty(customerId) || Constant.toEmpty(newCustomerPN) || Constant.toEmpty(modelId)) {
            try {
                List<Map<String, Object>> list = userCustomerMapper.findMore(map);
                if (list.size() > 0) {
                    return Constant.toReModel(CommonField.FAIL, "对不起,您已注册登录车V互助", null);
                }

                String i = invitationService.saveInvition(map);
                if (i.equals("0")) {

//                    //获取用户信息
//                    Map<String, Object> reqMap = new HashMap<>();
//                    reqMap.put("id", customerId);
//                    Map<String, Object> customer = userCustomerMapper.findUser(reqMap);
//                    //用户邀请
//                    reqMap.clear();
//                    reqMap.put("customerId", customer.get("id"));
//                    reqMap.put("customerPN", customer.get("customerPN"));
//                    reqMap.put("source", customer.get("source"));
//                    reqMap.put("createAt", customer.get("timeJoin"));
//                    reqMap.put("currentStatus", customer.get("status"));
//                    reqMap.put("optTime", DateUtils.formatDate(new Date()));
//                    reqMap.put("optType", 11);
//                    reqMap.put("optDesc", "用户邀请");
//                    reqMap.put("recordTime", DateUtils.formatDate(new Date()));
//                    userCustomerLogService.saveUserCustomerLog(reqMap);

                    return Constant.toReModel(CommonField.SUCCESS, "邀请成功", null);
                } else {
                    return Constant.toReModel(CommonField.FAIL, "邀请失败", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Constant.toReModel(CommonField.FAIL, CommonField.SERVER_FAILURE, e);
            }
        } else {
            return Constant.toReModel(CommonField.FAIL, "邀请信息不完整", null);
        }
    }

    @RequestMapping(value = "/editQuartz", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String editQuartz(HttpServletRequest request) {
        try {
            carService.editObservation();
            return Constant.toReModel(CommonField.SUCCESS, "修改成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL, "修改失败", null);
        }
    }

    @RequestMapping(value = "/hello")
    public String hello() {
        return "verify_0ad19e61d9b09ab379482b1ebf2a267d.html";
    }

    /**
     * 根据id获取店铺详情
     */
    @RequestMapping(value = "/getShopDetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getShopDetail(HttpServletRequest request) throws Exception {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("id", request.getParameter("shopId"));
        return Constant.toReModel(CommonField.SUCCESS, "成功", maintenanceshopService.getMaintenanceShopDetail(requestMap));
    }


}
