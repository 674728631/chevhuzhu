package com.zccbh.demand.service.business;


import com.zccbh.demand.controller.quartz.QuartzUtils;
import com.zccbh.demand.mapper.merchants.*;
import com.zccbh.demand.pojo.merchants.*;
import com.zccbh.demand.service.customer.MessageBackstageService;
import com.zccbh.demand.service.user.UserAdminService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.base.JSONPost;
import com.zccbh.util.base.SmsDemo;
import com.zccbh.util.collect.Constant;
import net.sf.json.JSONObject;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author:                     luoyuangang
 * @JDK version used:       	1.8
 * @Modified By:            	<修改人中文名或拼音缩写>
 * @Modified Date:          	<修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: 	<修改原因描述>
 * @create                      2018-06-12 16:32
 **/
@Service
public class BusinessInsuranceClaimService {

    @Autowired
    private CbhUserBusinessService userBusinessService;
    @Autowired
    private CbhOrderMapper cbhOrderMapper;
    @Autowired
    private CbhMessageMapper messageMapper;
    @Autowired
    private CbhUserBusinessMapper businessMapper;
    @Autowired
    private CbhCarMapper carMapper;
    @Autowired
    private CbhMaintenanceshopMapper maintenanceshopMapper;
    @Autowired
    private WeiXinUtils weiXinUtils;
    @Autowired
    private SchedulerFactoryBean schedulerFactory;
    @Autowired
    private MessageBackstageService messageBackstageService;
    @Autowired
    private UserAdminService userAdminService;
    /**
     * @param request       点击订单明细
     */
    public Map<String,Object> orderDetail(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(CommonField.STATUS,false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
        String token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
        String orderNo = Constant.toJsonValue(jsonObject, CommonField.ORDER_NO);
        CbhUserBusiness business = userBusinessService.validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.TOKEY_LOSE_EFFICACY);
            return resultMap;
        }
        if (!Constant.toEmpty(orderNo)){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.ORDER_NUMBER_CANNOT_EMPTY);
            return resultMap;
        }
        Map<String,Object> orderDetail = cbhOrderMapper.selectOrderDetailByOrderNo(token,orderNo);
        if (orderDetail==null||orderDetail.isEmpty()){
            resultMap.put(CommonField.RESULTMESSAGE,"该订单不存在或您登录已失效");
            return resultMap;
        }
        Map<String, Object> newMap = getNewMap(orderDetail);
        resultMap.put(CommonField.STATUS,true);
        resultMap.put("orderDetail",newMap);
        return resultMap;
    }

    public Map<String,Object> getNewMap(Map<String,Object> resultMap)throws Exception{
        for ( Map.Entry<String,Object> entry:resultMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Date){
                resultMap.put(key,DateUtils.getStringDateTime((Date) value));
            }

        }
        List<String> list = Arrays.asList("reasonSuccess", "deliverLatitude", "deliverLongitude", "deliverPlace", "timeReceiveCar", "pricingName", "carOwnerTel", "brand", "nameCarOwner", "licensePlateNumber",
                "assertDescription", "damagePosition", "damageExtent", "amtAssert", "deliverCarTime", "receiveOrderTime", "distributionTime", "accidentImg", "carPhotos", "accidentDescription",
                "complaintContent", "endRepairTime", "repairImg", "explanationRepair", "beginRepairTime", "maintenanceName", "comfirmAssertTime", "explanationAssert", "assertTime", "assertImg",
                "completeTime", "labelContent", "commentContent", "commentScore", "takeCarTime", "complaintTime","examineExplanation");

        list.forEach(a->{
            if (!resultMap.containsKey(a)){
                if (a.equals("commentScore")){
                    resultMap.put(a,-1);
                }else if (a.equals("amtAssert")){
                    resultMap.put(a,0.00);
                }else {
                    resultMap.put(a,"");
                }
            }
        });
        //        下面注释的是:把行驶证放在定损照片集合的首位
//        String drivingLicense = CommonField.getCarDrivingUrl((String) resultMap.get("drivingLicense"));
//        resultMap.remove("drivingLicense");
//        List<String> accidentImgList = CommonField.getOrderImgList(0,resultMap.get("accidentImg").toString());
//        ArrayList<String> objects = new ArrayList<>();
//        objects.add(drivingLicense);
//        orderDetail.put("accidentImg",objects.addAll(accidentImgList));
        resultMap.put("carPhotos",CommonField.getCarUrlList(resultMap.get("carPhotos").toString()));
        resultMap.put("accidentImg",CommonField.getOrderImgList(0,resultMap.get("accidentImg").toString()));
        resultMap.put("assertImg",CommonField.getOrderImgList(1,resultMap.get("assertImg").toString()));
        resultMap.put("repairImg",CommonField.getOrderImgList(2,resultMap.get("repairImg").toString()));
        resultMap.put("labelContent",resultMap.get("labelContent").toString().split("_"));
        return resultMap;
    }
    /**
     * @param request       立即接单
     */
    @Transactional
    public Map<String,Object> takingOrder(HttpServletRequest request) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(CommonField.STATUS,false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
        String token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
        String orderNo = Constant.toJsonValue(jsonObject, CommonField.ORDER_NO);
        String assertmanId = Constant.toJsonValue(jsonObject, "assertmanId");

        CbhUserBusiness business = userBusinessService.validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.TOKEY_LOSE_EFFICACY);
            return resultMap;
        }
        if (!Constant.toEmpty(orderNo)){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.ORDER_NUMBER_CANNOT_EMPTY);
            return resultMap;
        }
        CbhOrder order = cbhOrderMapper.selectByEventNo(orderNo);
        if (order==null){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.SORRY_THERE_IS_NO_ORDER);
            return resultMap;
        }
        if (order.getStatus()!=11){
            resultMap.put(CommonField.RESULTMESSAGE,"对不起,该订单不在待接单状态!");
            return resultMap;
        }
        if (!Constant.toEmpty(assertmanId)){
            resultMap.put(CommonField.RESULTMESSAGE,"对不起,定损员工不能为空! ");
            return resultMap;
        }
        if ("0".equals(assertmanId)){
            resultMap.put(CommonField.RESULTMESSAGE,"对不起,请先添加员工!");
            return resultMap;
        }
        order.setStatus(21);
        order.setAssertmanId(Integer.valueOf(assertmanId));
        order.setReceiveOrderTime(DateUtils.getDateTime());
        cbhOrderMapper.updateByPrimaryKey(order);
        //删除定时器
        Scheduler sche = schedulerFactory.getScheduler();
        QuartzUtils.removeJob(sche, orderNo);
        //删除接单消息
        messageMapper.deleteByOrderNo(orderNo);

        resultMap.put(CommonField.STATUS,true);
        return resultMap;
    }

    /**
     * @param request       放弃接单
     */
    @Transactional
    public Map<String,Object> abandonOrder(HttpServletRequest request) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(CommonField.STATUS,false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
        String token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
        String orderNo = Constant.toJsonValue(jsonObject, CommonField.ORDER_NO);
        String reason = Constant.toJsonValue(jsonObject, "reason");
        String instructions = Constant.toJsonValue(jsonObject, "instructions");

        CbhUserBusiness business = userBusinessService.validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.TOKEY_LOSE_EFFICACY);
            return resultMap;
        }
        if (!Constant.toEmpty(orderNo)){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.ORDER_NUMBER_CANNOT_EMPTY);
            return resultMap;
        }
        CbhOrder order = cbhOrderMapper.selectByEventNo(orderNo);
        if (order==null){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.SORRY_THERE_IS_NO_ORDER);
            return resultMap;
        }
        if (order.getStatus()!=11){
            resultMap.put(CommonField.RESULTMESSAGE,"对不起,该订单不在待接单状态!");
            return resultMap;
        }
        if (!Constant.toEmpty(reason)){
            resultMap.put(CommonField.RESULTMESSAGE,"原因不能为空! ");
            return resultMap;
        }
        order.setStatus(12);
        order.setReasonCancellations(reason);
        order.setExplanationCancellations(instructions);
        order.setFailReceiveOrderTime(DateUtils.getDateTime());
        cbhOrderMapper.updateByPrimaryKey(order);
        messageMapper.deleteByOrderNo(orderNo);
        List<String> phoneList = businessMapper.getadministratorPhone();
        CbhCar cbhCar = carMapper.selectByPrimaryKey(order.getCarId());
        phoneList.forEach(a->{
            try {
                SmsDemo.sendSms(81,a,cbhCar.getLicensePlateNumber());
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        Scheduler sche = schedulerFactory.getScheduler();
        //删除定时器
        QuartzUtils.removeJob(sche, orderNo);
        resultMap.put(CommonField.STATUS,true);
        // 保存后台消息
        Map<String, Object> backMsgParam = new HashMap<>();
        backMsgParam.put("type",2);
        backMsgParam.put("orderNo",orderNo);
        backMsgParam.put("title","放弃接单");
        if (cbhCar != null){
            backMsgParam.put("content",cbhCar.getLicensePlateNumber() + "的保险理赔订单被放弃接单，等待您重新分单！");
        }
        backMsgParam.put("isSolve",1);
        backMsgParam.put("orderStatus",12);
        backMsgParam.put("createTime", DateUtils.formatDate(new Date()));
        messageBackstageService.save(backMsgParam);
        // 发送推送消息
        Map<String, Object> pushMsgMap = new HashMap<>();
        pushMsgMap.put("orderNo", backMsgParam.get("orderNo"));
        pushMsgMap.put("type", backMsgParam.get("type"));
        pushMsgMap.put("orderStatus", backMsgParam.get("orderStatus"));
        userAdminService.pushMessageToManager("order",(String) backMsgParam.get("title"),(String) backMsgParam.get("content"),pushMsgMap);
        return resultMap;
    }

    /**
     * @param request       提交定损
     */
    @Transactional
    public Map<String,Object> submitAssert(HttpServletRequest request) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(CommonField.STATUS,false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
        String token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
        String orderNo = Constant.toJsonValue(jsonObject, CommonField.ORDER_NO);
        String damageExtent = Constant.toJsonValue(jsonObject, "damageExtent");//受损程度
        String damagePosition = Constant.toJsonValue(jsonObject, "damagePosition");//受损部位
        String assertImg = Constant.toJsonValue(jsonObject, "assertImg");//定损照片
        String description = Constant.toJsonValue(jsonObject, "description");//定损描述
        String amtAssert = Constant.toJsonValue(jsonObject, "amtAssert");//定损费用

        CbhUserBusiness business = userBusinessService.validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.TOKEY_LOSE_EFFICACY);
            return resultMap;
        }
        if (!Constant.toEmpty(orderNo)){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.ORDER_NUMBER_CANNOT_EMPTY);
            return resultMap;
        }
        CbhOrder order = cbhOrderMapper.selectByEventNo(orderNo);
        if (order==null){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.SORRY_THERE_IS_NO_ORDER);
            return resultMap;
        }
        if (!(order.getStatus()==31||order.getStatus() ==32)){
            resultMap.put(CommonField.RESULTMESSAGE,"对不起,该订单不在待定损或待确认状态!");
            return resultMap;
        }
        if (Constant.toEmpty(damageExtent)&&Constant.toEmpty(damagePosition)&&
                Constant.toEmpty(assertImg)&&Constant.toEmpty(description)&&Constant.toEmpty(amtAssert)) {
            order.setStatus(32);
            order.setAmtAssert(new BigDecimal(amtAssert));
            order.setDamageExtent(damageExtent);
            order.setDamagePosition(damagePosition);
            order.setAssertImg(assertImg);
            order.setAssertDescription(description);
            order.setAssertTime(DateUtils.getDateTime());
            cbhOrderMapper.updateByPrimaryKey(order);
            CbhCar cbhCar = carMapper.selectByPrimaryKey(order.getCarId());
            List<String> phoneList = businessMapper.getadministratorPhone();
            phoneList.forEach(a->{
                try {
                    SmsDemo.sendSms(61,a,cbhCar.getLicensePlateNumber()+"的定损信息");
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            resultMap.put(CommonField.STATUS,true);
            // 保存后台消息
            Map<String, Object> backMsgParam = new HashMap<>();
            backMsgParam.put("type",2);
            backMsgParam.put("orderNo",orderNo);
            backMsgParam.put("title", "待确认");
            if (cbhCar != null){
                backMsgParam.put("content",cbhCar.getLicensePlateNumber() + "的保险理赔订单已定损，等待您确认！");
            }
            backMsgParam.put("isSolve",1);
            backMsgParam.put("orderStatus",32);
            backMsgParam.put("createTime",DateUtils.formatDate(new Date()));
            messageBackstageService.save(backMsgParam);
            // 发送推送消息
            Map<String, Object> pushMsgMap = new HashMap<>();
            pushMsgMap.put("orderNo", backMsgParam.get("orderNo"));
            pushMsgMap.put("type", backMsgParam.get("type"));
            pushMsgMap.put("orderStatus", backMsgParam.get("orderStatus"));
            userAdminService.pushMessageToManager("order",(String) backMsgParam.get("title"),(String) backMsgParam.get("content"),pushMsgMap);
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,CommonField.MUST_NOT_BE_BLANK);
        return resultMap;
    }

    /**
     * @param request       开始维修
     */
    @Transactional
    public Map<String,Object> startMaintenance(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(CommonField.STATUS,false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
        String token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
        String orderNo = Constant.toJsonValue(jsonObject, CommonField.ORDER_NO);
        String repairmanId = Constant.toJsonValue(jsonObject, "repairmanId");

        CbhUserBusiness business = userBusinessService.validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.TOKEY_LOSE_EFFICACY);
            return resultMap;
        }
        if (!Constant.toEmpty(orderNo)){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.ORDER_NUMBER_CANNOT_EMPTY);
            return resultMap;
        }
        if (!Constant.toEmpty(repairmanId)){
            resultMap.put(CommonField.RESULTMESSAGE,"对不起,维修人员不能为空!");
            return resultMap;
        }
        if ("0".equals(repairmanId)){
            resultMap.put(CommonField.RESULTMESSAGE,"对不起,请先添加员工!");
            return resultMap;
        }
        CbhOrder order = cbhOrderMapper.selectByEventNo(orderNo);
        if (order==null){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.SORRY_THERE_IS_NO_ORDER);
            return resultMap;
        }
        if (order.getStatus()!=41){
            resultMap.put(CommonField.RESULTMESSAGE,"对不起,该订单不在待维修状态!");
            return resultMap;
        }
        order.setStatus(42);
        order.setRepairmanId(Integer.valueOf(repairmanId));
        order.setBeginRepairTime(DateUtils.getDateTime());
        resultMap.put(CommonField.STATUS,true);
        cbhOrderMapper.updateByPrimaryKey(order);
        return resultMap;
    }
    /**
     * @param request       提交维修
     */
    @Transactional
    public Map<String,Object> submitMaintenance(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(CommonField.STATUS,false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
        String token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
        String orderNo = Constant.toJsonValue(jsonObject, CommonField.ORDER_NO);
        String maintenanceImg = Constant.toJsonValue(jsonObject, "maintenanceImg");//维修后照片
        String maintenanceDescription = Constant.toJsonValue(jsonObject, "maintenanceDescription");//维修后描述

        CbhUserBusiness business = userBusinessService.validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.TOKEY_LOSE_EFFICACY);
            return resultMap;
        }
        if (!Constant.toEmpty(orderNo)){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.ORDER_NUMBER_CANNOT_EMPTY);
            return resultMap;
        }
        CbhOrder order = cbhOrderMapper.selectByEventNo(orderNo);
        if (order==null){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.SORRY_THERE_IS_NO_ORDER);
            return resultMap;
        }
        if (order.getStatus()!=42){
            resultMap.put(CommonField.RESULTMESSAGE,"对不起,该订单不在维修中状态!");
            return resultMap;
        }
        if (Constant.toEmpty(maintenanceImg)&&Constant.toEmpty(maintenanceDescription)) {
            order.setStatus(51);
            order.setExplanationRepair(maintenanceDescription);
            order.setRepairImg(maintenanceImg);
            order.setEndRepairTime(DateUtils.getDateTime());
            CbhMaintenanceshop maintenanceshop = maintenanceshopMapper.selectByPrimaryKey(order.getMaintenanceshopId());
            if (maintenanceshop != null) {
                order.setTakePlace(maintenanceshop.getAddress());
                order.setTakeLongitude(maintenanceshop.getLongitude());
                order.setTakeLatitude(maintenanceshop.getLatitude());
            }
            cbhOrderMapper.updateByPrimaryKey(order);
            //消息
            CbhMessage messageList = new CbhMessage();
            CbhCar cbhCar = carMapper.selectByPrimaryKey(Integer.valueOf(order.getCarId()));
            String licensePlateNumber=null;
            if (cbhCar != null) {
                licensePlateNumber = cbhCar.getLicensePlateNumber();
                String content = licensePlateNumber +"的理赔订单维修完成，请及时处理";
                messageList.setContent(content);
                messageList.setEventNo(orderNo);
                messageList.setLicensePlateNumber(licensePlateNumber);
            }
            Integer customerId = order.getCustomerId();
            messageList.setCustomerId(customerId);
            messageList.setType(141);
            messageList.setTitle("保险理赔|理赔消息");
            messageMapper.insert(messageList);
            Map<String, String> customerPhone = businessMapper.getCustomerPhone(customerId);
            if (customerPhone != null&&!customerPhone.isEmpty()) {
                SmsDemo.sendSms(31,customerPhone.get("customerPN"),licensePlateNumber);
                String openId = customerPhone.get("openId");
                //微信推送
                if (Constant.toEmpty(openId)) {
                    Map<String, String> result1 = new HashMap<>();
                    result1.put("openid", openId);
                    result1.put("licensePlateNumber", licensePlateNumber);
                    result1.put("model", cbhCar.getModel());
                    result1.put("eventNo", orderNo);
                    result1.put("address", maintenanceshop.getAddress());
                    result1.put("tel", maintenanceshop.getTel());
                    weiXinUtils.sendTemplate(3, result1);
                }
            }
            resultMap.put(CommonField.STATUS,true);
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,CommonField.MUST_NOT_BE_BLANK);
        return resultMap;

    }
}
