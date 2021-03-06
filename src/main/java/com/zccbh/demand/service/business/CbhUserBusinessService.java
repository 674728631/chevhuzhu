package com.zccbh.demand.service.business;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.google.gson.JsonObject;
import com.zccbh.demand.controller.alipay.AlipayConfig;
import com.zccbh.demand.controller.quartz.QuartzUtils;
import com.zccbh.demand.controller.quartz.ReceiveCarJob;
import com.zccbh.demand.mapper.foundation.FoundationMapper;
import com.zccbh.demand.mapper.merchants.*;
import com.zccbh.demand.pojo.common.Foundation;
import com.zccbh.demand.pojo.merchants.*;
import com.zccbh.demand.service.customer.MessageBackstageService;
import com.zccbh.demand.service.user.UserAdminService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.*;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.collect.MD5Util;
import net.sf.json.JSONObject;
import sun.tools.tree.ThisExpression;

import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: luoyuangang
 * @JDK version used:       	1.8
 * @Modified By:            	<修改人中文名或拼音缩写>
 * @Modified Date:          	<修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: 	<修改原因描述>
 * @create 2018-03-12 15:53
 **/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CbhUserBusinessService {
    @Autowired
    private CbhUserBusinessMapper businessMapper;
    @Autowired
    private CbhEventMapper eventMapper;
    @Autowired
    private CbhMaintenanceshopEmployeeMapper employeeMapper;
    @Autowired
    private CbhEventAssertMapper assertMapper;
    @Autowired
    private CbhMessageMapper messageMapper;
    @Autowired
    private cbhEventRepairMapper repairMapper;
    @Autowired
    private CbhAccountMapper accountMapper;
    @Autowired
    private CbhAccountDetailMapper accountDetailMapper;
    @Autowired
    private CbhMaintenanceshopMapper maintenanceshopMapper;
    @Autowired
    private CbhSuggestionsMapper cbhSuggestionsMapper;
    @Autowired
    private CbhCarMapper carMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WeiXinUtils weiXinUtils;
    @Autowired
    private SchedulerFactoryBean schedulerFactory;
    @Autowired
    private CbhOrderMapper cbhOrderMapper;
    @Autowired
    private FoundationMapper foundationMapper;
    @Autowired
    private MessageBackstageService messageBackstageService;
    @Autowired
    private UserAdminService userAdminService;

    /**
     * 验证token是否有效
     *
     * @param
     * @return
     */
    public CbhUserBusiness validationAccToken(String token){
        if (Constant.toEmpty(token)) {
            CbhUserBusiness business = businessMapper.selectByToken(token,null);
            if (business != null) {
                Date tokenAging = business.getTokenAging();
                token = business.getToKen();
                if (tokenAging != null && token != null) {
                    if (DateUtils.booleanToken(tokenAging, new Date())) {
                        return business;
                    }
                }
            }
        }
        return null;
    }

    /**
     *
     * @para
     * @return
     */
    public Map<String, Object> validationToken(HttpServletRequest request)throws Exception {
        String token = request.getParameter(CommonField.TOKEN);
        String mobileNumber = null;
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status",false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        resultMap.put(CommonField.TOKEN,"");
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            mobileNumber = Constant.toJsonValue(jsonObject, "mobileNumber");
        } else {
            mobileNumber = request.getParameter("mobileNumber");
        }
        if (Constant.toEmpty(token)&&Constant.toEmpty(mobileNumber)) {
            CbhUserBusiness business = businessMapper.selectByToken(token,mobileNumber);
            if (business != null) {
                Date tokenAging = business.getTokenAging();
                token = business.getToKen();
                if (tokenAging != null && token != null) {
                    if (DateUtils.booleanToken(tokenAging, new Date())) {
                        resultMap.put(CommonField.TOKEN, token);
                        resultMap.put("status",true);
                        return resultMap;
                    }
                }
                resultMap.put(CommonField.RESULTMESSAGE,"toKen已失效!");
                return resultMap;
            }
            resultMap.put(CommonField.RESULTMESSAGE,"没有这个用户!");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,"手机号和toKen不能为空");
        return resultMap;
    }

    /**
     *
     * @param request       登录请求
     * @return  method = RequestMethod.POST
     * @throws UnsupportedEncodingException
     */
    @Transactional
    public Map<String,Object> login(HttpServletRequest request)throws Exception {
        String userName = request.getParameter("userName");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status",false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        resultMap.put(CommonField.TOKEN, "");
        String passWord = null;
        String iosDeviceId = null;
        String androidDeviceId = null;
        if (StringUtils.isBlank(userName)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            userName = Constant.toJsonValue(jsonObject, "userName");
            passWord = Constant.toJsonValue(jsonObject, "passWord");
            iosDeviceId = Constant.toJsonValue(jsonObject, "iosDeviceId");
            androidDeviceId = Constant.toJsonValue(jsonObject, "androidDeviceId");
        } else {
            iosDeviceId = request.getParameter("iosDeviceId");
            androidDeviceId = request.getParameter("androidDeviceId");
            passWord = request.getParameter("passWord");
        }
        if (Constant.toEmpty(passWord)&&Constant.toEmpty(userName)) {
            passWord = MD5Util.getMD5Code(passWord);
            CbhUserBusiness business = businessMapper.selectByMobileNumber(userName,passWord);
            if (business != null) {
                if (business.getStatus()==2){
                    resultMap.put(CommonField.RESULTMESSAGE,"尊敬的商户您的账户由于违规操作、被举报或其他原因，已经被冻结，如有疑问请联系客服（400-0812-868）");
                    return resultMap;
                }
                Date tokenAging = business.getTokenAging();
                String token = business.getToKen();
                if (tokenAging != null && token != null) {
                    if (DateUtils.booleanToken(tokenAging, new Date())) {
                        Date tokenTime1 = DateUtils.getTokenTime();
                        String toKen1 = SecurityUtil.getToKen();
                        business.setToKen(toKen1);
                        business.setTokenAging(tokenTime1);
                        if (Constant.toEmpty(androidDeviceId)) {
                            business.setAndroidDeviceId(androidDeviceId);
                        }
                        if (Constant.toEmpty(iosDeviceId)) {
                            business.setIosDeviceId(iosDeviceId);
                        }
                        businessMapper.updateByPrimaryKeySelective(business);
                        resultMap.put(CommonField.TOKEN, toKen1);
                        resultMap.put("status",true);
                        return resultMap;
                    }
                }
                Date tokenTime = DateUtils.getTokenTime();
                String toKen = SecurityUtil.getToKen();
                business.setToKen(toKen);
                business.setTokenAging(tokenTime);
                business.setAndroidDeviceId(androidDeviceId);
                business.setIosDeviceId(iosDeviceId);
                businessMapper.updateByPrimaryKeySelective(business);
                resultMap.put(CommonField.TOKEN, toKen);
                resultMap.put("status",true);
                return resultMap;
            }
            resultMap.put(CommonField.RESULTMESSAGE,"账户或密码错误!");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,"用户名和密码不能为空");
        return resultMap;
    }
    /**
     *
     * @param request       验证验证码是否正确
     * @throws UnsupportedEncodingException
     */
    public Map<String,Object> phoneVerification(HttpServletRequest request)throws Exception {
        //验证token是否有效
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status",false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        String mobileNumber =null;
        String verificationCode =null;
        String type =null;
        String token = request.getParameter(CommonField.TOKEN);
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            mobileNumber = Constant.toJsonValue(jsonObject, "mobileNumber");
            verificationCode = Constant.toJsonValue(jsonObject, "verificationCode");
            type = Constant.toJsonValue(jsonObject, "type");
        }else{
            mobileNumber = request.getParameter("mobileNumber");
            verificationCode = request.getParameter("verificationCode");
            type = request.getParameter("type");

        }
        if (Constant.toEmpty(token)) {
            CbhUserBusiness business = validationAccToken(token);
            if (business==null){
                resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
                resultMap.put(CommonField.RESULTMESSAGE,"token失效!");
                return resultMap;
            }
        }

        if (Constant.toEmpty(mobileNumber)&&Constant.toEmpty(verificationCode)) {
            String redisStr;
            if (type.equals("1")) {
                redisStr = redisUtil.getStr(mobileNumber);
            }else{
                redisStr = redisUtil.getStr(mobileNumber+CommonField.EMBODY);
            }
            if (verificationCode.equals(redisStr)) {
                resultMap.put("status",true);
                resultMap.put(CommonField.RESULTMESSAGE,"");
                redisUtil.delect(mobileNumber+CommonField.EMBODY);
                return resultMap;
            }
            resultMap.put(CommonField.RESULTMESSAGE,"验证码错误!");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,"手机号和验证码不能为空");
        return resultMap;
    }
    /**
     * @param request       修改密码
     */
    @Transactional
    public Map<String,Object> updadePassWord(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status",false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        String passWord =request.getParameter("passWord");
        String verificationCode =null;
        String mobileNumber =null;
        if (StringUtils.isBlank(passWord)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            passWord = Constant.toJsonValue(jsonObject, "passWord");
            verificationCode = Constant.toJsonValue(jsonObject, "verificationCode");
            mobileNumber = Constant.toJsonValue(jsonObject, "mobileNumber");
        }else{
            verificationCode = request.getParameter("verificationCode");
            mobileNumber = request.getParameter("mobileNumber");
        }
        if (Constant.toEmpty(passWord)&&Constant.toEmpty(verificationCode)&&Constant.toEmpty(mobileNumber)) {
            String redisStr = redisUtil.getStr(mobileNumber);
            if (!verificationCode.equals(redisStr)) {
                resultMap.put(CommonField.RESULTMESSAGE,"验证码错误!");
                return resultMap;
            }
            CbhUserBusiness business = businessMapper.selectByPhoneNumber(mobileNumber);
            if (business == null) {
                resultMap.put(CommonField.RESULTMESSAGE,"没有该用户名!");
                return resultMap;
            }
            passWord = MD5Util.getMD5Code(passWord);
            business.setBusinessPW(passWord);
            resultMap.put("status",true);
            businessMapper.updateByPrimaryKey(business);
            redisUtil.delect(mobileNumber);
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,"密码不能为空");
        return resultMap;

    }
    /**
     * @param request       订单列表
     */
    public Map<String,Object> orderList(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status",false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        String page =null;
        String orderStatus =null;
        String token = request.getParameter(CommonField.TOKEN);
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            page = Constant.toJsonValue(jsonObject, "page");
            orderStatus = Constant.toJsonValue(jsonObject, "orderStatus");
        }else{
            page = request.getParameter("page");
            orderStatus = request.getParameter("orderStatus");
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,"token失效!");
            return resultMap;
        }
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("toKen",token);
        parameterMap.put("page",null);
        //已完成,未完成 总数
        int completeNumber =0;
        int noCompleteNumber =0;
        int allNumber=0;
        List<Map<String,Object>> orderLists = businessMapper.getOrderList(parameterMap);
        if (orderLists != null&&orderLists.size()>0&&orderLists.get(0)!=null) {
            allNumber = orderLists.size();
            completeNumber = (int) orderLists.stream().filter(CbhUserBusinessService::comparingByOrderStatus).count();
            noCompleteNumber=allNumber-completeNumber;
        }
        int totalPage =getTotalPage(allNumber);
        resultMap.put("allNumber",allNumber);
        resultMap.put("completeNumber",completeNumber);
        resultMap.put("noCompleteNumber",noCompleteNumber);
        Integer pages=0;
        if (page !=null&&!page.equals("")&&!page.equals("0")) {
            pages=(Integer.valueOf(page)-1)*20;
        }
        if (Constant.toEmpty(orderStatus)) {
            parameterMap.put("orderStatus",orderStatus);
            if (orderStatus.equals("1")) {
                totalPage =getTotalPage(completeNumber);
            }else{
                totalPage =getTotalPage(noCompleteNumber);
            }
        }
        parameterMap.put("page",pages);
        List<Map<String,Object>> orderList = businessMapper.getOrderList(parameterMap);
        if (orderList != null&&orderList.size()>0&&orderList.get(0)!=null) {
            for(int i = 0; i < orderList.size(); i++) {
                Map<String, Object> objectMap = orderList.get(i);
                Date timeApply = (Date) objectMap.get("timeApply");
                Date timeReceiveOrder = (Date) objectMap.get("timeReceiveOrder");
                Date timeAssert = (Date) objectMap.get("timeAssert");
                Date createAt = (Date) objectMap.get("createAt");
                Date timeReceiveCar = (Date) objectMap.get("timeReceiveCar");
                Date timeBegin = (Date) objectMap.get("timeBegin");
                Date timeEnd = (Date) objectMap.get("timeEnd");
                Date timeComplaint = (Date) objectMap.get("timeComplaint");
                Date timeComplete = (Date) objectMap.get("timeComplete");
                Date ceoCreateAt = (Date) objectMap.get("ceoCreateAt");
                Date meetingTrainTime = (Date) objectMap.get("meetingTrainTime");
                String pricingName = (String) objectMap.get("pricingName");
                String maintenanceName = (String) objectMap.get("maintenanceName");
                String nameCarOwner = (String) objectMap.get("nameCarOwner");
                String eventNo = (String) objectMap.get("eventNo");
                if (eventNo.length()==21){
                    objectMap.put("type",1);
                }else{
                    objectMap.put("type",0);
                }
                objectMap.put("timeApply",getStringDate(timeApply));
                objectMap.put("timeReceiveOrder",getStringDate(timeReceiveOrder));
                objectMap.put("timeAssert",getStringDate(timeAssert));
                objectMap.put("createAt",getStringDate(createAt));
                objectMap.put("meetingTrainTime",getStringDate(meetingTrainTime));
                objectMap.put("timeReceiveCar",getStringDate(timeReceiveCar));
                objectMap.put("timeBegin",getStringDate(timeBegin));
                objectMap.put("timeEnd",getStringDate(timeEnd));
                objectMap.put("timeComplaint",getStringDate(timeComplaint));
                objectMap.put("timeComplete",getStringDate(timeComplete));
                objectMap.put("ceoCreateAt",getStringDate(ceoCreateAt));
                objectMap.put("pricingName",getStringName(pricingName));
                objectMap.put("maintenanceName",getStringName(maintenanceName));
                objectMap.put("nameCarOwner",getStringName(nameCarOwner));
            }
            resultMap.put("status",true);
            resultMap.put("orderList",orderList);
            resultMap.put("page",getPage(orderList,page,totalPage));
            return resultMap;
        }
        resultMap.put("status",true);
        resultMap.put("page",getPage(orderList,page,totalPage));
        resultMap.put("orderList",orderList);
        return resultMap;
    }
    //获得string类型的时间
    public String getStringDate(Date date){
        if (date==null) {
            return "";
        }
      return  DateUtils.getStringDateTime(date);
    }
    public String getStringName(String parameter) {
        if (Constant.toEmpty(parameter)) {
            return parameter;
        }
        return "";
    }

    public Map<String,Object> getPage(List<Map<String,Object>> twitterLists,String page,int totalPage){
        Map<String,Object> pageList =new HashMap<>();
        if (Constant.toEmpty(page)) {
            pageList.put("currentPage",Integer.valueOf(page));
        }else{
            pageList.put("currentPage",1);
        }
        pageList.put("totalPage",totalPage);
        if (twitterLists != null&&twitterLists.size()>0&&twitterLists.get(0)!=null) {
            int size = twitterLists.size();
            pageList.put("remainingNumber",size);
            return pageList;
        }
        pageList.put("remainingNumber",0);
        return pageList;
    }

    private static Boolean comparingByOrderStatus(Map<String, Object> map){
        int orderStatus = (int) map.get("statusEvent");
        String eventNo = (String) map.get("eventNo");
        int length = eventNo.length();
        if (length ==20){
            if (orderStatus==71|orderStatus==100){
                return true;
            }
            return false;
        }else{
            if (orderStatus==61|orderStatus==100){
                return true;
            }
            return false;
        }
    }

    //获得总页数
    public int getTotalPage(int size){
        int result = 0;
        if (size==0){
            return result;
        }
        if ((size % 20) == 0) {
            result = size / 20;
        } else {
            result = size / 20 + 1;
        }
        return result;
    }
    /**
     * @param request       点击订单明细
     */
    public Map<String,Object> orderDetail(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status",false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        String orderNo =null;
        String token = request.getParameter(CommonField.TOKEN);
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            orderNo = Constant.toJsonValue(jsonObject, "orderNo");
        }else{
            orderNo = request.getParameter("orderNo");
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,"token失效!");
            return resultMap;
        }
        Integer businessId = business.getId();
        if (Constant.toEmpty(orderNo)) {
          CbhEvent event = eventMapper.selectByEventNo(orderNo);
            if (event != null) {
                Integer statusEvent = event.getStatusEvent();
                Map<String,Object> orderDetail;
                resultMap.put("status",true);
                switch (statusEvent){
                    case 11:
                        orderDetail = eventMapper.getOrderDetailByEventNo(orderNo,token);
                        resultMap.put("orderDetail",getOrderDetail(orderDetail,businessId,orderNo));
                        return resultMap;
                    case 21:
                        orderDetail = eventMapper.getOrderDetailByStatus21(orderNo,token);
                        resultMap.put("orderDetail",getOrderDetail(orderDetail,businessId,orderNo));
                        return resultMap;
                    case 22:
                        orderDetail = eventMapper.getOrderDetailByStatus21(orderNo,token);
                        resultMap.put("orderDetail",getOrderDetail(orderDetail,businessId,orderNo));
                        return resultMap;
                    case 31:
                        orderDetail = eventMapper.getOrderDetailByStatus21(orderNo,token);
                        resultMap.put("orderDetail",getOrderDetail(orderDetail,businessId,orderNo));
                        return resultMap;
                    case 41:
                        orderDetail = eventMapper.getOrderDetailByStatus21(orderNo,token);
                        resultMap.put("orderDetail",getOrderDetail(orderDetail,businessId,orderNo));
                        return resultMap;
                    case 51:
                        orderDetail = eventMapper.getOrderDetailByStatus21(orderNo,token);
                        resultMap.put("orderDetail",getOrderDetail(orderDetail,businessId,orderNo));
                        return resultMap;
                    case 52:
                        orderDetail = eventMapper.getOrderDetailByStatus52(orderNo,token);
                        resultMap.put("orderDetail",getOrderDetail(orderDetail,businessId,orderNo));
                        return resultMap;
                    case 61:
                        orderDetail = eventMapper.getOrderDetailByStatus52(orderNo,token);
                        resultMap.put("orderDetail",getOrderDetail(orderDetail,businessId,orderNo));
                        return resultMap;
                    case 71:
                        orderDetail = eventMapper.getOrderDetailByStatus81(orderNo,token,"3");
                        resultMap.put("orderDetail",getOrderDetail(orderDetail,businessId,orderNo));
                        return resultMap;
                    case 81:
                        orderDetail = eventMapper.getOrderDetailByStatus81(orderNo,token,"1");
                        resultMap.put("orderDetail",getOrderDetail(orderDetail,businessId,orderNo));
                        return resultMap;
                    case 100:
                        orderDetail = eventMapper.getOrderDetailByStatus100(orderNo,token);
                        resultMap.put("orderDetail",getOrderDetail(orderDetail,businessId,orderNo));
                        return resultMap;
                }

            }
            resultMap.put(CommonField.RESULTMESSAGE,"订单号不存在");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,"订单号不能为空");
        return resultMap;

    }

    private Map<String,Object> getOrderDetail(Map<String,Object> orderDetail,Integer businessId,String orderNo)throws Exception{
        if (orderDetail == null||orderDetail.isEmpty()) {
            return orderDetail;
        }
        String licensePlateNumber = (String) orderDetail.get("licensePlateNumber");
        String reciveCarTime = (String) orderDetail.get("reciveCarTime");
        Date timeReceiveCars = (Date) orderDetail.get("timeReceiveCars");
//        下面注释的是:把行驶证放在定损照片集合的首位
//        String drivingLicense = CommonField.getCarDrivingUrl((String) orderDetail.get("drivingLicense"));
//        List<String> accidentImgList = getImgList(1, 0, (String) orderDetail.get("accidentImgList"));
//        ArrayList<String> objects = new ArrayList<>();
//        objects.add(drivingLicense);
//        orderDetail.put("accidentImgList",objects.addAll(accidentImgList));
        //车辆照片 事故照片 定损照片 维修后照片
        orderDetail.put("carImgList", CommonField.getCarUrlList((String) orderDetail.get("carImgList")));
        orderDetail.put("accidentImgList",getImgList(1,0,(String) orderDetail.get("accidentImgList")));
        orderDetail.put("assertImgList",getImgList(1,1,(String) orderDetail.get("assertImgList")));
        orderDetail.put("repairImgList",getImgList(1,2,(String) orderDetail.get("repairImgList")));
        Date timeReceiveCar = (Date) orderDetail.get("timeReceiveCar");
        Integer statusEvent = (Integer) orderDetail.get("statusEvent");
        Date createAt = (Date) orderDetail.get("createAt");
        orderDetail.put("pickCarTime",getStringName(reciveCarTime));
        orderDetail.remove("timeReceiveCars");
        orderDetail.remove("drivingLicense");
        for (Map.Entry<String,Object> a:orderDetail.entrySet()) {
            String key = a.getKey();
            Object value = a.getValue();
            if (value instanceof Date) orderDetail.put(key,getStringDate((Date) value));
            if ("amtAssert".equals(key)) orderDetail.put(key,value.toString());
            if ("labelContent".equals(key)) orderDetail.put(key,((String)value).split("_"));
        }
        if (statusEvent <=21) {
            long v;
            String type=null;
            if(statusEvent <21){
                v =600-(DateUtils.cutTwoDateToSeconds(createAt));
            }else{
                v = 0-DateUtils.cutTwoDateToSeconds(DateUtils.getDateTimeByString(reciveCarTime));
                type="接车";
            }
            orderDetail = getCountdown(orderDetail,v,type,businessId,orderNo,licensePlateNumber);
        }else if (statusEvent >21&&statusEvent <51){
            long v = 0-DateUtils.cutTwoDateToSeconds(DateUtils.getDateTimeByString(reciveCarTime));
            orderDetail = getCountdown(orderDetail,v,"接车",businessId,orderNo,licensePlateNumber);
        }else if (statusEvent >=51&&statusEvent <61){
            long v = (60 * 60 * 48) - (DateUtils.cutTwoDateToSeconds(timeReceiveCar));
            orderDetail = getCountdown(orderDetail,v,"维修完成",businessId,orderNo,licensePlateNumber);
        }else if (statusEvent >=61&&statusEvent <70){
            long v = (60 * 60 * 48)-(DateUtils.cutTwoDateToSeconds(timeReceiveCar));
            orderDetail = getCountdown(orderDetail,v,null,businessId,orderNo,licensePlateNumber);
        }else{
            orderDetail.put("countdown",-1);
        }
        if(statusEvent==81){
            orderDetail.put("timeReceiveCar",getStringDate(timeReceiveCars));
        }
        List<String> strings = Arrays.asList("timeAssert", "name", "telCarOwner", "latitude", "longitude", "createAt", "accidentDescription", "place", "nameCarOwner", "brand",
                "content", "timeEnd", "timeBegin", "maintenanceDescription", "maintenanceName","description", "damagePosition", "damageExtent", "amtAssert", "timeReceiveCar",
                "assertReasonSuccess", "applyReasonSuccess","carReasonSuccess", "ceoCreateAt", "commentContent", "timeUnComplaint", "timeComplaint","score","labelContent");
        Map<String, Object> finalOrderDetail = orderDetail;
        strings.forEach(a->{
            if (!finalOrderDetail.containsKey(a)) {
                if("score".equals(a)){
                    finalOrderDetail.put(a,-1);
                }else if("labelContent".equals(a)){
                    finalOrderDetail.put(a,new String[0]);
                }else{
                    finalOrderDetail.put(a,"");
                }
            }
        });
        return finalOrderDetail;
    }

    private Map<String,Object> getCountdown(Map<String,Object> orderDetail,long v,String type,
                                            Integer businessId,String orderNo,String licensePlateNumber)throws Exception{
        if (v<=0) {
            if (Constant.toEmpty(type)) {
                Boolean aBoolean = saveMessage(businessId, orderNo, licensePlateNumber, licensePlateNumber + "的订单您没有按时" + type + ",扣2分!");
                if (aBoolean) {
                    servicePoints(orderNo, 2.0, "-");
                }
            }
            orderDetail.put("countdown", 0);
        }else{
            orderDetail.put("countdown", v);
        }
        return orderDetail;
    }

    @Transactional
    public Boolean saveMessage(Integer businessId,String orderNo,String licensePlateNumber,String content){
       CbhMessage message1 = messageMapper.selectByEventNoAndContent(orderNo,content);
        if (message1 == null) {
            CbhMessage message = new CbhMessage();
            message.setBusinessId(businessId);
            message.setEventNo(orderNo);
            message.setLicensePlateNumber(licensePlateNumber);
            message.setType(11);
            message.setTitle("扣分记录");
            message.setContent(content);
            message.setScore(2.0);
            messageMapper.insert(message);
            return true;
        }
        return false;
    }

    /**
     *
     * @param type 互助事件  车主 0--定损 1-维修后 2  types 1互助事件 2商铺
     * @param imgNames
     * @return
     * @throws Exception
     */
    private List<String> getImgList(int types,int type,String imgNames)throws Exception{
    	System.out.println("************图片名字*******" + imgNames);
        List<String> imgList = new ArrayList<>();
        if (Constant.toEmpty(imgNames)) {
            String[] strings = imgNames.split("_");
            for (int i = 0; i <strings.length ; i++) {
                String imgName = strings[i];
                String eventImg;
                if (types == 1) {
                    eventImg = CommonField.getEventImg(type, imgName);
                }else{
                    eventImg = CommonField.getMaintenanceShopImg(type, imgName);
                }
                imgList.add(i,eventImg);
                System.out.println("************图片地址*******" + eventImg);
            }            
            return imgList;
        }
        return imgList;

    }
    
    /**
     * @param request       放弃接单
     */
    @Transactional
    public Map<String,Object> abandonOrder(HttpServletRequest request) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status",false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        String orderNo =null;
        String reason =null;//原因
        String instructions =null;//说明
        String token = request.getParameter(CommonField.TOKEN);
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            orderNo = Constant.toJsonValue(jsonObject, "orderNo");
            reason = Constant.toJsonValue(jsonObject, "reason");
            instructions = Constant.toJsonValue(jsonObject, "instructions");
        }else{
            orderNo = request.getParameter("orderNo");
            reason = request.getParameter("reason");
            instructions = request.getParameter("instructions");
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,"token失效!");
            return resultMap;
        }
        if (Constant.toEmpty(orderNo)) {
            CbhEvent event = eventMapper.selectByEventNo(orderNo);
            if (event != null) {
                if (Constant.toEmpty(reason)) {
                    event.setCancellationsReason(reason);
                    event.setCancellationsInstructions(instructions);
                    event.setStatusEvent(12);
                    // 设置放弃接单时间
                    event.setFailReceiveOrderTime(new Date());
                    eventMapper.updateByPrimaryKey(event);
                    messageMapper.deleteByOrderNo(orderNo);
                    CbhCar cbhCar = carMapper.selectByPrimaryKey(event.getCarId());
                    List<String> phoneList = businessMapper.getadministratorPhone();
                    if (cbhCar != null) {
                        if (phoneList != null&&phoneList.size()>0) {
                            for(int i = 0; i < phoneList.size(); i++) {
                                String phone = phoneList.get(i);
                                SmsDemo.sendSms(81,phone,cbhCar.getLicensePlateNumber());
                            }
                        }
                    }
                    Scheduler sche = schedulerFactory.getScheduler();
                    QuartzUtils.removeJob(sche, orderNo);
                    resultMap.put("status",true);
                    // 保存后台消息
                    Map<String, Object> backMsgParam = new HashMap<>();
                    backMsgParam.put("type",1);
                    backMsgParam.put("orderNo",orderNo);
                    backMsgParam.put("title","放弃接单");
                    if (cbhCar != null){
                        backMsgParam.put("content",cbhCar.getLicensePlateNumber() + "的救助订单被放弃接单，等待您重新分单！");
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
                    userAdminService.pushMessageToManager("event",(String) backMsgParam.get("title"),(String) backMsgParam.get("content"),pushMsgMap);
                    return resultMap;
                }
                resultMap.put(CommonField.RESULTMESSAGE,"原因不能为空");
                return resultMap;
            }
            resultMap.put(CommonField.RESULTMESSAGE,"订单号不存在");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,"订单号不能为空");
        return resultMap;
    }
    /**
     * @param request       定损人员
     */
    public Map<String,Object> assertEmployeeList(HttpServletRequest request) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status",false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        String token = request.getParameter(CommonField.TOKEN);
        String type = null;
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            type = Constant.toJsonValue(jsonObject, "type");
        }else{
            type = request.getParameter("type");
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,"token失效!");
            return resultMap;
        }
        Map<String, Object> assertEmployee = new HashMap<>();
        assertEmployee.put("toKen",token);
        if (Constant.toEmpty(type)) {
            assertEmployee.put("type",Integer.valueOf(type));
        }
        List<Map<String, Object>> assertEmployeeList = employeeMapper.getAssertEmployee(assertEmployee);
        if (assertEmployeeList != null&&assertEmployeeList.size()>0&&assertEmployeeList.get(0)!=null) {
            resultMap.put("status",true);
            resultMap.put("assertEmployeeList",assertEmployeeList);
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,"暂无定损员");
        resultMap.put("assertEmployeeList",assertEmployeeList);
        return resultMap;
    }
    /**
     * @param request       立即接单
     */
    @Transactional
    public Map<String,Object> takingOrder(HttpServletRequest request) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status",false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        String orderNo =null;
        String id =null;//
        String token = request.getParameter(CommonField.TOKEN);

        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            orderNo = Constant.toJsonValue(jsonObject, "orderNo");
            id = Constant.toJsonValue(jsonObject, "id");
        }else{
            orderNo = request.getParameter("orderNo");
            id = request.getParameter("id");
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,"token失效!");
            return resultMap;
        }
        if ("0".equals(id)){
            resultMap.put(CommonField.RESULTMESSAGE,"对不起,请先添加员工!");
            return resultMap;
        }
        if (Constant.toEmpty(orderNo)) {
            CbhEvent event = eventMapper.selectByEventNo(orderNo);
            if (event != null) {
                if (Constant.toEmpty(id)) {
                    CbhEventAssert eventAssert = assertMapper.selectByEventNo(orderNo);
                    if (eventAssert != null) {
                        eventAssert.setMaintenanceshopEmployeeId(Integer.valueOf(id));
                        eventAssert.setCreateAt(DateUtils.getDateTime());
                        assertMapper.updateByPrimaryKey(eventAssert);
                    }else{
                        CbhEventAssert anAssert = new CbhEventAssert();
                        anAssert.setMaintenanceshopEmployeeId(Integer.valueOf(id));
                        anAssert.setEventNo(orderNo);
                        assertMapper.insert(anAssert);
                    }
                    event.setStatusEvent(21);
                    event.setTimeReceiveOrder(DateUtils.getDateTime());
                    eventMapper.updateByPrimaryKey(event);
                    messageMapper.deleteByOrderNo(orderNo);
                    Scheduler sche = schedulerFactory.getScheduler();
                    QuartzUtils.removeJob(sche, orderNo);
                    String jobName = "receivecar_"+orderNo;
                    Map<String, String> dateMap = DateUtils.getDateMap(150 * 60 * 1000);
                    String cron = dateMap.get("second")+" "+dateMap.get("minute")+" "+dateMap.get("hour")+" * * ? *";
                    Map<String, Object> params = new HashMap<>();
                    params.put("jobName", jobName);
                    CbhCar cbhCar = carMapper.selectByPrimaryKey(event.getCarId());
                    if (cbhCar != null) {
                        String content=cbhCar.getLicensePlateNumber()+"的订单接车倒计时还剩下30分钟，请及时接车哦！";
                        params.put("content", content);
                    }
                    QuartzUtils.addJob(sche, jobName, ReceiveCarJob.class, params, cron);
                    resultMap.put("status",true);
                    return resultMap;
                }
                resultMap.put(CommonField.RESULTMESSAGE,"定损人员id不能为空");
                return resultMap;
            }
            resultMap.put(CommonField.RESULTMESSAGE,"订单号不存在");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,"订单号不能为空");
        return resultMap;
    }

    /**
     * @param request       提交定损
     */
    @Transactional
    public Map<String,Object> submitAssert(HttpServletRequest request) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status",false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        String orderNo =null;
        String damageExtent =null;//受损程度
        String damagePosition =null;//受损部位
        String assertImg =null;//定损照片
        String description =null;//定损描述
        String amtAssert =null;//定损费用
        String token = request.getParameter(CommonField.TOKEN);
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            orderNo = Constant.toJsonValue(jsonObject, "orderNo");
            damageExtent = Constant.toJsonValue(jsonObject, "damageExtent");
            damagePosition = Constant.toJsonValue(jsonObject, "damagePosition");
            assertImg = Constant.toJsonValue(jsonObject, "assertImg");
            description = Constant.toJsonValue(jsonObject, "description");
            amtAssert = Constant.toJsonValue(jsonObject, "amtAssert");
        }else{
            orderNo = request.getParameter("orderNo");
            damageExtent = request.getParameter("damageExtent");
            damagePosition = request.getParameter("damagePosition");
            assertImg = request.getParameter("assertImg");
            description = request.getParameter("description");
            amtAssert = request.getParameter("amtAssert");
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,"token失效!");
            return resultMap;
        }
        if (Constant.toEmpty(orderNo)) {
            CbhEvent event = eventMapper.selectByEventNo(orderNo);
            if (event != null) {
                if (Constant.toEmpty(damageExtent)&&Constant.toEmpty(damagePosition)&&
                        Constant.toEmpty(assertImg)&&Constant.toEmpty(description)&&Constant.toEmpty(amtAssert)) {
                    CbhEventAssert anAssert = assertMapper.selectByEventNo(orderNo);
                    if (anAssert != null) {
                        Integer statusEvent = event.getStatusEvent();
                        if (!(statusEvent==21||statusEvent ==22)){
                            resultMap.put(CommonField.RESULTMESSAGE,"对不起,该订单不在待定损或待确认状态!");
                            return resultMap;
                        }
                        event.setStatusEvent(22);
                        eventMapper.updateByPrimaryKey(event);
                        anAssert.setDamageExtent(damageExtent);
                        anAssert.setDamagePosition(damagePosition);
                        anAssert.setAssertImg(assertImg);
                        anAssert.setDescription(description);
                        anAssert.setAmtAssert(new BigDecimal(amtAssert.trim()));
                        anAssert.setTimeAssert(DateUtils.getDateTime());
                        assertMapper.updateByPrimaryKey(anAssert);
                        CbhCar cbhCar = carMapper.selectByPrimaryKey(event.getCarId());
                        List<String> phones = businessMapper.getadministratorPhone();
                        if (cbhCar != null) {
                            if (phones != null&&phones.size()>0) {
                                for(int i = 0; i < phones.size(); i++) {
                                    String phone = phones.get(i);
                                    SmsDemo.sendSms(61,phone,cbhCar.getLicensePlateNumber()+"的定损信息");
                                }
                            }
                        }
                        resultMap.put("status",true);
                        // 保存后台消息
                        Map<String, Object> backMsgParam = new HashMap<>();
                        backMsgParam.put("type",1);
                        backMsgParam.put("orderNo",orderNo);
                        backMsgParam.put("title", "待确认");
                        if (cbhCar != null){
                            backMsgParam.put("content",cbhCar.getLicensePlateNumber() + "的救助订单已定损，等待您确认！");
                        }
                        backMsgParam.put("isSolve",1);
                        backMsgParam.put("orderStatus",22);
                        backMsgParam.put("createTime",DateUtils.formatDate(new Date()));
                        messageBackstageService.save(backMsgParam);
                        // 发送推送消息
                        Map<String, Object> pushMsgMap = new HashMap<>();
                        pushMsgMap.put("orderNo", backMsgParam.get("orderNo"));
                        pushMsgMap.put("type", backMsgParam.get("type"));
                        pushMsgMap.put("orderStatus", backMsgParam.get("orderStatus"));
                        userAdminService.pushMessageToManager("event",(String) backMsgParam.get("title"),(String) backMsgParam.get("content"),pushMsgMap);
                        return resultMap;
                    }
                    resultMap.put(CommonField.RESULTMESSAGE,"请先选择定损人");
                    return resultMap;
                }
                resultMap.put(CommonField.RESULTMESSAGE,"必填项不能为空");
                return resultMap;
            }
            resultMap.put(CommonField.RESULTMESSAGE,"订单号不存在");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,"订单号不能为空");
        return resultMap;
    }
    /**
     * @param request       首页
     */
    public Map<String,Object> homePage(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status",false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        String token = request.getParameter(CommonField.TOKEN);
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,"token失效!");
            return resultMap;
        }

        Map<String,Object> homePage = businessMapper.getHomePageParameter(token);
        Map<String, Object> objectMap = getTotalMoney(0,homePage);
        List<CbhMessage> cbhMessages = messageMapper.selectAllByToken(token);
//        Map<String, Object> parameterMap = businessMapper.getMyDetails(token);
        if (cbhMessages!=null&&cbhMessages.size()>0) {
            List<CbhMessage> collect = cbhMessages.stream().filter(a -> a.getType() == 21|a.getType() ==121).filter(a->a.getIsRead()==1).collect(Collectors.toList());
//            Integer servicePoints = cbhMessages.stream().filter(a -> a.getType() == 11).collect(Collectors.reducing(0, CbhMessage::getScore, (a, b) -> a + b));
            long count = cbhMessages.stream().filter(a -> a.getIsRead() == 1).count();
            if (collect != null&&collect.size()>0) {
                for(int i = 0; i < collect.size(); i++) {
                    CbhMessage message = collect.get(i);
                    Date createAt = message.getCreateAt();
                    String stringDate = getStringDate(createAt);
                    message.setChaining(stringDate);
                    message.setCreateAt(null);
                    int v = 600 - (DateUtils.cutTwoDateToSeconds(createAt));
                    if (v<=0){
                        message.setCountdown(0);
                    }else{
                        message.setCountdown(v);
                    }
                }
            }
            resultMap.put("messageList",collect);
            objectMap.put("messageNumber",count);
        }else{
            resultMap.put("messageList",cbhMessages);
            objectMap.put("messageNumber",0);
        }
        resultMap.put("status",true);
        resultMap.put("homePageParameter",objectMap);
        return resultMap;
    }

    private Map<String,Object> getTotalMoney(int type,Map<String,Object> parameterMap)throws Exception{
        String amtUnfreeze = (String) parameterMap.get("amtUnfreeze");
        String amtFreeze = (String) parameterMap.get("amtFreeze");
        String thirdPartyAccount = (String) parameterMap.get("thirdPartyAccount");
        Double servicePoints = (Double) parameterMap.get("servicePoints");
        BigDecimal amtPaid = (BigDecimal) parameterMap.get("amtPaid");
        amtUnfreeze = Constant.toOr(amtUnfreeze, Constant.toReadPro("orKey"), "decrypt");
        amtFreeze = Constant.toOr(amtFreeze, Constant.toReadPro("orKey"), "decrypt");
        parameterMap.remove("amtUnfreeze");
        parameterMap.remove("amtFreeze");
        parameterMap.remove("thirdPartyAccount");
        parameterMap.remove("amtPaid");
        if (type==1){
            parameterMap.put("preDeposit",amtUnfreeze);
            parameterMap.put("freezePreDeposit",amtFreeze);
            parameterMap.put("amtPaid",amtPaid.toString());
            if (Constant.toEmpty(thirdPartyAccount)) {
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(thirdPartyAccount);
                Object zfb = jsonObject.get("zfb");
                parameterMap.put("thirdPartyAccount",zfb);
            }else{
                parameterMap.put("thirdPartyAccount","");
            }
        }
        if (servicePoints != null) {
            BigDecimal b = new BigDecimal(servicePoints);
            BigDecimal bigDecimal = b.setScale(1, BigDecimal.ROUND_DOWN);
            parameterMap.put("servicePoints",bigDecimal);
        }
        BigDecimal bigDecimal = new BigDecimal(amtUnfreeze);
        bigDecimal=bigDecimal.add(new BigDecimal(amtFreeze));
        parameterMap.put("totalCount",bigDecimal.toString());
        return parameterMap;
    }
    /**
     * @param request       修改为已读
     */
    @Transactional
    public Map<String,Object> messageRead(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status",false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        String token = request.getParameter(CommonField.TOKEN);
        String messageId;
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            messageId = Constant.toJsonValue(jsonObject, "messageId");
        }else{
            messageId = request.getParameter(CommonField.TOKEN);
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,"token失效!");
            return resultMap;
        }
        if (Constant.toEmpty(messageId)) {
            CbhMessage message = messageMapper.selectByPrimaryKey(Integer.valueOf(messageId));
            if (message != null) {
                Integer type = message.getType();
                if (type == 21) {
                    resultMap.put("status",true);
                    return resultMap;
                }
                message.setIsRead(3);
                messageMapper.updateByPrimaryKey(message);
                resultMap.put("status",true);
                return resultMap;
            }
            resultMap.put(CommonField.RESULTMESSAGE,"消息不存在!");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,"消息id不能为空!");
        return resultMap;
    }

    /**
     * @param request       消息列表
     */
    public Map<String,Object> messageList(HttpServletRequest request)throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status",false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        String token = request.getParameter(CommonField.TOKEN);
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,"token失效!");
            return resultMap;
        }
        List<Map<String, Object>> messageList = messageMapper.selectByToken(token);
        if (messageList != null&&messageList.size()>0&&messageList.get(0)!=null) {
//            for (int i = 0; i < messageList.size(); i++) {
//                Map<String, Object> stringObjectMap = messageList.get(i);
//                Date foundDate = (Date) stringObjectMap.get("chaining");
//                String formatDate = DateUtils.formatDate(foundDate, DateUtils.FORMAT_HM);
//                stringObjectMap.put("chaining",formatDate);
//                String licensePlateNumber = (String) stringObjectMap.get("licensePlateNumber");
//                Object score = stringObjectMap.get("score");
//                Object statusEvent = stringObjectMap.get("statusEvent");
//                Integer type = (Integer) stringObjectMap.get("type");
//                String eventNo = (String) stringObjectMap.get("eventNo");
//                String img = (String) stringObjectMap.get("img");
//                stringObjectMap.put("licensePlateNumber",getStringName(licensePlateNumber));
//                stringObjectMap.put("img",getStringName(img));
//                if (score == null) {
//                    stringObjectMap.put("score", 0);
//                }
//
//                if (Constant.toEmpty(eventNo)) {
//                    if(type>100){
//                        CbhOrder order = cbhOrderMapper.selectByEventNo(eventNo);
//                        stringObjectMap.put("statusEvent", order.getStatus());
//                    }else{
//                        if (statusEvent == null) {
//                            stringObjectMap.put("statusEvent", 0);
//                        }
//                    }
//                }else{
//                    stringObjectMap.put("statusEvent", 0);
//                    stringObjectMap.put("eventNo", "");
//                }
//
//            }
            messageList.forEach((Map<String, Object> a) ->{
                a.put("chaining", DateUtils.formatDate((Date) a.get("chaining"), DateUtils.FORMAT_HM));
                if (!a.containsKey("eventNo")) {
                    a.put("statusEvent", 0);
                    a.put("eventNo", "");
                }else{
                    if((Integer)a.get("type")>100){
                    CbhOrder order = cbhOrderMapper.selectByEventNo(a.get("eventNo").toString());
                    a.put("statusEvent", order.getStatus());
                    }else{
                        if (a.get("statusEvent") == null) {
                            a.put("statusEvent", 0);
                        }
                    }
                }
                if (!a.containsKey("score")) {
                    a.put("score", 0);
                }
                List<String> stringList = Arrays.asList(new String[]{"img", "licensePlateNumber"});
                stringList.forEach(b->{
                    if (!a.containsKey(b)) {
                        a.put(b,"");
                    }
                });
            });
        }
        resultMap.put("status",true);
        resultMap.put("messageList",messageList);
        return resultMap;
    }
    /**
     * @param request       扣分记录
     */
    public Map<String,Object> servicePointsRecord(HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put(CommonField.RESULTMESSAGE, "");
        String token = request.getParameter(CommonField.TOKEN);
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business == null) {
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE, "token失效!");
            return resultMap;
        }
        List<Map<String, Object>> messageList = messageMapper.selectServicePointsByToken(token);
        resultMap.put("messageList",messageList);
        resultMap.put("status",true);
        List<CbhMaintenanceshop> maintenanceshops = maintenanceshopMapper.getByToken(token);
        CbhMaintenanceshop maintenanceshop = maintenanceshops.get(0);
        Double servicePoints = maintenanceshop.getServicePoints();
        if (messageList != null && messageList.size() > 0 && messageList.get(0) != null) {
            for (int i = 0; i < messageList.size(); i++) {
                Map<String, Object> stringObjectMap = messageList.get(i);
                Date foundDate = (Date) stringObjectMap.get("chaining");
                String formatDate = DateUtils.formatDate(foundDate, DateUtils.FORMAT_HM);
                stringObjectMap.put("chaining", formatDate);
                String eventNo = (String) stringObjectMap.get("eventNo");
                String img = (String) stringObjectMap.get("img");
                stringObjectMap.put("eventNo", getStringName(eventNo));
                stringObjectMap.put("img", getStringName(img));
            }
        }
        resultMap.put("servicePoints",servicePoints);
        return resultMap;
    }

    private static Integer getServicePoints(Map<String, Object> stringObjectMap){
        Integer score = (Integer) stringObjectMap.get("score");
        return score;
    }
    /**
     * @param request       我的总额接口
     */
    public Map<String,Object> totalAmount(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put(CommonField.RESULTMESSAGE, "");
        String token = request.getParameter(CommonField.TOKEN);
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business == null) {
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE, "token失效!");
            return resultMap;
        }
        Map<String,Object> homePage = businessMapper.getHomePageParameter(token);
        Map<String, Object> objectMap = getTotalMoney(1,homePage);

        resultMap.put("status", true);
        resultMap.put("totalAmount", objectMap);
        return resultMap;
    }
    /**
     * @param request       开始维修
     */
    @Transactional
    public Map<String,Object> startMaintenance(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put(CommonField.RESULTMESSAGE, "");
        String orderNo =null;
        String id =null;
        String token = request.getParameter(CommonField.TOKEN);
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            orderNo = Constant.toJsonValue(jsonObject, "orderNo");
            id = Constant.toJsonValue(jsonObject, "id");
        }else{
            orderNo = request.getParameter("orderNo");
            id = request.getParameter("id");
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business == null) {
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE, "token失效!");
            return resultMap;
        }
        if ("0".equals(id)){
            resultMap.put(CommonField.RESULTMESSAGE,"对不起,请先添加员工!");
            return resultMap;
        }
        if (Constant.toEmpty(orderNo)&&Constant.toEmpty(id)) {
            CbhEvent event = eventMapper.selectByEventNo(orderNo);
            if (event != null) {
                event.setStatusEvent(52);
                eventMapper.updateByPrimaryKey(event);
                cbhEventRepair repair = repairMapper.selectByEventNo(orderNo);
                if (repair != null) {
                    repair.setRepairmanId(Integer.valueOf(id));
                    repair.setTimeBegin(DateUtils.getDateTime());
                    repairMapper.updateByPrimaryKey(repair);
                }else{
                    cbhEventRepair eventRepair = new cbhEventRepair();
                    eventRepair.setEventNo(orderNo);
                    eventRepair.setRepairmanId(Integer.valueOf(id));
                    eventRepair.setTimeBegin(DateUtils.getDateTime());
                    repairMapper.insert(eventRepair);
                }
                resultMap.put("status", true);
                return resultMap;
            }
            resultMap.put(CommonField.RESULTMESSAGE, "订单号不存在");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE, "订单号和维修人员不能为空");
        return resultMap;
    }
    /**
     * @param request       提交维修
     */
    @Transactional
    public Map<String,Object> submitMaintenance(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status",false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        String orderNo =null;
        String maintenanceImg =null;//定损照片
        String maintenanceDescription =null;//定损描述
        String token = request.getParameter(CommonField.TOKEN);
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            orderNo = Constant.toJsonValue(jsonObject, "orderNo");
            maintenanceImg = Constant.toJsonValue(jsonObject, "maintenanceImg");
            maintenanceDescription = Constant.toJsonValue(jsonObject, "maintenanceDescription");
        }else{
            orderNo = request.getParameter("orderNo");
            maintenanceImg = request.getParameter("maintenanceImg");
            maintenanceDescription = request.getParameter("maintenanceDescription");

        }
        CbhUserBusiness business = validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,"token失效!");
            return resultMap;
        }
        if (Constant.toEmpty(orderNo)) {
            CbhEvent event = eventMapper.selectByEventNo(orderNo);
            if (event != null) {
                if (Constant.toEmpty(maintenanceImg)&&Constant.toEmpty(maintenanceDescription)) {
                    cbhEventRepair repair = repairMapper.selectByEventNo(orderNo);
                    if (repair != null) {
                        Integer statusEvent = event.getStatusEvent();
                        if (statusEvent==61){
                            resultMap.put(CommonField.RESULTMESSAGE,"对不起,该订单已经提交维修!");
                            return resultMap;
                        }
                        event.setStatusEvent(61);
                        eventMapper.updateByPrimaryKey(event);
                        repair.setTimeEnd(DateUtils.getDateTime());
                        repair.setImg(maintenanceImg);
                        repair.setDescription(maintenanceDescription);
                        repairMapper.updateByPrimaryKey(repair);

                        //消息
                        CbhMessage messageList = new CbhMessage();
                        Integer carId = event.getCarId();
                        CbhCar cbhCar = carMapper.selectByPrimaryKey(carId);
                        String licensePlateNumber=null;
                        if (cbhCar != null) {
                            licensePlateNumber = cbhCar.getLicensePlateNumber();
                            String content = licensePlateNumber +"的救助订单维修完成，请及时处理";
                            messageList.setContent(content);
                            messageList.setEventNo(orderNo);
                            messageList.setLicensePlateNumber(licensePlateNumber);
                        }
                        Integer customerId = event.getCustomerId();
                        messageList.setCustomerId(customerId);
                        messageList.setType(41);
                        messageList.setTitle("救助消息");
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
                                CbhMaintenanceshop maintenanceshop = maintenanceshopMapper.selectByPrimaryKey(event.getMaintenanceshopId());
                                result1.put("address", maintenanceshop.getAddress());
                                result1.put("tel", maintenanceshop.getTel());
                                weiXinUtils.sendTemplate(3, result1);
                           }
                        }
                        Scheduler sche = schedulerFactory.getScheduler();
                        QuartzUtils.removeJob(sche, "takeCar_"+orderNo);
                        resultMap.put("status",true);
                        return resultMap;
                    }
                    resultMap.put(CommonField.RESULTMESSAGE,"请先选择维修人");
                    return resultMap;
                }
                resultMap.put(CommonField.RESULTMESSAGE,"必填项不能为空");
                return resultMap;
            }
            resultMap.put(CommonField.RESULTMESSAGE,"订单号不存在");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,"订单号不能为空");
        return resultMap;
    }

    /**
     * @param request       我的
     */
    public Map<String,Object> myDetails(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put(CommonField.RESULTMESSAGE, "");
        String token = request.getParameter(CommonField.TOKEN);
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business == null) {
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE, "token失效!");
            return resultMap;
        }
        Map<String, Object> parameterMap = businessMapper.getMyDetails(token);
        Map<String, Object> objectMap = getTotalMoney(0,parameterMap);

        String logo = (String) objectMap.get("logo");
        String name = (String) objectMap.get("name");
        String posterList = (String) objectMap.get("posterList");
        System.out.println("+++++++++++++++service+++" + posterList);
        Integer type = (Integer) objectMap.get("type");
        objectMap.remove("type");
        BigDecimal commentPoints = (BigDecimal) objectMap.get("commentPoints");
        if (Constant.toEmpty(logo)) {
            objectMap.put("logo",CommonField.getMaintenanceShopImg(0,logo));
        }else{
            objectMap.put("logo",CommonField.getMaintenanceShopImg(0,"default_logo.png"));
        }
        if (type==20) posterList=null;
        System.out.println("进入service" + getImgList(2,3,posterList));
        objectMap.put("posterList",getImgList(2,3,posterList));
//        String[] s={"http://chevhuzhu.oss-cn-beijing.aliyuncs.com/event/maintenance/0ed1e76a88674fac9333d898ea4e31dd.jpg?Expires=1524714014&OSSAccessKeyId=LTAIocJnw9YCKcBy&Signature=iQ46tx6Q1u3zMgJSOLenllHyFuE%3D",
//                "http://chevhuzhu.oss-cn-beijing.aliyuncs.com/event/maintenance/d2cb808b19424193b10a4982dec28a53.jpg?Expires=1524714014&OSSAccessKeyId=LTAIocJnw9YCKcBy&Signature=Yr5KdTGSqJwvKhNdiku0mqC%2F3GE%3D",
//                "http://chevhuzhu.oss-cn-beijing.aliyuncs.com/event/maintenance/71cfa20b3b0a454bab35190ddee5eab8.jpg?Expires=1524714014&OSSAccessKeyId=LTAIocJnw9YCKcBy&Signature=vMSHcM0sDP7Jvgvy8wzCqYnaHz8%3D"};
//        objectMap.put("posterList",s);
        objectMap.put("name",getStringName(name));
        if (commentPoints == null) {
            objectMap.put("commentPoints",5.0);
        }else{
            objectMap.put("commentPoints",commentPoints.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        objectMap.remove("complaint");
        resultMap.put("status", true);
        resultMap.put("myDetails", objectMap);
        return resultMap;
    }

    private int getServicePoint(Map<String, Object> objectMap){
        Object commentPoints = objectMap.get("commentPoints");
        BigDecimal servicePoints = (BigDecimal) objectMap.get("servicePoints");
        Long complaint = (Long) objectMap.get("complaint");
        int score=100;
        if (commentPoints != null) {
            score =new BigDecimal(commentPoints.toString()).multiply(new BigDecimal(20)).intValue();
        }
        if (servicePoints !=null){
            score = score-(servicePoints.intValue());
        }
        if (complaint !=null) {
            score = (int) (score-(complaint*2));
        }
        return score;
    }



    /**
     * @param request       设置提现密码
     */
    @Transactional
    public Map<String,Object> setWithdrawCashPassword(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put(CommonField.RESULTMESSAGE, "");
        String token = request.getParameter(CommonField.TOKEN);
        String passWord =null;
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            passWord = Constant.toJsonValue(jsonObject, "passWord");
        }else{
            passWord = request.getParameter("passWord");
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business == null) {
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE, "token失效!");
            return resultMap;
        }
        if (isAdministrator(business)){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.PERMISSIONS);
            return resultMap;
        }
        if (Constant.toEmpty(passWord)) {
            passWord = MD5Util.getMD5Code(passWord);
            CbhAccount account = accountMapper.selectByToken(token,null);
            if (account != null) {
                account.setAccountPW(passWord);
                accountMapper.updateByPrimaryKey(account);
                resultMap.put("status", true);
                return resultMap;
            }
            resultMap.put(CommonField.RESULTMESSAGE,"您没有账户");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,"密码不能为空");
        return resultMap;
    }
    /**
     * @param request       绑定支付宝账户
     */
    @Transactional
    public Map<String,Object> bindingAlipay(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put(CommonField.RESULTMESSAGE, "");
        String token = request.getParameter(CommonField.TOKEN);
        String alipayAccount =null;
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            alipayAccount = Constant.toJsonValue(jsonObject, "alipayAccount");
        }else{
            alipayAccount = request.getParameter("alipayAccount");
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business == null) {
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE, "token失效!");
            return resultMap;
        }
        if (isAdministrator(business)){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.PERMISSIONS);
            return resultMap;
        }
        if (Constant.toEmpty(alipayAccount)) {
            CbhAccount account = accountMapper.selectByToken(token,null);
            if (account != null) {
                String thaccoundetail = account.getThirdPartyAccount();
                ThaccounDetail thaccounDetail;
                if (Constant.toEmpty(thaccoundetail)) {
                    thaccounDetail = JSON.parseObject(thaccoundetail, ThaccounDetail.class);
                }else{
                    thaccounDetail = new ThaccounDetail();
                }
                thaccounDetail.setZfb(alipayAccount);
                account.setThirdPartyAccount(JSON.toJSONString(thaccounDetail));
                accountMapper.updateByPrimaryKey(account);
                resultMap.put("status", true);
                return resultMap;
            }
            resultMap.put(CommonField.RESULTMESSAGE,"您没有账户");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,"支付宝账户不能为空");
        return resultMap;
    }
    private Boolean isAdministrator(CbhUserBusiness business){
        return business.getTypeUser()!=1?true:false;
    }

    /**
     * @param request       验证提现密码是否正确
     */
    public Map<String,Object> verifyPassword(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put(CommonField.RESULTMESSAGE, "");
        String token = request.getParameter(CommonField.TOKEN);
        String passWord =null;
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            passWord = Constant.toJsonValue(jsonObject, "passWord");
        }else{
            passWord = request.getParameter("passWord");
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business == null) {
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE, "token失效!");
            return resultMap;
        }
        if (isAdministrator(business)){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.PERMISSIONS);
            return resultMap;
        }
        if (Constant.toEmpty(passWord)) {
            CbhAccount account = accountMapper.selectByToken(token,MD5Util.getMD5Code(passWord));
            if (account != null) {
                resultMap.put("status",true);
                return resultMap;
            }
            resultMap.put(CommonField.RESULTMESSAGE,"密码错误!");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE,"密码不能为空");
        return resultMap;
    }
    /**
     * @param request       账户明细
     */
    public Map<String,Object> transactionDetails(HttpServletRequest request) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put(CommonField.RESULTMESSAGE, "");
        String token = request.getParameter(CommonField.TOKEN);
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business == null) {
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE, "token失效!");
            return resultMap;
        }

        List<Map<String,Object>> result = accountDetailMapper.selectByToken(token);
        List<Map<String,Object>> monneyList = accountDetailMapper.selectByYearAndMonth(token);
        if (result !=null&&monneyList !=null){
            for (int i = 0; i < monneyList.size(); i++) {
                List<Object> arrayList = new ArrayList<>();
                Map<String, Object> stringObjectMap1 = monneyList.get(i);
                Date createTime1 = (Date) stringObjectMap1.get("yearMonth");
//                    stringObjectMap1.put("revenue",revenue.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
                BigDecimal disbursement = (BigDecimal) stringObjectMap1.get("disbursement");
                stringObjectMap1.put("disbursement",disbursement);
//                    stringObjectMap1.put("disbursement",disbursement.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
                String formatDate = DateUtils.formatDate(createTime1, "yyyy年MM月");
                String formatDate2 = DateUtils.formatDate(createTime1, "yyyy-MM");
                stringObjectMap1.put("yearMonth", formatDate);
                if (result != null) {
                    for (int j = 0; j < result.size(); j++) {
                        Map<String, Object> stringObjectMap = result.get(j);
                        Date createTime = (Date) stringObjectMap.get("createTime");
                        String orderNo = (String) stringObjectMap.get("orderNo");
                        stringObjectMap.put("orderNo",getStringName(orderNo));
                        stringObjectMap.remove("createTime");
                        if (createTime !=null) {
                            String formatDate1 = DateUtils.formatDate(createTime, "yyyy-MM");
                            if (formatDate2.equals(formatDate1)){
                                String formatDate12 = DateUtils.formatDate1(createTime);
                                stringObjectMap.put("dateTime",formatDate12);
                                BigDecimal bigDecimal = (BigDecimal) stringObjectMap.get("amount");
                                String img = (String) stringObjectMap.get("img");
                                if (Constant.toEmpty(img)) {
                                    stringObjectMap.put("logoImg",CommonField.getMoneyDetailsLogoImg(img));
                                    stringObjectMap.remove("img");
                                }
//                                BigDecimal divide = bigDecimal.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                                stringObjectMap.put("amount",bigDecimal);
                                arrayList.add(stringObjectMap);
                            }
                        }

                    }
                }
                stringObjectMap1.put("transactionDetails", arrayList);

            }
        }
        resultMap.put("status", true);
        resultMap.put("transactionDetails", monneyList);
        return resultMap;
    }
    /**
     * @param request       店铺详情
     */
    public Map<String,Object> shopDetails(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put(CommonField.RESULTMESSAGE, "");
        String token = request.getParameter(CommonField.TOKEN);
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business == null) {
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE, "token失效!");
            return resultMap;
        }
        List<Map<String,Object>> maintenanceshopList = maintenanceshopMapper.selectByToken(token);
        if (maintenanceshopList != null&&maintenanceshopList.size()>0&&maintenanceshopList.get(0) !=null) {
            Map<String, Object> shopMap = maintenanceshopList.get(0);
            String logo = (String) shopMap.get("logo");
            String businessLicenseImg = (String) shopMap.get("businessLicenseImg");
            String name = (String) shopMap.get("name");
            String img = (String) shopMap.get("img");
            shopMap.remove("img");
            String businessHours = (String) shopMap.get("businessHours");
            String address = (String) shopMap.get("address");
            String describe = (String) shopMap.get("shopDescribe");
            String qrcodeImg = (String) shopMap.get("qrcodeImg");
            shopMap.remove("shopDescribe");
            Integer orderQuantity = (Integer) shopMap.get("orderQuantity");
            Integer ordersRadius = (Integer) shopMap.get("ordersRadius");
            shopMap.put("name",getStringName(name));
            shopMap.put("businessHours",getStringName(businessHours));
            shopMap.put("address",getStringName(address));
            shopMap.put("describe",getStringName(describe));
            if (ordersRadius == null) {
                shopMap.put("ordersRadius",-1);
            }
            if (orderQuantity == null) {
                shopMap.put("orderQuantity",-1);
            }
            if (Constant.toEmpty(logo)) {
                shopMap.put("logo",CommonField.getMaintenanceShopImg(0,logo));
            }else{
                shopMap.put("logo",CommonField.getMaintenanceShopImg(0,"default_logo.png"));
            }
            if (Constant.toEmpty(businessLicenseImg)) {
                shopMap.put("businessLicenseImg",CommonField.getMaintenanceShopImg(4,businessLicenseImg));
            }else{
                shopMap.put("businessLicenseImg","");
            }
            if (Constant.toEmpty(qrcodeImg)) {
                shopMap.put("qrcodeImg", CommonField.getMaintenanceShopImg(5, qrcodeImg));
            }else{
                shopMap.put("qrcodeImg","");
            }
            shopMap.put("name",getStringName(name));
            shopMap.put("imgList",getImgList(2,1,img));
            resultMap.put("status", true);
            resultMap.put("shopDetails", shopMap);
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE, "暂无店铺");
        return resultMap;
    }
    /**
     * @param request       编辑店铺
     */
    @Transactional
    public Map<String,Object> updateShops(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put(CommonField.RESULTMESSAGE, "");
        String token = request.getParameter(CommonField.TOKEN);
        String logoImg;
        String name;
        String orderQuantity;
        String ordersRadius;
        String address;
        String businessHours;
        String describe ;
        String businessLicenseImg;
        String shopImg;
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            logoImg = Constant.toJsonValue(jsonObject, "logoImg");
            name = Constant.toJsonValue(jsonObject, "name");
            orderQuantity = Constant.toJsonValue(jsonObject, "orderQuantity");
            ordersRadius = Constant.toJsonValue(jsonObject, "ordersRadius");
            address = Constant.toJsonValue(jsonObject, "address");
            businessHours = Constant.toJsonValue(jsonObject, "businessHours");
            describe = Constant.toJsonValue(jsonObject, "describe");
            businessLicenseImg = Constant.toJsonValue(jsonObject, "businessLicenseImg");
            shopImg = Constant.toJsonValue(jsonObject, "shopImg");
        }else{
            logoImg = request.getParameter("logoImg");
            name = request.getParameter("name");
            orderQuantity = request.getParameter("orderQuantity");
            ordersRadius = request.getParameter("ordersRadius");
            address = request.getParameter("address");
            businessHours = request.getParameter("businessHours");
            describe = request.getParameter("describe");
            businessLicenseImg = request.getParameter("businessLicenseImg");
            shopImg = request.getParameter("shopImg");
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business == null) {
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE, "token失效!");
            return resultMap;
        }
        List<CbhMaintenanceshop> maintenanceshopList = maintenanceshopMapper.getByToken(token);
        if (maintenanceshopList != null&&maintenanceshopList.size()>0&&maintenanceshopList.get(0) !=null) {
            CbhMaintenanceshop maintenanceshop = maintenanceshopList.get(0);
            if (Constant.toEmpty(name)&&Constant.toEmpty(orderQuantity)&&Constant.toEmpty(ordersRadius)&&Constant.toEmpty(address)&&
                    Constant.toEmpty(businessHours)&&Constant.toEmpty(describe)&&Constant.toEmpty(businessLicenseImg)
                    &&Constant.toEmpty(shopImg)) {
                if (Constant.toEmpty(logoImg)) {
                    maintenanceshop.setLogo(logoImg);
                }
                Map<String, Object> latitudeAndLongitude = GetLatAndLngByGaoDe.getLatitudeAndLongitude(address);
                if (latitudeAndLongitude != null) {
                    maintenanceshop.setLongitude(latitudeAndLongitude.get("longitude").toString());
                    maintenanceshop.setLatitude(latitudeAndLongitude.get("latitude").toString());;
                }
                maintenanceshop.setName(name);
                maintenanceshop.setOrderQuantity(Integer.valueOf(orderQuantity));
                maintenanceshop.setOrdersRadius(Integer.valueOf(ordersRadius));
                maintenanceshop.setAddress(address);
                maintenanceshop.setBusinessHours(businessHours);
                maintenanceshop.setShopDescribe(describe);
                maintenanceshop.setBusinessLicenseImg(businessLicenseImg);
                maintenanceshop.setImg(shopImg);
                maintenanceshopMapper.updateByPrimaryKey(maintenanceshop);
                resultMap.put("status", true);
                return resultMap;
            }
            resultMap.put(CommonField.RESULTMESSAGE, "必填项不能为空!");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE, "暂无店铺");
        return resultMap;
    }
    /**
     * @param request       添加员工
     */
    @Transactional
    public Map<String,Object> addOrUpdateEmployee(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put(CommonField.RESULTMESSAGE, "");
        String token = request.getParameter(CommonField.TOKEN);
        String employeeId;
        String logoImg;
        String name;
        String jobTitle;
        String workYear;
        String mobileNumber;
        String gender;
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            logoImg = Constant.toJsonValue(jsonObject, "logoImg");
            name = Constant.toJsonValue(jsonObject, "name");
            jobTitle = Constant.toJsonValue(jsonObject, "jobTitle");
            workYear = Constant.toJsonValue(jsonObject, "workYear");
            gender = Constant.toJsonValue(jsonObject, "gender");
            employeeId = Constant.toJsonValue(jsonObject, "employeeId");
            mobileNumber = Constant.toJsonValue(jsonObject, "mobileNumber");

        }else{
            logoImg = request.getParameter("logoImg");
            name = request.getParameter("name");
            jobTitle = request.getParameter("jobTitle");
            workYear = request.getParameter("workYear");
            gender = request.getParameter("gender");
            employeeId = request.getParameter("employeeId");
            mobileNumber = request.getParameter("mobileNumber");

        }
        CbhUserBusiness business = validationAccToken(token);
        if (business == null) {
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE, "token失效!");
            return resultMap;
        }
        List<CbhMaintenanceshop> maintenanceshopList = maintenanceshopMapper.getByToken(token);
        if (maintenanceshopList != null&&maintenanceshopList.size()>0&&maintenanceshopList.get(0) !=null) {
            CbhMaintenanceshop maintenanceshop = maintenanceshopList.get(0);
            Integer maintenanceshopId = maintenanceshop.getId();
            if (Constant.toEmpty(name)&&Constant.toEmpty(jobTitle)&&Constant.toEmpty(workYear)&&Constant.toEmpty(mobileNumber)&&
                    Constant.toEmpty(gender)&&Constant.toEmpty(logoImg)) {
                if (Constant.toEmpty(employeeId)) {
                    CbhMaintenanceshopEmployee employee = employeeMapper.selectByPrimaryKey(Integer.valueOf(employeeId));
                    if (employee != null) {
                        employee = getEmployee(employee, logoImg, name, jobTitle, workYear, mobileNumber, gender,maintenanceshopId);
                        employeeMapper.updateByPrimaryKey(employee);
                        resultMap.put("status", true);
                        return resultMap;
                    }
                    resultMap.put(CommonField.RESULTMESSAGE, "没有该员工!");
                    return resultMap;
                }
                CbhMaintenanceshopEmployee employee = new CbhMaintenanceshopEmployee();
                employee = getEmployee(employee, logoImg, name, jobTitle, workYear, mobileNumber, gender,maintenanceshopId);
                employeeMapper.insert(employee);
                resultMap.put("status", true);
                return resultMap;
            }
            resultMap.put(CommonField.RESULTMESSAGE, "必填项不能为空!");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE, "暂无店铺");
        return resultMap;
    }

    private CbhMaintenanceshopEmployee getEmployee(CbhMaintenanceshopEmployee employee,String logoImg,String name,
            String jobTitle, String workYear, String mobileNumber, String gender,Integer maintenanceshopId){
        employee.setName(name);
        employee.setJobTitle(jobTitle);
        employee.setWorkYear(Integer.valueOf(workYear));
        employee.setGender(Integer.valueOf(gender));
        employee.setTel(mobileNumber);
        employee.setMaintenanceshopId(maintenanceshopId);
        if (Constant.toEmpty(logoImg)) {
            employee.setImg(logoImg);
        }
        return employee;
    }
    /**
     * @param request       获得员工列表
     */
    public Map<String,Object> getEmployeeList(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put(CommonField.RESULTMESSAGE, "");
        String token = request.getParameter(CommonField.TOKEN);
        String employeeId;
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            employeeId = Constant.toJsonValue(jsonObject, "employeeId");
        }else{
            employeeId=request.getParameter("employeeId");
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business == null) {
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE, "token失效!");
            return resultMap;
        }
        List<CbhMaintenanceshop> maintenanceshopList = maintenanceshopMapper.getByToken(token);
        if (maintenanceshopList != null&&maintenanceshopList.size()>0&&maintenanceshopList.get(0) !=null) {
            CbhMaintenanceshop maintenanceshop = maintenanceshopList.get(0);
            Integer maintenanceshopId = maintenanceshop.getId();
               List<Map<String,Object>> employeeList = employeeMapper.getEmployeeList(maintenanceshopId,employeeId);
            if (employeeList != null&&employeeList.size()>0&&employeeList.get(0) !=null) {
                for(int i = 0; i < employeeList.size(); i++) {
                    Map<String, Object> objectMap = employeeList.get(i);
                    String img = (String) objectMap.get("img");
                    if (Constant.toEmpty(img)) {
//                        objectMap.put("img",getImgList(2,2,img));
                        objectMap.put("img",CommonField.getMaintenanceShopImg(2,img));
                    }else{
                        objectMap.put("img",CommonField.getMaintenanceShopImg(0,"default_logo.png"));
                    }
                }
            }
                resultMap.put("status", true);
                resultMap.put("EmployeeList", employeeList);
                return resultMap;
            }
        resultMap.put(CommonField.RESULTMESSAGE, "暂无店铺");
        return resultMap;
    }

    /**
     * @param request       删除员工
     */
    @Transactional
    public Map<String,Object> deleteEmployee(HttpServletRequest request) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put(CommonField.RESULTMESSAGE, "");
        String token = request.getParameter(CommonField.TOKEN);
        String employeeId;
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            employeeId = Constant.toJsonValue(jsonObject, "employeeId");
        }else{
            employeeId=request.getParameter("employeeId");
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business == null) {
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE, "token失效!");
            return resultMap;
        }
        if (isAdministrator(business)){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.PERMISSIONS);
            return resultMap;
        }
        List<CbhMaintenanceshop> maintenanceshopList = maintenanceshopMapper.getByToken(token);
        if (maintenanceshopList != null&&maintenanceshopList.size()>0&&maintenanceshopList.get(0) !=null) {
            if (Constant.toEmpty(employeeId)) {
                Integer id = Integer.valueOf(employeeId);
                CbhMaintenanceshopEmployee employee = employeeMapper.selectByPrimaryKey(id);
                if (employee != null) {
                    employee.setStatus(2);
                    employeeMapper.updateByPrimaryKey(employee);
                    resultMap.put("status", true);
                    return resultMap;
                }
                resultMap.put(CommonField.RESULTMESSAGE, "没有该员工");
                return resultMap;
            }
            resultMap.put(CommonField.RESULTMESSAGE, "用户id不能为空");
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE, "暂无店铺");
        return resultMap;
        
    }
    /**
     * @param request       意见反馈
     */
    public Map<String,Object> feedback(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put(CommonField.RESULTMESSAGE, "");
        String token = request.getParameter(CommonField.TOKEN);
        String content;
        String type;
        String img;
        String mobileNumber;
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            type = Constant.toJsonValue(jsonObject, "type");
            img = Constant.toJsonValue(jsonObject, "img");
            mobileNumber = Constant.toJsonValue(jsonObject, "mobileNumber");
            content = Constant.toJsonValue(jsonObject, "content");

        }else{
            type = request.getParameter("type");
            img = request.getParameter("img");
            mobileNumber = request.getParameter("mobileNumber");
            content = request.getParameter("content");

        }
        CbhUserBusiness business = validationAccToken(token);
        if (business == null) {
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE, "token失效!");
            return resultMap;
        }
        if (Constant.toEmpty(type)) {
            CbhSuggestions suggestions = new CbhSuggestions();
            suggestions.setTypeQuestion(Integer.valueOf(type));
            suggestions.setImg(img);
            suggestions.setContent(content);
            suggestions.setTel(mobileNumber);
            suggestions.setSuggestionsId(business.getId());
            cbhSuggestionsMapper.insert(suggestions);
            resultMap.put("status", true);
            return resultMap;
        }
        resultMap.put(CommonField.RESULTMESSAGE, "反馈问题不能为空!");
        return resultMap;
    }

    /**
     * @param request      支付宝提现
     */
    @Transactional
    public Map<String,Object> cashWithdrawal(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put(CommonField.RESULTMESSAGE, "");
        String token = request.getParameter(CommonField.TOKEN);
        String money;
        String alipayAccount;
        if (!Constant.toEmpty(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            money = Constant.toJsonValue(jsonObject, "money");
            alipayAccount = Constant.toJsonValue(jsonObject, "alipayAccount");
        }else{
            money = request.getParameter("money");
            alipayAccount = request.getParameter("alipayAccount");
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business == null) {
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE, "token失效!");
            return resultMap;
        }

        try {
            //  根据Token判断Redis中是否存在
            if (null != redisUtil.getStr("CASH_" + token)){
                resultMap.put(CommonField.RESULTMESSAGE, "您还有提现操作正在处理中，请1分钟之后再试!");
                return resultMap;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (isAdministrator(business)){
            resultMap.put(CommonField.RESULTMESSAGE,CommonField.PERMISSIONS);
            return resultMap;
        }
        if (!Constant.toEmpty(money)||!Constant.toEmpty(alipayAccount)) {
            resultMap.put(CommonField.RESULTMESSAGE, "提现金额和支付宝账户不能为空!");
            return resultMap;
        }
        // 提现金额必须大于
        if (!(new BigDecimal(money).compareTo(new BigDecimal(0.099999)) > 0)) {
            resultMap.put(CommonField.RESULTMESSAGE, "支付宝最少提现0.1元!");
            return resultMap;
        }
        //账户
        CbhAccount account = accountMapper.selectByToken(token,null);
        if (account == null) {
            resultMap.put(CommonField.RESULTMESSAGE, "账户不存在!");
            return resultMap;
        }
        String amtUnfreeze = account.getAmtUnfreeze();
        amtUnfreeze = Constant.toOr(amtUnfreeze, Constant.toReadPro("orKey"), "decrypt");
        BigDecimal bs =new BigDecimal(amtUnfreeze);
        // 提现金额是大于总额
        if (!(new BigDecimal(money).compareTo(bs) <= 0)) {
            resultMap.put(CommonField.RESULTMESSAGE, "超出可以提现金额!");
            return resultMap;
        }
        String thaccoundetail = account.getThirdPartyAccount();
        if (!Constant.toEmpty(thaccoundetail)) {
            resultMap.put(CommonField.RESULTMESSAGE, "你还没绑定账户账户!");
            return resultMap;
        }

        if (Constant.toEmpty(thaccoundetail)) {
            ThaccounDetail thaccounDetail = JSON.parseObject(thaccoundetail, ThaccounDetail.class);
            String zfb = thaccounDetail.getZfb();
            if (!Constant.toEmpty(zfb)) {
                resultMap.put(CommonField.RESULTMESSAGE, "支付宝账户不存在!");
                return resultMap;
            }
            if (!alipayAccount.equals(zfb)) {
                resultMap.put(CommonField.RESULTMESSAGE, "支付宝账号错误!");
                return resultMap;
            }

        }
        try {
            // 准备正式执行提现的操作,向Redis中设置提现时间
            redisUtil.set("CASH_" + token,token,60);
        }catch (Exception e){
            e.printStackTrace();
        }
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "JSON", "utf-8", AlipayConfig.alipay_public_key,AlipayConfig.sign_type);
        AlipayFundTransToaccountTransferRequest request1 = new AlipayFundTransToaccountTransferRequest();
        JsonObject object=new JsonObject();
        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        object.addProperty("out_biz_no", out_trade_no);
        object.addProperty("payee_type", "ALIPAY_LOGONID");
        object.addProperty("payee_account", alipayAccount);
        object.addProperty("amount", money);
        object.addProperty("remark", "提现申请");
        request1.setBizContent(object.toString());
        AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request1);
        String string = response.getBody().toString();
        if(response.isSuccess()){
            //     * (bs:总金额变动值,operation:操作+/-,accountId:账户id alreadyPresented 提现金额 提现的时候必写
            // detailType 总额消息类型 content 总额消息文本 orderNo 总额消息订单号 customerId 移动用户id)
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put("bs", money);
            parameterMap.put("operation", "-");
            parameterMap.put("accountId", account.getId());
            parameterMap.put("alreadyPresented", money);
            parameterMap.put("detailType", 42);
            parameterMap.put("content", CommonField.ALIPAY_CASH_WITHDRAWAL(money));
            parameterMap.put("businessId", business.getId());
            parameterMap.put("img", "b_tixian.png");
            int i = upAccountInfo(parameterMap);
            int i1 = sendMessage(parameterMap);
            if (i==1&&i1==1){
                System.out.println("*********************调用成功");
            }
            //数据统计
            updateFoundationRealPaid(new BigDecimal(money));
            resultMap.put(CommonField.RESULTMESSAGE, "提现成功!");
            resultMap.put("status", true);
            return resultMap;
        } else {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(string);
            com.alibaba.fastjson.JSONObject transferResponse = JSON.parseObject(jsonObject.get("alipay_fund_trans_toaccount_transfer_response").toString());
            resultMap.put(CommonField.RESULTMESSAGE, transferResponse.get("sub_msg").toString());
            return resultMap;
        }
    }

    @Transactional
    public void updateFoundationRealPaid (BigDecimal money)throws Exception{
        Foundation foundation =  foundationMapper.findEntitySingle(new HashMap<>());
        Map<String,Object> upfMap = new HashMap<>();
        upfMap.put("realPaid", foundation.getRealPaid().add(money));
        upfMap.put("versions", foundation.getVersions());
        Integer integer = foundationMapper.updateData(upfMap);
        if (integer==0)
            updateFoundationRealPaid(money);
        else
            System.out.println("数据修改成功");
        return;

    }


    /**
     * 账户信息统一 修改
     * @param parameterModelMap (bs:总金额变动值,operation:操作+/-,accountId:账户id,content:明细内容,detailType:明细类型,reAmt:返佣金额
     *                          orderReward 订单用户返佣  spreadReward 上级代理奖励 superiorAccountId 上级账户id  superDetailType 上级账户明细类型 51
     *                         )
     * @return -1:账户余额不足,1.更改成功
     * @throws Exception
     */
    @Transactional
    public int upAccountInfo(Map<String,Object> parameterModelMap) throws Exception{
        int reInt = 0;
        String accountId = parameterModelMap.get("accountId").toString();
        Integer detailType = (Integer) parameterModelMap.get("detailType");
        synchronized (accountId){
            CbhAccount account = accountMapper.selectByPrimaryKey(Integer.valueOf(accountId));
            double paramBs = Double.parseDouble(parameterModelMap.get("bs").toString());//变动金额
            String amtUnfreeze = account.getAmtUnfreeze();
            amtUnfreeze = Constant.toOr(amtUnfreeze, Constant.toReadPro("orKey"), "decrypt");
            double bsData = Double.parseDouble(amtUnfreeze);//原有金额
            String operation = parameterModelMap.get("operation").toString();
            if(operation.equals("-") && paramBs > bsData){
                reInt = -1;
            }else{
                Double comput = Constant.toComput(bsData, paramBs, operation);
                String presented = parameterModelMap.get("alreadyPresented").toString();
                if(Constant.toEmpty(presented)) {
                    double alreadyPresented = Double.parseDouble(presented);//已提现金额
                    comput = Constant.toComput(bsData, alreadyPresented, operation);
                    BigDecimal amtPaid = account.getAmtPaid();
                    double toComput = Constant.toComput(amtPaid.doubleValue(), alreadyPresented, "+");
                    account.setAmtPaid(new BigDecimal(toComput));
                }
                amtUnfreeze = Constant.toOr(comput.toString(), Constant.toReadPro("orKey"), "encrypt");
                account.setAmtUnfreeze(amtUnfreeze);
                reInt = accountMapper.updateByPrimaryKey(account);//账户变动
                CbhAccountDetail accountDetail = new CbhAccountDetail();
                accountDetail.setAccountId(Integer.valueOf(accountId));
                accountDetail.setType(detailType);
                accountDetail.setAmt(new BigDecimal(paramBs));
                accountDetail.setContent(parameterModelMap.get("content").toString());
                accountDetail.setImg(parameterModelMap.get("img").toString());
                accountDetailMapper.insert(accountDetail); //账户变动明细 superiorId
            }
        }
        return reInt;
    }

    /**
     * 账户信息统一 修改
     * @param parameterModelMap
     * (bs:总金额变动值,operation:操作+/-,accountId:账户id
     * detailType 总额消息类型 content 总额消息文本 orderNo 总额消息订单号 customerId 移动用户id
     * superiorId 上级用户 superContent 上级账户明细类型)
     * @return -1:账户余额不足,0.更改成功
     * @throws Exception
     */
    @Transactional
    public int sendMessage(Map<String,Object> parameterModelMap) throws Exception{
        int reInt = 0;
        double paramBs = Double.parseDouble(parameterModelMap.get("bs").toString());//变动金额
        String operation = parameterModelMap.get("operation").toString();
        //移动会员id
        Integer businessId = (Integer) parameterModelMap.get("businessId");
        //金额
        String txMoney = String.valueOf(paramBs);
        CbhUserBusiness business = businessMapper.selectByPrimaryKey(businessId);
        if (business != null) {
            String openid = business.getOpenId();
            String androidDeviceId = business.getAndroidDeviceId();
            String iosDeviceId = business.getIosDeviceId();
            if ("-".equals(operation)) {
                String content = CommonField.ALIPAY_CASH_WITHDRAWAL(txMoney);
                //消息
                CbhMessage messageList = new CbhMessage();
                messageList.setBusinessId(businessId);
                messageList.setType(3);
                messageList.setTitle("总额变动");
                messageList.setContent(content);
                messageMapper.insert(messageList);
                //微信推送
                if (Constant.toEmpty(openid)) {
                    Map<String, String> result1 = new HashMap<>();
                    result1.put("openid", openid.toString());
                    result1.put("keyword1", Constant.toStrTime());
                    result1.put("keyword2", "支付宝到账");
                    result1.put("keyword3", txMoney);
                    result1.put("keyword4", "0.00");
                    result1.put("keyword5", txMoney);
                    weiXinUtils.sendTemplate(3, result1);
                }
                //移动推送
                if (Constant.toEmpty(androidDeviceId) || Constant.toEmpty(iosDeviceId)) {
                    Map<String, Object> parameterHashMap = new HashMap<>();
                    parameterHashMap.put("orderNo", "");
                    parameterHashMap.put("orderStatus", "");
                    parameterHashMap.put("type", 3);
                    if (Constant.toEmpty(androidDeviceId)) {
                        SmsDemo.mobilePushMessage(20, 3, androidDeviceId, content, parameterHashMap);
                    }
                    if (Constant.toEmpty(iosDeviceId)) {
                        SmsDemo.mobilePushMessage(10, 3, iosDeviceId, content, parameterHashMap);
                    }
                }
                //短信
                SmsDemo.sendSms(41, Constant.toReadPro("financeMobileNumber"), txMoney);
            }
        }
        return reInt;
    }

    /**
     *
     * @param eventNo       订单号
     * @param score         每次加减分数
     * @param operation     加分传 +  减分传 -
     * @return
     * @throws Exception
     */
    @Transactional
    public int servicePoints(String eventNo,Double score,String operation) throws Exception{
        int reInt = 0;
        CbhEvent event = eventMapper.selectByEventNo(eventNo);
        if (event != null) {
            Integer maintenanceshopId = event.getMaintenanceshopId();
            synchronized (maintenanceshopId){
                CbhMaintenanceshop maintenanceshop = maintenanceshopMapper.selectByPrimaryKey(maintenanceshopId);
                Double servicePoints = maintenanceshop.getServicePoints();
                BigDecimal b1 = new BigDecimal(Double.toString(servicePoints));
                BigDecimal b2 = new BigDecimal(Double.toString(score));
                if (operation.equals("+")) {
                    servicePoints = b1.add(b2).doubleValue();
                    if (servicePoints>100){
                        maintenanceshop.setServicePoints(100.0);
                    }else{
                        maintenanceshop.setServicePoints(servicePoints);
                    }
                }else{
                    if (servicePoints<80){
                        maintenanceshop.setServicePoints(servicePoints);
                    }else{
                        servicePoints = b1.subtract(b2).doubleValue();
                        maintenanceshop.setServicePoints(servicePoints);
                    }
                }
                reInt = maintenanceshopMapper.updateByPrimaryKey(maintenanceshop);
            }
        }
        return reInt;
    }
    /**
     * @param request      查看加入互助名单
     */
    public Map<String,Object> helpEachOtherList(HttpServletRequest request)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status",false);
        resultMap.put(CommonField.RESULTMESSAGE,"");
        String page =null;
        String selectField =null;
        String token = request.getParameter(CommonField.TOKEN);
        if (StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSONPost.readJSONStringFromRequestBody(request);
            token = Constant.toJsonValue(jsonObject, CommonField.TOKEN);
            page = Constant.toJsonValue(jsonObject, "page");
            selectField = Constant.toJsonValue(jsonObject, "selectField");
        }else{
            page = request.getParameter("page");
            selectField = request.getParameter("selectField");
        }
        CbhUserBusiness business = validationAccToken(token);
        if (business==null){
            resultMap.put(CommonField.VALIDATION_CODE, CommonField.TOKEN_FAILURE);
            resultMap.put(CommonField.RESULTMESSAGE,"token失效!");
            return resultMap;
        }
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("toKen",token);
        parameterMap.put("page",null);
        parameterMap.put("selectField",selectField);
        List<Map<String,Object>> helpEachOtherList = businessMapper.helpEachOtherList(parameterMap);
        int size = helpEachOtherList.size();
        Integer pages=0;
        if (page !=null&&!page.equals("")&&!page.equals("0")) {
            pages=(Integer.valueOf(page)-1)*20;
        }
        if (Constant.toEmpty(page)) {
            if (helpEachOtherList != null&&helpEachOtherList.size()>0) {
                helpEachOtherList = helpEachOtherList.stream().skip(pages).limit(20).collect(Collectors.toList());
            }
//            parameterMap.put("page",pages);
//            helpEachOtherList = businessMapper.helpEachOtherList(parameterMap);
        }
        resultMap.put("totalNumber",size);
        if (helpEachOtherList != null&&helpEachOtherList.size()>0&&helpEachOtherList.get(0) != null) {
            for(int i = 0; i < helpEachOtherList.size(); i++) {
                Map<String, Object> objectMap = helpEachOtherList.get(i);
                String img = (String) objectMap.get("img");
                String name = (String) objectMap.get("name");
                objectMap.put("name", getStringName(name));
                if (Constant.toEmpty(img)) {
                    if(!img.contains("http://")){
                        objectMap.put("img",CommonField.getCustomerImgUrl(img));
                    }
                }else{
                    objectMap.put("img",CommonField.getMaintenanceShopImg(0,"default_logo.png"));
                }
            }
        }
        resultMap.put("page", getPage(helpEachOtherList,page,getTotalPage(size)));
        resultMap.put("helpEachOtherList",helpEachOtherList);
        resultMap.put("status",true);
        return resultMap;
    }
}
