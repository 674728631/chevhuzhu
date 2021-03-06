package com.zccbh.demand.service.appBackstage;

import com.alibaba.druid.sql.visitor.functions.Right;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.controller.quartz.CommentJob;
import com.zccbh.demand.controller.quartz.QuartzUtils;
import com.zccbh.demand.mapper.business.MaintenanceshopMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.customer.ComplaintMapper;
import com.zccbh.demand.mapper.event.EventApplyMapper;
import com.zccbh.demand.mapper.event.EventMapper;
import com.zccbh.demand.mapper.event.EventReceivecarMapper;
import com.zccbh.demand.mapper.merchants.CbhMessageMapper;
import com.zccbh.demand.mapper.order.OrderMapper;
import com.zccbh.demand.mapper.system.RightsMapper;
import com.zccbh.demand.mapper.user.MessageBackstageMapper;
import com.zccbh.demand.mapper.user.ModelMapper;
import com.zccbh.demand.mapper.user.UserAdminMapper;
import com.zccbh.demand.pojo.system.Rights;
import com.zccbh.demand.pojo.user.MessageBackstage;
import com.zccbh.demand.pojo.user.UserAdmin;
import com.zccbh.demand.service.basic.DictionaryService;
import com.zccbh.demand.service.customer.ComplaintService;
import com.zccbh.demand.service.customer.MessageBackstageService;
import com.zccbh.demand.service.customer.MessageService;
import com.zccbh.demand.service.event.DistributionOrder;
import com.zccbh.demand.service.event.EventApplyFailService;
import com.zccbh.demand.service.event.EventApplyService;
import com.zccbh.demand.service.event.EventAssertService;
import com.zccbh.demand.service.event.EventService;
import com.zccbh.demand.service.foundation.FoundationService;
import com.zccbh.demand.service.system.ChartService;
import com.zccbh.demand.service.system.MenuService;
import com.zccbh.demand.service.user.UserAdminService;
import com.zccbh.demand.service.weChat.ThreadCache;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.test.exception.CustomException;
import com.zccbh.util.base.*;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.collect.MD5Util;
import com.zccbh.util.qrcode.QRCodeUtil;
import com.zccbh.util.uploadImg.UploadFileUtil;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;


/**
 * 
 * @Author: luoyuangang
 * @JDK version used:       	1.8
 * @Modified By:            	<修改人中文名或拼音缩写>
 * @Modified Date:          	<修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: 	<修改原因描述>
 * @create 2018-08-06 11:45
 **/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AppBackstageService {
    @Autowired
    private SchedulerFactoryBean schedulerFactory;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WeiXinUtils weiXinUtils;
    @Autowired
    private UserAdminMapper adminMapper;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CbhMessageMapper cbhMessageMapper;
    @Autowired
    private MessageBackstageMapper messageBackstageMapper;
    @Autowired
    private DistributionOrder distributionOrder;
    @Autowired
    private ComplaintMapper complaintMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MaintenanceshopMapper maintenanceshopMapper;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EventApplyService eventApplyService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private EventAssertService eventAssertService;

    @Autowired
    EventReceivecarMapper eventReceivecarMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private FoundationService foundationService;

    @Autowired
    private ChartService chartService;
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private RightsMapper rightsMapper;
    
    @Autowired
    private MessageBackstageService messageBackstageService;
    
    @Autowired
    private EventApplyFailService eventApplyFailService;
    
    @Autowired
    private EventApplyMapper eventApplyMapper;

    /**         登录
     * @return  method = RequestMethod.POST
     * @throws
     */
    @Transactional
    public Object appLogin()throws CustomException {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        String userName = (String) jsonObject.get("userName");
        String passWord = (String) jsonObject.get(CommonField.PASS_WORD);
        String iosDeviceId = null;
        String androidDeviceId = null;
        if(jsonObject.containsKey(CommonField.IOS_DEVICEID)) iosDeviceId = (String) jsonObject.get(CommonField.IOS_DEVICEID);
        if(jsonObject.containsKey(CommonField.ANDROID_DEVICEID)) androidDeviceId = (String) jsonObject.get(CommonField.ANDROID_DEVICEID);
        if (Constant.toEmpty(passWord)&&Constant.toEmpty(userName)) {
            UserAdmin userAdmin = adminMapper.selectByToken(MapUtil.build().put("adminPN", userName).put("adminPW", MD5Util.getMD5Code(passWord)).over());
            if (userAdmin == null) {
                throw new CustomException("账户或密码错误!");
            }
            if (userAdmin.getStatus()==2){
                throw new CustomException("尊敬的管理员您的账户由于违规操作、被举报或其他原因，已经被冻结，如有疑问请联系客服（400-0812-868）!");
            }
            Date tokenAging = userAdmin.getTokenAging();
            String token = userAdmin.getToKen();
            Map<String, Object> over = new HashMap<>();
            if (tokenAging != null && token != null) {
                if (DateUtils.booleanToken(tokenAging, new Date())) {
                    Date tokenTime1 = DateUtils.getTokenTime();
                    String toKen1 = SecurityUtil.getToKen();
                    userAdmin.setToKen(toKen1);
                    userAdmin.setTokenAging(tokenTime1);
                    over.put(CommonField.TO_KEN,toKen1);
                    over.put(CommonField.TOKEN_AGING,tokenAging);
                    over.put("id",userAdmin.getId());
                    if (Constant.toEmpty(androidDeviceId)) {
                        over.put(CommonField.ANDROID_DEVICEID,androidDeviceId);
                    }
                    if (Constant.toEmpty(iosDeviceId)) {
                        over.put(CommonField.IOS_DEVICEID,iosDeviceId);
                    }
                    try {
                        adminMapper.updateModel(over);
                    } catch (Exception e) {
                        throw new CustomException(e.getMessage());
                    }
                    return MapUtil.build().put(CommonField.TOKEN, toKen1).put("isScrapResource",userAdmin.getIsScrapResource()).over();
                }else{
                	  Date tokenTime1 = DateUtils.getTokenTime();
                      String toKen1 = SecurityUtil.getToKen();
                      userAdmin.setToKen(toKen1);
                      userAdmin.setTokenAging(tokenTime1);
                      over.put(CommonField.TO_KEN,toKen1);
                      over.put(CommonField.TOKEN_AGING,tokenTime1);
                      over.put("id",userAdmin.getId());
                      if (Constant.toEmpty(androidDeviceId)) {
                          over.put(CommonField.ANDROID_DEVICEID,androidDeviceId);
                      }
                      if (Constant.toEmpty(iosDeviceId)) {
                          over.put(CommonField.IOS_DEVICEID,iosDeviceId);
                      }
                      try {
                          adminMapper.updateModel(over);
                      } catch (Exception e) {
                          throw new CustomException(e.getMessage());
                      }
                	return MapUtil.build().put(CommonField.TOKEN, userAdmin.getToKen()).put("isScrapResource",userAdmin.getIsScrapResource()).over();
                }
                
            }else{
            	 Date tokenTime = DateUtils.getTokenTime();
                 String toKen = SecurityUtil.getToKen();
                 try {
                 adminMapper.updateModel(MapUtil.build().put(CommonField.TO_KEN,toKen).put(CommonField.TOKEN_AGING,tokenTime)
                         .put(CommonField.ANDROID_DEVICEID,androidDeviceId).put(CommonField.IOS_DEVICEID,iosDeviceId).put("id",userAdmin.getId()).over());
                 } catch (Exception e) {
                     throw new CustomException(e.getMessage());
                 }
                 return MapUtil.build().put(CommonField.TOKEN, toKen).put("isScrapResource",userAdmin.getIsScrapResource()).over();
            }
        }
        throw new CustomException("用户名和密码不能为空!");
    }
    /**
     *          专门查询token是否在有效期内
     */
    public Object validationToken()throws CustomException {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        String token = jsonObject.getString("token");
        String mobileNumber = jsonObject.getString("mobileNumber");
        if (Constant.toEmpty(token)&&Constant.toEmpty(mobileNumber)) {
            UserAdmin admin = adminMapper.selectByToken(MapUtil.build().put("adminPN",mobileNumber).put("toKen",token).over());
            if (admin != null) {
                Date tokenAging = admin.getTokenAging();
                token = admin.getToKen();
                if (tokenAging != null && token != null) {
                    if (DateUtils.booleanToken(tokenAging, new Date())) {
                        return token;
                    }
                }
                throw new CustomException("388","toKen已失效!");
            }
            throw new CustomException("没有这个用户!");
        }
        throw new CustomException("手机号和toKen不能为空");
    }
    /**
     *          验证验证码是否正确
     */
    public Object phoneVerification()throws CustomException {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        String mobileNumber = (String) jsonObject.get(CommonField.MOBILE_NUMBER);
        String verificationCode =(String) jsonObject.get(CommonField.VERIFICATION_CODE);
        if (Constant.toEmpty(mobileNumber)&&Constant.toEmpty(verificationCode)) {
            String redisStr = redisUtil.getStr(mobileNumber);
            if (verificationCode.equals(redisStr)) {
                return CommonField.STRING_SUCCESS;
            }
            throw new CustomException("验证码错误!");
        }
        throw new CustomException("手机号和验证码不能为空");
    }
    /**
     *       修改密码
     */
    @Transactional
    public Object updadePassWord()throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        String mobileNumber = (String) jsonObject.get(CommonField.MOBILE_NUMBER);
        String verificationCode =(String) jsonObject.get(CommonField.VERIFICATION_CODE);
        String passWord =(String) jsonObject.get(CommonField.PASS_WORD);
        if (Constant.toEmpty(passWord)&&Constant.toEmpty(verificationCode)&&Constant.toEmpty(mobileNumber)) {
            String redisStr = redisUtil.getStr(mobileNumber);
            if (!verificationCode.equals(redisStr)) {
                throw new CustomException("验证码错误!");
            }
            UserAdmin adminPN = adminMapper.selectByToken(MapUtil.build().put("adminPN", mobileNumber).over());
            if (adminPN == null) {
                throw new CustomException("没有该用户名!");
            }
            adminMapper.updateModel(MapUtil.build().put("adminPW", MD5Util.getMD5Code(passWord)).put("id",adminPN.getId()).over());
            redisUtil.delect(mobileNumber);
            return CommonField.STRING_SUCCESS;
        }
        throw new CustomException(CommonField.MUST_NOT_BE_BLANK);
    }
    /**
     *       修改头像
     */
    @Transactional
    public Object updatePortrait()throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        String token = (String) jsonObject.get(CommonField.TOKEN);
        String portrait = (String) jsonObject.get(CommonField.PORTRAIT);
        if (!Constant.toEmpty(portrait)) {
            throw new CustomException(CommonField.MUST_NOT_BE_BLANK);
        }
        UserAdmin userAdmin = adminMapper.selectByToken(MapUtil.build().put(CommonField.TO_KEN, token).over());
        if (userAdmin != null) {
            adminMapper.updateModel(MapUtil.build().put(CommonField.PORTRAIT, portrait).put("id", userAdmin.getId()).over());
            return CommonField.STRING_SUCCESS;
        }
        throw new CustomException("用户不存在!");
    }
    /**
     *       首页数据
     */
    public Object homePage()throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        String token = (String) jsonObject.get(CommonField.TOKEN);
        Map<String,Object> adminMap =adminMapper.homepage(token);
        if (adminMap != null&&!adminMap.isEmpty()) {
            Object portrait = adminMap.get(CommonField.PORTRAIT);
            Long messageNumber = (Long) adminMap.get(CommonField.MESSAGE_NUMBER);
            if (Constant.toEmpty(portrait)) {
                adminMap.put(CommonField.PORTRAIT, UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL,portrait.toString()));
            }else{
                adminMap.put(CommonField.PORTRAIT,CommonField.getMaintenanceShopImg(0,"default_logo.png"));
            }
            if (messageNumber > 99) {
                adminMap.put(CommonField.MESSAGE_NUMBER, "99+");
            } else {
                adminMap.put(CommonField.MESSAGE_NUMBER, messageNumber.toString());
            }
//            adminMap.put(CommonField.MESSAGE_NUMBER, messageNumber.toString());
            return adminMap;
        }
        throw new CustomException("用户不存在!");
    }
    
    /**
     * 主页数据
     * @author xiaowuge  
     * @date 2018年9月11日  
     * @version 1.0
     */
    public Object homeMenu()throws Exception{
    	String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        String token = (String) jsonObject.get(CommonField.TOKEN);
        Map<String,Object> reModelMap = new HashMap<String,Object>();
        if (Constant.toEmpty(token)) {
            UserAdmin admin = adminMapper.selectByToken(MapUtil.build().put("toKen",token).over());
            if (admin != null) {
                Date tokenAging = admin.getTokenAging();
                token = admin.getToKen();
                if (tokenAging != null && token != null) {
                	Map<String, Integer> map1 = new HashMap<>();
                	map1.put("roleId", admin.getRoleId());
                	
                	Rights rights = rightsMapper.selectByRoleId(admin.getRoleId());
                   if(Constant.toEmpty(rights.getRightsMenu())) {
                       String idArr = rights.getRightsMenu();
                       Map map = new HashMap();
                       map.put("idArr",idArr);
                       reModelMap.put("leftMenu", menuService.findAppMenuList(map));
                   }
                   return reModelMap;
                }
                throw new CustomException("388","toKen已失效!");
            }
            throw new CustomException("没有这个用户!");
        }
        throw new CustomException("toKen不能为空");
    }
    /**
     *       消息列表
     * @throws Exception 
     */
    public Object messageList() throws Exception{
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        String token = (String) jsonObject.get(CommonField.TOKEN);
        UserAdmin admin = null;
        Boolean huzhu = false;
        Boolean baoxian = false;
        String type = null;        
        if (Constant.toEmpty(token)) {
        	admin = adminMapper.selectByToken(MapUtil.build().put("toKen", token).over());
        	Rights rights = rightsMapper.selectByRoleId(admin.getRoleId());
			String[] right = rights.getRightsMenu().split(",");
			for(int i = 0; i < right.length; i++){
				if(("4").equals(right[i])){    //1是互助理赔消息 4是互助理赔菜单
					huzhu = true;
				}else if(("5").equals(right[i])){
					baoxian = true;
				}				
			continue;
			}
        }
        if(huzhu && !baoxian){
        	type = "1";
        }else if(!huzhu && baoxian){
        	type = "2";
        }else if(huzhu && baoxian){
        	type = "3";
        }else{
        	type = null;
        }
		
        System.out.println("type :" + type );
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("type", type);
        List<Map<String, Object>> messageList = messageBackstageMapper.getMessageListByType(typeMap);
        if (messageList==null||messageList.isEmpty() && type == null) return new String[0];
        messageList.forEach(a->{
            Date date = (Date) a.get("createTime");
            if (DateUtils.getStringDateTime(DateUtils.getDateTime(),DateUtils.FORMAT_)
                    .equals(DateUtils.getStringDateTime(date,DateUtils.FORMAT_))){
                a.put("createTime",DateUtils.getStringDateTime(date,DateUtils.FORMAT_YMD_HM));
            }else {
                a.put("createTime",DateUtils.getStringDateTime(date,DateUtils.FORMAT_));
            }

        });
        return messageList;

    }
    /**
     *    消息已读接口
     */
    @Transactional
    public Object messageRead()throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        String token = (String) jsonObject.get(CommonField.TOKEN);
        Integer messageId =Integer.valueOf( (String) jsonObject.get("messageId"));
        MessageBackstage messageBackstage = messageBackstageMapper.selectById(MapUtil.build().put(CommonField.TO_KEN,token).put("id",messageId).over());
//        if (messageBackstage == null) throw new CustomException("你没有这条消息!");
        if (messageBackstage.getIsSolve()==3) throw new CustomException("消息已经已读无需更改!");
        messageBackstageMapper.updateModel(MapUtil.build().put("isSolve",3).put("id",messageId).over());
        return CommonField.STRING_SUCCESS;
    }
    /**
     *    统计各状态车辆数量
     */
    public Object countCar() throws CustomException {
        return carMapper.findCount(new HashMap<>());
    }
    /**
     *    车辆列表
     */
    public Object listCar() throws CustomException {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        // 将车牌号转化成大写
        String licensePlateNumber = Constant.toEmpty(jsonObject.get("searchInfo"))? (String) jsonObject.get("searchInfo"):"";

        int pageNo = Constant.toEmpty(jsonObject.get("pageNo"))?Integer.parseInt(jsonObject.get("pageNo").toString()):1;
        PageHelper.startPage(pageNo, 10);
        List<Map<String, Object>> carList = carMapper.findCarList2(MapUtil.build().put("searchInfo", jsonObject.get("searchInfo")).put("status", jsonObject.get("status"))
                .put("licensePlateNumber", licensePlateNumber.toUpperCase()).over());
        if (carList != null&&carList.size()>0&&carList.get(0)!=null) {
            for(int i = 0; i < carList.size(); i++) {
                Map<String, Object> objectMap = carList.get(i);
                objectMap.put("observationEndTime",getStringDate((Date) objectMap.get("observationEndTime")));
                objectMap.put("timeBegin",getStringDate((Date) objectMap.get("timeBegin")));
                objectMap.put("timeEnd",getStringDate((Date) objectMap.get("timeEnd")));
                objectMap.put("timeSignout",getStringDate((Date) objectMap.get("timeSignout")));
            }
        }
        return new PageInfo<>(carList);
    }
    /**
     *    车辆详情
     */
    public Object detailCar() throws CustomException {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);

        try {
            Map<String, Object> carDetail = carMapper.findSingle(MapUtil.build().put("id", jsonObject.get("id")).over());
            if (Constant.toEmpty(carDetail.get("carPhotos"))) {
                carDetail.put("carPhotos", CommonField.getCarUrl((String) carDetail.get("carPhotos")));
            }
            if (null != carDetail.get("timeBegin")){
                carDetail.put("joinDay",DateUtils.cutTwoDateToDayInteger((Date) carDetail.get("timeBegin"),new Date()));
            } else {
                carDetail.put("joinDay",0);
            }
            carDetail.put("createAt",getStringDate((Date) carDetail.get("createAt")));
            carDetail.put("payTime",getStringDate((Date) carDetail.get("payTime")));
            carDetail.put("observationEndTime",getStringDate((Date) carDetail.get("observationEndTime")));
            carDetail.put("timeBegin",getStringDate((Date) carDetail.get("timeBegin")));
            carDetail.put("timeEnd",getStringDate((Date) carDetail.get("timeEnd")));
            carDetail.put("timeSignout",getStringDate((Date) carDetail.get("timeSignout")));
            carDetail.put("unavailableTime",getStringDate((Date) carDetail.get("unavailableTime")));
            // 转换退出的原因
            if (Constant.toEmpty(carDetail.get("reasonSignout"))){
                if ("1".equals(carDetail.get("reasonSignout"))){
                    carDetail.put("reasonSignout","余额不足");
                }else if("2".equals(carDetail.get("reasonSignout"))){
                    carDetail.put("reasonSignout","包年时间到了");
                }
            }
            return carDetail;
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
    /**
     *    统计各状态保险理赔数量
     */
    public Object countOrder() throws CustomException {
        return orderMapper.findCount(new HashMap<>());
    }
    /**
     *    保险理赔列表
     */
    public Object listOrder() throws CustomException {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);

        int pageNo = Constant.toEmpty(jsonObject.get("pageNo"))?Integer.parseInt(jsonObject.get("pageNo").toString()):1;
        PageHelper.startPage(pageNo, 10);
        List<Map<String, Object>> orderList = orderMapper.findOrderList(MapUtil.build().put("searchInfo",jsonObject.get("searchInfo")).put("status",jsonObject.get("status")).put("isInvalid",jsonObject.get("isInvalid")).over());
        if (orderList != null&&orderList.size()>0&&orderList.get(0)!=null) {
            for(int i = 0; i < orderList.size(); i++) {
                Map<String, Object> objectMap = orderList.get(i);
                objectMap.put("applyTime",getStringDate((Date) objectMap.get("applyTime")));
            }
        }
        return new PageInfo<>(orderList);
    }
    /**
     *    保险理赔详情
     */
    public Object detailOrder() throws CustomException {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);

        try {
            Map<String, Object> order = orderMapper.findDetail(MapUtil.build().put("orderNo",jsonObject.get("orderNo")).over());
            if(Constant.toEmpty(order.get("drivingLicense"))){
                order.put("drivingLicense",CommonField.getCarDrivingUrl((String) order.get("drivingLicense")));
            }
            if(Constant.toEmpty(order.get("carPhotos"))){
                order.put("carPhotos",CommonField.getCarUrl((String) order.get("carPhotos")));
            }
            if(Constant.toEmpty(order.get("accidentImg"))){
                List<String> accidentImg = CommonField.getImgLists(1,0,(String) order.get("accidentImg"));
                order.put("accidentImg",accidentImg);
            }
            if(Constant.toEmpty(order.get("assertImg"))){
                List<String> assertImg = CommonField.getImgLists(1,1,(String) order.get("assertImg"));
                order.put("assertImg",assertImg);
            }
            if(Constant.toEmpty(order.get("repairImg"))){
                List<String> repairImg = CommonField.getImgLists(1,2,(String) order.get("repairImg"));
                order.put("repairImg",repairImg);
            }
            if(Constant.toEmpty(order.get("complaintImg"))){
                List<String> complaintImg = CommonField.getImgLists(1,3,(String) order.get("complaintImg"));
                order.put("complaintImg",complaintImg);
            }
            order.put("uploadImgTime",getStringDate((Date) order.get("uploadImgTime")));
            order.put("applyTime",getStringDate((Date) order.get("applyTime")));
            order.put("examineTime",getStringDate((Date) order.get("examineTime")));
            order.put("reciveCarTime",getStringDate((Date) order.get("reciveCarTime")));
            order.put("applyDistributionTime",getStringDate((Date) order.get("applyDistributionTime")));
            order.put("deliverCarTime",getStringDate((Date) order.get("deliverCarTime")));
            order.put("distributionTime",getStringDate((Date) order.get("distributionTime")));
            order.put("receiveOrderTime",getStringDate((Date) order.get("receiveOrderTime")));
            order.put("failReceiveOrderTime",getStringDate((Date) order.get("failReceiveOrderTime")));
            order.put("assertTime",getStringDate((Date) order.get("assertTime")));
            order.put("comfirmAssertTime",getStringDate((Date) order.get("comfirmAssertTime")));
            order.put("beginRepairTime",getStringDate((Date) order.get("beginRepairTime")));
            order.put("endRepairTime",getStringDate((Date) order.get("endRepairTime")));
            order.put("takeCarTime",getStringDate((Date) order.get("takeCarTime")));
            order.put("payTime",getStringDate((Date) order.get("payTime")));
            order.put("completeTime",getStringDate((Date) order.get("completeTime")));
            order.put("complaintTime",getStringDate((Date) order.get("complaintTime")));
            order.put("unComplaintTime",getStringDate((Date) order.get("unComplaintTime")));
            order.put("solveComplaintTime",getStringDate((Date) order.get("solveComplaintTime")));
            order.put("invalidTime",getStringDate((Date) order.get("invalidTime")));

            // 返回分享的相关信息
            String shareUrl = Constant.toReadPro("shareUrl") + "?id=" + jsonObject.get("orderNo")+ "&type=2";
            String shareTitle = "预估定损价";
            String shareDesciption = "车V互助邀请您预估车辆定损价,点击开始报价";
            String shareIcon = Constant.toReadPro("chevhuzhuUrl") + "hfive/img/app_logo_2.jpg";
            order.put("shareUrl",shareUrl);
            order.put("shareTitle",shareTitle);
            order.put("shareDesciption",shareDesciption);
            order.put("shareIcon",shareIcon);

            // 返回估算的价格
            if (Constant.toEmpty(order.get("amtAssert")) && Constant.toEmpty(order.get("ratio"))){
                BigDecimal amtAssertDecimal = (BigDecimal)order.get("amtAssert");
                BigDecimal ratioDecimal = (BigDecimal)order.get("ratio");
                order.put("estimatePrice", amtAssertDecimal.multiply(ratioDecimal));
            }else {
                order.put("estimatePrice", "");
            }

            return order;
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
    /**
     *    作废保险理赔订单
     */
    @Transactional
    public void invalidOrder() throws CustomException {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        try {
            //查询保险理赔详情
            Map detail = orderMapper.findDetail(MapUtil.build().put("orderNo",jsonObject.get("orderNo")).over());
            //判断订单是否作废，未作废才能进行作废操作
            Integer isInvalid = (Integer) detail.get("isInvalid");
            if(isInvalid == 1){
                //修改保险理赔订单状态
                orderMapper.updateModel(MapUtil.build().put("orderNo",jsonObject.get("orderNo")).put("isInvalid",10).put("invalidTime",DateUtils.formatDate(new Date())).over());

                //如果是接单状态，删除接单定时器和message里面的接单消息
                Integer status = (Integer) detail.get("status");
                if(status==11){
                    Scheduler sche = schedulerFactory.getScheduler();
                    QuartzUtils.removeJob(sche,jsonObject.get("orderNo").toString());
                    cbhMessageMapper.deleteByOrderNo(jsonObject.get("orderNo").toString());
                }
            }else {
                throw new CustomException("该订单已被作废，请勿重复操作");
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
    /**
     *    保险理赔申请通过
     */
    @Transactional
    public void successOrder() throws CustomException {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        try {
            //查询订单详情信息
            Map orderInfo = orderMapper.findDetail(MapUtil.build().put("orderNo",jsonObject.get("orderNo")).over());
            //查询车辆详情信息
            Map carInfo = carMapper.findCarInfo(MapUtil.build().put("carId",orderInfo.get("carId")).over());
            //根据订单状态判断是否能进行审核操作
            Integer status = (Integer) orderInfo.get("status");
            if(status==1){
                //修改保险理赔订单状态
                Map map = new HashMap();
                map.put("orderNo",jsonObject.get("orderNo"));
                if(Constant.toEmpty(jsonObject.get("examineExplanation"))){
                    map.put("examineExplanation",jsonObject.get("examineExplanation"));
                }else {
                    map.put("examineExplanation2","将examineExplanation设为空");
                }
                map.put("examineTime",DateUtils.formatDate(new Date()));
                map.put("status",3);
                orderMapper.updateModel(map);
                //消息推送通知用户 申请理赔通过
                map.clear();
                map.put("customerId",orderInfo.get("customerId"));
                map.put("licensePlateNumber",carInfo.get("licensePlateNumber"));
                map.put("eventNo",jsonObject.get("orderNo"));
                map.put("type",41);
                map.put("title","理赔消息");
                map.put("content",carInfo.get("licensePlateNumber") + "的理赔订单申请审核通过，请及时处理。");
                cbhMessageMapper.saveMessage(map);
                //微信推送通知用户 申请理赔通过
                if (Constant.toEmpty(carInfo.get("openId"))) {
                    map.clear();
                    map.put("openid", carInfo.get("openId"));
                    map.put("eventNo", jsonObject.get("orderNo"));
                    map.put("licensePlateNumber",carInfo.get("licensePlateNumber"));
                    map.put("content", "爱车"+carInfo.get("licensePlateNumber")+"申请理赔审核通过");
                    map.put("theme", "保险代理订单");
                    map.put("keyword1", "审核通过");
                    map.put("keyword2", DateUtils.formatDate(new Date(),DateUtils.FORMAT_DATE_CN1));
                    map.put("url", Constant.toReadPro("realURL")+"hfive/view/baoxian_order_detail.html?id="+jsonObject.get("orderNo"));
                    weiXinUtils.sendTemplate(1, map);
                }
                //短信推送通知用户 申请理赔通过
                SmsDemo.sendSms(11,carInfo.get("customerPN").toString(),carInfo.get("licensePlateNumber").toString());
            }else {
                throw new CustomException("该订单已被审核，请勿重复操作");
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
    /**
     *    保险理赔申请不通过
     */
    @Transactional
    public void failOrder() throws CustomException {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        try {
            //查询订单详情信息
            Map orderInfo = orderMapper.findDetail(MapUtil.build().put("orderNo",jsonObject.get("orderNo")).over());
            //查询车辆详情信息
            Map carInfo = carMapper.findCarInfo(MapUtil.build().put("carId",orderInfo.get("carId")).over());
            //根据订单状态判断是否能进行审核操作
            Integer status = (Integer) orderInfo.get("status");
            if(status==1){
                //修改保险理赔订单状态
                Map map = new HashMap();
                map.put("orderNo",jsonObject.get("orderNo"));
                map.put("examineExplanation",jsonObject.get("examineExplanation"));
                map.put("examineTime",DateUtils.formatDate(new Date()));
                map.put("status",2);
                orderMapper.updateModel(map);
                //消息推送通知用户 申请理赔不通过
                map.clear();
                map.put("customerId",orderInfo.get("customerId"));
                map.put("licensePlateNumber",carInfo.get("licensePlateNumber"));
                map.put("eventNo",jsonObject.get("orderNo"));
                map.put("type",41);
                map.put("title","理赔消息");
                map.put("content",carInfo.get("licensePlateNumber") + "的理赔订单申请审核未通过，请及时处理。");
                cbhMessageMapper.saveMessage(map);
                //微信推送通知用户 申请理赔不通过
                if (Constant.toEmpty(carInfo.get("openId"))) {
                    map.clear();
                    map.put("openid", carInfo.get("openId"));
                    map.put("licensePlateNumber",carInfo.get("licensePlateNumber"));
                    map.put("theme", "理赔代办订单");
                    map.put("keyword1", "理赔申请");
                    map.put("remark", jsonObject.get("examineExplanation"));
                    map.put("url", Constant.toReadPro("realURL")+"hfive/view/baoxian_order_detail.html?id="+jsonObject.get("orderNo"));
                    weiXinUtils.sendTemplate(9, map);
                }
                //短信通知用户 申请理赔不通过
                SmsDemo.sendSms(21,carInfo.get("customerPN").toString(),carInfo.get("licensePlateNumber").toString());
            }else {
                throw new CustomException("该订单已被审核，请勿重复操作");
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
    /**
     *    保险理赔分单
     */
    @Transactional
    public void distributionOrder() throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        //查询订单详情信息
        Map orderInfo = orderMapper.findDetail(MapUtil.build().put("orderNo",jsonObject.get("orderNo")).over());
        //根据订单状态判断是否能进行分单操作
        Integer status = (Integer) orderInfo.get("status");
        if(status==10 || status==12){
            distributionOrder.distributionOfOrder(MapUtil.build().put("orderNo",jsonObject.get("orderNo")).put("maintenanceshopId",jsonObject.get("maintenanceshopId")).put("carId",orderInfo.get("carId")).over());
        }else {
            throw new CustomException("该订单已被分单，请勿重复操作");
        }
    }
    /**
     *    保险理赔确认定损
     */
    @Transactional
    public void assertOrder() throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        //查询订单详情信息
        Map orderInfo = orderMapper.findDetail(MapUtil.build().put("orderNo",jsonObject.get("orderNo")).over());
        //根据订单状态判断是否能进行确认定损操作
        Integer status = (Integer) orderInfo.get("status");
        if(status==32 || status==41){
            Map map = new HashMap();
            map.put("orderNo",jsonObject.get("orderNo"));
            map.put("status",41);
            map.put("amtAssert",new BigDecimal((String) jsonObject.get("amtAssert")));
            map.put("amtBusiness",new BigDecimal((String) jsonObject.get("amtBusiness")));
            map.put("assertDescription",jsonObject.get("assertDescription"));
            map.put("explanationAssert",jsonObject.get("explanationAssert"));
            map.put("comfirmAssertTime",DateUtils.formatDate(new Date()));
            orderMapper.updateModel(map);
        }else {
            throw new CustomException("该订单已确认定损，请勿重复操作");
        }
    }
    /**
     *    保险理赔处理投诉
     */
    @Transactional
    public void complaintOrder() throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        //查询订单详情信息
        Map orderInfo = orderMapper.findDetail(MapUtil.build().put("orderNo",jsonObject.get("orderNo")).over());
        //根据订单状态判断是否能进行处理投诉操作
        Integer status = (Integer) orderInfo.get("status");
        if(status==71){
            orderMapper.updateModel(MapUtil.build().put("orderNo",jsonObject.get("orderNo")).put("status",51).put("solveComplaintTime",DateUtils.formatDate(new Date())).over());
        }else {
            throw new CustomException("该订单投诉已处理，请勿重复操作");
        }
    }
    /**
     *    统计各状态投诉订单数量
     */
    public Object countComplaint() throws CustomException {
        return complaintMapper.findCount(new HashMap<>());
    }
    /**
     *    投诉订单列表
     */
    public Object listComplaint() throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);

        int pageNo = Constant.toEmpty(jsonObject.get("pageNo"))?Integer.parseInt(jsonObject.get("pageNo").toString()):1;
        PageHelper.startPage(pageNo, 10);
        List<Map<String, Object>> complaintList = complaintMapper.findMore(MapUtil.build().put("searchInfo",jsonObject.get("searchInfo")).put("status",jsonObject.get("status")).over());
        if (complaintList != null&&complaintList.size()>0&&complaintList.get(0)!=null) {
            for(int i = 0; i < complaintList.size(); i++) {
                Map<String, Object> objectMap = complaintList.get(i);
                objectMap.put("createAt",getStringDate((Date) objectMap.get("createAt")));
                objectMap.put("solveAt",getStringDate((Date) objectMap.get("solveAt")));
            }
        }
        return new PageInfo<>(complaintList);
    }
    /**
     *    投诉订单详情
     */
    public Object detailComplaint() throws CustomException {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);

        try {
            Map<String, Object> complaint = complaintMapper.findSingle(MapUtil.build().put("complaintId",jsonObject.get("complaintId")).over());
            complaint.put("createAt",getStringDate((Date) complaint.get("createAt")));
            complaint.put("solveAt",getStringDate((Date) complaint.get("solveAt")));
            return complaint;
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
    /**
     *    处理投诉订单
     */
    @Transactional
    public void solveComplaint() throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        //查询订单详情信息
        Map complaintInfo = complaintMapper.findSingle(MapUtil.build().put("complaintId",jsonObject.get("complaintId")).over());
        //根据订单状态判断是否能处理
        Integer status = (Integer) complaintInfo.get("status");
        if(status==1){
            complaintMapper.updateModel(MapUtil.build().put("complaintId",jsonObject.get("complaintId")).put("status",3).put("solveAt",DateUtils.formatDate(new Date())).over());
        }else {
            throw new CustomException("该投诉已被处理，请勿重复操作");
        }
    }
    /**
     *    添加模板
     */
    @Transactional
    public void addModel() throws CustomException {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        try {
            UserAdmin userAdmin = adminMapper.selectByToken(MapUtil.build().put("toKen",jsonObject.get("token")).over());
            modelMapper.saveSingle(MapUtil.build().put("adminId",userAdmin.getId()).put("title",jsonObject.get("title")).put("content",jsonObject.get("content")).over());
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
    /**
     *    删除模板
     */
    @Transactional
    public void deleteModel() throws CustomException {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        if(!Constant.toEmpty(jsonObject.get("modelId"))){
            throw new CustomException("请至少选择一个要删除的模板");
        }
        try {
            if(jsonObject.get("modelId") instanceof String){
                String[] strarr = String.valueOf(jsonObject.get("modelId")).split(",");
                int[] modelId = new int[strarr.length];
                for(int i=0;i<strarr.length;i++) {
                    modelId[i] = Integer.parseInt(strarr[i]);
                }
                jsonObject.put("modelId",modelId);
            }
            modelMapper.updateModel(MapUtil.build().put("modelId",jsonObject.get("modelId")).put("isDel",1).put("delTime",DateUtils.formatDate(new Date())).over());
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
    /**
     *    模板列表
     */
    public Object listModel() throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);

        int pageNo = Constant.toEmpty(jsonObject.get("pageNo"))?Integer.parseInt(jsonObject.get("pageNo").toString()):1;
        PageHelper.startPage(pageNo, 10);
        return new PageInfo<>(modelMapper.findModelList());
    }
    /**
     *    查询可接单的商家
     */
    public Object canDistributionShop() throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        List<Map<String, Object>> maintenanceshopList = maintenanceshopMapper.findCanDistribution(new HashMap<>());
        if(Constant.toEmpty(jsonObject.get("latitude")) && Constant.toEmpty(jsonObject.get("longitude"))){
            Double laCarOwner = Double.valueOf(String.valueOf(jsonObject.get("latitude")));
            Double loCarOwner = Double.valueOf(String.valueOf(jsonObject.get("longitude")));
            for (Map<String, Object> maintenanceshop:maintenanceshopList) {
                if(String.valueOf(maintenanceshop.get("latitude")).equals("null")){
                    continue;
                }
                Double laShop = Double.valueOf(String.valueOf(maintenanceshop.get("latitude")));
                Double loShop = Double.valueOf(String.valueOf(maintenanceshop.get("longitude")));
                Double dis = LocationUtils.getDistance(laCarOwner, loCarOwner, laShop, loShop);
                maintenanceshop.put("distance", dis);
            }
            Collections.sort(maintenanceshopList, new Comparator<Map<String,Object>>() {
                public int compare(Map<String,Object> o1, Map<String,Object> o2) {
                    return new BigDecimal(String.valueOf(o1.get("distance")).equals("null")?"0":String.valueOf(o1.get("distance"))).compareTo(new BigDecimal(String.valueOf(o2.get("distance")).equals("null")?"0":String.valueOf(o2.get("distance"))));
                }
            });
        }
        return maintenanceshopList;
    }

    //获得string类型的时间
    public String getStringDate(Date date){
        if (date==null) {
            return "";
        }
        return  DateUtils.getStringDateTime(date);
    }

    /**
     * 互助理赔 统计各种状态订单的数据
     * @return
     */
    public Object hzCount() throws Exception {
        Map<String,Object> paramModelMap = new HashMap<>();
        return eventService.findEventCount(paramModelMap);
    }

    /**
     * 互助理赔 订单的列表
     * @return
     */
    public Object hzOrderList() throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        Map<String,Object> paramModelMap = new HashMap<>();
        paramModelMap.put("pageNo", jsonObject.get("pageNo"));
        paramModelMap.put("pageSize", jsonObject.get("pageSize"));
        paramModelMap.put("searchInfo", jsonObject.get("searchInfo"));
        paramModelMap.put("statusEvent", jsonObject.get("status"));
        paramModelMap.put("isInvalid", jsonObject.get("isInvalid"));
        return eventService.findEventList(paramModelMap);
    }

    /**
     * 互助理赔 订单详情
     * @return
     */
    public Object hzOrderDetail() throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        Map<String, Object> order = eventMapper.findHzOrderDetail(MapUtil.build().put("eventNo",jsonObject.get("eventNo")).over());
        if (null == order){
            throw new CustomException("根据订单号，没有查出订单!");
        }

        // 处理加入时长, 减去观察结束时间
        if (null != order.get("observationEndTime")){
            order.put("joinDays",DateUtils.cutTwoDateToDay((Date) order.get("observationEndTime"),new Date()));
        }
        if(Constant.toEmpty(order.get("img"))){
            List<String> repairImg = CommonField.getImgList(2,(String) order.get("img"));
            order.put("repairImg",repairImg);
        }
        if(Constant.toEmpty(order.get("assertImg"))){
            List<String> assertImg = CommonField.getImgList(1,(String) order.get("assertImg"));
            order.put("assertImg",assertImg);
        }
        if(Constant.toEmpty(order.get("drivingLicense"))){
            order.put("drivingLicense",CommonField.getCarDrivingUrl((String) order.get("drivingLicense")));
        }
        if(Constant.toEmpty(order.get("accidentImg"))){
            List<String> accidentImg = CommonField.getImgList(0,(String) order.get("accidentImg"));
            // 加入行驶证照片
            if (order.get("drivingLicense") != null){
                accidentImg.add((String) order.get("drivingLicense"));
            }
            order.put("accidentImg",accidentImg);
        }
        if(Constant.toEmpty(order.get("carPhotos"))){
            order.put("carPhotos",CommonField.getCarUrl((String) order.get("carPhotos")));
        }

        //判断是否有不通过记录
        Map<String, Object> eventApplyFail = eventApplyFailService.getEventApplyFail(MapUtil.build().put("eventNo", jsonObject.get("eventNo")).over());
        if (Constant.toEmpty(eventApplyFail.get("accidentImg"))) {
			order.put("eventFailFlag", true);
			if (Constant.toEmpty(eventApplyFail.get("accidentImg"))) {
				List<String> accidentImg = CommonField.getImgList(0, (String)eventApplyFail.get("accidentImg"));
				eventApplyFail.put("accidentImg", accidentImg);
			}
			order.put("eventApplyFail", eventApplyFail);
		}else{
			order.put("eventFailFlag", false);
		}
        
        // 返回估算的价格
        if (Constant.toEmpty(order.get("amtAssert")) && Constant.toEmpty(order.get("ratio"))){
            BigDecimal amtAssertDecimal = (BigDecimal)order.get("amtAssert");
            BigDecimal ratioDecimal = (BigDecimal)order.get("ratio");
            order.put("estimatePrice", amtAssertDecimal.multiply(ratioDecimal));
        }else {
            order.put("estimatePrice", "");
        }
        

        // 返回分享的相关信息
        String shareUrl = Constant.toReadPro("shareUrl") + "?id=" + jsonObject.get("eventNo")+ "&type=1";
        String shareTitle = "预估定损价";
        String shareDesciption = "车V互助邀请您预估车辆定损价,点击开始报价";
        String shareIcon = Constant.toReadPro("chevhuzhuUrl") + "hfive/img/app_logo_2.jpg";
        order.put("shareUrl",shareUrl);
        order.put("shareTitle",shareTitle);
        order.put("shareDesciption",shareDesciption);
        order.put("shareIcon",shareIcon);


        order.put("handCarTime",getStringDate((Date) order.get("handCarTime")));
        order.put("timeComplaint",getStringDate((Date) order.get("timeComplaint")));
        order.put("repairCarOverTime",getStringDate((Date) order.get("repairCarOverTime")));
        order.put("repairCarTime",getStringDate((Date) order.get("repairCarTime")));
        order.put("receiveCarTime",getStringDate((Date) order.get("receiveCarTime")));
        order.put("submitAssertTime",getStringDate((Date) order.get("submitAssertTime")));
        order.put("commentTime",getStringDate((Date) order.get("commentTime")));
        order.put("invalidTime",getStringDate((Date) order.get("invalidTime")));
        order.put("createOrderTime",getStringDate((Date) order.get("createOrderTime")));
        order.put("applyTime",getStringDate((Date) order.get("applyTime")));
        order.put("applyFenDanTime",getStringDate((Date) order.get("applyFenDanTime")));
        order.put("FenDanTime",getStringDate((Date) order.get("FenDanTime")));
        order.put("timeReceiveCar",getStringDate((Date) order.get("timeReceiveCar")));
        order.put("receiveOrderTime",getStringDate((Date) order.get("receiveOrderTime")));
        order.put("failReceiveOrderTime",getStringDate((Date) order.get("failReceiveOrderTime")));
        order.put("comfirmAssertTime",getStringDate((Date) order.get("comfirmAssertTime")));
        return order;
    }

    /**
     * 互助理赔 作废订单
     */
    @Transactional
    public void hzInvalidOrder() throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        Map<String,Object> paramModelMap =MapUtil.build().put("eventNo",jsonObject.get("eventNo")).over();
        eventService.invalidOrder(paramModelMap);
    }

    /**
     * 互助理赔 审核通过
     */
    @Transactional
    public void hzApplyPass(HttpServletRequest request) throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        //获取当前订单状态
        int newStatus = eventService.getEventStatus(jsonObject.get("eventNo").toString());
        if (newStatus != 1) {
        	throw new CustomException("该订单已处理，请勿重复操作!");
		}
        // 判断是否该订单是否已经审核通过
        Map<String,Object> orderInfo = eventService.findEventByEventNo(MapUtil.build().put("eventNo",jsonObject.get("eventNo")).over());
        if (orderInfo != null && (int)orderInfo.get("statusEvent") == 3){
            throw new CustomException("该订单已审核通过，请勿重复操作!");
        }
        Map<String,Object> paramModelMap = MapUtil.build().put("eventNo",jsonObject.get("eventNo"))
                .put("reasonSuccess",jsonObject.get("reasonSuccess"))
                .put("timeExamine",DateUtils.formatDate(new Date()))
                .over();
        //修改理赔申请明细
        eventApplyService.updateEventApply(paramModelMap);
        //创建活动二维码
        String shareUrl = Constant.toReadPro("shareUrl") + "?id=" + paramModelMap.get("eventNo");
        String logoPath = request.getSession().getServletContext().getRealPath("/")+"/cite/images/qrcodeLogo.png";
        String qrcodeName = QRCodeUtil.saveQrcode(shareUrl,logoPath,"event/qrcode/");
        //修改互助单
        Map map = new HashMap();
        map.put("eventNo",paramModelMap.get("eventNo"));
        map.put("statusEvent",3);
        map.put("eventQrcode",qrcodeName);
        eventService.updateEvent(map);
        //推送消息给用户告知他审核通过
        map.clear();
        map.put("eventNo",paramModelMap.get("eventNo"));
        Map<String,Object> eventInfo = eventService.findEventByEventNo(map);
        map.clear();
        map.put("customerId",(Integer)eventInfo.get("customerId"));
        map.put("licensePlateNumber",eventInfo.get("licensePlateNumber"));
        map.put("eventNo",paramModelMap.get("eventNo"));
        map.put("type",41);
        map.put("title","救助消息");
        map.put("content",eventInfo.get("licensePlateNumber") + "的救助订单申请审核通过，请及时处理。");
        messageService.saveMessage(map);
        //微信推送
        if (Constant.toEmpty(eventInfo.get("openId"))) {
            map.clear();
            map.put("openid", eventInfo.get("openId"));
            map.put("eventNo", paramModelMap.get("eventNo"));
            map.put("licensePlateNumber", eventInfo.get("licensePlateNumber"));
            map.put("content", "爱车"+eventInfo.get("licensePlateNumber")+" "+eventInfo.get("model")+"申请救助审核通过");
            map.put("theme", "互助申请");
            map.put("keyword1", "审核通过");
            map.put("keyword2", new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
            map.put("url", Constant.toReadPro("realURL")+"hfive/view/order_detail.html?id="+paramModelMap.get("eventNo"));
            weiXinUtils.sendTemplate(1, map);
        }
        //短信通知
        SmsDemo.sendSms(11,eventInfo.get("customerPN").toString(),eventInfo.get("licensePlateNumber").toString());
    }

    /**
     * 互助理赔 审核不通过
     * @param request
     */
    @Transactional
    public void hzApplyFail(HttpServletRequest request) throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        //获取当前订单状态
        int newStatus = eventService.getEventStatus(jsonObject.get("eventNo").toString());
        if (newStatus != 1) {
        	throw new CustomException("该订单已处理，请勿重复操作!");
		}
        // 判断是否该订单是否已经审核不通过
        Map<String,Object> orderInfo = eventService.findEventByEventNo(MapUtil.build().put("eventNo",jsonObject.get("eventNo")).over());
        if (orderInfo != null && (int)orderInfo.get("statusEvent") == 2){
            throw new CustomException("该订单已审核不通过，请勿重复操作!");
        }
        Map<String,Object> paramModelMap = MapUtil.build()
                .put("eventNo",jsonObject.get("eventNo"))
                .put("reasonFailure",jsonObject.get("reasonFailure"))
                .put("timeExamine",DateUtils.formatDate(new Date()))
                .over();
        //更新理赔申请的失败原因
        String result = eventApplyService.updateEventApply(paramModelMap);
        //更新互助事件的状态
        Map map = new HashMap();
        map.put("statusEvent",2);
        map.put("eventNo",paramModelMap.get("eventNo"));
        eventService.updateEvent(map);
        
        map.clear();
        map.put("orderNo", paramModelMap.get("eventNo"));
        map.put("orderStatus", "1");
        messageBackstageService.deleteBackMsg(map);
        //推送消息给用户告知他审核不通过
        map.clear();
        map.put("eventNo",paramModelMap.get("eventNo"));
        Map<String,Object> eventInfo = eventService.findEventByEventNo(map);
        map.clear();
        map.put("customerId",(Integer)eventInfo.get("customerId"));
        map.put("licensePlateNumber",eventInfo.get("licensePlateNumber"));
        map.put("eventNo",paramModelMap.get("eventNo"));
        map.put("type",41);
        map.put("title","救助消息");
        map.put("content",eventInfo.get("licensePlateNumber") + "的救助订单申请审核未通过，请及时处理。");
        messageService.saveMessage(map);
        
        //根据订单号获取申请理赔详情
        map.clear();
        map.put("eventNo", paramModelMap.get("eventNo"));
        Map<String, Object> parameMap = eventApplyMapper.getEventApply(map);
        eventApplyFailService.saveEventApplyFail(parameMap);
        
        //微信推送给用户
        if (Constant.toEmpty(eventInfo.get("openId"))) {
            map.clear();
            map.put("openid", eventInfo.get("openId"));
            map.put("licensePlateNumber",eventInfo.get("licensePlateNumber"));
            map.put("model",eventInfo.get("model"));
            map.put("theme", "互助申请订单");
            map.put("keyword1", "救助申请");
            map.put("remark", paramModelMap.get("reasonFailure"));
            map.put("url",Constant.toReadPro("realURL")+"hfive/view/order_detail.html?id=" + paramModelMap.get("eventNo").toString());
            weiXinUtils.sendTemplate(9, map);
        }
        //短信通知
        SmsDemo.sendSms(21,eventInfo.get("customerPN").toString(),eventInfo.get("licensePlateNumber").toString());
    }

    /**
     * 互助理赔 分单
     * @param request
     */
    @Transactional
    public void hzDistribution(HttpServletRequest request) throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams); 
        //获取当前订单状态
        int newStatus = eventService.getEventStatus(jsonObject.get("eventNo").toString());
        System.out.println((newStatus != 10 && newStatus != 12) + ">>>>>>>>>>>>>>>");
        if (newStatus != 10 && newStatus != 12) {
        	throw new CustomException("该订单已处理，请勿重复操作!");
		}
        // 判断是否该订单是否已经审核不通过
        Map<String,Object> orderInfo = eventService.findEventByEventNo(MapUtil.build().put("eventNo",jsonObject.get("eventNo")).over());
        if (orderInfo != null && (int)orderInfo.get("statusEvent") == 11){
            throw new CustomException("该订单已经分单，请勿重复操作!");
        }
        Map<String,Object> paramModelMap = MapUtil.build()
                .put("eventNo",jsonObject.get("eventNo"))
                .put("maintenanceshopId",jsonObject.get("maintenanceshopId"))
                .put("licensePlateNumber", orderInfo.get("licensePlateNumber"))
                .over();
        distributionOrder.distributionOfEvent(paramModelMap);
    }

    /**
     * 互助理赔 定损确认
     * @param request
     */
    @Transactional
    public void hzAssertOrder(HttpServletRequest request) throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        // 判断是否该订单是否已经定损确认过
         Map<String,Object> orderInfo = eventService.findEventByEventNo(MapUtil.build().put("eventNo",jsonObject.get("eventNo")).over());
        if (orderInfo != null && (int)orderInfo.get("statusEvent") == 31){
            throw new CustomException("该订单已经确认定损，请勿重复操作!");
        }
        Map<String,Object> paramModelMap = MapUtil.build()
                .put("eventNo",jsonObject.get("eventNo"))
                .put("amtAssert",jsonObject.get("amtAssert")) //定损费用
                .put("amtBusiness",jsonObject.get("amtBusiness"))
                .put("reasonAssert",jsonObject.get("reasonAssert"))
                .put("description",jsonObject.get("description"))
                .over();
        eventAssertService.assertSuccess(paramModelMap);
    }

    /**
     * 互助理赔 投诉解决
     * @param request
     */
    @Transactional
    public void hzSolveComplaint(HttpServletRequest request) throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);
        // 判断是否该订单是否已经解决投诉了
         Map<String,Object> orderInfo = eventService.findEventByEventNo(MapUtil.build().put("eventNo",jsonObject.get("eventNo")).over());
        if (orderInfo != null && (int)orderInfo.get("statusEvent") == 71){
            throw new CustomException("该订单已经解决投诉，请勿重复操作!");
        }
        // 查询车辆的用户信息
        Map<String,Object> carInfo = carMapper.findCarInfo(MapUtil.build().put("carId", orderInfo.get("carId")).over());
        Map<String,Object> paramModelMap = MapUtil.build()
                .put("eventNo",jsonObject.get("eventNo"))
                .put("customerId", orderInfo.get("customerId"))
                .put("maintenanceshopId", orderInfo.get("maintenanceshopId"))
                .put("nameCarOwner", carInfo.get("carInfo"))
                .put("customerPN", carInfo.get("customerPN"))
                .over();
        Map map = new HashMap();
        //修改互助单
        map.put("eventNo",paramModelMap.get("eventNo"));
        map.put("statusEvent",71);
        eventService.updateEvent(map);
        //记录交车信息
        map.clear();
        map.put("eventNo", paramModelMap.get("eventNo")); //订单号
        map.put("status", 3);
        map.put("timeReceiveEnd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("nameCarOwner", paramModelMap.get("nameCarOwner"));
        map.put("telCarOwner", paramModelMap.get("customerPN"));
        eventReceivecarMapper.saveSingle(map);
        //定时器默认好评
        map.clear();
        map.put("jobName","commentJob_" + paramModelMap.get("eventNo"));
        map.put("eventNo", paramModelMap.get("eventNo"));
        map.put("maintenanceshopId", paramModelMap.get("maintenanceshopId"));
        map.put("customerId", paramModelMap.get("customerId"));
        Scheduler sche = schedulerFactory.getScheduler();
        QuartzUtils.removeJob(sche,map.get("jobName").toString());
        Map dicMap = new HashMap();
        dicMap.put("type","commentTime");
        Map dictionary = dictionaryService.findSingle(dicMap);
        Map<String, String> dateMap = DateUtils.getDateMap(new Long(dictionary.get("value").toString()));
        String cron = dateMap.get("second")+" "+dateMap.get("minute")+" "+dateMap.get("hour")+" "+ dateMap.get("day") +" "+ dateMap.get("month") +" ? *";
        QuartzUtils.addJob(sche,map.get("jobName").toString(), CommentJob.class, map, cron);

    }

    /**
     * 创建投诉订单
     */
    @Transactional
    public void createComplaint() throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);

        Map<String,Object> paramModelMap = MapUtil.build()
                .put("name",jsonObject.get("name"))
                .put("tel",jsonObject.get("tel"))
                .put("typeUser",jsonObject.get("typeUser"))
                .put("typeQuestion",jsonObject.get("typeQuestion"))
                .put("content",jsonObject.get("content"))
                .put("status",1)
                .put("createAt",DateUtils.formatDate(new Date()))
                .over();
        complaintService.createComplaint(paramModelMap);

    }

    /**
     * 财务数据统计
     */
    @Transactional
    public Map<String, Object> financialDataStatistics() throws Exception {
        Map<String, Object> paramModelMap = new HashMap<>();
        Map<String, Object> result = foundationService.findFoundationData(paramModelMap);
        return result;
    }

    /**
     * 运营数据
     * @return
     */
    @Transactional
    public Map<String, Object> operateDate() throws Exception {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);

        // 判断如果没有开始时间和结束时间，直接返回空
        if ((!Constant.toEmpty(jsonObject.get("endTime"))) || (!Constant.toEmpty(jsonObject.get("beginTime")))){
            return null;
        }

        // 处理结束时间，多加一天，sql用的是between and
        String endTime = LocalDate.parse((String)jsonObject.get("endTime")).plusDays(1).toString();
        Map<String,Object> parameModelMap = MapUtil.build()
                .put("type", jsonObject.get("type"))
                .put("beginTime", jsonObject.get("beginTime"))
                .put("endTime", endTime)
                .over();
        Map<String, Object> count = null;
        if("register".equals(parameModelMap.get("type").toString())){
            count = chartService.register(parameModelMap);
        }else if ("view".equals(parameModelMap.get("type").toString())){
            count = chartService.view(parameModelMap);
        }else if("guarantee".equals(parameModelMap.get("type").toString())){
            count = chartService.guarantee(parameModelMap);
        }else if("shop".equals(parameModelMap.get("type").toString())){
            count = chartService.shop(parameModelMap);
        }else if("channel".equals(parameModelMap.get("type").toString())){
            count = chartService.channel(parameModelMap);
        }else if("event".equals(parameModelMap.get("type").toString())){
            count = chartService.event(parameModelMap);
        }else if("foundation".equals(parameModelMap.get("type").toString())){
            count = chartService.foundation(parameModelMap);
        }else if("initNum".equals(parameModelMap.get("type").toString())){
            count = chartService.initNum(parameModelMap);
        }else if("observationNum".equals(parameModelMap.get("type").toString())){
            count = chartService.observationNum(parameModelMap);
        }else if("guaranteeNum".equals(parameModelMap.get("type").toString())){
            count = chartService.guaranteeNum(parameModelMap);
        }else if("outNum".equals(parameModelMap.get("type").toString())){
            count = chartService.outNum(parameModelMap);
        }else if("twiceRecharge".equals(parameModelMap.get("type").toString())){
            count = chartService.twiceRecharge(parameModelMap);
        }else if("weChatConcerns".equals(parameModelMap.get("type").toString())){
            count = chartService.weChatConcerns(parameModelMap);
        }else if("eventApplyFail".equals(parameModelMap.get("type").toString())){
            count = chartService.eventApplyFail(parameModelMap);
        }else if("eventApplySuccess".equals(parameModelMap.get("type").toString())){
            count = chartService.eventApplySuccess(parameModelMap);
        }else if("order".equals(parameModelMap.get("type").toString())){
            count = chartService.order(parameModelMap);
        }else if("orderApplyFail".equals(parameModelMap.get("type").toString())){
            count = chartService.orderApplyFail(parameModelMap);
        }else if("orderApplySuccess".equals(parameModelMap.get("type").toString())){
            count = chartService.orderApplySuccess(parameModelMap);
        }
        // 处理结束时间返回的结果
        if (count != null){
            String[] datelist = (String[])count.get("datelist");
            String[] dateListTarget = new String[datelist.length - 1];
            Integer[] numlist = (Integer[])count.get("numlist");
            Integer[] numListTarget = new Integer[numlist.length - 1];
            System.arraycopy(datelist,0,dateListTarget,0,dateListTarget.length);
            System.arraycopy(numlist,0,numListTarget,0,numListTarget.length);
            // 重新赋值
            count.put("datelist", dateListTarget);
            count.put("numlist", numListTarget);
        }
        return count;
    }

    /**
     * 渠道拉新数据
     * @return
     */
    public Object channelData() {
        String postRequestParams = ThreadCache.getPostRequestParams();
        JSONObject jsonObject = JSONObject.parseObject(postRequestParams);

        // 处理结束时间，多加一天，sql用的是between and
        String endTime = LocalDate.parse((String)jsonObject.get("endTime")).plusDays(1).toString();
        Map<String,Object> parameModelMap = MapUtil.build()
                .put("beginTime", jsonObject.get("beginTime"))
                .put("endTime", endTime)
                .over();
        Map<String, Object> count = chartService.channelData(parameModelMap);
        return count;
    }
}
