package com.zccbh.demand.service.event;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.controller.quartz.CommentJob;
import com.zccbh.demand.controller.quartz.QuartzUtils;
import com.zccbh.demand.mapper.business.AccountMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.customer.RecordShareMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.mapper.customer.WechatLoginMapper;
import com.zccbh.demand.mapper.event.EventApplyMapper;
import com.zccbh.demand.mapper.event.EventAssertMapper;
import com.zccbh.demand.mapper.event.EventMapper;
import com.zccbh.demand.mapper.event.EventReceivecarMapper;
import com.zccbh.demand.mapper.foundation.FoundationMapper;
import com.zccbh.demand.mapper.foundation.PosterMapper;
import com.zccbh.demand.mapper.merchants.CbhAccountDetailMapper;
import com.zccbh.demand.mapper.merchants.CbhMessageMapper;
import com.zccbh.demand.pojo.common.Foundation;
import com.zccbh.demand.pojo.merchants.CbhMessage;
import com.zccbh.demand.service.basic.DictionaryService;
import com.zccbh.demand.service.business.CbhUserBusinessService;
import com.zccbh.demand.service.customer.CarService;
import com.zccbh.demand.service.system.UserCustomerLogService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.test.exception.CustomException;
import com.zccbh.util.base.Base64;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.base.LocationUtils;
import com.zccbh.util.base.MapUtil;
import com.zccbh.util.base.SmsDemo;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.uploadImg.UploadFileUtil;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EventService {
	@Autowired
	private EventMapper eventMapper;
	@Autowired
	private EventApplyMapper eventApplyMapper;
	@Autowired
	private EventReceivecarMapper eventReceivecarMapper;
	@Autowired
	private CbhMessageMapper messageMapper;
	@Autowired
	private RecordShareMapper recordShareMapper;
	@Autowired
	private CarService carService;
	@Autowired
	private CarMapper carMapper;
	@Autowired
	private CbhUserBusinessService cbhUserBusinessService;
	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	private PosterMapper posterMapper;
	@Autowired
	private FoundationMapper foundationMapper;
	@Autowired
	private WeiXinUtils weiXinUtils;
	@Autowired
	private CbhAccountDetailMapper cbhAccountDetailMapper;
	@Autowired
    private DictionaryService dictionaryService;
	@Autowired
    private SchedulerFactoryBean schedulerFactory;
	@Autowired
	private WechatLoginMapper wechatLoginMapper;
	@Autowired
	private UserCustomerMapper userCustomerMapper;
	@Autowired
	private EventAssertMapper eventAssertMapper;
	@Autowired
	private UserCustomerLogService userCustomerLogService;

	/**
	 * 修改互助事件
	 * @param paramModelMap 互助事件信息
	 * @return 修改结果信息
	 * @throws Exception
	 */
	@Transactional
	public String updateEvent(Map<String, Object> paramModelMap) throws Exception {
		eventMapper.updateModel(paramModelMap);
		return "0";
	}

	@Transactional
	public void saveSingle(Map<String, Object> paramModelMap) throws Exception{
		eventMapper.saveSingle(paramModelMap);
	}

	@Transactional
	public void saveApply(Map<String, Object> paramModelMap) throws Exception{
		eventApplyMapper.saveSingle(paramModelMap);
	}

	@Transactional
	public void saveReceivecar(Map<String, Object> paramModelMap) throws Exception{
		eventReceivecarMapper.saveSingle(paramModelMap);
	}

	@Transactional
	public String saveEvent(Map<String, Object> paramModelMap) throws Exception{
		eventMapper.saveSingle(paramModelMap);
		return "0";
	}

	public List<Map<String, Object>> findJionNew() throws Exception{
		return eventMapper.findJionNew();
	}

	public List<Map<String, Object>> findMessageNew(Map<String,Object> map) throws Exception{
		return eventMapper.findMessageNew(map);
	}

	/**
	 * 查询互助事件基本信息
	 * @param paramModelMap 查询条件
	 * @return 互助事件信息
	 * @throws Exception
	 */
	 public PageInfo<Map<String, Object>> findEventList(Map<String, Object> paramModelMap) throws Exception {
	        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
	        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
	        PageHelper.startPage(pageNo, pageSize);
	        List<Map<String, Object>> eventList = eventMapper.findMore(paramModelMap);
	        PageInfo<Map<String, Object>> eventInfo = new PageInfo<>(eventList);
	        return eventInfo;
	}

	 public PageInfo<Map<String, Object>> findCompensateList(Map<String, Object> paramModelMap,int flag) throws Exception {
	        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
	        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
	        PageHelper.startPage(pageNo, pageSize);
	        List<Map<String, Object>> eventList = eventMapper.findCompensateList(paramModelMap);
	        for(Map<String,Object> m:eventList){
	        	String portrait = String.valueOf(m.get("portrait"));
	        	m.put("portrait", portrait.indexOf("thirdwx.qlogo.cn")==-1?UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL,portrait):portrait);
	        	if(flag==1){
//	        		m.put("licensePlateNumber", carService.hideStr(String.valueOf(m.get("licensePlateNumber"))));
	        		m.put("nameCarOwner", hideName(String.valueOf(m.get("nameCarOwner"))));
	        		String model = String.valueOf(m.get("model"));
	        		if(model.length()>3){
	        			m.put("model", model = model.substring(0,model.length()-3)+"***");
	        		}
	        	}
	        }
	        PageInfo<Map<String, Object>> eventInfo = new PageInfo<>(eventList);
	        return eventInfo;
	}

	/**
	 * 根据订单号查询互助事件查询互助事件
	 * @param paramModelMap 查询条件
	 * @return 互助事件信息
	 * @throws Exception
	 */
	public Map<String,Object> findEventByEventNo(Map<String, Object> paramModelMap) throws Exception{
		return eventMapper.findSingle(paramModelMap);
	}

	/**
	 * 车V互助车主端首页统计信息
 	 * @return 总事件数，总成员，互助金余额，首页海报图
	 * @throws Exception
	 */
	public Map<String,Object> getWeChatIndexInfo() throws Exception{
		 Map<String,Object> foundationMap = eventMapper.selectFoundation();
		 foundationMap.put("amtBalance", new BigDecimal(String.valueOf(foundationMap.get("amtPaid"))).add(new BigDecimal(String.valueOf(foundationMap.get("amtBalance")))));
		 List<Map<String,Object>> list = posterMapper.findMore(null);
		 for(Map<String,Object> m:list){
			 m.put("linkImg", UploadFileUtil.getImgURL("poster/",String.valueOf(m.get("linkImg"))));
		 }
		 foundationMap.put("imgList", list);
		 Date date = new Date();
		 foundationMap.put("time", date.getTime());
		 return foundationMap;
	 }

	public List<Map<String,Object>> getWeChatIndexInfo(String id) throws Exception{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("customerId", id);
		List<Map<String,Object>> list1 = carMapper.findMore(map);
		for(Map<String,Object> m:list1){
			Map<String,Object> mm = new HashMap<String, Object>();
			mm.put("nameCarOwner", m.get("nameCarOwner"));
			mm.put("licensePlateNumber", m.get("licensePlateNumber"));
			mm.put("model", m.get("model"));
			mm.put("status", m.get("status"));
			mm.put("id", m.get("id"));
			list.add(mm);
		}
		return list;
	}

	public Map<String,Object> findPublicity() throws Exception{
		 Map<String,Object> map = eventMapper.findPublicityAmount();
		 Map<String,Object> eventMap = eventMapper.findPublicityEvent();
		 if(map!=null&&eventMap!=null){
			 map.putAll(eventMap);
		 }
		 return map;
	 }

	/**
	 * 查看订单详情
	 * @author xiaowuge  
	 * @date 2018年9月17日  
	 * @version 1.0
	 */
	public Map<String, Object> findOrderDetail(Map<String,Object> pMap) throws Exception{
		Map<String,Object> map = eventMapper.findOrderDetail(pMap);
		if(map==null){
			return new HashMap<String, Object>();
		}
		if(Constant.toEmpty(map.get("drivingLicense"))){
			map.put("drivingLicense",CommonField.getCarDrivingUrl((String) map.get("drivingLicense")));
        }
		map.put("nickname", String.valueOf(map.get("nickname")).equals("null")?String.valueOf(map.get("nickname")):Base64.getFromBase64(String.valueOf(map.get("nickname"))));
		String img = String.valueOf(map.get("accidentImg"));
		String[] imgA = img.split("_");
		String a ="";
		for(String i : imgA){
			String url = UploadFileUtil.getImgURL(CommonField.COMPENSATE_IMG_URL,i);
			a += a==""?url:"_"+url;
		}
		if(!img.equals("null")){
			map.put("accidentImg", a);
		}
		img = String.valueOf(map.get("assertImg"));
		imgA = img.split("_");
		a ="";
		for(String i : imgA){
			String url = UploadFileUtil.getImgURL("event/assert/",i);
			a += a==""?url:"_"+url;
		}
		if(!img.equals("null")){
			map.put("assertImg", a);	
		}
		
		img = String.valueOf(map.get("repairImg"));
		imgA = img.split("_");
		a ="";
		for(String i : imgA){
			String url = UploadFileUtil.getImgURL("event/maintenance/",i);
			a += a==""?url:"_"+url;
		}
		if(!img.equals("null")){
			map.put("repairImg", a);
		}
		
		img = String.valueOf(map.get("complaintImg"));
		imgA = img.split("_");
		a ="";
		for(String i : imgA){
			String url = UploadFileUtil.getImgURL("event/complaint/",i);
			a += a==""?url:"_"+url;
		}
		if(!img.equals("null")){
			map.put("complaintImg", a);
		}
		img = String.valueOf(map.get("img1"));
		if(!img.equals("null")){
			map.put("img1", UploadFileUtil.getImgURL("maintenanceshop/maintenanceshopImg/",img));
		}
		img = String.valueOf(map.get("img2"));
		if(!img.equals("null")){
			map.put("img2", UploadFileUtil.getImgURL("maintenanceshop/employeeimg/",img));
		}
		img = String.valueOf(map.get("img3"));
		if(!img.equals("null")){
			map.put("img3", UploadFileUtil.getImgURL("maintenanceshop/employeeimg/",img));
		}
		String status = String.valueOf(map.get("statusEvent"));
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		if(status.equals("21")||status.equals("22")||status.equals("23")||status.equals("31")){  //待定损
			map.put("presetTime", "10800");
			
			//String assertCreateTime = String.valueOf(map.get("assertCreateTime"));
			String reciveCarTime = String.valueOf(map.get("reciveCarTime"));
			if(!reciveCarTime.equals("null")){
				Date nowD = new Date();
				Calendar nowTime = Calendar.getInstance();
				nowTime.setTime(time.parse(reciveCarTime));
				//nowTime.add(Calendar.HOUR_OF_DAY, 3);
				long interval = (nowTime.getTime().getTime()-nowD.getTime())/1000;
				map.put("interval", interval);
			}
			String longitude = String.valueOf(map.get("longitude"));
			String latitude = String.valueOf(map.get("latitude"));
			String shopLongitude = String.valueOf(map.get("shopLongitude"));
			String shopLatitude = String.valueOf(map.get("shopLatitude"));
			if(!longitude.equals("null")&&!latitude.equals("null")&&!shopLongitude.equals("null")&&!shopLatitude.equals("null")){
				Double d1 = LocationUtils.getDistance(Double.valueOf(latitude), Double.valueOf(longitude), Double.valueOf(shopLatitude), 
						Double.valueOf(shopLongitude));
				map.put("distance", d1);
			}
			
		}else if(status.equals("10")||status.equals("11")||status.equals("12")){
			map.put("presetTime", "600");
			String endTime = String.valueOf(map.get("timeEnd"));
			if(!endTime.equals("null")){
				Date nowD = new Date();
				long interval = (time.parse(endTime).getTime()-nowD.getTime())/1000;
				map.put("interval", interval);
			}
		}else if(status.equals("41")||status.equals("51")||status.equals("52")){
			map.put("presetTime", "172800");
			String timePay = String.valueOf(map.get("timePay"));
			if(!timePay.equals("null")){
				Date nowD = new Date();
				Calendar nowTime = Calendar.getInstance();
				nowTime.setTime(time.parse(timePay));
				nowTime.add(Calendar.HOUR_OF_DAY, 48);
				long interval = (nowTime.getTime().getTime()-nowD.getTime())/1000;
				map.put("interval", interval);
			}
		}
		
		String carPhotos = String.valueOf(map.get("carPhotos"));
		if(!carPhotos.equals("null")&&!carPhotos.equals("")){
			carPhotos = CommonField.getCarUrl(carPhotos);
			JSONObject cp= JSON.parseObject(carPhotos);
			if(String.valueOf(cp.get("qd")).equals("null")||String.valueOf(cp.get("qd")).equals("")){
				map.put("carPhotos", cp.get("zh")+"_"+cp.get("yh")+"_"+cp.get("zq")+"_"+cp.get("yq"));
			}else{
				map.put("carPhotos", cp.get("zh")+"_"+cp.get("yh")+"_"+cp.get("zq")+"_"+cp.get("yq")+"_"+cp.get("qd")+"_"+cp.get("zc")+"_"+cp.get("yc"));
			}
		}

		
		return map;
	}
	public void saveMessageScore(String eventNo,Double score,String score1) throws Exception{
		Map<String,Object> pMap = new HashMap<String, Object>();
		pMap.put("eventNo", eventNo);
		Map<String,Object> map = eventMapper.findOrderDetail(pMap);
		CbhMessage message = new CbhMessage();
        message.setBusinessId(Integer.valueOf(String.valueOf(map.get("businessId"))));
        message.setEventNo(eventNo);
        String licensePlateNumber = String.valueOf(map.get("licensePlateNumber"));
        message.setLicensePlateNumber(licensePlateNumber);
        String content = "";
        if(score==-5.0){
        	 message.setType(11);
             message.setTitle("扣分记录");
        	content = licensePlateNumber+"的车主投诉"+String.valueOf(map.get("maintenanceshopName2"))+",扣"+Math.abs(score)+"分";
        }else if(score==0.5){
        	 message.setType(12);
             message.setTitle("加分记录");
        	content = licensePlateNumber+"的车主评价"+String.valueOf(map.get("maintenanceshopName2"))+"5星,加"+Math.abs(score)+"分";
        }else{
        	 message.setType(11);
             message.setTitle("扣分记录");
        	String a = score1+"星";
        	content = licensePlateNumber+"的车主评价"+String.valueOf(map.get("maintenanceshopName2"))+a+",扣"+Math.abs(score)+"分";
        }
        message.setContent(content);
        message.setScore(Math.abs(score));
        messageMapper.insert(message);
        
        cbhUserBusinessService.servicePoints(eventNo, Math.abs(score), score>0?"+":"-");
	}

	public int findApplyCount(Map<String,Object> map) throws Exception{
		return eventMapper.findApplyCount(map);
	}
	public List<Map<String,Object>> findApplyId(Map<String,Object> map) throws Exception{
		return eventMapper.findApplyId(map);
	}
	
	/**
	 * 接车
	 * @author xiaowuge  
	 * @date 2018年9月12日  
	 * @version 1.0
	 */
	@Transactional
	public void reveiveCar(Map<String,Object> detail) throws Exception{
		try {
			//更新车辆理赔次数
			Map<String, Object> carInfoMap = new HashMap<>();
			carInfoMap.put("eventNo", detail.get("eventNo"));
			Map<String, Object> eventInfo = eventMapper.findSingle(carInfoMap);
			carInfoMap.clear();
			carInfoMap.put("id", eventInfo.get("carId"));
			Map<String, Object> carInfo = carMapper.findSingle(carInfoMap);
			carInfoMap.put("compensateNum", new BigDecimal(carInfo.get("compensateNum").toString()).add(new BigDecimal("1")));
		
			Foundation foundation =  foundationMapper.findEntitySingle(new HashMap<String, Object>());
			BigDecimal eventAmtCooperation = new BigDecimal(String.valueOf(detail.get("eventAmtCooperation")).equals("null")?"0":String.valueOf(detail.get("eventAmtCooperation")));
			BigDecimal amtPay = new BigDecimal(String.valueOf(detail.get("amtPay")).equals("null")?"0":String.valueOf(detail.get("amtPay")));
			BigDecimal amtAssert = new BigDecimal(String.valueOf(detail.get("amtAssert")).equals("null")?"0":String.valueOf(detail.get("amtAssert")));
			BigDecimal cha = amtAssert.subtract(amtPay);
			Map<String,Object> upfMap = new HashMap<String, Object>();
			upfMap.put("amtPaid", foundation.getAmtPaid().add(amtAssert));
			upfMap.put("amtBalance", foundation.getAmtBalance().subtract(amtAssert));
			upfMap.put("showTotal", foundation.getShowTotal().subtract(amtAssert));
			foundationMapper.updateModel(upfMap);
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("amtCooperation", detail.get("amtShare"));
			carService.updateCarAmt(map);
			
			try {    //如果有退出保障的车辆，则互助人数减少
				Map<String,Object> countMap = carService.findUpdateCount();
				if(countMap!=null){
					int count = Integer.valueOf(String.valueOf(countMap.get("count")));
					if(count>0){
//	                    Map<String,Object> paramModelMap = new HashMap<String, Object>();
//	                    paramModelMap.put("showCustomer", foundation.getShowCustomer()-count);
//	                    foundationMapper.updateModel(paramModelMap);
						updateDayNumber("outNum",count);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			BigDecimal amtCompensation = new BigDecimal(String.valueOf(detail.get("amtCompensation")).equals("null")?"0":String.valueOf(detail.get("amtCompensation")));
			BigDecimal amtCBH = new BigDecimal(String.valueOf(detail.get("amtCBH")).equals("null")?"0":String.valueOf(detail.get("amtCBH")));
			map = new HashMap<String, Object>();
			map.put("id", detail.get("carId"));
			BigDecimal ac = amtCompensation.subtract(amtCBH).subtract(eventAmtCooperation);
			map.put("amtCompensation", ac.compareTo(new BigDecimal("0"))>0?ac:0);
			carService.updateCar(map);
			
			if(ac.compareTo(new BigDecimal("500"))<=0){
				map.put("eventNo", detail.get("eventNo"));
				Map<String, Object> eventDetail = eventMapper.findSingle(map);
				//所接车辆，详细信息
				Map<String, Object> carMap = new HashMap<>();
				carMap.put("id", eventDetail.get("carId"));
				Map<String, Object> carDetail = carMapper.findSingle(carMap);
				//所接车辆车主信息
				Map<String, Object> customerMap = new HashMap<>();
				customerMap.put("id", eventDetail.get("customerId"));
				Map<String, Object> customerDetail = userCustomerMapper.findUser(customerMap);
				
				Map<String, String> parameMap = new HashMap<>();
				parameMap.put("licensePlateNumber", carDetail.get("licensePlateNumber").toString());
				parameMap.put("amtCompensation", map.get("amtCompensation").toString());
				parameMap.put("openid", customerDetail.get("openId").toString());
				weiXinUtils.sendTemplate(16, parameMap);
			}
			
			//保障中 ，互助金小于1元的车辆
			List<Map<String,Object>> outList = carService.findOutList();
			if(outList!=null&&outList.size()>0){
				try {
					for(Map<String,Object> m:outList){
						String carId = String.valueOf(m.get("id"));
						String openId = String.valueOf(m.get("openId"));
						String nickname = String.valueOf(m.get("nickname")).equals("null")?String.valueOf(m.get("nickname")):Base64.getFromBase64(String.valueOf(m.get("nickname")));
						String licensePlateNumber = String.valueOf(m.get("licensePlateNumber"));
						String model = String.valueOf(m.get("model"));
						BigDecimal amtCooperation = (BigDecimal) m.get("amtCooperation");
						Integer messageFlag = (Integer) m.get("messageFlag");
						Integer signoutMessageFlag = (Integer) m.get("signoutMessageFlag");
						String customerPN = m.get("customerPN").toString();
						int chemamaFlag = carService.checkChemamaCarById(carId);
						if(chemamaFlag==1){

						}else{
							int i = amtCooperation.compareTo(new BigDecimal(0.5));
							int e = amtCooperation.compareTo(new BigDecimal(0.0));
							if (i==0){
								continue;
							}
							Map<String,String> rmap = new HashMap<>();
							Map<String,Object> parameterMap = new HashMap<>();
							parameterMap.put("id",carId);
							rmap.put("model", model);
							rmap.put("openid", openId);
							rmap.put("licensePlateNumber", licensePlateNumber);
							rmap.put("name", nickname);
							rmap.put("carId", carId);
							if (i==1&&(null != signoutMessageFlag && signoutMessageFlag == 0)){
								rmap.put("money", "1");
								rmap.put("content", licensePlateNumber+"账户余额已低于1元，为保障您继续享受互助权益，请您尽快为账户充值！");
								rmap.put("notice", "预计一周后退出计划，");
								rmap.put("num", "3");
								parameterMap.put("messageFlag",10);
								parameterMap.put("signoutMessageFlag",10);
								carMapper.updateModel(parameterMap);
								weiXinUtils.sendTemplate(4, rmap);
								//发送余额不足短信
								SmsDemo.sendSms(2, customerPN, null);
							}else if (signoutMessageFlag != null && i==-1&&10==signoutMessageFlag){
								rmap.put("money", "0.5");
								rmap.put("content", licensePlateNumber+"账户余额不足0.5元，为避免您被强制退出互助计划，请您尽快完成账户充值，感谢您对车V互助的支持与厚爱！");
								rmap.put("notice", "");
								rmap.put("num", "1");
								parameterMap.put("messageFlag",20);
								parameterMap.put("signoutMessageFlag",20);
								carMapper.updateModel(parameterMap);
								weiXinUtils.sendTemplate(4, rmap);
							}else if(signoutMessageFlag != null && e <= 0 && 20 == signoutMessageFlag){    //余额小于等于0元退出计划通知
				                //获取用户信息
				                Map<String, Object> reqMap = new HashMap<>();
				                reqMap.put("openId", openId);
				                Map<String, Object> customer = userCustomerMapper.findUser(reqMap);
				                //用户车辆退出
				            	reqMap.clear();
				            	reqMap.put("customerId", customer.get("id"));
				            	reqMap.put("customerPN", customer.get("customerPN"));
				            	reqMap.put("source", customer.get("source"));
				            	reqMap.put("createAt", customer.get("timeJoin"));
				            	reqMap.put("currentStatus", customer.get("status"));
				            	reqMap.put("optTime", DateUtils.formatDate(new Date()));
				            	reqMap.put("optType", 12);
				            	reqMap.put("optDesc", "用户车辆退出");
				            	reqMap.put("recordTime", DateUtils.formatDate(new Date()));
				            	userCustomerLogService.saveUserCustomerLog(reqMap);
								
								rmap.put("money", "0");
								parameterMap.put("messageFlag", 30);
								parameterMap.put("signoutMessageFlag", 30);
								carMapper.updateModel(parameterMap);
								weiXinUtils.sendTemplate(14, rmap);
							}else {
								continue;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			carService.updateCarStatus();
			
			map = new HashMap<String, Object>();
			map.put("id", detail.get("accountId"));
			Map<String,Object> acountMap = accountMapper.findSingle(map);
			
			String amtFreeze1 = Constant.toOr(String.valueOf(acountMap.get("amtFreeze")), Constant.toReadPro("orKey"), "decrypt");
		    BigDecimal amtFreeze = new BigDecimal(amtFreeze1);
		    String amtUnfreeze1 = Constant.toOr(String.valueOf(acountMap.get("amtUnfreeze")), Constant.toReadPro("orKey"), "decrypt");
		    BigDecimal amtUnfreeze = new BigDecimal(amtUnfreeze1);
		    BigDecimal a = new BigDecimal(String.valueOf(detail.get("amtBusiness")));
		    BigDecimal newAf = amtFreeze.subtract(a).compareTo(new BigDecimal("0"))>0?amtFreeze.subtract(a):new BigDecimal("0");
		    BigDecimal newAu = amtUnfreeze.add(a);
		    map.put("amtFreeze", Constant.toOr(String.valueOf(newAf), Constant.toReadPro("orKey"), "encrypt"));
		    map.put("amtUnfreeze", Constant.toOr(String.valueOf(newAu), Constant.toReadPro("orKey"), "encrypt"));
		    accountMapper.updateModel(map);
		    
		    map = new HashMap<String, Object>();
		    map.put("accountId", detail.get("accountId"));
		    map.put("eventNo", detail.get("eventNo"));
		    map.put("type", 31);
		    map.put("amt", a);
		    map.put("img", "b_shouru.png");
		    map.put("content", detail.get("licensePlateNumber")+"车辆修理完成,"+detail.get("maintenanceshopName2")+"到账:"+a+"元");
		    map.put("isRead", 1);
		    cbhAccountDetailMapper.saveSingle(map);
		    
		    map = new HashMap<String, Object>();
		    map.put("amtCooperation", cha);
		    map.put("amtShare", detail.get("amtShare"));
		    map.put("description", detail.get("licensePlateNumber")+"申请救助成功，互助金额:"+cha+"元,分摊金额:"+detail.get("amtShare")+"元");
		    map.put("eventNo", detail.get("eventNo"));
		    recordShareMapper.saveSingle(map);
		    
		    setCommentJob(detail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setCommentJob(Map<String,Object> detail) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
	    map.put("jobName","commentJob_" + detail.get("eventNo"));
	    map.put("eventNo", detail.get("eventNo"));
	    map.put("maintenanceshopId", detail.get("maintenanceshopId"));
	    map.put("customerId", detail.get("customerId"));
	    Scheduler sche = schedulerFactory.getScheduler();
        QuartzUtils.removeJob(sche,map.get("jobName").toString());
	    Map dicMap = new HashMap();
        dicMap.put("type","commentTime");
        Map dictionary = dictionaryService.findSingle(dicMap);
	    Map<String, String> dateMap = DateUtils.getDateMap(new Long(dictionary.get("value").toString()));
        String cron = dateMap.get("second")+" "+dateMap.get("minute")+" "+dateMap.get("hour")+" "+ dateMap.get("day") +" "+ dateMap.get("month") +" ? *";
        QuartzUtils.addJob(sche,map.get("jobName").toString(), CommentJob.class, map, cron);
	}
	public Map<String,Object> findRandomCar() throws Exception{
		return eventMapper.findRandomCar();
	}

	/**
	 * 查询待处理的订单数量
	 * @throws Exception
	 */
	public Map<String,Object> findEventCount(Map<String, Object> paramModelMap) throws Exception{
		return eventMapper.findCount(paramModelMap);
	}
	public String hideName(String str){
		StringBuilder s = new StringBuilder(str);
		if(s.length()>1&&!"null".equals(str)){
			s.replace(1, 2,"*");
		}
		return String.valueOf(s);
	}
	public void updateDayNumber(String key,Integer number) throws Exception {
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd");
		String day = time.format(new Date());
		Map<String,Object> pMap = new HashMap<>();
		pMap.put("day",day);
        List<Map<String,Object>> list = wechatLoginMapper.findMore(pMap);
        if(list!=null&&list.size()>0){
            Map<String,Object> map = list.get(0);
            String data = String.valueOf(map.get(key));
            int num = data.equals("null")||data.equals("")?0:Integer.valueOf(data);
            num = num + number;
            pMap.clear();
            pMap.put(key,num);
            pMap.put("id",map.get("id"));
            wechatLoginMapper.updateModel(pMap);
        }else{
            pMap.clear();
            pMap.put(key,number);
            pMap.put("day",day);
            wechatLoginMapper.saveSingle(pMap);
        }
	}

	/**
	 * 废弃订单
	 */
	@Transactional
	public void invalidOrder(Map<String, Object> paramModelMap) throws Exception {
		//查询互助理赔详情
		Map detail = eventMapper.findSingle(paramModelMap);
		//判断订单是否作废，未作废才能进行作废操作
		Integer isInvalid = (Integer) detail.get("isInvalid");
		if(isInvalid == 1){
			Map map = new HashMap();
			//修改互助理赔订单状态
			map.put("eventNo",paramModelMap.get("eventNo"));
			map.put("isInvalid",10);
			map.put("invalidTime", DateUtils.formatDate(new Date()));
			updateEvent(map);

			//接单状态的时候删除 接单定时器和message里面的接单消息
			Integer status = (Integer) detail.get("statusEvent");
			Scheduler sche = schedulerFactory.getScheduler();
			if(status==11){
				QuartzUtils.removeJob(sche, paramModelMap.get("eventNo").toString());
				messageMapper.deleteByOrderNo(paramModelMap.get("eventNo").toString());
			}
			QuartzUtils.removeJob(sche, "takeCar_"+paramModelMap.get("eventNo").toString());
			QuartzUtils.removeJob(sche, "receivecar_"+paramModelMap.get("eventNo").toString());
		}else{
			throw new CustomException("该订单已被作废，请勿重复操作");
		}

	}

	/**
	 * 查询商家报价时的互助订单详情数据
	 */
	public Map<String, Object> findEventQuotation(String eventNo, String type) throws Exception {
		if (Constant.toEmpty(type) && "2".equals(type)){
			// 查询保险理赔的数据
			Map<String, Object> result = eventMapper.findOrderQuotation(eventNo);
			if(Constant.toEmpty(result)){
				if(Constant.toEmpty(result.get("accidentImg"))){
					List<String> accidentImg = CommonField.getOrderImgList(0,(String) result.get("accidentImg"));
					result.put("accidentImg",accidentImg);
				}
				if(Constant.toEmpty(result.get("drivingLicense"))){
					result.put("drivingLicense",CommonField.getCarDrivingUrl((String) result.get("drivingLicense")));
				}
			}
			return result;
		} else {
			Map<String, Object> result = eventMapper.findEventQuotation(eventNo);
			if(Constant.toEmpty(result)){
				if(Constant.toEmpty(result.get("accidentImg"))){
					List<String> accidentImg = CommonField.getImgList(0,(String) result.get("accidentImg"));
					result.put("accidentImg",accidentImg);
				}
				if(Constant.toEmpty(result.get("drivingLicense"))){
					result.put("drivingLicense",CommonField.getCarDrivingUrl((String) result.get("drivingLicense")));
				}
			}
			return result;
		}
	}
	
	/**
	 * 传入参数：eventNo 
	 * @author xiaowuge  
	 * @date 2018年11月14日  
	 * @version 1.0
	 */
	public void updateAmtShare(Map<String, Object> map) throws Exception{
		System.out.println("++++++++++++++++统计时间编号+++++:::"+map.get("eventNo"));
		//获取定损详情
		Map<String, Object> eventAsserDetail = eventAssertMapper.eventAssertDetail(map);
		//定损费用
		BigDecimal amtAssert = new BigDecimal(eventAsserDetail.get("amtAssert").toString());
		//获取剩余理赔额度
		Map<String, Object> carDetail = carMapper.findCarByEventNo((String) eventAsserDetail.get("eventNo"));
		BigDecimal amtCompensation = (BigDecimal) carDetail.get("amtCompensation");
		//比较剩余理赔额度和定损金额，得出车主应支付金额
		BigDecimal amtPay = amtAssert.divide(new BigDecimal(10), 2, RoundingMode.HALF_UP); //车主支付10%金额
		BigDecimal amtCooperation = amtAssert;//互助金额
		int answer = amtCompensation.compareTo(amtAssert);
		if(answer < 1){
			amtPay = amtCompensation.divide(new BigDecimal(10), 2, RoundingMode.HALF_UP).add(amtAssert.subtract(amtCompensation));
			amtCooperation = amtCompensation;
		}
		
		//获取保障中的车辆数量
		Map<String, Object> requestMap = new HashMap<>();
		//查询保障中车辆数
		requestMap.put("status",20);
		int carCount = carMapper.findCarCountByStatus(requestMap);
		
		//计算分摊金额
		BigDecimal amtShare = amtCooperation.divide(new BigDecimal(carCount),2,RoundingMode.HALF_UP);
		BigDecimal amtCBH = new BigDecimal(0).setScale(2,RoundingMode.HALF_UP);
		int answer2 = amtShare.compareTo(new BigDecimal(0.1));
		if(answer2==1){
			amtShare = new BigDecimal(0.1).setScale(2,RoundingMode.HALF_UP);
			amtCBH = amtCooperation.subtract(amtShare.multiply(new BigDecimal(carCount))).setScale(2,RoundingMode.HALF_UP);
			amtCooperation = amtShare.multiply(new BigDecimal(carCount)).setScale(2,RoundingMode.HALF_UP);
		}
		int answer3 = amtShare.compareTo(new BigDecimal(0.01));
		if(answer3!=1){
			amtShare = new BigDecimal(0.01).setScale(2,RoundingMode.HALF_UP);
			amtCBH = new BigDecimal(0).setScale(2,RoundingMode.HALF_UP);
		}
		
		//修改互助订单
		requestMap.clear();
		requestMap.put("eventNo",map.get("eventNo"));
		requestMap.put("amtCooperation",amtCooperation);
		requestMap.put("amtCBH",amtCBH);
		requestMap.put("amtShare",amtShare);
		requestMap.put("amtPay",amtPay);
		System.out.println("++++++++++++++++++最新统计结果+++：："+requestMap);
		eventMapper.updateModel(requestMap);
	}
	
	public int getEventStatus(String eventNo){
		return eventMapper.getEventStatus(eventNo);
	}
	
}
